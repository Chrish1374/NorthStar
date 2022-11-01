//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import org.xml.sax.SAXException;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.Header;
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
//public class E2E_TC1386_Workflow extends TestBase {
//
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private SinglePatientStudyPage spStudyListPage;
//
//	//Loading the patient on viewer
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		// Begin on the Patient List Page, select the patient Data.
//		patientListPage = new PatientListPage(driver) ;
//		patientListPage.waitForPatientPageToLoad();
//	}	
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_E2E_TC1386_verifyKeyboardShortcutsCine() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		// Select the specific Patient Name to go to the Single Patient Study List for the Patient
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		viewerPage.mouseHover(viewerPage.viewboxImg1);
//
//		viewerPage.playOrStopCineUsingKeyboardCKey();
//
//		String position = viewerPage.getCurrentScrollPosition(1);
//		viewerPage.waitForSilicesToChange(1);
//
//
//		viewerPage.playOrStopCineUsingKeyboardCKey();
//
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(1),position ,"Verifying the Cine performed using Keyboard C key","Cine is working fine");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test02_E2E_TC1386_verifyKeyboardShortcutsWindowLeveling() throws InterruptedException, AWTException, SAXException, IOException, ParserConfigurationException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		String currentWidth = viewerPage.getWindowWidthValueOverlayText(1);
//		String currentCenter = viewerPage.getWindowCenterValueOverlayText(1);
//
//		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();		
//		viewerPage.dragAndReleaseOnViewerWithoutHop(viewerPage.viewboxImage1,0, 0, 100, 50);		
//
//		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1),currentWidth ,"Verifying the WW/WL(width) performed using Keyboard W key","WW/WL is working fine");
//		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1),currentCenter ,"Verifying the WW/WL(center) performed using Keyboard W key","WW/WL is working fine");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test03_E2E_TC1386_verifyKeyboardShortcutsForF5() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		int pos = viewerPage.getCurrentScrollPositionOfViewbox(1);
//		
//		viewerPage.mouseWheelScrollInViewer(1, "down", 4);	
//
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1),(pos+4)+"","Scrolling the mouse wheel 4 times","Current scroll count is 5");
//
//		Header header= new Header(driver);
//
//		header.refreshPageUsingF5();
//
//		//After US676 on refresh, login screen is displaying
//		loginPage = new LoginPage(driver);
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1),pos+"","Verifying after page reload","Current scroll count is 1");
//
//	}
//
//	public void test04_E2E_TC1386_verifyKeyboardShortcutsForF11() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/goldImages/F11.png");
//
//		Header header= new Header(driver);		
//		header.browserFullMode();
//		header.waitForTimePeriod(1000);
//
//		viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/actualImages/F11.png");
//
//		String expectedImagePath = newImagePath+"/goldImages/F11.png";
//		String actualImagePath = newImagePath+"/actualImages/F11.png";
//		String diffImagePath = newImagePath+"/actualImages/F11_1.png";
//
//		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		viewerPage.assertFalse(cpStatus, "The actual and Expected image are same.","verified");
//
//
//
//
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test05_E2E_TC1386_verifyScrollUsingUpAndDownArrow() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		viewerPage.mouseHover(viewerPage.viewboxImg1);
//		int position = Integer.parseInt(viewerPage.getCurrentScrollPosition(1));
//
//		viewerPage.scrollDownToSliceUsingKeyboard(1);
//
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),position+1,"Verifying the forward scroll using up key","Scroll is working fine");
//
//		viewerPage.scrollUpToSliceUsingKeyboard(1);
//
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),position,"Verifying the backward scroll using up key","Scroll is working fine");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test06_E2E_TC1386_verifyDeletionOfMeasurement() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.waitForAllImagesToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the distance measurement from context Menu" );
//
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//
//		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
////		viewerPage.selectLinearMeasurementFromContextMenu(viewerPage.viewboxImg1);
//
//		// Clicking on the first viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Double clicking on first viewer" );
//
//
//
//		viewerPage.doubleClickOnViewbox(1);
//
//		// Drawing a horizontal line
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing the measurement on coordinates (0,0,100,0)" );
//		viewerPage.dragAndReleaseOnViewerWithClick(10,10, 400, 0);
//		viewerPage.dragAndReleaseOnViewerWithClick(10,10, 0, 400);		
//		viewerPage.dragAndReleaseOnViewerWithClick(10,10, 200, 300);
//		viewerPage.dragAndReleaseOnViewerWithClick(10,10, -100, -50);
//
//		// Taking screenshot after measurement is drawn
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/goldImages/distance.png");
//
////		viewerPage.deleteMeasurementOnFocusedViewbox();
//
//		viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/actualImages/distance.png");
//
//		String expectedImagePath = newImagePath+"/goldImages/distance.png";
//		String actualImagePath = newImagePath+"/actualImages/distance.png";
//		String diffImagePath = newImagePath+"/actualImages/distance1.png";
//
//		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		viewerPage.assertFalse(cpStatus, "The actual and Expected image are not same.","");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
//	public void test07_E2E_TC1386_verifySyncOFFFunctionality() throws InterruptedException, AWTException {
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Functional Testing – Input Devices : Keyboard Functions – Windows PC");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientListPage.clickOnPatientRow(PatientName);
//
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//
//		spStudyListPage.clickOntheFirstStudy();
//
//		ViewerPage viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//verifying the By default sync is set to Relative
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "To Check the default sync level is set to RELATIVE");
////		viewerpage.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"verifying the sync level","verified - sync level is set to RELATIVE");
//
//		// Enable the Window level icon 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from Radial Menu" );
//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);		
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
//		//Applying the window leveling
//		viewerpage.dragAndReleaseOnViewerWithoutHop(0, 0, 100, -50);
//
//
//		String widthBeforeSync = viewerpage.getWindowWidthValueOverlayText(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		
//
//		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(1) , widthBeforeSync, "Verify the width of viewbox1", "Window Width is for viewbox1 = "+widthBeforeSync+" - verified");		
//		viewerpage.assertEquals(viewerpage.windowWidthValueViewbox2.getText() , widthBeforeSync, "Verify the width of viewbox2", "Window Width is for viewbox1 = "+widthBeforeSync+" and viewbox2="+viewerpage.windowWidthValueViewbox2.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.windowWidthValueViewbox3.getText() , widthBeforeSync, "Verify the width of viewbox3", "Window Width is for viewbox1 = "+widthBeforeSync+" and viewbox3="+viewerpage.windowWidthValueViewbox3.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(4) , widthBeforeSync, "Verify the width of viewbox4", "Window Width is for viewbox1 = "+widthBeforeSync+" and viewbox4="+viewerpage.getWindowWidthValueOverlayText(4)+"- verified");
//
//
//		String windowCenterBeforeSync = viewerpage.getWindowCenterValueOverlayText(1);
//
//		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1) , windowCenterBeforeSync, "Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterBeforeSync+" - verified");		
//		viewerpage.assertEquals(viewerpage.windowCenterValueViewbox2.getText() , windowCenterBeforeSync, "Verify the window center of viewbox2", "Window window center is for viewbox1 = "+windowCenterBeforeSync+" and viewbox2="+viewerpage.windowCenterValueViewbox2.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.windowCenterValueViewbox3.getText() , windowCenterBeforeSync, "Verify the window center of viewbox3", "Window window center is for viewbox1 = "+windowCenterBeforeSync+" and viewbox3="+viewerpage.windowCenterValueViewbox3.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4) , windowCenterBeforeSync, "Verify the window center of viewbox4", "Window window center is for viewbox1 = "+windowCenterBeforeSync+" and viewbox4="+viewerpage.getWindowCenterValueOverlayText(4)+"- verified");
//
//
//		// sync off and performing the window leveling on second viewbox
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the sync OFF");		
//		viewerpage.performSyncONorOFF();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the WW/WL on second viewbox");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.viewboxImage2,0, 0, 100, -100);
//
//		String widthAfterSync = viewerpage.windowWidthValueViewbox2.getText();
//		String windowCenterAfterSync = viewerpage.windowCenterValueViewbox2.getText();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is OFF on click of spacebar and performing the window leveling");
//
//		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(1), widthBeforeSync, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox1="+viewerpage.getWindowWidthValueOverlayText(1)+"- verified");		
//		viewerpage.assertEquals(viewerpage.windowWidthValueViewbox2.getText(), widthAfterSync,  "Verify the width of viewbox2", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox2="+viewerpage.windowWidthValueViewbox2.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.windowWidthValueViewbox3.getText(), widthBeforeSync, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox3="+viewerpage.windowWidthValueViewbox3.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(4), widthBeforeSync, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox4="+viewerpage.getWindowWidthValueOverlayText(4)+"- verified");
//
//
//
//		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1) , windowCenterBeforeSync, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox1="+viewerpage.getWindowCenterValueOverlayText(1)+"- verified");		
//		viewerpage.assertEquals(viewerpage.windowCenterValueViewbox2.getText() , windowCenterAfterSync,  "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox2="+viewerpage.windowCenterValueViewbox2.getText()+"- verified");
//		viewerpage.assertEquals( viewerpage.windowCenterValueViewbox3.getText(), windowCenterBeforeSync, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox3="+viewerpage.windowCenterValueViewbox3.getText()+"- verified");
//		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4) , windowCenterBeforeSync, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox4="+viewerpage.getWindowCenterValueOverlayText(4)+"- verified");
//
//		// enabling the sync and performing the WW/WL on second viewbox
//
//		int widthDelta = Integer.parseInt(widthAfterSync)- Integer.parseInt(widthBeforeSync);
//		int windowCenterDelta=Integer.parseInt(windowCenterAfterSync)- Integer.parseInt(windowCenterBeforeSync);		
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is ON on click of spacebar and performing the window leveling on second viewbox for RELATIVE check");
//
//		viewerpage.performSyncONorOFF();
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.viewboxImage2,0, 0, 100, -100);
//
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowWidthValueViewbox2.getText())-Integer.parseInt(viewerpage.getWindowWidthValueOverlayText(1))) , widthDelta, "Verifying the Window width is relatively increased in viewbox1", "verified");		
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowWidthValueViewbox2.getText())-Integer.parseInt(viewerpage.windowWidthValueViewbox3.getText())) , widthDelta, "Verifying the Window width is relatively increased in viewbox3", "verified");	
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowWidthValueViewbox2.getText())-Integer.parseInt(viewerpage.getWindowWidthValueOverlayText(4))) , widthDelta, "Verifying the Window width is relatively increased in viewbox4", "verified");	
//
//
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowCenterValueViewbox2.getText())-Integer.parseInt(viewerpage.getWindowCenterValueOverlayText(1))) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox1", "verified");		
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowCenterValueViewbox2.getText())-Integer.parseInt(viewerpage.windowCenterValueViewbox3.getText())) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox3", "verified");	
//		viewerpage.assertEquals((Integer.parseInt(viewerpage.windowCenterValueViewbox2.getText())-Integer.parseInt(viewerpage.getWindowCenterValueOverlayText(4))) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox4", "verified");	
//
//
//	}
//
//
//
//
//}
