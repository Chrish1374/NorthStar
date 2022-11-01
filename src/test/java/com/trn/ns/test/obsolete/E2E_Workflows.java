//package com.trn.ns.test.obsolete;
//import java.awt.AWTException;
//import java.text.ParseException;
//
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class E2E_Workflows extends TestBase 
//{
//
//	private StudyListPage studyPage;
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	private ViewerPage viewerpage;
//	private SinglePatientStudyPage patientstudypage ;
//	private ExtentTest extentTest;
//
//	//Loading the patient on viewer
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}	
//
//	@Test(groups ={"Chrome"})
//	public void tese01_E2E_TC1376_workflowUsingPiclineData() throws InterruptedException, AWTException
//	{	 
//
//		String testcaseName = "E2E_TC1376";
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("E2E - Workflow using CT PICCLINE data");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//Loading the patient on viewer
//		String filePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		//Clicking on Patient piccline
//		patientPage.clickOnPatientRow(PatientName); 
//
//		//Clicking first study of Piccline patient data
//		patientstudypage = new SinglePatientStudyPage(driver);
//		patientstudypage.waitForSingleStudyToLoad();
//		patientstudypage.clickOntheFirstStudy();
//
//		//Waiting for loading of first viewbox 
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(2);
//
//		//Performing and verifying zoom on DICOM image (viewbox 2)
//		performAndverifyZoom(viewerpage.viewboxImg2, 2 , testcaseName, "For Dicom");
//
//		//Performing and verifying windowleveling 
//		performAndverifyWindowLeveling(viewerpage.viewboxImg2, 2, testcaseName, "For Dicom");
//
//		//Performing and verifying panning on DICOM Image (viewbox 2)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg2, testcaseName, "For Dicom");
//
//		//Performing and verifying zoom on JPEG image (viewbox 1)
//		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For Non Dicom");
//
//		//Performing and verifying pan performed on non DICOM (JPEG) image (viewbox 1)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg1, testcaseName, "For Non Dicom");
//
//		//Accept non DICOM (JPEG) image
////		
//		viewerpage.rejectResult(1);
//
//		//verifying checkbox is checked
//		verifyCheckBox(viewerpage.checkboxBoneage, testcaseName, "For Non Dicom");
//
//		viewerpage.acceptResult(1);
//
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying checkbox is checked or not on image.","Checkpoint_checkbox"+ "_" +testcaseName+ "_Non_DICOM");
//				
//		//Navigate back to studylist page
//		viewerpage.navigateBackToStudyListPage();
//
//	}
//
//	@Test(groups ={"Chrome"})
//	public void test02_E2E_TC1375_workflowUsingBonageData() throws InterruptedException,  AWTException, TimeoutException, ParseException {
//
//		String testcaseName = "E2E_TC1375";
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "E2E - Workflow using CR BONEAGE data");
//
//		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//		patientPage = new PatientListPage(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientPage.clickOnPatientRow(PatientName);
//
//		//get and verify tooltip		
//		//getAndVerifyToolTip(NSConstants.TOOLTIP_FIRSTROW_BONEAGE, NSConstants.TOOLTIP_LASTROW_BONEAGE, NSConstants.TOOLTIP_LOWER_LIMIT_BONEAGE, NSConstants.TOOLTIP_UPPER_LIMIT_BONEAGE );
//
//		patientstudypage = new SinglePatientStudyPage(driver);
//		//Clicking first study of Piccline patient data
//		patientstudypage.clickOntheFirstStudy();
//
//		//Waiting for loading of first viewbox 
//		viewerpage = new ViewerPage(driver);
//		//viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//
//		//Performing and verifying scroll on non DICOM (JPEG) image (viewbox 1)
//		performAndVerifyScroll(viewerpage.viewboxImg1, viewerpage.resultIdForViewBox1);
//
//		//Performing and verifying zoom on DICOM image (viewbox 2)
//		performAndverifyZoom(viewerpage.viewboxImg2, 2, testcaseName, "For Dicom");
//
//		//Performing and verifying windowleveling 
//		performAndverifyWindowLeveling(viewerpage.viewboxImg2, 2, testcaseName, "For Dicom");
//
//		//Performing and verifying panning on DICOM Image (viewbox 2)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg2, testcaseName, "For Dicom");
//
//		//Performing and verifying zoom on JPEG image (viewbox 1)
//		performAndverifyZoom(viewerpage.viewboxImg1, 1,testcaseName, "For Non Dicom");
//
//		//Performing and verifying pan performed on non DICOM (JPEG) image (viewbox 1)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg1, testcaseName, "For Non Dicom");
//
//		//Accept non DICOM (JPEG) image
//		viewerpage.checkBoneage(1);
//
//		//verifying checkbox is checked
//		verifyCheckBox(viewerpage.checkboxBoneage, testcaseName, "For Non Dicom");
//
//		//Navigate back to studylist page
//		viewerpage.navigateBackToStudyListPage();
//	}
//
//	@Test(groups ={"Chrome"})
//	public void test03_E2E_TC1377_workflowContentSelectorUsingDoeLillyData() throws InterruptedException, AWTException, TimeoutException, ParseException{
//
//		String testcaseName = "E2E_TC1377";
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify content selector using Doe lilly data");
//
//		patientPage = new PatientListPage(driver);
//
//		//Loading the patient on viewer
//		String filePath = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		//Clicking on Patient CT Doe_Lilly
//
//		patientPage.clickOnPatientRow(PatientName);
//
//		//get and verify tooltip
////		getAndVerifyToolTip(NSConstants.TOOLTIP_FIRSTROW_IMBIO, NSConstants.TOOLTIP_LASTROW_IMBIO, NSConstants.TOOLTIP_LOWER_LIMIT_IMBIO,NSConstants.TOOLTIP_UPPER_LIMIT_IMBIO);
//		patientstudypage = new SinglePatientStudyPage(driver);
//		//Clicking first study of CT Doe_Lilly
//		patientstudypage.clickOntheFirstStudy();
//
//		//Waiting for viewerpage to load
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageWithPDFToLoad();
//
//		//Performing and verifying zoom on DICOM image (viewbox 2)
//		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For Dicom");
//
//		//selecting and performing window leveling
//		String viewbox1_width = viewerpage.getText("visibility",viewerpage.windowWidthValueViewbox1);
//		String viewbox1_windowCenter = viewerpage.getText("visibility",viewerpage.windowCenterValueViewbox1);
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WW/WL from context menu" );
//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//		viewerpage.assertNotEquals(viewerpage.getText("visibility",viewerpage.windowWidthValueViewbox1)  , viewbox1_width, "verifying the WW/WL in viewbox1", "verified");		
//		viewerpage.assertNotEquals(viewerpage.getText("visibility",viewerpage.windowCenterValueViewbox1)  , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");
//
//		//Performing and verifying panning on DICOM Image (viewbox 2)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg1, testcaseName, "For Dicom");
//
//		//Scrolling pdf
//		viewerpage.scrollPdf(viewerpage.viewboxImage3);
//
//		//verifying scroll pdf and selecting series from content selector and verifying it 
//		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES01", filePath);
//		String secondSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES02", filePath);
//		String fifthSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES05", filePath);
//		String sixthSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES06", filePath);
//
////		viewerpage.selectSeriesFromContentSelector(1, firstSeriesDescription);
////		viewerpage.selectSeriesFromContentSelector(2, secondSeriesDescription);
////		viewerpage.compareElementImage(protocolName,viewerpage.mainViewer, "Verifying pdf is scrolled and additonal series is displayed using content selector", "Pdf_Scroll_Content_Selector_"+testcaseName);
////
////		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescription), "Verifying Checkbox appears next to newly displayed content series 1", "Verified Checkbox appears next to newly displayed content series 1");
////		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(2, secondSeriesDescription), "Verifying Checkbox appears next to newly displayed content series 2", "Verified Checkbox appears next to newly displayed content series 2");
////		viewerpage.assertFalse(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fifthSeriesDescription), "Verifying Checkbox disaappears for series 5", "Verified Checkbox disaappears for series 5");
////		viewerpage.assertFalse(viewerpage.validateSeriesIsSelectedOnContentSelector(2, sixthSeriesDescription), "Verifying Checkbox disappears for series 6", "Verified Checkbox disaappears for series 6");
//
//		//changing and verifying layout
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(NSConstants.TWO_BY_THREE_LAYOUT), "Verifying Layout change applied and result and image "
//				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");
//
//		//Navigate back to studylist page
//		viewerpage.navigateBackToStudyListPage();
//		viewerpage.assertTrue(driver.getCurrentUrl().contains("singlePatient"), "Verifying navigated to patient study list", "Verified navigated to patient study list");
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test05_E2E_TC1378_workflowContentSelectorUsingHeadCT() throws InterruptedException, AWTException
//	{	 
//
//		String testcaseName = "E2E_TC1378";
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Base Demo Steps -HEAD CT EVAL (AIDOC) DICOM and DICOM SC");		
//
//		//Loading the patient on viewer
//		String filePath = Configurations.TEST_PROPERTIES.get("Head_CT_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		//Clicking on Patient CT Doe_Lilly
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		//get and verify tooltip
//		//		getAndVerifyToolTip(NSConstants.TOOLTIP_FIRSTROW_IMBIO, NSConstants.TOOLTIP_LASTROW_IMBIO, NSConstants.TOOLTIP_LOWER_LIMIT_IMBIO,NSConstants.TOOLTIP_UPPER_LIMIT_IMBIO);
//		patientstudypage = new SinglePatientStudyPage(driver);
//		patientstudypage.waitForSingleStudyToLoad();
//
//		//Clicking first study of CT Doe_Lilly
//		patientstudypage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Performing and verifying zoom on DICOM image (viewbox 2)
//		performAndverifyZoom(viewerpage.viewboxImg2, 2, testcaseName, "For Dicom");
//
//		//Performing and verifying windowleveling 
//		performAndverifyWindowLeveling(viewerpage.viewboxImg2,2, testcaseName, "For Dicom");
//
//		//Performing and verifying panning on DICOM Image (viewbox 2)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg2, testcaseName, "For Dicom");
//
//		//Performing and verifying zoom on JPEG image (viewbox 1)
//		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For Non Dicom");
//
//		//Performing and verifying pan performed on non DICOM (JPEG) image (viewbox 1)
//		performAndverifyPanSynchronization(viewerpage.viewboxImg1, testcaseName, "For Non Dicom");
//
//
//		//Navigate back to studylist page
//		viewerpage.navigateBackToStudyListPage();
//
//	}
//
//	@Test(groups ={"Chrome"})
//	public void test06_E2E_TC1379_workflowUrlLaunching() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Feature Testing – Non-Image Related : URL Launching");
//		patientPage = new PatientListPage(driver);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		//opening patient list in new tab directly 
//		patientPage.openNewWindow("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.PATIENT_LIST_URL);
//		loginPage = new LoginPage(driver);
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage.waitForPatientPageToLoad();
//		//verifying patient list in new tab directly 
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains("patient"), "Verifying the Successful Login", "User is on page "+ patientPage.getCurrentPageURL());
//
//		//opening study list in new tab directly 
//		loginPage.navigateToURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL);
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		//verifying study list in new tab directly 
//		studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains("studylist"), "Verifying the Successful Login", "User is on page "+ studyPage.getCurrentPageURL());
//
//		//opening login page in new tab directly 
//		loginPage.navigateToURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.LOGIN_PAGE_URL);
//		//verifying login page in new tab 
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//
//	}
//
//	@Test(groups ={"Chrome"})
//	public void test07_E2E_TC1380_workflowForLogoff() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Feature Testing – Non-Image Related : Log Off");
//
//		patientPage = new PatientListPage(driver);
//
//		Header header = new Header(driver);
//
//		//Loading patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
//
//		//Clicking on scan 
//		header.logout();
//
//		loginAndMoveToPatientList();
//
//		//Clicking on scan 
//		header.logout();
//
//		loginAndMoveToPatientList();
//
//		//Clicking on first study
//		patientstudypage = new SinglePatientStudyPage(driver);
//		patientstudypage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Clicking on scan 
//		header.logout();
//
//		//Verifying whether user is on Login page
//		viewerpage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//
//	}
//
//	//======================VERIFICATION METHODS===============================================
//
//	private void getAndVerifyToolTip(String firstValueInPopUp, String lastValueInPopup, int subStringFirstValueStartDate, int subStringLastValueStartDate) throws TimeoutException, InterruptedException, ParseException {
//		// Mouse hover over Result Icon
//		patientstudypage = new SinglePatientStudyPage(driver);
//		patientstudypage.mouseHover("presence",patientstudypage.getEnvoyAlCell(1)); 
//
//		String str = patientstudypage.getEnvoyAlTooltip();
//		//Verifing tooltip information
//		patientstudypage.assertTrue(str.contains(firstValueInPopUp), "Verifying "+firstValueInPopUp +" is present in tooltip ", lastValueInPopup+ " is present in tooltip");
//		patientstudypage.assertTrue(str.contains("Last updated:"), "Verifying Last updated: is present in tooltip", "Last updated: is present in tooltip");
//		patientstudypage.assertTrue(str.contains("Result Status:"), "Verifying Result Status: Run with findings is present in tooltip", "Result Status: Run with findings is present in tooltip");
//		patientstudypage.assertTrue(str.contains(lastValueInPopup), "Verifying "+lastValueInPopup+" is present in tooltip", lastValueInPopup+"  is present in tooltip");
//		//Verifying date format
//		patientstudypage.assertTrue(patientstudypage.verifyDateFormat("MM/dd/yyyy, hh:mm aaa", patientstudypage.getDateTimeFromToolTip()), "Verifying date format present in tooltip","Date format is in MM/dd/yyyy, hh:mm ");
//	}
//
//	private void performAndVerifyScroll(WebElement element, WebElement elementViewboxResult) throws InterruptedException, AWTException {
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify scroll icon is selected.");
//		String beforeScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
//		viewerpage.dragAndReleaseOnViewer(element, 0, 0, 10, 10);
//		String afterScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
//		viewerpage.assertFalse(beforeScrollResultID.equals(afterScrollResultID), "Verifying scroll", "scroll");
//	}
//
//	private void performAndverifyPanSynchronization(WebElement element, String testcaseName, String imageType) throws InterruptedException, AWTException {
//		viewerpage.selectPanFromQuickToolbar(element);
//		viewerpage.dragAndReleaseOnViewer(element, 0, 0, 300, 0);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","Checkpoint_Right"+ "_" +testcaseName+ "_" +imageType);
//		viewerpage.dragAndReleaseOnViewer(element, 300, 0, -600, 0);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","Checkpoint_left"+ "_" +testcaseName+ "_" +imageType);
//		viewerpage.dragAndReleaseOnViewer(element, -300, 0, 300, -100);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","Checkpoint_Top"+ "_" +testcaseName+ "_" +imageType);
//		viewerpage.dragAndReleaseOnViewer(element, 0, -100, 0, 200);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","Checkpoint_Bottom"+ "_" +testcaseName+ "_" +imageType);	
//	}
//
//	private void performAndverifyWindowLeveling(WebElement element, int elementForWL, String testcaseName, String imageType) throws AWTException, InterruptedException {
//		String valueBeforeWL = viewerpage.getValueOfWindowWidth(elementForWL);
//		viewerpage.selectWindowLevelFromQuickToolbar(element);
//		viewerpage.dragAndReleaseOnViewer(element, 0, 39, -10, -20);
//		String valueAfterWL= viewerpage.getValueOfWindowWidth(elementForWL);
//		viewerpage.assertFalse(valueAfterWL.equals(valueBeforeWL),"", "Values are same");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify panned location should not be reset when WW/WL is applied." + "_" +testcaseName+ "_" +imageType);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WW/WL.","Checkpoint_WW_WL"+ "_" +testcaseName+ "_" +imageType);
//
//	}
//
//	private void performAndverifyZoom(WebElement element, int zoomLevelViewboxNumber, String testcaseName, String imageType) throws AWTException, InterruptedException{
//		viewerpage.selectZoomFromQuickToolbar(element);
//		int beforeZoom = viewerpage.getZoomLevel(zoomLevelViewboxNumber);
//		viewerpage.dragAndReleaseOnViewer(element, 0, 0, -10, 50);
//		int afterZoom = viewerpage.getZoomLevel(zoomLevelViewboxNumber);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify images should Zoom.."+ "_" +testcaseName+ "_" +imageType);
//		viewerpage.assertTrue(beforeZoom > afterZoom, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should Zoom.","Checkpoint Zoom"+ "_" +testcaseName+ "_" +imageType);
//	
//	}
//
//	
//
//	private boolean verifyCheckBox(WebElement element, String testcaseName, String imageType) throws InterruptedException {
//		viewerpage.changeLayout();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying checkbox is checked or not on image.","Checkpoint checkbox"+ "_" +testcaseName+ "_" +imageType);
//		return viewerpage.isChecked(viewerpage.checkboxBoneage);
//	}
//
//	private void loginAndMoveToPatientList() {
//
//
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//
//		//Entering credentials and clicking on Login
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		//Clicking on passed patient - Liver9
//		patientPage.clickOnPatientRow(PatientName);
//	}
//
//
//
//
//}
