package com.trn.ns.test.viewer.basic;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class QuickToolbarTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerToolbar tool;
	private ViewBoxToolPanel preset;


	// Get Patient Name
	String filePath1=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2=Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String iblJohnDoe = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3=Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String sr3Chest = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("CT_PET_Multiphase_filepath");
	String ctpetMultiphase = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerLayout layout;
	private String loadedTheme;
	private ViewerToolbar toolbar;
	private ViewBoxToolPanel viewBoxPanel;

	@Test(groups ={"US2217", "Positive","F1090","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2217_TC9458_TC9459_TC9460_TC9461_TC9462_verifyThemeOnQT(String theme) throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the quickToolBox on eureka theme"
				+ "<br> Verify look and feel of the quickToolBox on dark theme"
				+ "<br> Verify thin purple border and tooltip is displayed on hovering over the icons present inside quicktoolbox in eureka theme"
				+ "<br> Verify thin purple border and tooltip is displayed on hovering over the icons present inside quicktoolbox in dark theme"
				+ "<br> Verify quick toolbox on browser resize"
				+ "<br> Verify 4D icon from quicktoolbox is disabled when not applicable");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9, 1);	
	    tool=new ViewerToolbar(driver);
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[1/18]", "Verifying all the icons are displayed in quicktoolbar");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[2/18]", "Verifying all the icons are displayed in quicktoolbar");
		PagesTheme pageTheme = new PagesTheme(driver);
		viewerPage.assertTrue(pageTheme.verifyThemeForPopup(viewerPage.quickToolboxMenu,loadedTheme),"Checkpoint[3/18]","Verifying the theme is getting applied on quicktoolbar");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL),"Checkpoint[4/18]","verifying that scroll icon is default selected");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(pageTheme.verifyButtonIsFilled(viewerPage.allViewerRectIcons.get(0), loadedTheme),"Checkpoint[5/18]","Verifying the scroll icon background color when selected");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.scrollIcon), loadedTheme),"Checkpoint[6/18]","Verifying the tooltip is adhering the theme");
		
		for (int i =1;i<viewerPage.allViewerIcons.size();i++) {
		
			viewerPage.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allViewerRectIcons.get(i), loadedTheme),"Checkpoint[7/18]","verifying that icon's background is not filled");
			viewerPage.assertTrue(pageTheme.verifyThemeForSectionWithoutBorder(viewerPage.allViewerIcons.get(i), loadedTheme),"Checkpoint[8/18]","verifying the border is not displayed");
			
			viewerPage.mouseHover(viewerPage.allViewerIcons.get(i));
			viewerPage.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allViewerRectIcons.get(i), loadedTheme),"Checkpoint[9/18]","Verifying that background is not changed on mousehover");
			if(i==6) {
				viewerPage.assertTrue(pageTheme.verifyThemeForSectionWithoutBorder(viewerPage.allViewerIcons.get(i), loadedTheme),"Checkpoint[10/18]","verifying the cine4D icon's background is not changed");
				viewerPage.assertTrue(viewerPage.verifyCine4DIsInactive(),"Checkpoint[11/18]","verifying that cine4d icon is disabled for liver9 patient");
			}
			else
				viewerPage.assertTrue(pageTheme.verifyThemeForSectionWithBorder(viewerPage.allViewerIcons.get(i), loadedTheme),"Checkpoint[12/18]","verifying the border is displayed on mouse hover as per theme");
						
			viewerPage.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allAnnotationRectIcons.get(i-1), loadedTheme),"Checkpoint[13/18]","verifying the annotation icon's background");
			viewerPage.assertTrue(pageTheme.verifyThemeForSectionWithoutBorder(viewerPage.allAnnotationIcons.get(i-1), loadedTheme),"Checkpoint[14/18]","verifying that border is not displayed");
			
			viewerPage.mouseHover(viewerPage.allAnnotationIcons.get(i-1));
			viewerPage.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allAnnotationRectIcons.get(i-1), loadedTheme),"Checkpoint[15/18]","verifying that no background is displayed for annotation icons on mouse hover");
			viewerPage.assertTrue(pageTheme.verifyThemeForSectionWithBorder(viewerPage.allAnnotationIcons.get(i-1), loadedTheme),"Checkpoint[16/18]","verifying the border is displayed on mouse hover as per theme");
			
		}
		
		int width = viewerPage.getWidthOfWebElement(viewerPage.quickToolbarMenu);
		int height = viewerPage.getHeightOfWebElement(viewerPage.quickToolbarMenu);
		
		viewerPage.resizeBrowserWindow(700, 700);
		viewerPage.openQuickToolbar(2);
				
		viewerPage.assertEquals(width,viewerPage.getWidthOfWebElement(viewerPage.quickToolbarMenu),"Checkpoint[17/18]","verifying that quicktoolbar is not changed/cut after browser resize");
		viewerPage.assertEquals(height,viewerPage.getHeightOfWebElement(viewerPage.quickToolbarMenu),"Checkpoint[18/18]","verifying that quicktoolbar is not changed/cut after browser resize");
		
		
		
		
	}
	
	@Test(groups ={"US2217", "Negative","F1090","E2E"})
	public void test02_US2217_TC9463_TC9465_VerifyNoToolbaronPDFSR() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify quick toolbox should not open on PDF/SR series  and on empty viewbox"
				+ "<br> Verify quick toolbox on opening after layout change or opening near viewbox boundries");
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(iblJohnDoe, 1,1);
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(3));
		viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[1/4]", "Verifying the quick toolbar is not displayed on PDF");
		
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		int totalviewboxes = layout.getNumberOfCanvasForLayout();
		
		for(int i=6;i<totalviewboxes;i++) {
			viewerPage.performMouseRightClick(viewerPage.getViewPort(i));
			viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[2."+i+"/4]", "Verifying the quick toolbar is not displayed on empty viewbox");
		
		}
	
		viewerPage.click(viewerPage.getViewPort(4));

		helper.browserBackAndReloadViewer(sr3Chest, 1, 2);

		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[3/4]", "Verifying the quick toolbar is not displayed on SR");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		for(int i=4;i<totalviewboxes;i++) {
			viewerPage.performMouseRightClick(viewerPage.getViewPort(i));
			viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[4."+i+"/4]", "Verifying the quick toolbar is not displayed on empty viewbox");
		
		}
		
		
	}
		
	@Test(groups ={"US2217", "Negative","F1090"})
	public void test03_US2217_TC9466_VerifyClosingScenarios() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify quick toolbox closing scenarios");
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9, 1);
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[1/12]", "verifying the quick toolbar is displayed");

		viewerPage.click(-50,-50);		
		viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[2/12]", "verifying that quicktool bar is closed on left click of same viewbox");
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[3/12]", "verifying the quick toolbar is displayed");

		viewerPage.click(viewerPage.getViewPort(2));		
		viewerPage.assertFalse(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[4/12]", "verifying that quicktool bar is closed on left click of other viewbox");
		
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[5/12]", "verifying the quick toolbar is displayed");

		int x = viewerPage.getXCoordinate(viewerPage.quickToolbarMenu);
		int y = viewerPage.getYCoordinate(viewerPage.quickToolbarMenu);
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1),-50,-50);		
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[6/12]", "verifying the first instance of quicktool bar is closed and new is opened on right click");
		viewerPage.assertNotEquals(x, viewerPage.getXCoordinate(viewerPage.quickToolbarMenu), "Checkpoint[7/12]", "verifying the coordinates are changed");
		viewerPage.assertNotEquals(y, viewerPage.getYCoordinate(viewerPage.quickToolbarMenu), "Checkpoint[8/12]", "verifying the coordinates are changed");
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[9/12]", "verifying the quick toolbar is displayed");

		x = viewerPage.getXCoordinate(viewerPage.quickToolbarMenu);
		y = viewerPage.getYCoordinate(viewerPage.quickToolbarMenu);
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[10/12]", "verifying the first instance of quicktool bar is closed and new is opened on right click in another viewbox");
		viewerPage.assertNotEquals(x, viewerPage.getXCoordinate(viewerPage.quickToolbarMenu), "Checkpoint[11/12]", "verifying the coordinates are changed");
		viewerPage.assertNotEquals(y, viewerPage.getYCoordinate(viewerPage.quickToolbarMenu), "Checkpoint[12/12]", "verifying the coordinates are changed");
	

		
	}
	
	@Test(groups = {"US2217","Positive","F1090"})
	public void test04_US2217_verifyQuickToolbarOnEdge() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify quicktool bar on edge");
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9, 1);
		
		viewerPage.resizeBrowserWindow(1366, 768);
		
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[1/4]", "verifying the toolbar is present");
		
		int width = viewerPage.getWidthOfWebElement(viewerPage.quickToolbarMenu);
		int height = viewerPage.getHeightOfWebElement(viewerPage.quickToolbarMenu);
			
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1),250,0);
		viewerPage.assertTrue(viewerPage.verifyQuickToolboxPresence(), "Checkpoint[2/4]", "verifying the quick toolbar is opened on click of viewbox edge");
		
		viewerPage.assertEquals(width,viewerPage.getWidthOfWebElement(viewerPage.quickToolbarMenu),"Checkpoint[3/4]","verifying that dimensions are same");
		viewerPage.assertEquals(height,viewerPage.getHeightOfWebElement(viewerPage.quickToolbarMenu),"Checkpoint[4/4]","verifying that dimensions are same");
	

		
	}
	
	@Test(groups ={"US2325", "Positive","F1090","E2E"})
	public void test05_US2325_TC9774_TC9780_verifyZoom() throws  InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll and zoom icons and its functionality from quick toolbar"
				+ "<br> Verify icons state from quick toolbar and top viewer toolbar are in sync");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String zoomBefore = newImagePath+"/goldImages/zoomBefore.png";
		String zoomAfter = newImagePath+"/actualImages/zoomAfter.png";
		String diffImagePath = newImagePath+"/actualImages/diffImageZoom.png";
	
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), zoomBefore);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting Zoom from quicktoolbar");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM), "Checkpoint[1/4]", "Verifying the zoom icon is selected");
		viewerPage.click(50,50);
		
		toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM), "Checkpoint[2/4]", "verifying that zoom icon is selected on viewer toolbar");
		
		viewerPage.click(50,75);
		int beforeZoom = viewBoxPanel.getZoomValue(1);
		preset=new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Validate Zoom functionality" );
		viewerPage.assertTrue(beforeZoom < viewBoxPanel.getZoomValue(1), "Checkpoint[3/4]", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+viewBoxPanel.getZoomValue(1));
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), zoomAfter);
		
		boolean cpStatus =  viewerPage.compareimages(zoomBefore, zoomAfter, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[4/4]","Verifying the zoom is changed");

	
	}
		
	@Test(groups ={"US2325","Positive","F1090","E2E"})
	public void test06_US2325_TC9775_TC9780_verifyPan() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN, WW, invert icons and its functionality from quick toolbar"
				+ "<br> Verify icons state from quick toolbar and top viewer toolbar are in sync");
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String panBefore = newImagePath+"/goldImages/panBefore.png";
		String panAfter = newImagePath+"/actualImages/panAfter.png";
		String diffImagePath = newImagePath+"/actualImages/diffImagePan.png";

	
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), panBefore);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting Pan from quicktoolbar");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Checkpoint[1/3]", "Verifying the pan icon is selected");
		viewerPage.click(50,50);		
		
		toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Checkpoint[2/3]", "verifying that pan is selected in viewer tool bar as well");
		
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Validate pan functionality" );
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), panAfter);
		
		boolean cpStatus =  viewerPage.compareimages(panBefore, panAfter, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[3/3]","After PAN image location is changed");

	

	}
		
	@Test(groups ={"US2325","Positive","F1090","E2E"})
	public void test07_US2325_TC9774_TC9780_verifyScroll() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll and zoom icons and its functionality from quick toolbar"
				+ "<br> Verify icons state from quick toolbar and top viewer toolbar are in sync");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL), "Checkpoint[1/3]", "Verifying the scroll icon is selected in quicktoolbar");
		viewerPage.click(50,50);
	
		toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL), "Checkpoint[2/3]", "Verifying the scroll icon is selected in viewertoolbar");
		
		int currentScrollPos = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.click(viewerPage.getViewPort(2));
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Validate scroll functionality" );
		viewerPage.assertNotEquals(currentScrollPos,viewerPage.getCurrentScrollPositionOfViewbox(1), "Checkpoint[3/3]", "scroll is working fine");
	}
	
	@Test(groups ={"US2325","Positive","F1090","E2E"})
	public void test08_US2325_TC9775_TC9780_verifyWindowing() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN, WW, invert icons and its functionality from quick toolbar"
				+ "<br> Verify icons state from quick toolbar and top viewer toolbar are in sync");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);		
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting WW/WL from quicktoolbar");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Checkpoint[1/4]", "Verifying the windowing icon is selected in quicktoolbar");
		viewerPage.click(50,50);
			
		toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.WINDOW_LEVEL_TITLE), "Checkpoint[2/4]", "Verifying the windowing icon is selected in viewer toolbar");
		
		//performing  WW/WL 
		
		String ww = viewerPage.getWindowWidthValText(1);
		String wc = viewerPage.getWindowCenterText(1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);
		
		viewerPage.assertEquals(ww, viewerPage.getWindowWidthValText(1),"Checkpoint[3/4]","verifying there is no change is window width");
		viewerPage.assertNotEquals(wc, viewerPage.getWindowCenterText(1),"Checkpoint[4/4]","verifying that contrast is changed");

	}
	
	@Test(groups ={"US2325","Positive","F1090","E2E"})
	public void test09_US2325_TC9776_CinePlayUsingQuicktoolbar() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 2D and 4D cine  icons and its functionality from quick toolbar");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);	
		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);
		
		//playing cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify cine is played.");
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));

		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/cineImage.png";
		String actualImagePath = newImagePath+"/actualImages/cineImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImageCineImage.png";

		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), expectedImagePath);

		//stopping cine
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));

		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1),actualImagePath);
		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Checkpoint[1/2]", "cine is stopped and working fine");
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[2/2]","After cine images are change");
	}
		
	@Test(groups ={"US2325","Positive","F1090","E2E"})
	public void test10_US2325_TC9778_verifyWindowingKeyBoard() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify icons are getting highlighted in quick toolbar when keyboard shortcuts are selected for cine/WW/Distance"
				+ "<br> Verify icons state from quick toolbar and top viewer toolbar are in sync");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);		
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);

		//Selecting  WW/WL 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting WW/WL from keyboard by pressing w key");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();			
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL),"Checkpoint[1/4]","verifying that icon is selected in quicktoolbar after keyboard trigger");
		
		toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.WINDOW_LEVEL_TITLE), "Checkpoint[2/4]", "icon is selected on viewer toolbar as well");
		
		viewerPage.click(50,50);
		
		//performing  WW/WL 
		
		String ww = viewerPage.getWindowWidthValText(1);
		String wc = viewerPage.getWindowCenterText(1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);
		
		viewerPage.assertEquals(ww, viewerPage.getWindowWidthValText(1),"Checkpoint[3/4]","verifying the width is not changed");
		viewerPage.assertNotEquals(wc, viewerPage.getWindowCenterText(1),"Checkpoint[4/4]","verifying the window contrast is changed as moving mouse in vertical direction");

	}
	
	@Test(groups ={"IE11","Chrome","US2325","F1090","E2E"})
	public void test11_US2325_US9778_CinePlayUsingKeyboard() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify icons are getting highlighted in quick toolbar when keyboard shortcuts are selected for cine/WW/Distance/");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);	
		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liver9, 1);
		
		//playing cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify cine is played.");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.CINE),"Checkpoint[1/5]","verifying the cine icon is selected in quick toolbar");

		int currentImage =viewerPage.getCurrentScrollPositionOfViewbox(1);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/cineImage.png";
		String actualImagePath = newImagePath+"/actualImages/cineImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImageCineImage.png";

		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), expectedImagePath);

		//stopping cine
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.openQuickToolbar(1);
		viewerPage.assertFalse(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.CINE),"Checkpoint[2/5]","verifying that after cine stop the cine icon is not selected");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL),"Checkpoint[3/5]","verifying that scroll is default selected");
		
		viewerPage.click(50,50);
		
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1),actualImagePath);
		int imageAfterCine =viewerPage.getCurrentScrollPositionOfViewbox(1);

		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Checkpoint[4/5]", "cine is stopped and working fine");
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[5/5]","After cine images are change");
	}
			
	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

	}

}





