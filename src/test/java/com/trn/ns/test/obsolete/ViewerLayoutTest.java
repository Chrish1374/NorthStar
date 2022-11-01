//package com.trn.ns.test.obsolete;
////package com.terarecon.northstar.test.viewer;
////import java.awt.AWTException;
////import java.io.IOException;
////import java.util.Arrays;
////import java.util.Date;
////import java.util.LinkedHashSet;
////import java.util.List;
////import java.util.Set;
////
////import jxl.read.biff.BiffException;
////
////import org.openqa.selenium.WebElement;
////import org.testng.Reporter;
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.terarecon.northstar.dataProviders.DataProviderArguments;
////import com.terarecon.northstar.dataProviders.ExcelDataProvider;
////import com.terarecon.northstar.enums.BrowserType;
////import com.terarecon.northstar.page.factory.ContentSelector;
////import com.terarecon.northstar.page.factory.DatabaseMethods;
////import com.terarecon.northstar.page.factory.Header;
////import com.terarecon.northstar.page.factory.LoginPage;
////import com.terarecon.northstar.page.factory.NSConstants;
////import com.terarecon.northstar.page.factory.PatientListPage;
////import com.terarecon.northstar.page.factory.SimpleLine;
////import com.terarecon.northstar.page.factory.SinglePatientStudyPage;
////import com.terarecon.northstar.page.factory.ViewerPage;
////import com.terarecon.northstar.test.base.TestBase;
////import com.terarecon.northstar.test.configs.Configurations;
////import com.terarecon.northstar.utilities.DataReader;
////import com.terarecon.northstar.utilities.ExtentManager;
////
////@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
////public class ViewerLayoutTest extends TestBase {
////
////	private ViewerPage viewerpage;
////	private LoginPage loginPage;
////	private PatientListPage patientPage;
////	private SinglePatientStudyPage singlePatientStudyPage;
////	private ExtentTest extentTest;
////
////	// Get Patient Name
////	String filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
////	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
////
////	// Get Patient Name
////	String filePath1=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
////	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
////
////	//Get Patient Name
////	String filePath2 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
////	String boneAgePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);	
////
////	//Get Patient Name
////	String filePath3 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
////	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
////
////	//Get Patient Name
////	String filePath4 = Configurations.TEST_PROPERTIES.get("Picline_filepath");
////	String piccLinePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
////
////	private String filePath5 = Configurations.TEST_PROPERTIES.get("SQA_Testing");
////	String sqaTestingPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
////	
////	private String filePath6 = Configurations.TEST_PROPERTIES.get("LiverTumor");
////	String liverTumorPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath6);
////
////	String filePath7 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
////	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath7);
////	
////	
////	
////	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
////	String firstSeriesDescriptionGSPS_Multiseries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath"));
////	private ContentSelector contentSelector;
////
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod() {
////
////		loginPage = new LoginPage(driver);
////		loginPage.navigateToBaseURL();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////	}
////
////
////	@Test(groups ={"firefox","Chrome","multimonitor","US60","Sanity"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
////	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test02_ChangeLayout" })
////	public void test02_US60_TC782_SelectPageLayoutMultiMonitor(String Testcase_id, String PatientName,String Modality, String Rows, String Columns) throws InterruptedException,IOException {
////		
////		Reporter.getCurrentTestResult().setAttribute("TEST_CASE_ID", Testcase_id);
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("US60_TC782_"+Modality+"verify the  function select page layout option to change window layout-Single Monitor for "+Modality+" modality data");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(PatientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////
////		String parentWindow = driver.getWindowHandle();
////		viewerpage.openOrCloseChildWindows(2);
////
////		viewerpage.switchToWindow(parentWindow);
////
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
////
////		Set<String> childWinHandles = driver.getWindowHandles();
////		for (String childWindow : childWinHandles) 
////			if(!childWindow.equals(parentWindow)){
////				viewerpage.switchToWindow(childWindow);
////				viewerpage.maximizeWindow();
////			}
////
////		viewerpage.switchToWindow(parentWindow);
////
////
////		int j = 1;
////		for (int i = 0; i < viewerpage.totalNumberOfLayoutIcons.size()&&i<2; i++) {
////			WebElement layoutName = viewerpage.totalNumberOfLayoutIcons.get(i);
////			viewerpage.selectLayout(layoutName,true);
////			viewerpage.waitForAllImagesToLoad();
////			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint " + j + " Verify the layout changed based on the selected option on parent window",Testcase_id + "_" + Modality + "_" + "Parent_Window_" + "Checkpoint" + j);
////			
////			childWinHandles.remove(parentWindow);
////			for (String childWindow : childWinHandles) {
////				if (!childWindow.equals(parentWindow)) {
////					viewerpage.switchToWindow(childWindow);					
////					viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint " + j + " Verify the layout changed based on the selected option on child window",Testcase_id + "_" + Modality + "_" + "Child_Window_" + "Checkpoint" + j);
////					j++;
////				}
////			}
////			viewerpage.switchToWindow(parentWindow);
////		}
////
////	}
////
////	@Test(groups = { "firefox", "Chrome","multimonitor","US60"})
////	public void test03_01_US60_TC783_SelectPageLayoutFromChildWindow() throws InterruptedException, IOException {
////		
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("US60_TC783 verify the function select page layout option to change window layout-Single Monitor");
////
////		String parentWindowID= driver.getWindowHandle();
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(patientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();	
////
////
////		viewerpage.openOrCloseChildWindows(3);		
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
////
////		Set<String> childWinHandles = driver.getWindowHandles();
////		for (String childWindow : childWinHandles) 
////			if(!childWindow.equals(parentWindowID)){
////				viewerpage.switchToWindow(childWindow);
////				viewerpage.maximizeWindow();
////			}
////
////		int j = 0;
////		List<WebElement> layoutToBeSlected = Arrays.asList(viewerpage.oneByOneLayoutIcon, viewerpage.oneByOneTAndOneByTwoBLayoutIcon, viewerpage.twoByOneLAndOneByOneRLayoutIcon);
////		int i =1;
////		//Select layout after selecting checkbox apply to all monitor and verfiying that selected layout is applied to all the open windows. 
////		for (String childWindow : childWinHandles) {
////
////			viewerpage.switchToWindow(childWindow);
////			viewerpage.selectLayout(layoutToBeSlected.get(j),true);
////			viewerpage.waitForAllImagesToLoad();
////
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify selected layout "+ layoutToBeSlected.get(j) +" is applied to all the open windows");
////			j++;
////			for (String childWindows : childWinHandles) {
////				viewerpage.switchToWindow(childWindows);				
////				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint "+i+" Verify layout selected is applied on all the windows and series loaded on viewport accordingly", "TC783_"+"Checkpoint"+i);
////				i++;
////			}
////		}
////	}
////
////	@Test(groups = { "firefox", "Chrome","multimonitor","US60"})
////	public void test03_02_US60_TC783_SelectPageLayoutFromChildWindowAfterUncheckApplyTOAllMonitor() throws InterruptedException, IOException {
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription( "verify the function select page layout option to change window layout-Single Monitor after uncheck checkbox apply to all monitor");
////
////		String parentWindowID= driver.getWindowHandle();
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(patientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////
////
////		viewerpage.openOrCloseChildWindows(3);	
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
////
////
////		Set<String> childWinHandles = driver.getWindowHandles();
////		for (String childWindow : childWinHandles) 
////			if(!childWindow.equals(parentWindowID)){
////				viewerpage.switchToWindow(childWindow);
////				viewerpage.maximizeWindow();
////			}
////		int j = 0;
////		List<WebElement> layoutToBeSlected = Arrays.asList(viewerpage.oneByOneLayoutIcon, viewerpage.oneByOneTAndOneByTwoBLayoutIcon, viewerpage.twoByOneLAndOneByOneRLayoutIcon);
////		int i =1;
////		//Select layout after uncheck checkbox apply to all monitor and verfiying that selected layout is applied to current window.
////		for (String childWindow : childWinHandles) {
////			viewerpage.switchToWindow(childWindow);			
////			viewerpage.selectLayout(layoutToBeSlected.get(j),true);
////			viewerpage.waitForAllImagesToLoad();
////
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify selected layout "+ layoutToBeSlected.get(j) +" is applied to current window");
////			j++;
////			for (String childWindows : childWinHandles) {
////				viewerpage.switchToWindow(childWindows);
////				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint "+i+" Verify layout selected is applied on the current window ", "TC783_"+"Uncheck_Checkbox"+"Checkpoint"+i);
////				i++;
////			}
////		}
////	}
////
////	
////
////	@Test(groups ={"firefox","Chrome","Edge","multimonitor","US136"})
////	public void test06_US136_TC290_VerfiyAllRadialOperationOnChildWindow() throws InterruptedException, AWTException{
////		String testcaseName = "US136_TC290";
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify all operations on child window");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(liver9Patient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForViewerpageToLoad();
////		viewerpage.switchToWindow(childWinHandles.get(1));
////
////		viewerpage.waitForViewerpageToLoad();
////
////		//changing layout to 2X3 and getting the Deltas w.r.t. fifth viewbox where we are gonna change the WWWL
////		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Performing and verifying scroll on  DICOM (JPEG) image (viewbox 1)
////		performAndVerifyScroll(viewerpage.viewboxImg1, viewerpage.currentScrollPositionOfViewbox);
////
////		//Performing and verifying zoom on DICOM image (viewbox 1)
////		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For Dicom");
////
////		//Performing and verifying windowleveling 
////		performAndverifyWindowLeveling(viewerpage.viewboxImg1, 1, testcaseName, "For Dicom");
////
////		//Performing and verifying panning on DICOM Image (viewbox 1)
////		performAndverifyPanSynchronization(viewerpage.viewboxImg1, testcaseName, "For Dicom");
////
////		//Performing and verifying cine on  DICOM (JPEG) image (viewbox 1)
////		performAndverifyCine(viewerpage.viewboxImg1, viewerpage.currentScrollPositionOfViewbox);
////
////	}
////
////	
////	//	Verify zoom synchronization on DICOM + jpeg images on layout change and on child window
////	@Test(groups ={"multimonitor","dbConfig","DE268"})
////	public void test14_DE268_TC1270_VerfiyZoomSynForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, AWTException, IOException{
////		String testcaseName = "DE268_TC1270";
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription( "Verify Zoom sync on parent - child window DICOM and non DICOM images on layout change");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		DatabaseMethods db = new DatabaseMethods(driver);
////		db.updateValueInSelectorTypeTable("TRUE");
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForAllImagesToLoad();
////
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForAllImagesToLoad();
////
////
////		//changing layout to 1X1
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////
////		//Performing and verifying zoom on non DICOM (JPEG) image (viewbox 1)
////		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For non Dicom");
////
////		//Zoom value of dicom image
////		int afterPerformAction = viewerpage.getZoomLevel(1);
////
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.waitForAllImagesToLoad();
////		viewerpage.waitForViewerpageToLoad(1);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying Zoom should not performed on DICOM images on parent child windows."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.assertEquals(afterPerformAction, viewerpage.getZoomLevel(1), "Verifying zoom syn on Dicom images on parent child window", "Verified zoom syn on Dicom images on parent child window");
////
////	}
////
////	//TC1278  Verify scroll synchronization on DICOM + jpeg images on layout change and on child window
////	@Test(groups ={"multimonitor","dbConfig","DE268"})
////	public void test15_DE268_TC1278_VerfiyScrollSynForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, AWTException, IOException{
////		String testcaseName = "DE268_TC1278";
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify scroll sync on parent - child window DICOM and non DICOM images on layout change");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		DatabaseMethods db = new DatabaseMethods(driver);
////		db.updateValueInSelectorTypeTable("TRUE");
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForAllImagesToLoad();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForAllImagesToLoad();
////		viewerpage.waitForViewerpageToLoad(2);
////
////		//Zoom value of dicom image
////		String beforePerformAction = viewerpage.getCurrentScrollPosition(2);
////
////		//changing layout to 1X1 
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Performing and verifying zoom on non DICOM (JPEG) image (viewbox 1)
////		performAndVerifyScroll(viewerpage.viewboxImg1, viewerpage.getResultIDForViewbox(1));
////
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.waitForAllImagesToLoad();
////
////		//Zoom value of dicom image
////		String afterPerformAction = viewerpage.getCurrentScrollPosition(1);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying scroll should not performed on DICOM images on parent child windows."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.assertEquals(beforePerformAction,afterPerformAction, "Verifying scroll sync on Dicom images parent child window", "Verified scroll sync on Dicom images parent child window");
////
////	}
////
////	//TC1274  Verify PAN synchronization on DICOM + jpeg images on layout change and on child window
////	@Test(groups ={"multimonitor","dbConfig","DE268"})
////	public void test16_DE268_TC1274_VerfiyPANSynForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, AWTException, IOException{
////		String testcaseName = "DE268_TC1274";
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify PAN sync on parent - child window DICOM and non DICOM images on layout change");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		DatabaseMethods db = new DatabaseMethods(driver);
////		db.updateValueInSelectorTypeTable("TRUE");
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForAllImagesToLoad();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForAllImagesToLoad();
////
////		//changing layout to 1X1 
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Added zoom so that we can see the image is panned
////		//Performing and verifying zoom on non DICOM (JPEG) image (viewbox 1)
////		performAndverifyZoom(viewerpage.viewboxImg1, 1, testcaseName, "For non Dicom");
////
////		//viewerpage.viewboxImg1, testcaseName, "For Dicom"
////		viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
////		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, 300, 0);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying PAN should not performed on DICOM images on parent window."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","Checkpoint_Right"+ "_" +testcaseName+ "_" +"For non Dicom");
////
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying PAN should not performed on DICOM images on child windows."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should not PAN towards right.","Checkpoint_Right"+ "_" +testcaseName+ "_" +"For Dicom");
////	}
////
////	//TC1275  Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images
////
////	@Test(groups ={"IE11","Chrome","firefox","Edge","DE268"})
////	public void test17_DE268_TC1275_verifyPanSyncStoppedWhenSpaceBarPressed() throws InterruptedException, AWTException 
////	{
////		String testcaseName = "DE268_TC1275";
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images");
////
////
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad(4);
////		contentSelector = new ContentSelector(driver);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
////		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
////
////		//changing layout to 3x3 
////		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon, true);
////		//viewerpage.waitForAllImagesToLoad();
////
////		//Selecting dicom image in 3rd viewbox to check sync
////		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
////		contentSelector.selectSeriesFromContentSelector(3, firstSeriesDescription);
////
////		contentSelector.selectSeriesFromContentSelector(4, firstSeriesDescription);
////
////		//Selecting non dicom images
////
////		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);
////
////		contentSelector.selectResultFromContentSelector(6, firstResultDescription);
////
////		//performing sync off
////		viewerpage.performSyncONorOFF();
////
////		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
////		viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg2);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
////		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
////		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify All series should NOT PAN synchronously.");
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying  All series should NOT PAN synchronously. Only image on which PAN was selected should PAN.", testcaseName);
////	}
////
////	//TC1276  Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images on layout change and on child window 
////
////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","DE268"})
////	public void test18_DE268_TC1276_verifyPanSyncOnChildWindowStoppedWhenSpaceBarPressed() throws InterruptedException, AWTException 
////	{
////		String testcaseName = "DE268_TC1276";
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images on layout change and on child window");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad(2);
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForViewerpageToLoad(2);
////
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		//changing layout to 2x2 
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Selecting dicom image in 3rd viewbox to check sync
////		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
////		viewerpage.selectSeriesFromContentSelector(1, firstSeriesDescription);
////
////		viewerpage.selectSeriesFromContentSelector(2, firstSeriesDescription);
////
////		//Selecting non dicom images
////
////		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);
////
////		viewerpage.selectResultFromContentSelector(3, firstResultDescription);
////
////		viewerpage.selectResultFromContentSelector(4, firstResultDescription);
////
////		//performing sync off
////		viewerpage.performSyncONorOFF();
////
////		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
////		viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg2);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
////		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
////		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify All series should NOT PAN synchronously on chlid window.");
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying  All series should NOT PAN synchronously on chlid window. Only image on which PAN was selected should PAN.",testcaseName);
////	}
////
////	//TC1280  Verify scroll synchronization can be disabled using space bar on DICOM + jpeg images on layout change and on child window
////	@Test(groups ={"multimonitor","DE268"})
////	public void test19_DE268_TC1280_VerfiyScrollSynDisabledForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, IOException{
////		String testcaseName = "DE268_TC1280";
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription( "TC1280  Verify scroll synchronization can be disabled using space bar on DICOM + jpeg images on layout change and on child window");
////
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForAllImagesToLoad();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForAllImagesToLoad();
////		viewerpage.waitForViewerpageToLoad(2);
////
////		//scroll value of dicom image
////		String beforePerformAction = viewerpage.getCurrentScrollPosition(2);
////
////		//changing layout to 2x2 
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////		viewerpage.waitForViewerpageToLoad(2);
////		//Performing and verifying scroll on non DICOM (JPEG) image (viewbox 1)
////		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, 10, 10);
////		String afterPerformAction = viewerpage.getCurrentScrollPosition(2);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying scroll sync should be off using spacebar pressed on DICOM images on Parent window."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.assertEquals(beforePerformAction,afterPerformAction, "Verifying scroll sync should be off using spacebar pressed on DICOM images on parent window", "Verified sync should be off using spacebar pressed on DICOM images on Parent window");
////
////
////		//scroll value of non dicom image
////		String beforePerformActionJPEG = viewerpage.getTextOfResultIDForViewbox(1);
////
////		//Performing and verifying scroll on non DICOM (JPEG) image (viewbox 1)
////		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg2, 0, 0, 10, 10);
////		String afterPerformActionJPEG = viewerpage.getTextOfResultIDForViewbox(1);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying scroll sync should be off using spacebar pressed on DICOM images on Parent window."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.assertEquals(beforePerformActionJPEG,afterPerformActionJPEG, "Verifying scroll sync should be off using spacebar pressed on DICOM images on parent window", "Verified sync should be off using spacebar pressed on DICOM images on Parent window");
////
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.waitForAllImagesToLoad();
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying scroll sync should be off using spacebar pressed on DICOM images on child windows."+ "_" +testcaseName+ "_" + "Dicom Image");
////		viewerpage.assertEquals(beforePerformAction,afterPerformAction, "Verifying scroll sync should be off using spacebar pressed on DICOM images on child windows", "Verified sync should be off using spacebar pressed on DICOM images on child windows");
////
////	}
////
////
////	//	TC1272  Verify zoom synchronization can be disabled using space bar on DICOM + jpeg images on layout change and on child window     
////	@Test(groups ={"multimonitor","DE268"})
////	public void test22_DE268_TC1272_VerfiyZoomSynForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, AWTException, IOException{
////		
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify zoom synchronization can be disabled using space bar on DICOM + jpeg images");
////
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForAllImagesToLoad();
////		viewerpage.waitForViewerpageToLoad(2);
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToWindow(parentWindow);
////
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.waitForAllImagesToLoad();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.browserBackWebPage();
////
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage.waitForAllImagesToLoad();
////
////
////		//changing layout to 1x1 
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, false);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon, false);
////		viewerpage.waitForAllImagesToLoad();
////
////		//performing sync off
////		viewerpage.performSyncONorOFF();
////
////		//Selecting dicom image in 3rd viewbox to check sync
////		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
////		viewerpage.selectSeriesFromContentSelector(2, firstSeriesDescription);
////
////		//Selecting non dicom images
////
////		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);
////
////		viewerpage.selectSeriesFromContentSelector(3, firstSeriesDescription);
////
////		viewerpage.selectResultFromContentSelector(4, firstResultDescription);
////
////
////		//Performing and verifying zoom on non DICOM (JPEG) image (viewbox 1)
////		viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg2);
////
////		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage2, 10, 10, -100, 100);
////
////
////		//Zoom value of dicom image
////		int afterPerformAction = viewerpage.getZoomLevel(2);
////
////		viewerpage.assertNotEquals(afterPerformAction, viewerpage.getZoomLevel(1), "Verifying zoom syn on Dicom images on changing Parent child windows", "Verified zoom syn on Dicom images on changing  Parent child windows");
////		viewerpage.assertNotEquals(afterPerformAction, viewerpage.getZoomLevel(3), "Verifying zoom syn on Dicom images on changing Parent child windows", "Verified zoom syn on Dicom images on changing  Parent child windows");
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.assertNotEquals(afterPerformAction, viewerpage.getZoomLevel(1), "Verifying zoom syn on Dicom images on changing Parent child windows", "Verified zoom syn on Dicom images on changing  Parent child windows");
////	}
////
////	
////
////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US352"})
////	public void test29_US352_TC1242_VerfiyTextOverlayForPICCLineDataOnMutliMointor() throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Text overlay for PICC Line data on MultiMointor");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(piccLinePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad(2);
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default Annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////		//Verify Text overlay for Patient Date on Viewbox2
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify text overlay on orginal Patient Data for default annotation");
////		verifyDefaultOverlayDataForBoneage(filePath4, 1);
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
////
////		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Minimun Annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////		//Verify Text overlay for Patient Date on Viewbox2
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay on orginal Patient Data for Minimum annotation");
////		verifyMinimumOverlayDataForBoneage(filePath4, 1);
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectAnnotation(ViewerPageConstants.FULL_ANNOTATION);
////
////
////		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Full Annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		//Verify Text overlay for Patient Date on Viewbox2
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for Full annotation");
////		verifyMinimumOverlayDataForBoneage(filePath4, 1);
////
////	}
////
////	
////
////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US352"})
////	public void test31_US352_TC1243_VerfiyTextOverlayOnResutSetForBoneAgeOnMultiMointor() throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Text overlay on Result Set for BoneAge");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad(2);
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////		//Switching to child window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		viewerpage.maximizeWindow();
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon, true);
////		viewerpage.waitForAllImagesToLoad();
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default Annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////		//Verify Text overlay for Patient Date on Child Window
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify text overlay on orginal Patient Data for default annotation");
////		verifyDefaultOverlayDataForBoneage(filePath2, 1);
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
////
////		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in minimum Annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////
////		//Verify Text overlay for Patient Date on Viewbox2
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay on orginal Patient Data for Minimum annotation");
////		verifyMinimumOverlayDataForBoneage(filePath2, 1);
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.selectAnnotation(ViewerPageConstants.FULL_ANNOTATION);
////
////		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Full annotation mode");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
////		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.zoomLevelViewbox1), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
////
////		//Verify Text overlay for Patient Date on Viewbox2
////		viewerpage.switchToWindow(childWinHandles.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for Full annotation");
////		verifyMinimumOverlayDataForBoneage(filePath2, 1);
////
////	}
////
////	
////	@Test(groups = {"Chrome","multimonitor","DE450"})
////	public void test33_DE450_TC1880_verifySeriesPeristenceLayoutChange() throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Layout change is not working on multimonitor - Series Persistence and Layout coverage");
////		patientPage = new PatientListPage(driver);
////
////		String expectedSeriesDescription1 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription2 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription3 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription5 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription6 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES06_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription7 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES07_TEXTOVERLAY, filePath5);
////		String expectedSeriesDescription8 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES08_TEXTOVERLAY, filePath5);
////
////		patientPage.clickOnPatientRow(sqaTestingPatientName);
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOnStudy(1);		
////		
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		viewerpage.waitForAllImagesToLoad();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate that second and third window content is emtpy by default and multi-monitor menu closes after selecting an option");	
////
////		viewerpage.openOrCloseChildWindows(4);
////
////		viewerpage.waitForNumberOfWindowsToEqual(4);
////
////		// validating only 2 windows(1 parent, 1 child) to be open
////		LinkedHashSet<String> windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
////		viewerpage.assertEquals(windowHandles.size(), 4, "Validating only 4 windows to be open", "Only 4 windows are open");
////
////		windowHandles.remove(parentWindow);	
////
////		String childWindow3 = windowHandles.iterator().next();
////		windowHandles.remove(childWindow3);	
////
////		String childWindow2 = windowHandles.iterator().next();
////		windowHandles.remove(childWindow2);	
////
////		String childWindow1 = windowHandles.iterator().next();
////		windowHandles.remove(childWindow1);	
////		
////		viewerpage.switchToWindow(parentWindow);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate that series that cannot fit in the first browser window are loaded into the additional browser window");	
////		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon, true);
////		
////		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription3);
////		validateSeriesDescriptionInEachWindow(1, childWindow2, expectedSeriesDescription5);
////		validateSeriesDescriptionInEachWindow(1, childWindow3, expectedSeriesDescription7);
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),parentWindow, "Validate switching back to parent window", "Successfully switched to parent window");
////
////		viewerpage.switchToWindow(childWindow1);
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate that series that cannot fit in the first browser window are loaded into the additional browser window");	
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon, true);
////		
////		validateSeriesDescriptionInEachWindow(1, parentWindow, expectedSeriesDescription1);
////		validateSeriesDescriptionInEachWindow(2, parentWindow, expectedSeriesDescription2);
////		validateSeriesDescriptionInEachWindow(3, parentWindow, expectedSeriesDescription3);
////		validateSeriesDescriptionInEachWindow(4, parentWindow, expectedSeriesDescription4);
////
////		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription5);
////		validateSeriesDescriptionInEachWindow(2, childWindow1, expectedSeriesDescription6);
////		validateSeriesDescriptionInEachWindow(3, childWindow1, expectedSeriesDescription7);
////		validateSeriesDescriptionInEachWindow(4, childWindow1, expectedSeriesDescription8);
////		
////	}
////	private void validateSeriesDescriptionInEachWindow(int seriesNum, String childWindow, String expectedSeriesDescription) throws InterruptedException {
////		viewerpage.switchToWindow(childWindow);
////		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),childWindow, "Validate switching to window", "Successfully switched to window: "+childWindow);
////		viewerpage.waitForElementVisibility(viewerpage.getSeriesDescriptionOverlay(seriesNum));
////		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(seriesNum),expectedSeriesDescription, "Validate additional series that cannot fit in Parent window moves to "+seriesNum+"st child window", expectedSeriesDescription+" series is successfully loaded in "+seriesNum+"st child window");
////
////	}
////	
//////	private void verifyOnlyOneResultSelectorIsChecked() {
//////		viewerpage.checkBoneage(1);
//////		viewerpage.assertTrue(viewerpage.verifyResultSelectorIsChecked(viewerpage.selectBoneageImage(1)), "Verifying Checkbox", "Verified checkbox:checked");
//////		viewerpage.checkBoneage(2);
//////		viewerpage.assertTrue(viewerpage.verifyResultSelectorIsChecked(viewerpage.selectBoneageImage(2)), "Verifying Checkbox", "Verified checkbox:checked");
//////		viewerpage.assertFalse(viewerpage.verifyResultSelectorIsChecked(viewerpage.selectBoneageImage(1)), "Verifying Checkbox", "Verified checkbox:checked");
//////		viewerpage.assertFalse(viewerpage.verifyResultSelectorIsChecked(viewerpage.selectBoneageImage(3)), "Verifying Checkbox", "Verified checkbox:checked");
//////	}
////
////	private void verifyWindowLevelForNonDicomImage(WebElement element, String testcaseName, String imageType) throws AWTException, InterruptedException {
////		viewerpage.selectWindowLevelFromQuickToolbar(element);
////		viewerpage.dragAndReleaseOnViewer(element, 0, 39, -10, -20);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify  WW/WL is applied." + "_" +testcaseName+ "_" +imageType);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WW/WL.","Checkpoint_WW_WL"+ "_" +testcaseName+ "_" +imageType);
////
////	}
////
////	private void drawAndVerifyLinearMeasurement(WebElement element, int viewbox,  String testcaseName, String imageType ) throws AWTException, InterruptedException {
////		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
////		//		viewerpage.selectLinearMeasurementFromContextMenu(element);
////		SimpleLine line = new SimpleLine(driver);
////		line.selectLinearMeasurementFromContextMenu(element);
////		// Drawing a horizontal line
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint_Linear", "Drawing the measurement on coordinates (0,0,100,0)" );
////		viewerpage.dragAndReleaseOnViewerWithClick(10,10, 100, 100);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify the measurement drawn @ coordinates (0,0,100,0)", "Viewbox_Measurement_ContextMenu" +testcaseName +"_" +imageType);
////	}
////
////	private void performAndverifyPanSynchronization(WebElement element, String testcaseName, String imageType) throws InterruptedException, AWTException {
////		viewerpage.selectPanFromQuickToolbar(element);
////		viewerpage.dragAndReleaseOnViewer(element, 0, 0, 300, 0);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","Checkpoint_Right"+ "_" +testcaseName+ "_" +imageType);
////		viewerpage.dragAndReleaseOnViewer(element, 300, 0, -600, 0);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","Checkpoint_left"+ "_" +testcaseName+ "_" +imageType);
////		viewerpage.dragAndReleaseOnViewer(element, -300, 0, 300, -100);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","Checkpoint_Top"+ "_" +testcaseName+ "_" +imageType);
////		viewerpage.dragAndReleaseOnViewer(element, 0, -100, 0, 200);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","Checkpoint_Bottom"+ "_" +testcaseName+ "_" +imageType);	
////	}
////
////	private void performAndverifyWindowLeveling(WebElement element, int elementForWL , String testcaseName, String imageType) throws AWTException, InterruptedException {
////		String valueBeforeWL = viewerpage.getValueOfWindowWidth(elementForWL);
////		viewerpage.selectWindowLevelFromQuickToolbar(element);
////		viewerpage.dragAndReleaseOnViewer(element, 0, 39, -10, -20);
////		String valueAfterWL= viewerpage.getValueOfWindowWidth(elementForWL);
////		viewerpage.assertFalse(valueAfterWL.equals(valueBeforeWL),"", "Values are same");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify panned location should not be reset when WW/WL is applied." + "_" +testcaseName+ "_" +imageType);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WW/WL.","Checkpoint_WW_WL"+ "_" +testcaseName+ "_" +imageType);
////	}
////
////	private void performAndverifyZoom(WebElement element, int zoomLevelViewboxNumber, String testcaseName, String imageType) throws AWTException, InterruptedException{
////		viewerpage.selectZoomFromQuickToolbar(element);
////		int beforeZoom = viewerpage.getZoomLevel(zoomLevelViewboxNumber);
////		viewerpage.dragAndReleaseOnViewer(element, 10, 10, -100, 100);
////		int afterZoom =  viewerpage.getZoomLevel(zoomLevelViewboxNumber);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify images should Zoom.."+ "_" +testcaseName+ "_" +imageType);
////		viewerpage.assertTrue(beforeZoom != afterZoom, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should Zoom.","Checkpoint Zoom"+ "_" +testcaseName+ "_" +imageType);
////	}
////
////	private void performAndVerifyScroll(WebElement element, WebElement elementViewboxResult) throws InterruptedException, AWTException {
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify scroll icon is selected.");
////		String beforeScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
////		viewerpage.dragAndReleaseOnViewer(element, 0, 0, 10, 10);
////		String afterScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
////		viewerpage.assertFalse(beforeScrollResultID.equals(afterScrollResultID), "Verifying scroll", "scroll");
////	}
////
////	private void performAndverifyCine(WebElement element, WebElement elementViewboxResult) throws AWTException, InterruptedException{
////		String beforeScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
////		//enabling cine option
////		viewerpage.selectCineFromContextMenuOthertab(element);
////		//pause cine icon
////		viewerpage.selectCineFromRadialBar(element);
////		String afterScrollResultID = viewerpage.getText("visibility", elementViewboxResult);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify images should cine..");
////		viewerpage.assertFalse(beforeScrollResultID.equals(afterScrollResultID), "Verifying scroll", "scroll");
////	}
////
////	public void verifyDefaultOverlayDataForBoneage(String filePath, int viewNum) {
////
////		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientNameOverlayText(viewNum)) , "Verifying patient name textoverlay", "verified patient name textoverlay");
////		viewerpage.assertTrue(("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(viewerpage.getPatientIDOverlayText(viewNum)), "Verifying patient id textoverlay", "verified patient id textoverlay");	
////		viewerpage.assertTrue(DataReader.getStudyDetails(NSConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified tudy date time textoverlay");
////		viewerpage.assertTrue(DataReader.getPatientDetails(NSConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientSexOverlayText(viewNum)) , "Verifying patient sex textoverlay", "verified patient sex textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImageCurrentScrollPositionOverlayText(viewNum)),"Verifying image current scroll textoverlay", "verified image current scroll textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowWidthValueOverlayText(viewNum)),"Verifying window width textoverlay", "verified window width textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowCenterValueOverlayText(viewNum)), "Verifying window center textoverlay", "verified window center textoverlay");
////		viewerpage.assertTrue(viewerpage.getZoomLevelText(viewNum) != null, "Verifying zoom value", "verified zoom value");
////	}
////
////	public void verifyMinimumOverlayDataForBoneage(String filePath, int viewNum) {
////		viewerpage.assertTrue(("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(viewerpage.getPatientIDOverlayText(viewNum)), "Verifying patient name textoverlay", "verified patient name textoverlay");
////		viewerpage.assertTrue(DataReader.getStudyDetails(NSConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified tudy date time textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImageCurrentScrollPositionOverlayText(viewNum)), "Verifying image current scroll textoverlay", "verified image current scroll textoverlay");
////		viewerpage.assertTrue(("/"+DataReader.getSeriesDesc(NSConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getImageMaxScrollPositionOverlayText(viewNum)), "Verifying image max scroll textoverlay", "verified image max scroll textoverlay");
////		viewerpage.assertTrue(viewerpage.getZoomLevelText(viewNum) != null,"Verifying zoom value", "verified zoom value");
////	}
////
////	public void verifyDFullOverlayDataForBoneage(String filePath, int viewNum) {
////		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientNameOverlayText(viewNum)) , "Verifying patient name textoverlay", "verified patient name textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, "STUDY01", "STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(viewNum)),"Verifying series description textoverlay", "verified series description textoverlay");
////		viewerpage.assertTrue(DataReader.getStudyDetails(NSConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified study date time textoverlay");
////
////		viewerpage.assertTrue(DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDescriptionOverlayText(viewNum)),"Verifying study description textoverlay", "verified study description textoverlay");
////		viewerpage.assertTrue(DataReader.getPatientDetails(NSConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientSexOverlayText(viewNum)) , "Verifying patient sex textoverlay", "verified patient sex textoverlay");
////		viewerpage.assertTrue(DataReader.getPatientDetails(NSConstants.PATIENT_CLASS_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientClassOverlayText(viewNum)), "Verifying patient class textoverlay", "verified patient class textoverlay");
////
////		viewerpage.assertTrue(DataReader.getSeriesDesc(NSConstants.IMAGE_MATRIX_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImageMatrixOverlayText(viewNum)),"Verifying image matrx textoverlay", "verified image matrix textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(NSConstants.IMAGE_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImagePositionOverlayText(viewNum)),"Verifying image position textoverlay", "verified image position textoverlay");
////
////		viewerpage.assertTrue(("Thickness: "+DataReader.getSeriesDesc(NSConstants.SLICE_THICKNESS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getSliceThicknessOverlayText(viewNum)),"Verifying slice thickness textoverlay", "verified slice thickness textoverlay");
////		viewerpage.assertTrue(("Location: "+DataReader.getSeriesDesc(NSConstants.SLICELOCATION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getSliceLocationOverlayText(viewNum)), "Verifying slice location textoverlay", "verified slice location name textoverlay");	
////
////		viewerpage.assertTrue(("Kvp: "+DataReader.getSeriesDesc(NSConstants.KVP_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getKvpOverlayText(viewNum)), "Verifying KVP textoverlay", "verified KVP textoverlay");
////		viewerpage.assertTrue(("FOV: "+DataReader.getSeriesDesc(NSConstants.FOV_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getFOVOverlayText(viewNum)),"Verifying FOV textoverlay", "verified FOV textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImageCurrentScrollPositionOverlayText(viewNum)), "Verifying current scroll position textoverlay", "verified current scroll position textoverlay");
////		viewerpage.assertTrue(("/"+DataReader.getSeriesDesc(NSConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getImageMaxScrollPositionOverlayText(viewNum)), "Verifying MAX scroll postion textoverlay", "verified MAX scroll postion textoverlay");
////		viewerpage.assertTrue(("mAs: "+DataReader.getSeriesDesc(NSConstants.MAS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getmAsOverlayText(viewNum)),"Verifying mAS textoverlay", "verified mA textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowWidthValueOverlayText(viewNum)),"Verifying Window width textoverlay", "verified Window width textoverlay");
////		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowCenterValueOverlayText(viewNum)), "Verifying Window center textoverlay", "verified Window center textoverlay");
////		viewerpage.assertTrue(viewerpage.getZoomLevelText(viewNum) != null, "Verifying zoom value", "verified zoom value");
////	}
////
////	//Verify the text overlay on layout change
////	@Test(groups ={"IE11","Chrome","firefox", "Chrome","US334","Sanity"})
////	//Not working on IE11, FireFox 
////	public void test23_US334_TC1106_verifyTextOverlayOnLayoutChange() throws InterruptedException, AWTException{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Risk Coverage");
////		
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(liver9Patient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1-12", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for data");
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 1 Verify the layout changed to 1x1 and series loaded","TC1106_Checkpoint1");
////
////		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint 2 Verify the layout changed to 2x1 and series loaded","TC1106_Checkpoint2");
////
////		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 3 Verify the layout changed to 1x2 and series loaded","TC1106_Checkpoint3");
////
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 4 Verify the layout changed to 2x2 and series loaded","TC1106_Checkpoint4");
////
////		viewerpage.selectLayout(viewerpage.threeByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 5 Verify the layout changed to 3x2 and series loaded","TC1106_Checkpoint5");
////
////		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 6 Verify the layout changed to 2x3 and series loaded","TC1106_Checkpoint6");
////
////		viewerpage.selectLayout(viewerpage.oneByOneLAndThreeByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 7 Verify the layout changed to 1x1L-3x1R and series loaded","TC1106_Checkpoint7");
////
////		viewerpage.selectLayout(viewerpage.oneByOneLAndTwoByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 8 Verify the layout changed to 1x1L-2x1R and series loaded","TC1106_Checkpoint8");
////
////		viewerpage.selectLayout(viewerpage.oneByOneTAndOneByTwoBLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 9 Verify the layout changed to 1x1T-1x2B and series loaded","TC1106_Checkpoint9");
////
////		viewerpage.selectLayout(viewerpage.twoByOneLAndOneByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 10 Verify the layout changed to 2x1L-1x1R and series loaded","TC1106_Checkpoint10");
////
////		viewerpage.selectLayout(viewerpage.threeByOneLAndOneByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 11 Verify the layout changed to 3x1L-1x1R and series loaded","TC1106_Checkpoint11");
////
////		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 12 Verify the layout changed to 3x3 and series loaded","TC1106_Checkpoint12");
////
////		viewerpage.browserBackWebPage();
////		singlePatientStudyPage.browserBackWebPage();
////		
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(patientName);
////		
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage.waitForViewerpageToLoad();
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint 13-24", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for data");
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint 13 Verify the layout changed to 1x1 and series loaded for reloaded study Mr LSP","TC1106_ Checkpoint13");
////
////		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint 14 Verify the layout changed to 2x1 and series loaded for reloaded study Mr LSP","TC1106_ Checkpoint14");
////
////		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 15 Verify the layout changed to 1x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint15");
////
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 16 Verify the layout changed to 2x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint16");
////		
////		viewerpage.selectLayout(viewerpage.threeByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 17 Verify the layout changed to 3x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint17");
////
////		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 18 Verify the layout changed to 2x3 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint18");
////
////		viewerpage.selectLayout(viewerpage.oneByOneLAndThreeByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 19 Verify the layout changed to 1x1L-3x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint19");
////
////		viewerpage.selectLayout(viewerpage.oneByOneLAndTwoByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 20 Verify the layout changed to 1x1L-2x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint20");
////
////		viewerpage.selectLayout(viewerpage.oneByOneTAndOneByTwoBLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 21 Verify the layout changed to 1x1T-1x2B and series loaded for reloaded study Mr LSP","TC1106_Checkpoint21");
////
////		viewerpage.selectLayout(viewerpage.twoByOneLAndOneByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 22 Verify the layout changed to 2x1L-1x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint22");
////
////		viewerpage.selectLayout(viewerpage.threeByOneLAndOneByOneRLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 23 Verify the layout changed to 3x1L-1x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint23");
////
////		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 24 Verify the layout changed to 3x3 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint24");
////
////	}
////	
////	@Test(groups ={"firefox","Chrome","Edge","IE11","US586"})
////	public void test33_US586_TC1889_verifyAboutLinkFromViewer() throws InterruptedException
////	{                      
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify licensing Information using About link from Viewer page");
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(ah4PatientName);
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		Header header = new Header(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Scan drop down menu is present on top left on page");
////		viewerpage.assertTrue(header.userInfo.isDisplayed(), "Verifying that Scan drop down menu is present on top left of Patient List Page", "Scan drop down menu is present on left corner of page");
////		header.viewAboutPage();
////		header.switchToNewWindow(2);
////		header.maximizeWindow();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify About page opens in a new tab");
////		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verifying the About Page is open", "User is on About page "+ loginPage.getCurrentPageURL());
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Hammer JS licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.HAMMERJS), "Verifying Hammer JS licensing information is present", "The Hammer JS licensing information is present on page");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify ng-bootstrap licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.BOOTSTARP), "Verifying ng-bootstrap licensing information is present", "The ng-bootstrap licensing information is present on page");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Newtonsoft.Json licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.NEWTONSOFT), "Verifying Newtonsoft.Json licensing information is present", "The Newtonsoft.Json licensing information is present on page");
////				
////	}
////	
////	@Test(groups ={"firefox","Chrome","Edge","IE11","multimonitor","US586"})
////	public void test34_US586_TC1890_verifyAboutLinkFromViewerMultiMointor() throws InterruptedException
////	{                      
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify licensing Information using About link from Viewer Page in Multi Mointor Mode");
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(ah4PatientName);
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		singlePatientStudyPage.clickOntheFirstStudy();
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		viewerpage.openOrCloseChildWindows(2);
////		viewerpage.switchToNewWindow(2);
////		Header header = new Header(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Scan drop down menu is present on top left on page");
////		viewerpage.assertTrue(header.userInfo.isDisplayed(), "Verifying that Scan drop down menu is present on top left of Patient List Page", "Scan drop down menu is present on left corner of page");	
////		header.viewAboutPage();
////		header.switchToNewWindow(3);
////        header.maximizeWindow();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify About page opens in a new tab");
////		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verifying the About Page is open", "User is on About page "+ loginPage.getCurrentPageURL());
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Hammer JS licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.HAMMERJS), "Verifying Hammer JS licensing information is present", "The Hammer JS licensing information is present on page");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify ng-bootstrap licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.BOOTSTARP), "Verifying ng-bootstrap licensing information is present", "The ng-bootstrap licensing information is present on page");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Newtonsoft.Json licensing Information is present on About page");
////		header.assertTrue(header.getTextForPage().contains(NSConstants.NEWTONSOFT), "Verifying Newtonsoft.Json licensing information is present", "The Newtonsoft.Json licensing information is present on page");
////				
////	}
////	
////	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US548"})
////	public void test35_US548_TC1623_verifyDoubleClickOneUpForViewboxWithDicomOrPDFOrJPEG() throws AWTException, InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Double click One up for a Viewbox with PDF and JPEG image");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(ah4PatientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		
////		//variable to store default layout for 
////		int defaultCount = viewerpage.getNumberOfCanvasForLayout();
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify default layout for "+ah4PatientName+" Patient");
////		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
////		
////		//Double click on Viewbox1 containing DICOM image
////		viewerpage.doubleClickOnViewbox(1);
////		int doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
////		
////		//Verify layout after One-up is 1X1
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify layout changes to 1X1 after Double click One-Up on DIOCM image");
////		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
////		
////		//Double click on Viewbox1 for default layout
////		viewerpage.doubleClickOnViewbox(1);
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		defaultCount = viewerpage.getNumberOfCanvasForLayout();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
////		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
////		
////		//Double click on Viewbox3 containing Non-DICOM image
////		viewerpage.doubleClickOnViewport(viewerpage.viewboxImg3);
////		doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
////		
////		//Verify layout after One-up is 1X1
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify layout changes to 1X1 after Double click One-Up on Non-DIOCM image");
////		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
////		
////		//Double click on Viewbox1 for default layout
////		viewerpage.doubleClick();
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		defaultCount = viewerpage.getNumberOfCanvasForLayout();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
////		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
////
////		//Double click on Viewbox4 containing Non-DICOM image
////		viewerpage.doubleClickOnViewport(viewerpage.pdfInFourthViewport);
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
////		
////		//Verify layout after One-up is 1X1
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify layout changes to 1X1 after Double click One-Up on PDF");
////		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
////	
////		//Double click on Viewbox1 for default layout
////		viewerpage.doubleClickOnViewport(viewerpage.pdfInFourthViewport);
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		defaultCount = viewerpage.getNumberOfCanvasForLayout();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
////		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
////	
////		//Double click on First text inside PDF
////		viewerpage.doubleClickOnViewport(viewerpage.pdfFirstText);
////		viewerpage.waitForPdfToRenderInViewbox(4);
////		defaultCount = viewerpage.getNumberOfCanvasForLayout();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify layout does not change on double clicking text inside PDF");
////		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
////		
////	}
////	
////	
////	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE375" })
////	public void test36_DE375_TC1535_verifyStudyDetailFromWIAGate() throws AWTException, InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Study detail from WIA gate services");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////		patientPage.clickOnPatientRow(boneAgePatient);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.waitForSingleStudyToLoad();
////		String studyDesciption = DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY,"STUDY01", filePath2);	
////
////		//Verify Study description on Study page
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Study desciption on Study list page for "+boneAgePatient+" Patient");
////		singlePatientStudyPage.assertEquals(studyDesciption,singlePatientStudyPage.getStudyDescription(0),"Verify Study description on Study list page","Result description is : "+studyDesciption);
////		
////		//Hover mouse on EnvoyAI Icon and verify algorithm detail
////		singlePatientStudyPage.mouseHover("presence",singlePatientStudyPage.getEnvoyAlCell(1)); 
////		String str = singlePatientStudyPage.getEnvoyAlTooltip();
////		
////		//Verify result with finding is present on hovering on Envoy AI 
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Result status on tooltip of EnvoyAI icon");
////		singlePatientStudyPage.assertTrue(str.contains(NSConstants.RESULT_STATUS), "Verify Result Status is present on tooltip", "Result Status: Run with findings is present in tooltip");
////		
////		//Verify algorithm detail
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Algorithm detail is present on tooltip of EnvoyAI Icon");
////		singlePatientStudyPage.assertTrue(str.contains(NSConstants.ALGORITHM_BONE_AGE), "Verify algorithm detail on tooltip of EnvoyAI Icon", "Algorithm Boneage is present in tooltip");
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad(4);
////		
////		//Verify Default layout for Bone-Age Data
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify Default layout for Boneage Data is 1X2");
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for BoneAge","TC1535_CheckPoint1");
////		
////		//Verify Atlas appear on Result set by default
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Atlas appear on Result set by default");
////		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getResultDescriptionOverlayText(1)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
////	
////		//Verify Text overlay for Patient Date on Viewbox2
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for default annotation");
////		verifyDefaultOverlayDataForBoneage(filePath2, 4);
////	}
////	
////	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE758" })
////	public void test37_DE758_TC2901_verifyStudyDetailFromWIAGate() throws AWTException, InterruptedException 
////	{
////		
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("LayoutChange not working if empty viewboxes are there in viewer- With GSPS present data");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(GSPS_PatientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		contentSelector = new ContentSelector(driver);
////		
////		//Verifying that the Default layout is displayed which is 1*2 for GSPS_Multiseries_Data
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for GSPS_Multiseries_Data","TC37_CheckPoint1");
////
////		//Verifying the change in Layout to 1*1
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 1*1 for GSPS_Multiseries_Data","TC37_CheckPoint2");
////
////		//Verifying the change in Layout to 2*2
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 2*2 for GSPS_Multiseries_Data","TC37_CheckPoint3");
////		
////		//Verifying the change in Layout to 3*3
////		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 3*3 for GSPS_Multiseries_Data","TC37_CheckPoint4");
////		
////		//Selecting any series in an empty viewbox through content selector
////		contentSelector.selectSeriesFromContentSelector(4,firstSeriesDescriptionGSPS_Multiseries);	
////		viewerpage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(4, firstSeriesDescriptionGSPS_Multiseries), "Verifying that series is selected from content selector on an empty viewbox.", "Verified that series is selected from content selector on an empty viewbox.");
////
////		//Verifying that series selected on an empty viewbox is visible
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change for GSPS_Multiseries_Data after selecting series in an empty viewbox","TC37_CheckPoint5");
////
////	}
////	
////	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE758" })
////	public void test38_DE758_TC2902_verifyStudyDetailFromWIAGate() throws AWTException, InterruptedException 
////	{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("LayoutChange not working if empty viewboxes are there in viewer- Without GSPS data");
////
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(ah4PatientName);
////
////		singlePatientStudyPage = new SinglePatientStudyPage(driver);
////		singlePatientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		contentSelector = new ContentSelector(driver);
////		
////		//Verifying that the Default layout is displayed which is 1*2 for GSPS_Multiseries_Data
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for AH.4_Data","TC38_CheckPoint1");
////
////		//Verifying the change in Layout to 1*1
////		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 1*1 for AH.4_Data","TC38_CheckPoint2");
////
////		//Verifying the change in Layout to 2*2
////		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 2*2 for AH.4_Data","TC38_CheckPoint3");
////		
////		//Verifying the change in Layout to 3*3
////		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 3*3 for AH.4_Data","TC38_CheckPoint4");
////		
////		//Selecting any series in an empty viewbox through content selector
////		contentSelector.selectSeriesFromContentSelector(8,firstSeriesDescriptionAH4);	
////		viewerpage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(8, firstSeriesDescriptionAH4), "Verifying that series is selected from content selector on an empty viewbox.", "Verified that series is selected from content selector on an empty viewbox.");
////
////		////Verifying that series selected on an empty viewbox is visible
////		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change for AH.4_Data after selecting series in an empty viewbox","TC38_CheckPoint5");
////
////	}
////	
////
////	
////}
//
//import java.util.Date;
//
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////Obsolete
//	//@Test(groups ={"IE11","Chrome","firefox","Edge","dbConfig","US273"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	//@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test01_US273" })
//	public void test26_US273_TC761_TC762_verifyMultiframeDataRenderedAndCineplay(String PatientName, String Fps) throws InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that multi frame data is rendered successfully and cine play based on FPS for patient "+PatientName );
//
//		int expectedFramesPerSecond = Integer.parseInt(Fps);
//		int multiFrameTime = 1000/expectedFramesPerSecond;
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateMultiFrameTimeInDB(PatientName, multiFrameTime);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		patientPage.clickOntheFirstStudy();		
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying data "+PatientName+" is rendered" );
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify Study "+PatientName+" should be rendered","US273_TC761_TC762_"+PatientName);
//
//		int initialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		viewerpage.playOrStopCineUsingKeyboardCKey();
//		Date beforeCine = new Date();
//		long duringCineTimeInMilliSec = beforeCine.getTime();
//
//		viewerpage.playOrStopCineUsingKeyboardCKey();
//		int finalScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		Date afterCine = new Date();
//		long afterCineTimeInMilliSec = afterCine.getTime();
//		long differenceInSec = (afterCineTimeInMilliSec-duringCineTimeInMilliSec)/1000;
//
//		int scrollLevelDifference = finalScrollLevel - initialScrollLevel;
//
//		long fps = scrollLevelDifference/differenceInSec;
//		int framesPerSecond = (int) fps;
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying frames per second for data "+PatientName);
//		viewerpage.assertEquals(framesPerSecond, expectedFramesPerSecond, "Verfiying FPS should be "+Fps+" frames per second", "Verified FPS is "+Fps+" frames per second");
//	}
//
