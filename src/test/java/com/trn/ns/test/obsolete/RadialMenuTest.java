//import org.testng.annotations.Test;
//
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.obsolete;
////import static com.trn.ns.test.configs.Configurations.PASS;
////import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
////
////import java.awt.AWTException;
////import java.util.Set;
////
////import org.openqa.selenium.TimeoutException;
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.trn.ns.dataProviders.DataProviderArguments;
////import com.trn.ns.dataProviders.ExcelDataProvider;
////import com.trn.ns.page.factory.LoginPage;
////import com.trn.ns.page.factory.MeasurementWithUnit;
////
////import com.trn.ns.page.factory.PatientListPage;
////import com.trn.ns.page.factory.PointAnnotation;
////import com.trn.ns.page.factory.SimpleLine;
////
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.DataReader;
////import com.trn.ns.utilities.ExtentManager;
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class RadialMenuTest extends TestBase {
////
////	private ViewerPage viewerPage;
////	private LoginPage loginPage;
////	private PatientListPage patientPage;
////	
////	private ExtentTest extentTest;
////
////	// Get Patient Name
////	String filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
////	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
////
////	// Get Patient Name
////	String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
////	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath1);
////
////	// Get Patient Name
////	String filePath2=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
////	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
////
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod() 
////	{
////
////		loginPage = new LoginPage(driver);
////		loginPage.navigateToBaseURL();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
////		patientPage = new PatientListPage(driver);
////		patientPage.waitForPatientPageToLoad();
////	}
////
////	@Test(groups = { "firefox", "Chrome", "multimonitor","US242" })
////	//Due to ongoing issue with  Browser Switch, this script is not running in IE11 and Edge
////	public void test11_US242_TC656_verifyCineOnChildWindow() throws InterruptedException, AWTException 
////	{
////		
////			extentTest = ExtentManager.getTestInstance();
////			extentTest.setDescription("Verify Radial Menu option on Multi Mointor window.");
////			patientPage = new PatientListPage(driver);
////			
////			viewerPage = new ViewerPage(driver);
////			
////			patientPage.clickOnPatientRow(PatientName2);
////			studyPage.clickOntheFirstStudy();
////			viewerPage.waitForViewerpageToLoad();
////			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/5]","Verify user is navigating to viewer page.");
////			viewerPage.assertTrue(viewerPage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
////			// selecting 2 window
////			String parentWindow = driver.getWindowHandle();
////			viewerPage.openOrCloseChildWindows(2);
////			viewerPage.switchToWindow(parentWindow);
////			viewerPage.maximizeWindow();
//////			viewerPage.selectCheckBoxApplyToAllMonitor();
////			// changing layout to 2X1
////			viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
////			viewerPage.waitForAllImagesToLoad();
////			int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
////			String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
////			viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/goldImages/radialCinePlayImage.png");
////			//open a Radial Menu for Viewbox1 and Close Cine operation
////			viewerPage.selectCineFromRadialBar(viewerPage.viewboxImg1);
////			viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/actualImages/radialCinePlayImage.png");
////			int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]", "Verify Cine Play is stopped on Parent Window Viewbox");
////			viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Cine play is stopped", "Cine is stopped and working fine on Parent Window Viewbox");
////			String expectedImagePath = newImagePath+"/goldImages/radialCinePlayImage.png";
////			String actualImagePath = newImagePath+"/actualImages/radialCinePlayImage.png";		
////			boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, actualImagePath);
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]", "Verify post Cine operation images on Parent Window Viewbox are different");
////			viewerPage.assertFalse(cpStatus, "The Pre and Post Cine Images are different","");
////			ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying Cine operation from Radial Menu on Parent Window", "Successfully verified checkpoint with image comparison.<br>Image name is radialCinePlayImage.png");
////			Set<String> childWinHandles = driver.getWindowHandles();
////			for (String childWindow : childWinHandles)
////				if (!childWindow.equals(parentWindow)) 
////				{
////					viewerPage.switchToWindow(childWindow);
////					viewerPage.maximizeWindow();	
////				}	
////			//open a Radial Menu for Viewbox1 and Perform Cine operation
////			currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
////			newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
////			viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/goldImages/radialCinePlayImage.png");
////			//open a Radial Menu for Viewbox1 and Close Cine operation
////			viewerPage.selectCineFromRadialBar(viewerPage.viewboxImg1);
////			viewerPage.takeElementScreenShot(viewerPage.viewboxImage1, newImagePath+"/actualImages/radialCinePlayImage.png");
////			imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]", "Verify Cine Play is stopped on Child Window Viewbox");
////			viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Cine play is stopped", "Cine is stopped and working fine on Child Window Viewbox");
////			expectedImagePath = newImagePath+"/goldImages/radialCinePlayImage.png";
////			actualImagePath = newImagePath+"/actualImages/radialCinePlayImage.png";
////			cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, actualImagePath);
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]", "Verify post Cine operation images on Child Window Viewbox are different");
////			viewerPage.assertFalse(cpStatus, "The Pre and Post Cine Images are different","");
////			ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying Cine operation from Radial Menu on Child Window", "Successfully verified checkpoint with image comparison.<br>Image name is radialCinePlayImage.png");
////		}	
////
////	@Test(groups = { "firefox", "Chrome","multimonitor","DE329" })
////	//Due to ongoing issue with  Browser Switch, this script is not running in IE11 and Edge
////	public void test16_DE329_TC1427_verifyRadialCommandOnChildWindowAppliesToParentWindow()throws InterruptedException, AWTException
////	{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Radial Menu Commands on Child Window applies to Parent Window.");
////		patientPage = new PatientListPage(driver);
////		
////		viewerPage = new ViewerPage(driver);
////		
////		patientPage.clickOnPatientRow(PatientName2);
////		studyPage.clickOntheFirstStudy();
////		viewerPage.waitForViewerpageToLoad();
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/5]","Verify user is navigating to viewer page.");
////		viewerPage.assertTrue(viewerPage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
////		// selecting 2 window
////		String parentWindow = driver.getWindowHandle();
////		viewerPage.openOrCloseChildWindows(2);
////		viewerPage.switchToWindow(parentWindow);
////		viewerPage.maximizeWindow();
//////		viewerPage.selectCheckBoxApplyToAllMonitor();
////		// changing layout to 2X1
////		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
////		viewerPage.waitForAllImagesToLoad();
////		// Select Window Level Icon form Radial Menu
////		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.viewboxImg1);
////		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);	
////		int windowWidth_parentviewbox1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
////		int windowCenter_parentViewbox1=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
////		int parentIntialZoomLevel = viewerPage.getZoomLevel(1);
////		Set<String> childWinHandles = driver.getWindowHandles();
////		for (String childWindow : childWinHandles)
////			if (!childWindow.equals(parentWindow)) 
////			{
////				viewerPage.switchToWindow(childWindow);
////				viewerPage.maximizeWindow();	
////			}	
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying parent and child window are in sync when operated WWL from child window.");		
////		viewerPage.assertEquals(windowWidth_parentviewbox1 , Integer.parseInt(viewerPage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is absolute in viewbox1", "verified");
////		viewerPage.assertEquals(windowWidth_parentviewbox1 , Integer.parseInt(viewerPage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is absolute in viewbox2", "verified");
////		viewerPage.assertEquals(windowCenter_parentViewbox1 , Integer.parseInt(viewerPage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is absolute in viewbox1", "verified");
////		viewerPage.assertEquals(windowCenter_parentViewbox1 , Integer.parseInt(viewerPage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is absolute in viewbox2", "verified");
////		int ChildIntialZoomLevel= viewerPage.getZoomLevel(1);
////		//open a Radial Menu for Viewbox1 and perform Zoom operation
////		viewerPage.selectZoomFromQuickToolbar(viewerPage.viewboxImg1);
////		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -50,-50);
////		int ChildFinalZoomLevel = viewerPage.getZoomLevel(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/5]","Verify Zoom level in Child Window increases on Mouse Up");
////		viewerPage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
////		viewerPage.switchToWindow(parentWindow);
////		viewerPage.maximizeWindow();
////		int parentFinalZoomLevel = viewerPage.getZoomLevel(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/5]","Verify Zoom level in Parent Window increases on Mouse Up");
////		viewerPage.assertNotEquals(parentFinalZoomLevel,parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ parentIntialZoomLevel + " to "+ parentFinalZoomLevel);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/5]","Verify Zoom level on both Widow remain same");
////		viewerPage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");	
////	}	
////
////	@Test(groups = { "firefox", "Chrome", "multimonitor","DE264" })
////	//Due to ongoing issue with  Browser Switch, this script is not running in IE11 and Edge
////	public void test18_DE264_TC1330_verifyRadialMenuPersistOnCompositeCommand() throws InterruptedException, AWTException
////	{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Radial Menu persist after applying a Composite Command.");
////		patientPage = new PatientListPage(driver);
////		
////		viewerPage = new ViewerPage(driver);
////	
////		patientPage.clickOnPatientRow(PatientName2);
////		studyPage.clickOntheFirstStudy();
////		viewerPage.waitForViewerpageToLoad();
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/5]","Verify user is navigating to viewer page.");
////		viewerPage.assertTrue(viewerPage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
////		// selecting 2 window
////		String parentWindow = driver.getWindowHandle();
////		viewerPage.openOrCloseChildWindows(2);
////		viewerPage.switchToWindow(parentWindow);
////		viewerPage.maximizeWindow();
//////		viewerPage.selectCheckBoxApplyToAllMonitor();
////		// changing layout to 2X1
////		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
////		viewerPage.waitForAllImagesToLoad();
////		// Select Window Level Icon form Radial Menu
////		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.viewboxImg1);
////		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);	
////		int windowWidth_parentviewbox1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
////		int windowCenter_parentViewbox1=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
////		int parentIntialZoomLevel = viewerPage.getZoomLevel(1);
////		Set<String> childWinHandles = driver.getWindowHandles();
////		for (String childWindow : childWinHandles)
////			if (!childWindow.equals(parentWindow)) 
////			{
////				viewerPage.switchToWindow(childWindow);
////				viewerPage.maximizeWindow();	
////			}	
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying parent and child window are in sync when operated WWL from child window.");		
////		viewerPage.assertEquals(windowWidth_parentviewbox1 , Integer.parseInt(viewerPage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is absolute in viewbox1", "verified");
////		viewerPage.assertEquals(windowWidth_parentviewbox1 , Integer.parseInt(viewerPage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is absolute in viewbox2", "verified");
////		viewerPage.assertEquals(windowCenter_parentViewbox1 , Integer.parseInt(viewerPage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is absolute in viewbox1", "verified");
////		viewerPage.assertEquals(windowCenter_parentViewbox1 , Integer.parseInt(viewerPage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is absolute in viewbox2", "verified");
////		int ChildIntialZoomLevel= viewerPage.getZoomLevel(1);
////		//open a Radial Menu for Viewbox1 and perform Zoom operation
////		viewerPage.selectZoomFromQuickToolbar(viewerPage.viewboxImg1);
////		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -50,-50);
////		int ChildFinalZoomLevel = viewerPage.getZoomLevel(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/5]","Verify Zoom level in Child Window increases on Mouse Up");
////		viewerPage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
////		viewerPage.switchToWindow(parentWindow);
////		viewerPage.maximizeWindow();
////		int parentFinalZoomLevel = viewerPage.getZoomLevel(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/5]","Verify Zoom level in Parent Window increases on Mouse Up");
////		viewerPage.assertNotEquals(parentFinalZoomLevel,parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ parentIntialZoomLevel + " to "+ parentFinalZoomLevel);
////		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/5]","Verify Zoom level on both Widow remain same");
////		viewerPage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");	
////		for (String childWindow : childWinHandles)
////			if (!childWindow.equals(parentWindow)) 
////			{
////				viewerPage.switchToWindow(childWindow);
////				viewerPage.maximizeWindow();	
////			}
////		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
////		//open a Radial Menu for Viewbox1 and Perform Cine operation
////		viewerPage.selectCineFromRadialBar(viewerPage.viewboxImg1);
////		viewerPage.waitForSilicesToChange(1);
////		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]", "Verify Cine Play is stopped on Child Window Viewbox");
////		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Cine play is stopped", "Cine is stopped and working fine on Child Window Viewbox");
////		//open a Radial Menu for ViewBox1 on Child Window
////		viewerPage.openRadialMenu(viewerPage.viewboxImg1);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]", "Verify Instance of Radial Menu appear on ViewBox");
////		viewerPage.assertTrue(viewerPage.isAllRadialBarIconDisplayed(), "Validate Menu is visible on ViewBox1 of Child Window", "The Radial menu is visble on ViewBox1 of Child Window");
////	}	
////}
////
////
////
////
////
//
//@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US242"})
//public void test15_US242_TC674_verifyOuterArcOfRadialToolbarIsAddedOnClickingObject() throws InterruptedException 
//{
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify that second layer arc should get added by selecting object on the out layer");
//	helper = new HelperClass(driver);
//	viewerPage = helper.loadViewerDirectly(PatientName2, 1);
//
//	//open a Radial Menu for Viewbox1
//	viewerPage.openRadialMenu(viewerPage.getViewPort(1));
//	viewerPage.selectThreeDotIcon();
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify Instance of Radial Menu appear on ViewBox");
//	viewerPage.assertTrue(viewerPage.isAllRadialBarIconDisplayed(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify Outer arch of Radial Menu appear on clicking three dot icon");
//	viewerPage.assertTrue(viewerPage.checkForElementVisibility(viewerPage.contextMenuAllIcon), "Verify Outer arch of Radial Menu appear on clicking three dot icon", "The Outer Arch of Radial Menu appear on clicking three dot icon");
//	//open a Radial Menu for Viewbox2
//	viewerPage.openRadialMenu(viewerPage.getViewPort(2));
//	viewerPage.selectThreeDotIcon();
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify Instance of Radial Menu appear on ViewBox");
//	viewerPage.assertTrue(viewerPage.isAllRadialBarIconDisplayed(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox2");
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify Outer arch of Radial Menu appear on clicking three dot icon");
//	viewerPage.assertTrue(viewerPage.checkForElementVisibility(viewerPage.contextMenuAllIcon), "Verify Outer arch of Radial Menu appear on clicking three dot icon", "The Outer Arch of Radial Menu appear on clicking three dot icon");
//}


//@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE207" })
//	public void test19_DE207_TC825_verifyIconsOfRadialBarAreWhiteColorOnGreyBackground() throws  InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Icons of Radial Bar are of White Color on Grey Background");
//		helper = new HelperClass(driver);
//		viewerPage = helper.loadViewerDirectly(PatientName2, 1);
//		
//		//open a Radial Menu for Viewbox1
//		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]", "Verify BackGround Color of Distance Measurement Icon");
//		viewerPage.assertTrue(viewerPage.verifyBackGroundGColorForRadialMenuOption(viewerPage.distanceIcon), "Validate back ground color of Distance Measurement Icon", "The BackGround color of distance measurement icon is Grey :"+ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]", "Verify BackGround Color of Cine Icon");
////		viewerPage.assertTrue(viewerPage.verifyBackGroundGColorForRadialMenuOption(viewerPage.cinePlayIconOnRadialBar), "Validate back ground color of Distance Measurement Icon", "The BackGround color of Cine icon is Grey: "+ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]", "Verify BackGround Color of Window Leveling Icon");
//		viewerPage.assertTrue(viewerPage.verifyBackGroundGColorForRadialMenuOption(viewerPage.windowLevelingIcon), "Validate back ground color of Wndow Leveling Icon", "The BackGround color of Cine icon is Grey: "+ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]", "Verify BackGround Color of Zoom Icon");
//		viewerPage.assertTrue(viewerPage.verifyBackGroundGColorForRadialMenuOption(viewerPage.zoomIcon), "Validate back ground color of Zoom Icon", "The BackGround color of Cine icon is Grey: "+ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]", "Verify BackGround Color of Scroll Icon");
//		viewerPage.assertTrue(viewerPage.verifyBackGroundGColorForRadialMenuOption(viewerPage.scrollIcon), "Validate back ground color of Scroll Icon","The BackGround color of Cine icon is Grey: "+ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]", "Verify Icons of Radial Bar are of White Color");
//		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify Icons of Radial Bar are of White Color","TC825_Verfiy_Icons_Of_Radial_Bar"); 
//	}