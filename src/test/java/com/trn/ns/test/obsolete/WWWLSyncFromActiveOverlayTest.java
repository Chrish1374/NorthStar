//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.util.List;
//import java.util.Set;
//
//import org.openqa.selenium.TimeoutException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
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
//public class WWWLSyncFromActiveOverlayTest extends TestBase{
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String Liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//
//	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementInVisibility(patientPage.loadingText);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//	}
//
//	//TC887 - Values applied from preset do not match menu - Context Menu Coverage
//	//Limitation : Not work in edge and IE11 because of mouse move event
//	@Test(groups ={"firefox","Chrome","IE11","Edge","multimonitor"})
//	public void test06_DE199_TC887_verifyPresetMenuDisplay() throws  InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Window preset menu is not displayed in firefox - Defect Coverage");
//		//Loading the patient on viewer
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Loading the Patient "+patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		//1. Default Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Loading the Patient data in default overlay" );
//		viewerPage.selectTextOverlays("default");
//		//Step1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify the Preset menu options for 'W' annotation in the first loaded series ");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowWidthLabelOverlay(1),"CT",1), "Verifying the Preset menu options for 'W' annotation","Preset menu is displaying properly");
//		//Step2
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify the Preset menu options for 'C' annotation in the first loaded series ");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowCenterLabelOverlay(1),"CT",1), "Verifying the Preset menu options for 'C' annotation","Preset menu is displaying properly");
//		//Step3
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify the Preset menu options for 'W' and 'C' annotation in the Second loaded series ");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowWidthLabelOverlay(2),"CT",2), "Verifying the Preset menu options for 'W' annotation","Preset menu is displaying properly");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowCenterLabelOverlay(2),"CT",2), "Verifying the Preset menu options for 'C' annotation","Preset menu is displaying properly");
//
//		String parentWindow = viewerPage.getCurrentWindowID();
//		viewerPage.openOrCloseChildWindows(2);
//		//		viewerpage.switchToWindow(viewerpage.getAllOpenedWindowsIDs().get(1));
//		//		viewerpage.maximizeWindow();
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.viewerback();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Step4
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify the layout changed to 1x1");
//		viewerPage.selectLayout(viewerPage.oneByOneLayoutIcon);
//		viewerPage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.maximizeWindow();
//			}
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.selectTextOverlays("default");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify the Preset menu options for 'W' and 'C' annotation in the Second browser window");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowWidthLabelOverlay(1),"CT",1), "Verifying the Preset menu options for 'W' annotation","Preset menu is displaying properly");
//		viewerPage.assertTrue(viewerPage.compareWWWLFromDBAndPresetMenu(viewerPage.getWindowCenterLabelOverlay(1),"CT",1), "Verifying the Preset menu options for 'C' annotation","Preset menu is displaying properly");
//
//		//Comparing Preset image
//		viewerPage.click(viewerPage.getWindowWidthLabelOverlay(1));
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1, "Checkpoint 1: Verify the preset menu on child window", "test06_DE199_TC887_checkpoint1");
//		viewerPage.mouseHover(viewerPage.viewboxImg1);
//
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1, "Checkpoint 2: Verify the preset menu on parent window", "test06_DE199_TC887_checkpoint2");
//
//
//	}

//@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
//	public void test17_US1081_TC5139_verifyRelativeWWWCSyncWhenDefaultSyncOnOff() throws SQLException, IOException, InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is relative and we turn on and off synchronization");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);
//		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
//		db.resetIISPostDBChanges();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
//		presetMenu=new ViewBoxToolPanel(driver);
//
//		viewerPage.scrollToImage(1,1);
//		int windowWidth1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//		int windowCenter1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
//		int windowWidth2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(2)));
//		int windowCenter2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(2)));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify window center and Window width value for each slice in the loaded series in viewbox1 and viewbox2" );
//		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);
//
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.scrollToImage(1,1);
//
//		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
//		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 50 , 50);
//
//		int WWAfterEdit=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//		int WCAfterEdit=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//		int diffWidth=WWAfterEdit-windowWidth1;
//		int diffCenter=WCAfterEdit-windowCenter1;
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify window center and Window width value from viewbox1 and viewbox2 after using window leveling option from radial menu");
//		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,diffWidth,diffCenter);
//
//		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_FALSE);
//		db.resetIISPostDBChanges();
//
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.scrollToImage(1,1);
//		presetMenu.selectPresetValue(viewerPage.getWindowWidthLabelOverlay(1), ViewerPageConstants.RESET,1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify window center and Window width value from viewbox1 and viewbox2 after using preset option ");
//		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);
//
//		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);
//		db.resetIISPostDBChanges();
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.scrollToImage(1,1);
//		viewerPage.inputWWNumber(200, 1);
//		viewerPage.inputWCNumber(300, 1);
//		int WWAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//		int WCAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//		int diffWidth1=WWAfterPreset-windowWidth1;
//		int diffCenter1=WCAfterPreset-windowCenter1;
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify window center and Window width value after using window leveling option from radial menu");
//		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,diffWidth1,diffCenter1);
//	}  
////US1081: Add support for per image WW/WL synchronization
//@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
//public void test13_US1081_TC5135_verifyRelativeWWWCForSyncModeOn() throws SQLException, IOException, InterruptedException, AWTException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Relative");
//
//	DatabaseMethods db = new DatabaseMethods(driver);
//	db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);	
//	db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
//	db.resetIISPostDBChanges();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
//	contentSelector=new ContentSelector(driver);
//
//	//store value of WW and WC from both the loaded series 
//	viewerPage.scrollToImage(1,1);
//	int windowWidthForSeries1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int windowCenterForSeries1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
//	int windowWidthForSeries2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(2)));
//	int windowCenterForSeries2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(2)));
//
//	//change value for WWWC by editing annotation in 1st viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Editing of WW and WC centre value from viewer page for first series");
//	viewerPage.inputWWNumber(200, 1);
//	viewerPage.inputWCNumber(400, 1);
//
//	//store WW WC ,and difference value from 1st viewbox 
//	int WWAfterEditAnn=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterEditAnn=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int diffWidth=WWAfterEditAnn-windowWidthForSeries1;
//	int diffCenter=WCAfterEditAnn-windowCenterForSeries1;
//
//	//checkpoint to verify WWWC on both the viewboxes after editing annotation in first viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify window center and Window width value in first and second viewbox after editing annotation");
//	verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidthForSeries1, windowCenterForSeries1,windowWidthForSeries2, windowCenterForSeries2,diffWidth,diffCenter) ;
//
//	//load 2nd series in First viewbox and 1st series in 2nd viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify user able to load another series in active viewbox");
//	contentSelector.selectSeriesFromSeriesTab(1, Series2ToSelect_AH4);
//	contentSelector.selectSeriesFromSeriesTab(2, Series1ToSelect_AH4);
//
//	// validate WWWC by loading different series in active viewbox(Change value for WWWC by editing annotation)
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify Editing of WW and WC centre value after loading different series in active viewbox");
//	viewerPage.mouseHover(viewerPage.getViewPort(1));
//	viewerPage.scrollToImage(1,1);
//	viewerPage.inputWWNumber(500, 1);
//	viewerPage.inputWCNumber(600, 1);
//
//	//store value of WWWC and difference in variable after editing annotation for second series
//	int WWAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int diffWidth1=WWAfterDrag-windowWidthForSeries2;
//	int diffCenter1=WCAfterDrag-windowCenterForSeries2;
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify window center and Window width value in first and second viewbox after editing annotation");
//	verifyWindowWidthAndCenterWhenSyncModeOnForRelative(2,1,maxScrollPos,windowWidthForSeries1, windowCenterForSeries1,windowWidthForSeries2, windowCenterForSeries2 ,diffWidth1,diffCenter1) ;
//
//	// validate WWWC by loading different series in active viewbox(Change value for WWWC by selecting window leveling option from radial menu)
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify window center and Window width value after using window leveling option from radial menu");
//	viewerPage.mouseHover(viewerPage.getViewPort(1));
//	viewerPage.scrollToImage(1,1);
//	viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
//	viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, +100,+100);
//
//	int WWAfterDrag1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterDrag1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int diffWidth2=WWAfterDrag1-windowWidthForSeries2;
//	int diffCenter2=WCAfterDrag1-windowCenterForSeries2;
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify window center and Window width value in first and second viewbox after using window leveling");
//	verifyWindowWidthAndCenterWhenSyncModeOnForRelative(2,1,maxScrollPos,windowWidthForSeries1, windowCenterForSeries1,windowWidthForSeries2, windowCenterForSeries2 ,diffWidth2,diffCenter2) ;
//
//}
//
//@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
//public void test14_US1081_TC5132_verifyAbsoluteWWWCForSyncModeOn() throws SQLException, IOException, InterruptedException, AWTException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Absolute");
//
//	DatabaseMethods db = new DatabaseMethods(driver);
//	db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);	
//	db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);
//	db.resetIISPostDBChanges();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
//	
//	contentSelector=new ContentSelector(driver);
//
//	//store value of WW and WC from both the loaded series 
//	viewerPage.scrollToImage(1,1);
//	int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
//
//	//change value for WWWC by editing annotation in 1st viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Editing of WW and WC centre value from viewer page for first series");
//	viewerPage.inputWWNumber(200, 1);
//	viewerPage.inputWCNumber(300, 1);
//	viewerPage.waitForEndOfAllAjaxes();
//
//	//store WW WC ,and difference value from 1st viewbox 
//	int WWAfterEditAnn=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterEditAnn=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//
//	//checkpoint to verify WWWC on both the viewboxes after editing annotation in first viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify window center and Window width value in first and second viewbox after editing annotation");
//	verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,WWAfterEditAnn, WCAfterEditAnn);
//
//	//load 2nd series in First viewbox and 1st series in 2nd viewbox
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify user able to load another series in active viewbox");
//	contentSelector.selectSeriesFromSeriesTab(1, Series2ToSelect_AH4);
//	contentSelector.selectSeriesFromSeriesTab(2, Series1ToSelect_AH4);
//
//	// validate WWWC by loading different series in active viewbox(Change value for WWWC by editing annotation)
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify Editing of WW and WC centre value from viewer page for second series");
//	viewerPage.mouseHover(viewerPage.getViewPort(1));
//	viewerPage.scrollToImage(1,1);
//	viewerPage.inputWWNumber(500, 1);
//	viewerPage.inputWCNumber(600, 1);
//
//	//store value of WWWC and difference in variable after editing annotation for second series
//	int WWAfterEdit1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterEdit1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify window center and Window width value in first and second viewbox after editing annotation");
//	verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,WWAfterEdit1, WCAfterEdit1,0,0);
//
//	// validate WWWC by loading different series in active viewbox(Change value for WWWC by selecting window leveling option from radial menu)
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify window center and Window width value after using window leveling option from radial menu");
//	viewerPage.mouseHover(viewerPage.getViewPort(1));
//	viewerPage.scrollToImage(1,1);
//	viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
//	viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, +100,+100);
//
//	int WWAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify window center and Window width value in first and second viewbox after using window leveling");
//	verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,WWAfterDrag, WCAfterDrag,0,0);
//
//}

//@Test(groups ={"Chrome","IE11","Edge", "US1080","Positive"})
//public void test11_US1080_TC5042_TC5044_TC5138_verifyWWWCEditedValueByChangingSliceInDifferentWay() throws InterruptedException, SQLException, IOException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify-Viewer applies the relative WW/WL value to all the images when user edits WW/WL level values by editing the annotation ");
//
//	DatabaseMethods db = new DatabaseMethods(driver);
//	db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_FALSE);		
//	db.resetIISPostDBChanges();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
//
//	viewerPage.scrollToImage(1,1);
//	int windowWidth=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int windowCenter=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify window center and Window width value for each slice in the loaded series" );
//	verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth,windowCenter,0,0);
//
//	viewerPage.scrollToImage(1,1);
//	viewerPage.inputWWNumber(2300, 1);
//	viewerPage.inputWCNumber(150, 1);
//
//	int WWAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int WCAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//
//	int diffWidth=WWAfterDrag-windowWidth;
//	int diffCenter=WCAfterDrag-windowCenter;
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify window center and Window width value by changing slice using mousewheel");
//	verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth,windowCenter,diffWidth,diffCenter);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify window center and Window width value by manually entering the slice number ");
//	verifyWindowWidthAndCenterForAllSlicesByEnteringvalue(1,maxScrollPos,windowWidth,windowCenter,diffWidth,diffCenter);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify window center and Window width value by selecting slice from scrollbar");
//	viewerPage.scrollToImage(1,1);
//	//		viewerPage.click(viewerPage.getViewPort(1));
//	verifyWindowWidthAndCenterForAllSlicesUsingScrollBar(1,maxScrollPos,windowWidth,windowCenter,diffWidth,diffCenter);
//
//}
////	//TC1168 - Values applied from preset do not match menu - Manual Input Coverage
////Limitation : Not work in edge and IE11 because of mouse move event
//@Test(groups ={"firefox","Chrome","IE11","Edge", "DE263","US289"})
//public void test08_US289_TC2375_US289_TC2416_verifyWCOverlayByManualInput() throws InterruptedException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify scroll synchronization with series having different FrameReferenceUID and same Orientation.'"
//			+ "<br> Verify Sync ON/OFF for WWWL.");
//
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+ah4_patientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password, 1);
//	textOverlay=new ViewerTextOverlays(driver);
//
//	//1. Default Overlay
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Loading the Patient data in default overlay" );
//	textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
//	String beforeWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String beforeWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//
//	String beforeWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//	String beforeWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	//Step 1 - verifying that only WW values are changed for sync series not WC values
//	viewerPage.inputWWNumber(100, 1);
//	String currentWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String currentWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	String currentWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	String currentWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying that only WW values are changed for sync series not WC values after inputing values in WW input box" );
//	viewerPage.assertNotEquals(beforeWWValueFirstViewbox, currentWWValueFirstViewbox, "Verify that the WW values are changes to new values", "WW values are not same as previous");
//	viewerPage.assertNotEquals(beforeWWValueSecViewbox, currentWWValueSecViewbox, "Verify that the WW values are same for sync series", "WW values are same for sync series");
//
//	viewerPage.assertEquals(beforeWCValueFirstViewbox, currentWCValueFirstViewbox, "Verify that the WC values are not changes to new values", "WC values are same as previous");
//	viewerPage.assertEquals(beforeWCValueSecViewbox, currentWCValueSecViewbox, "Verify that the WC values are not changes to new values for sync series", "WC values are same as previous for sync series");
//
//	//Step 2 - verifying that only WC values are changed for sync series not WW values
//	viewerPage.inputWCNumber(60, 1);
//	String afterWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String afterWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	String afterWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	String afterWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying that only WC values are changed for sync series not WW values after inputing values in WC input box" );
//	viewerPage.assertNotEquals(currentWCValueFirstViewbox, afterWCValueFirstViewbox, "Verify that the WC values are changes to new values", "WC values are not same as previous");
//	viewerPage.assertNotEquals(currentWCValueSecViewbox, afterWCValueSecViewbox, "Verify that the WC values are same for sync series", "WC values are same for sync series");
//
//	viewerPage.assertEquals(currentWWValueFirstViewbox, afterWWValueFirstViewbox, "Verify that the WW values are not changes to new values", "WW values are same as previous");
//	viewerPage.assertEquals(currentWWValueSecViewbox, afterWWValueSecViewbox, "Verify that the WW values are not changes to new values for sync series", "WW values are same as previous for sync series");
//
//	viewerPage.performSyncONorOFF();
//	currentWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	currentWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	currentWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	currentWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	viewerPage.inputWCNumber(100, 1);
//
//	afterWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	afterWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	afterWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	afterWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying that only WC values are changed for sync series not WW values after inputing values in WC input box" );
//	viewerPage.assertEquals(currentWWValueFirstViewbox, afterWWValueFirstViewbox, "Verify that the WW values are not changes to new values", "WW values are same as previous");
//	viewerPage.assertEquals(currentWWValueSecViewbox, afterWWValueSecViewbox, "Verify that the WW values are not changes to new values for sync series", "WW values are same as previous for sync series");
//	viewerPage.assertNotEquals(afterWCValueSecViewbox, afterWCValueFirstViewbox, "Verify that the WC values are not in sync as sync is off", "WC values are not in sync");
//	viewerPage.assertEquals(afterWCValueFirstViewbox, "100", "Verify that the WC values are not in sync as sync is off", "WC values are not in sync");
//
//	viewerPage.performSyncONorOFF();
//
//
//}
//
////US1080: Update models to support WW/WL at each slice
//@Test(groups ={"Chrome","IE11","Edge", "US1080","Positive"})
//public void test09_US1080_TC5039_TC5047_verifyWWWCFromDICOMHeaderInformation() throws InterruptedException, SQLException, IOException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify-Viewer applies the WW/WL value to from the DICOM header information <br> "+
//			"Verify-Viewer resets the WW/WL to original value from the DICOM header after edit");
//
//	DatabaseMethods db = new DatabaseMethods(driver);
//	db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_FALSE);		
//	db.resetIISPostDBChanges();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
//
//	viewerPage.scrollToImage(1,1);
//	int windowWidth=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
//	int windowCenter=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
//	int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify window center and Window width value for each slice in the loaded series" );
//	verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth,windowCenter,0,0);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify window center and Window width value change after editing" );
//	viewerPage.scrollToImage(1,1);
//	viewerPage.inputWWNumber(2650, 1);
//	viewerPage.inputWCNumber(100, 1);
//	viewerPage.assertNotEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1))),windowWidth, "Verify window width for fifth slice is changed ", "Verified");
//	viewerPage.assertNotEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1))),windowCenter , "Verify window center for second slice changed", "Verified");
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify window center and Window width value reset to its original value" );
//	viewerPage.inputWWNumber(windowWidth, 1);
//	viewerPage.inputWCNumber(windowCenter, 1);
//	viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1))),windowWidth, "Verify window width for fifth slice is reset to original value ", "Verified");
//	viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1))),windowCenter, "Verify window center for second slice is reset to original value", "Verified");
//
//}//	//TC1168 - Values applied from preset do not match menu - Manual Input Coverage
////Limitation : Not work in edge and IE11 because of mouse move event
////	TC 4: Verify window leveling by manual input on overlay (Automated) 
//@Test(groups ={"firefox","Chrome","IE11","Edge", "DE263","US289","Sanity"})
//public void test02_DE263_TC1168_US289_TC2374_verifyWCOverlayByManualInput() throws InterruptedException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Values applied from preset do not match menu - Manual Input Coverage"
//			+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");
//
//	//Loading the patient on viewer
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+PatientName+"in viewer" );
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(PatientName, username, password, 1);
//	viewerPage.closingNotificationAndWaterMark();
//	textOverlay=new ViewerTextOverlays(driver);
//	
//	//1. Default Overlay
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Loading the Patient data in default overlay" );
//	textOverlay.selectTextOverlays("default");
//	String beforeWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String beforeWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//
//	String beforeWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//	String beforeWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	//Step 1 - verifying that only WW values are changed for sync series not WC values
//	viewerPage.inputWWNumber(100, 1);
//	String currentWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String currentWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	String currentWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	String currentWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint[3/4]", "Verifying that only WW values are changed for sync series not WC values after inputing values in WW input box" );
//	viewerPage.assertNotEquals(beforeWWValueFirstViewbox, currentWWValueFirstViewbox, "Verify that the WW values are changes to new values", "WW values are not same as previous");
//	viewerPage.assertNotEquals(beforeWWValueSecViewbox, currentWWValueSecViewbox, "Verify that the WW values are same for sync series", "WW values are same for sync series");
//
//	viewerPage.assertEquals(beforeWCValueFirstViewbox, currentWCValueFirstViewbox, "Verify that the WC values are not changes to new values", "WC values are same as previous");
//	viewerPage.assertEquals(beforeWCValueSecViewbox, currentWCValueSecViewbox, "Verify that the WC values are not changes to new values for sync series", "WC values are same as previous for sync series");
//
//	//Step 2 - verifying that only WC values are changed for sync series not WW values
//	viewerPage.inputWCNumber(60, 1);
//	String afterWWValueFirstViewbox = viewerPage.getValueOfWindowWidth(1);
//	String afterWWValueSecViewbox = viewerPage.getValueOfWindowWidth(2);
//
//	String afterWCValueFirstViewbox = viewerPage.getValueOfWindowCenter(1);
//	String afterWCValueSecViewbox = viewerPage.getValueOfWindowCenter(2);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2] & Checkpoint[4/4]", "Verifying that only WC values are changed for sync series not WW values after inputing values in WC input box" );
//	viewerPage.assertNotEquals(currentWCValueFirstViewbox, afterWCValueFirstViewbox, "Verify that the WC values are changes to new values", "WC values are not same as previous");
//	viewerPage.assertNotEquals(currentWCValueSecViewbox, afterWCValueSecViewbox, "Verify that the WC values are same for sync series", "WC values are same for sync series");
//
//	viewerPage.assertEquals(currentWWValueFirstViewbox, afterWWValueFirstViewbox, "Verify that the WW values are not changes to new values", "WW values are same as previous");
//	viewerPage.assertEquals(currentWWValueSecViewbox, afterWWValueSecViewbox, "Verify that the WW values are not changes to new values for sync series", "WW values are same as previous for sync series");
//}
//
//}
