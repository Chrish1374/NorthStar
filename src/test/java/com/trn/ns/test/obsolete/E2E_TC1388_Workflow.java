//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
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
//public class E2E_TC1388_Workflow extends TestBase{
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	//Get Patient Name
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	//Limitation : Not work in edge because of mouse move event and also not work in IE11 because of DE397
//	@Test(groups ={"Chrome","firefox"})
//	public void test01_E2E_TC1388_Workflow_verifyRadialToolbar() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Isolated Feature Testing – Image Related : Radial Toolbar");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/21]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();	
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/21]", "Performing the right click" );
//		viewerpage.performMouseRightClick(viewerpage.viewboxImg1);
//
//		//1. Checking radial bar ->Ruler, Cine, W/C, Zoom, Scroll icons
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/21]", "Validate radial tool bar icons" );
//		viewerpage.assertTrue(viewerpage.isAllRadialBarIconDisplayed(), "Verifying the Radial Menu Presence with Ruler, Cine, W/C, Zoom, Scroll icons", "Radial Menu is present with Ruler, Cine, W/C, Zoom, Scroll icons");
//
//		//2. Clicking on the 3 dots icon on radial bar -> allIcon 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/21]", "Validate All icon Presence" );
//		viewerpage.assertTrue(viewerpage.threeDotsIcon.isDisplayed(), "Verifying the Three dots icon Presence", "Three dots icon is present");
//		viewerpage.click(viewerpage.threeDotsIcon);
//		viewerpage.assertTrue(viewerpage.allIcon.isDisplayed(), "Verifying the All icon Presence", "All icon is present");		
//
//		//3. Clicking All -> Context Menu 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/21]", "Validate Context Menu Presence" );
//		viewerpage.click(viewerpage.allIcon);
//		viewerpage.assertTrue(viewerpage.contextMenu.isDisplayed(), "Verifying the Context Presence", "Context Menu is present");
//		//Close context menu
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/21]", "Validate Context Menu Absence" );
////		viewerpage.closeContextMenu();
//		viewerpage.assertFalse(viewerpage.contextMenu.isDisplayed(), "Verifying the Context Absence", "Context Menu is absent");
//
//		//4. W/C using radial menu
//		// Right clicking on viewbox 1-> Select W/C -> Perform W/C
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/21]", "Performing the right click" );
//
//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//		String beforeWWValue = viewerpage.getValueOfWindowWidth(1);
//		String beforeWCValue = viewerpage.getValueOfWindowCenter(1);
//		viewerpage.doubleClickJs(viewerpage.viewboxImg1);
//		viewerpage.waitForViewerpageToLoad(1);
//		// Adding this wait as it happens so fast
//		viewerpage.waitForTimePeriod(2000);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/21]", "WW/WL is enabled" );
//		viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is enabled","WW/WL is enabled");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(10,10, 200, 200);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/21]", "Validate WW/WL functionality" );
//		viewerpage.assertNotEquals(beforeWWValue,viewerpage.getValueOfWindowWidth(1), "Verifying WW functionality", "WW is working fine");
//		viewerpage.assertNotEquals(beforeWCValue,viewerpage.getValueOfWindowCenter(1), "Verifying WC functionality", "WC is working fine");
//
//		//6. Scroll using radial menu
//		// Right clicking on viewbox 1-> Select Scroll -> Perform Scroll
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/21]", "Performing the right click" );
//		String currentScrollPos = (viewerpage.currentScrollPositionOfViewbox.getText().trim());
//
//		viewerpage.selectScrollFromQuickToolbar(viewerpage.viewboxImg1);
//		viewerpage.dragAndReleaseOnViewer(0, 0, 0, 100);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/21]", "Validate Scroll functionality" );
//		viewerpage.assertNotEquals(currentScrollPos,(viewerpage.currentScrollPositionOfViewbox.getText().trim()), "Verifying the scroll functionality", "scroll is working fine");
//
//		//7. Zoom using radial menu
//		// Right clicking on viewbox 1-> Select Zoom -> Perform Zoom
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[16/21]", "Performing the right click and enabling the zoom from Radial Menu for first series" );
//		int beforeZoom = viewerpage.getZoomLevel(1);
//
//		viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//		viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[18/21]", "Validate Zoom functionality" );
//		viewerpage.assertTrue(beforeZoom < viewerpage.getZoomLevel(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+viewerpage.getZoomLevel(1));
//
//		//5. Measurement using radial menu
//		// Right clicking on viewbox 1-> Select ruler -> Perform liner distance measurement
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/21]", "Performing the right click" );
//
////		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/21]", "Linear Distance is enabled" );
//		viewerpage.assertTrue(viewerpage.distanceIcon.isEnabled(),"Verifying Linear Distance is enabled","Linear Distance is enabled");
//
//		// Drawing a horizontal line
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/21]", "Validate Linear distance functionality" );
//		viewerpage.dragAndReleaseOnViewerWithoutHop(50,50, 100, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1,"Verify the measurement drawn @ coordinates (0,0,100,0)", "Viewbox1_Measurement_RadialMenu",new int[] { (int) viewerpage.getImageCoordinate(viewerpage.viewboxImg1).get("X"), (int) viewerpage.getImageCoordinate(viewerpage.viewboxImg1).get("Y"), (int) viewerpage.getImageCoordinate(viewerpage.viewboxImg1).get("Width"), (int) viewerpage.getImageCoordinate(viewerpage.viewboxImg1).get("Height")});
//
//
//		//8. Cine using radial menu
//		// Right clicking on viewbox 1-> Select Cine -> Perform cine
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[19/21]", "Performing the right click" );
//
//		viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[20/21]", "Cine is enabled" );
////		viewerpage.assertTrue(viewerpage.distanceIcon.isEnabled(),"Verifying Linear Distance is enabled","Linear Distance is enabled");
//
//		int currentImage = Integer.parseInt(viewerpage.currentScrollPositionOfViewbox.getText().trim());
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/BeforeCineplayImage.png");
//
//		viewerpage.selectCineFromRadialBar(viewerpage.viewboxImg1);	
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/AfterCineplayImage.png");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[21/21]", "Validate Cine functionality" );
//
//		viewerpage.assertNotEquals(currentImage, Integer.parseInt(viewerpage.currentScrollPositionOfViewbox.getText().trim()), "verifying the cine play is stopped", "cine is stopped and working fine");
//
//		String beforeCinePlayImage = newImagePath+"/actualImages/BeforeCineplayImage.png";
//		String afterCinePlayImageA = newImagePath+"/actualImages/AfterCineplayImage.png";
//		String diffCinePlayImageA = newImagePath+"/actualImages/DifferentImage.png";
//
//		boolean cpStatusA =  viewerpage.compareimages(beforeCinePlayImage, afterCinePlayImageA, diffCinePlayImageA);
//		viewerpage.assertFalse(cpStatusA, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//
//	}
//}
