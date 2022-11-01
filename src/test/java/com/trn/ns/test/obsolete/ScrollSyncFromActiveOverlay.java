//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.util.Set;
//
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
//public class ScrollSyncFromActiveOverlay extends TestBase{
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerpage;
//	private SinglePatientStudyPage singlePatientStudyPage;
//	private ExtentTest extentTest;
//
//
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	String filePath3 = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//	String doeLillypatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//
//	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//	
//	String filePath2 = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
//	String tda = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//	
//	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//	
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//
//	}
//
//	
//
//	//Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study
//	//Not working on IE11, FireFox 
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor"})
//	public void test03_DE90_TC467_verifyIfScrollRetainsOnLayoutChange() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cine play, scroll, window leveling retain its position on layout change when it is performed using multiple monitor setup");
//		
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(doeLillypatientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//				int beforeScrollValue = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//				//Performing scroll on first view-box
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1,0, 0, 10, 10);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing after scroll value
//				int afterScrollValue = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//				//change layout to 1*1
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad(1);
//
//				//verifying that scroll is retained after change in layout
//				int scrollValueAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Scroll is retained after change in layout and when performed on Child Window and Parent Window as well");
//				viewerpage.assertEquals(afterScrollValue,scrollValueAfterLayoutChange, "Verifying scroll functionality is performed", "Scroll is functioning fine");
//
//				//Changing control to Child Window to see if scroll was performed
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Verifying that Scroll was performed
//				int afterScrollValueOnCW = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				viewerpage.assertNotEquals(beforeScrollValue,afterScrollValueOnCW, "Verifying scroll functionality is performed on Child Window as well", "Scroll is functioning fine and is performed on Child Window and is changed from"+beforeScrollValue + "to"+afterScrollValueOnCW);
//
//				//Performing scroll again on CHild Monitor
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1,0, 0, 10, 10);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing values after scroll is performed in Child Monitor
//				int afterScrollValueFromCW = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//				//Changing control to Parent Window
//				viewerpage.switchToWindow(parentWindow);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Verifying that scroll was performed on Parent Window as well
//				int afterScrollValueFromPW = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				viewerpage.assertEquals(afterScrollValueFromCW,afterScrollValueFromPW, "Verifying scroll functionality is reflected on Parent Window", "Scroll is functioning fine and is reflected on Parent Window");
//
//			}}}
//
//	//Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study
//		
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor"})
//	//Not working on IE11, FireFox 
//	public void test04_DE90_TC467_verifyIfCineRetainsOnLayoutChange() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cine play, scroll, window leveling retain its position on layout change when it is performed using multiple monitor setup");
//		
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(doeLillypatientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
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
//
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
////				int currentImage = Integer.parseInt(viewerpage.getCurrentScrollPosition(1));
//
//				String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//
//				//performing Cineplay in view-boxes 1
//				viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);
//
//				viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/BeforeCineplayImage.png");
//
//				//disabling Cineplay in view-box 1
//				viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);
//
//				viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/AfterCineplayImage.png");
//
//				//the paths for respective images in both view-boxes
//				String beforeCinePlayImage = newImagePath+"/actualImages/BeforeCineplayImage.png";
//				String afterCinePlayImage = newImagePath+"/actualImages/AfterCineplayImage.png";
//				String diffCinePlayImage = newImagePath+"/actualImages/ActualImage.png";
//
//				viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing values after change in layout
//				String cinePlayImageAfterLayoutChange = newImagePath+"/actualImages/AfterCineplayImage.png";
//
//				//verifying the images
//				boolean cpStatusA =  viewerpage.compareimages(beforeCinePlayImage, afterCinePlayImage, diffCinePlayImage);
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Cine is retained after change in layout for parent window");
//
//				viewerpage.assertFalse(cpStatusA, "Verify that the actual and Expected image are not same and Cine was performed","The actual and Expected image are not same and Cine was performed.");
//
//				viewerpage.assertEquals(cinePlayImageAfterLayoutChange, afterCinePlayImage,"Verifying that cine position is retained with change in layout", "Cine position is retained with change in layout");
//
//				//For another monitor
////				int currentImage2 = Integer.parseInt(viewerpage.getCurrentScrollPosition(1));
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.waitForViewerpageToLoad();
//
//				//performing Cineplay in view-boxes 2
//				viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);
//				viewerpage.waitForViewerpageToLoad();
//
//				viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/BeforeCineplayImage.png");
//
//				//disabling Cineplay in view-box 2
//				viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);
//				viewerpage.waitForViewerpageToLoad();
//
//				viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/AfterCineplayImage.png");
//
//				String beforeCinePlayImage2 = newImagePath+"/actualImages/BeforeCineplayImage.png";
//				String afterCinePlayImage2 = newImagePath+"/actualImages/AfterCineplayImage.png";
//				String diffCinePlayImage2 = newImagePath+"/actualImages/ActualImage.png";
//
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing values after change in layout
//				String cinePlayImageAfterLayoutChange2 = newImagePath+"/actualImages/AfterCineplayImage.png";
//
//				//verifying the images
//				boolean cpStatusA2 =  viewerpage.compareimages(beforeCinePlayImage2, afterCinePlayImage2, diffCinePlayImage2);
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Cine is retained after change in layout on child monitor");
//
//				viewerpage.assertFalse(cpStatusA2, "Verify that the actual and Expected image are not same and Cine was performed","The actual and Expected image are not same and Cine was performed.");
//
//				viewerpage.assertEquals(cinePlayImageAfterLayoutChange2, afterCinePlayImage2,"Verifying that cine position is retained with change in layout", "Cine position is retained with change in layout");
//
//			}}}
//
//	//Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome", "multimonitor"})
//	//Not working on IE11, FireFox 
//	public void test05_DE90_TC483_verifyIfScrollResetsOnReloadOfNewStudy() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(doeLillypatientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		
//		//verification for Liver9
//		int beforeScrollValue = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		//Performing scroll on Liver9
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1,0, 0, 10, 10);
//		viewerpage.waitForViewerpageToLoad();
//
//		int afterScrollValue = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForViewerpageToLoad();
//
//		int scrollValueAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		//Verifying that scroll is retained once layout is changed
//		viewerpage.assertEquals(afterScrollValue,scrollValueAfterLayoutChange, "Verifying scroll functionality", "Scroll is functioning fine");
//
//		//going back to select a new study-AH.4
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.browserBackWebPage();
//
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(ah4patientName);
//
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Performing scroll on AH.4
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1,0, 0, 10, 10);
//		viewerpage.waitForViewerpageToLoad();
//
//		int scrollValueAfterChangeInStudy =  viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForViewerpageToLoad();
//
//		int scrollValueAfterChangeinStudyAndLayout = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		//Verifying that scroll is retained once layout is changed
//		viewerpage.assertEquals(scrollValueAfterChangeInStudy,scrollValueAfterChangeinStudyAndLayout,"Verifying scroll functionality is not reatined", "Scroll is functioning fine and is not retained");
//
//		//Reselecting Liver9
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.clickOnPatientRow(patientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//verifying that changes are not retained for Liver9
//		int scrollValueReloadingSameStudy =  viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Study is loaded fresh for Liver9 and changes are not retained");
//		viewerpage.assertEquals(scrollValueReloadingSameStudy,beforeScrollValue, "Verifying scroll functionality and how it is not retained after reloading same study", "Scroll is functioning fine and the changes made earlier are not retained");
//
//		//performing child window
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
//				viewerpage.waitForViewerpageToLoad();
//
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1,0, 0, 10, 10);
//				viewerpage.waitForViewerpageToLoad();
//
//				int beforeScrollValueCW =viewerpage.getCurrentScrollPositionOfViewbox(1);
//				//Reloading the same study
//				viewerpage.switchToWindow(parentWindow);
//				viewerpage.browserBackWebPage();
//				singlePatientStudyPage.waitForSingleStudyToLoad();
//				singlePatientStudyPage.clickOntheFirstStudy();
//				viewerpage.waitForViewerpageToLoad();
//
//				int scrollValueForReloadedStudy = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				//verifying scroll is not retained
//				viewerpage.assertNotEquals(beforeScrollValueCW,scrollValueForReloadedStudy, "Verifying scroll functionality is not retained with reloading the same study", "Scroll is functioning fine and not retainedand ");
//
//			}
//		}
//	}
//
//	//Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study
//	//Not working on IE11, FireFox 
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor"})
//	public void test06_US620_TC1660_verifyIfRotationWorksAfterCine() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify image rotation works fine after cine/scroll");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(doeLillypatientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Changing Layout to 3*3
//		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Playing and stopping Cine
//		viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg5);
//		viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg5);
//
//		//Inputting value as Cine stops at different slice positions everytime
//		viewerpage.inputImageNumber(350, 5);
//
//		//Rotate the series
//		viewerpage.rotateSeries(viewerpage.getTopOrientationMarker(5), viewerpage.topClockwiseRotationMarker(5));
//		viewerpage.waitForViewerpageToLoad();
//
//		//Comparing Image to verify that rotation of series took place 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verified image rotation and it works fine after cine/scroll");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint1 : Verified image rotation and it works fine after cine/scroll ","TC1660_" + "_" + "Checkpoint1");
//
//	}
//	//To compare the updated values with other series values
//	private void compareImageValues(int viewNum, Boolean validOrInvalid){
//		for(int j=1 ;j<=4; j++){
//			if(j!=viewNum){
//				if(validOrInvalid){
//					viewerpage.assertEquals(viewerpage.getValueOfImage(viewNum), viewerpage.getValueOfImage(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
//							"Image values are "+viewerpage.getValueOfImage(viewNum)+" and "+viewerpage.getValueOfImage(j)+" ");
//				}else{
//					viewerpage.assertNotEquals(viewerpage.getValueOfImage(viewNum), viewerpage.getValueOfImage(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
//							"Image values are "+viewerpage.getValueOfImage(viewNum)+" and "+viewerpage.getValueOfImage(j)+" ");
//				}
//			}
//		}
//	}
//}
