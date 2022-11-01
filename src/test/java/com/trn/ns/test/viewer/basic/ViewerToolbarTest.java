package com.trn.ns.test.viewer.basic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerToolbarTest extends TestBase{


	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private LoginPage loginPage;
	private ViewerToolbar viewerTool;
	private SimpleLine line;
	private ContentSelector cs;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private TextAnnotation textAn;
	private PolyLineAnnotation poly;
	private MeasurementWithUnit lineWithUnit;
	private PointAnnotation point;
	private ViewBoxToolPanel viewBoxPanel;


	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String rtStructFilepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String rtStructPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, rtStructFilepath);

	String ah4Us675Filepath = Configurations.TEST_PROPERTIES.get("AH.4_US675_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ah4Us675Filepath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);


	private HelperClass helper;
	private PatientListPage patientPage;
	private ViewerLayout layout;
	private ViewerTextOverlays overlays;
	private String loadedTheme;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws IOException, InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups={"Chrome","IE11","Edge","US2144","Positive","E2E","F1081"})
	public void test01_US2144_TC9614_verifyPatientIconOrBrowserBack() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'Patients' button functionality from the viewer tool bar.");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerPageUsingSearch(liver9PatientName,1, 1);
		viewerpage.click(viewerpage.patientsIcon);

		patientPage = new PatientListPage(driver);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();

		patientPage.assertTrue(viewerpage.isEnabled(viewerpage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerpage.refreshWebPage();
		LoginPage loginPage=new LoginPage(driver);
		loginPage.login(username, password);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[3/3]", "Verified that patient icon is disable on viewer toolbar after browser refresh.");

	}

	@Test(groups={"Chrome","IE11","Edge","US2144","Positive","E2E","F1081"})
	public void test02_US2144_TC9593_verifyEditingToolsFromViewerToolbar() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify selection of tools from the viewer tool bar - Middle section: Editing tools");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);
		viewerTool=new ViewerToolbar(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForElementInVisibility(overlays.overlayContainer);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[1/28]","Verified default scroll icon is selected.");

		viewerTool.selectIconFromViewerToolbar(viewerTool.lineIcon);
		line = new  SimpleLine(driver);
		line.drawLine(1,-10,10,20,10);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.LINE),"Checkpoint[2/28]","Verified that line icon is selcted in viewer toolbar.");
		cs=new ContentSelector(driver);
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[3/28]", "Verified that new clone is created after drawing Line annotation.");

		//browser back and reload then draw circle annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[5/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.circleIcon);
		circle=new CircleAnnotation(driver);
		circle.drawCircle(1, 20, 20, -80,-80);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.CIRCLE),"Checkpoint[6/28]","Verified that circle icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2"), "Checkpoint[7/28]", "Verified that new clone is created after drawing circle annotation.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.CIRCLE),"Checkpoint[8/28]","Verified that circle icon is selcted in context menu.");

		//browser back and reload then draw text annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[9/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.textAnnotationIcon);
		textAn=new TextAnnotation(driver);
		textAn.drawText(1,-250, -50, "myText");
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.TEXT),"Checkpoint[10/28]","Verified that text annotation icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3"), "Checkpoint[11/28]", "Verified that new clone is created after drawing text annotation.");

		//browser back and reload then draw distance annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[13/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.distanceIcon);
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.drawLine(1, 50, 50, 100, 100);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.DISTANCE),"Checkpoint[14/28]","Verified that distance icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_4"), "Checkpoint[15/28]", "Verified that new clone is created after drawing distance annotation.");

		//browser back and reload then draw point annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[17/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.pointIcon);
		point=new PointAnnotation(driver);
		point.drawPointAnnotationMarkerOnViewbox(1, -120, -120);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.POINT),"Checkpoint[18/28]","Verified that point icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_5"), "Checkpoint[19/28]", "Verified that new clone is created after drawing point annotation.");

		//browser back and reload then draw ellipse annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[21/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.ellipseIcon);
		ellipse=new EllipseAnnotation(driver);
		ellipse.drawEllipse(1, 0, 0, -50,-50);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ELLIPSE),"Checkpoint[22/28]","Verified that ellipse icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_6"), "Checkpoint[23/28]", "Verified that new clone is created after drawing ellipse annotation.");

		//browser back and reload then draw polyline annotation
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[25/28]","Verified default scroll icon is selected after reload of viewer.");
		viewerTool.selectIconFromViewerToolbar(viewerTool.polylineIcon);
		poly=new PolyLineAnnotation(driver);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.POLYLINE),"Checkpoint[26/28]","Verified that polyline icon is selcted in viewer toolbar.");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_7"), "Checkpoint[27/28]", "Verified that new clone is created after drawing polyline annotation.");
	}

	@Test(groups={"Chrome","IE11","Edge","US2144","Positive","F1081"})
	public void test03_US2144_TC9595_verifyViewingToolsFromViewerToolbar() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify selection of tools from the viewer tool bar - Middle section: Viewing tools");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName,1);

		viewerTool=new ViewerToolbar(driver);

		//verify scroll
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.SCROLL),"Checkpoint[1/9]","Verified default scroll icon is selected.");
		int currentScroll=viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.dragAndReleaseOnViewer(0, 0, 0, -10);
		viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), currentScroll, "Checkpoint[2/9]", "Verified that scroll works fine on viewer.");

		//verify zoom
		viewBoxPanel=new ViewBoxToolPanel(driver);
		String currentZoom=viewBoxPanel.getZoomLevelValue(1);
		viewerTool.selectIconFromViewerToolbar(viewerTool.zoomIcon);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Checkpoint[3/9]","Verified that zoom icon is selected.");
		viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
		viewerpage.assertNotEquals(viewBoxPanel.getZoomLevelValue(1), currentZoom, "Checkpoint[4/9]", "Verified that zoom works fine on viewer");

		//verify ww and wc
		String currentWW=viewerpage.getWindowWidthValueOverlayText(1);
		String currentWC=viewerpage.getWindowCenterValueOverlayText(1);	
		viewerTool.selectIconFromViewerToolbar(viewerTool.windowWidthIcon);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer("WW/WL"),"Checkpoint[5/9]","Verified that WW and WC icon is selected.");
		viewerpage.dragAndReleaseOnViewer(0, 0, 25, 50);
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(1), currentWW, "Checkpoint[6/9]", "Verified that Window width works fine on viewer");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), currentWC, "Checkpoint[7/9]", "Verified that window centre works fine on viewer");

		//verify PAN
		viewerTool.selectIconFromViewerToolbar(viewerTool.panIcon);
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN),"Checkpoint[8/9]","Verified that PAN icon is selected.");
		viewerpage.dragAndReleaseOnViewer(0, 0, 0, -100);
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(1),"Checkpoint[9/9]:Verify PAN is working fine on viewer.","test04_09_viewer");
	}

	@Test(groups={"Chrome","IE11","Edge","DR2439","Positive"})
	public void test04_DR2439_TC9680_verifyPatientIconForDiffData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[UI scenario]: Verify that clicking on browser back button on viewer page does not navigate to the Login page.");

		helper =new HelperClass(driver);
		List<String> patientList = Arrays.asList(rtStructPatientName,liver9PatientName,ah4PatientName);


		for (String name : patientList) {

			viewerpage=helper.loadViewerPageUsingSearch(name,1, 1);
			viewerpage.assertTrue(viewerpage.isEnabled(viewerpage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
			viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");

			viewerpage.click(viewerpage.patientsIcon);		
			patientPage = new PatientListPage(driver);
			patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/3]","Verified that user is navigate back to patient list page after click on patient back icon on viewer.");

		}
	}

	@Test(groups={"Chrome","Edge","DR2457","US2144","Positive","E2E","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test05_DR2457_TC9840_US2144_TC9587_verifyIconsAreVisibleAfterSelection(String theme)throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the viewer tool bar displayed on the viewer."
				+ "<br> Risk and Impact - US2144 - TC9587");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			Header header = new Header(driver);
			header.logout();

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;


		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName,1);
		viewerTool=new ViewerToolbar(driver);
		PagesTheme pageTheme = new PagesTheme(driver);

		for (WebElement icon : viewerTool.allToolIcons)
			viewerTool.assertTrue(viewerTool.verifyViewerToolIconVisibility(icon,loadedTheme),"Checkpoint[1/10]","verifying the tools icon are visible");

		for (int i=0;i<viewerTool.allViewerIcons.size()-1;i++)
			viewerTool.assertTrue(viewerTool.verifyViewerToolIconVisibility(viewerTool.allViewerIcons.get(i),loadedTheme),"Checkpoint[2/10]","verifying the viewer functional icons are visible");

		viewerTool.assertTrue(viewerTool.verifyViewerToolIsSelected(viewerTool.scrollIcon,loadedTheme),"Checkpoint[3/10]","verifying that scroll icon is selected and visible");

		for (WebElement icon : viewerTool.allToolIcons) {
			viewerTool.mouseHover(icon);
			viewerTool.assertTrue(pageTheme.verifyThemeForSectionWithBorder(icon, loadedTheme),"Checkpoint[4/10]","verifying the border is displayed on mouse hover as per theme");
			viewerTool.assertTrue(pageTheme.verifyThemeForText(icon, loadedTheme),"Checkpoint[5/10]","verifying the border is displayed on mouse hover as per theme");
			viewerTool.click(icon);
			viewerTool.assertTrue(viewerTool.verifyViewerToolIsSelected(icon,loadedTheme),"Checkpoint[6/10]","verifying that tool icon is visible on selection");
			viewerTool.assertTrue(pageTheme.verifyThemeForSelectedText(icon, loadedTheme),"Checkpoint[7/10]","verifying the border is displayed on mouse hover as per theme");


		}		
		for (WebElement icon : viewerTool.allViewerIcons) {
			viewerTool.mouseHover(icon);
			viewerTool.assertTrue(pageTheme.verifyThemeForSectionWithBorder(icon, loadedTheme),"Checkpoint[8/10]","verifying the border is displayed on mouse hover as per theme");
			viewerTool.assertTrue(pageTheme.verifyThemeForText(icon, loadedTheme),"Checkpoint[9/10]","verifying the border is displayed on mouse hover as per theme");
			viewerTool.click(icon);
			viewerTool.assertTrue(viewerTool.verifyViewerToolIsSelected(icon,loadedTheme),"Checkpoint[10/10]","verifying that viewer tool icon is visible on selection");


		}


	}

	@Test(groups={"Chrome","IE11","Edge","US2146","Positive","E2E","F1081"})
	public void test06_US2146_TC9793_TC9795_TC9802_verifyMoreIcon()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tool bar on viewer is resized to the width of the viewer/browser."
				+ "<br> Verify More button on the tool bar."
				+ "<br> Verify selecting tools from More list.");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		viewerTool=new ViewerToolbar(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		viewerpage.click(viewerTool.windowWidthIcon);

		viewerpage.resizeBrowserWindow(650, 700);

		viewerpage.assertTrue(viewerTool.isElementPresent(viewerTool.moreViewerIcon), "Checkpoint[1/11]", "Verifying the More button is displayed when browser was resized");
		viewerpage.assertEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE, "Checkpoint[2/11]", "verifying the more text is displayed");

		viewerpage.assertNotEquals(viewerTool.getText(viewerTool.zoomIcon),ViewerPageConstants.ZOOM, "Checkpoint[3/11]", "Verifying that zoom icon is not displayed");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.panIcon), "Checkpoint[4/11]", "verifying that pan icon is not displayed");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.windowLevelingIcon), "Checkpoint[5/11]", "verifying that wwwl is not dsiaplyed");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.scrollIcon), "Checkpoint[6/11]", "verifying the scroll icon is also not displayed");

		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.WINDOW_LEVEL_TITLE), "Checkpoint[7/11]", "verifying that WWWL is selected");

		viewerTool.click(viewerTool.allInActiveIconsInMoreTile.get(0));

		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM), "Checkpoint[8/11]", "Verifying that after selection of zoom icon from more icon zoom icon is highlighted");

		String currentZoom = viewBoxPanel.getZoomLevelValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -100,-100);

		viewerpage.assertNotEquals(currentZoom , viewBoxPanel.getZoomLevelValue(1), "Checkpoint[9/11]", "verifying that zoom is applied");

		viewerpage.maximizeWindow();

		viewerpage.assertNotEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE, "Checkpoint[10/11]", "verifying that after window is maximized the more icon is not displayed");
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM), "Checkpoint[11/11]", "verifyin that zoom icon is displayed");







	}

	@Test(groups={"Chrome","IE11","Edge","US2146","Positive","E2E","F1081"})
	public void test07_US2146_TC9704_TC9802_verifyMoreIconwhenOPIsOpened()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tool bar is minimized to the size of the viewer when Output panel is opened."
				+ "<br> Verify selecting tools from More list.");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		viewerTool=new ViewerToolbar(driver);

		viewerpage.click(viewerTool.windowWidthIcon);

		viewerpage.resizeBrowserWindow(950, 700);

		OutputPanel op = new OutputPanel(driver);
		op.openAndCloseOutputPanel(true);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		viewerpage.assertTrue(viewerTool.isElementPresent(viewerTool.moreViewerIcon),"Checkpoint[1/13]", "Verifying that More icon when OP is opened");
		viewerpage.assertEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE,"Checkpoint[2/13]", "Verifying the more icon text is displayed");

		viewerpage.assertNotEquals(viewerTool.getText(viewerTool.zoomIcon),ViewerPageConstants.ZOOM,"Checkpoint[3/13]", "Verifying zoom icon is not displayed in viewer toolbar");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.panIcon),"Checkpoint[4/13]", "Verifying pan icon is not displayed in viewer toolbar");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.windowLevelingIcon),"Checkpoint[5/13]", "Verifying WWWL icon is not displayed in viewer toolbar");
		viewerpage.assertFalse(viewerTool.isElementPresent(viewerTool.scrollIcon),"Checkpoint[6/13]", "Verifying scroll icon is not displayed in viewer toolbar");

		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.WINDOW_LEVEL_TITLE),"Checkpoint[7/13]", "Verifying the WWWL icon is selected");

		viewerTool.click(viewerTool.allInActiveIconsInMoreTile.get(0));

		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Checkpoint[8/13]", "Verifying the zoom is selected");

		String currentZoom = viewBoxPanel.getZoomLevelValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -100,-100);

		viewerpage.assertNotEquals(currentZoom , viewBoxPanel.getZoomLevelValue(1),"Checkpoint[9/13]", "verifying the applying the zoom");

		op.openAndCloseOutputPanel(false);
		viewerpage.assertNotEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE,"Checkpoint[10/13]", "Verifying after that output panel closes the more icon is also not visible");
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Checkpoint[11/13]", "Verifying the zoom icon is displayed");

		viewerpage.maximizeWindow();

		viewerpage.assertNotEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE,"Checkpoint[12/13]", "verifying that more icon is not present");
		viewerpage.assertTrue(viewerTool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Checkpoint[13/13]", "verifying that zoom icon is enabled after browser is maximized");

	}

	@Test(groups={"Chrome","Edge","US2146","Positive","E2E","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test08_US2146_TC9845_verifyThemeForMore(String theme)throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify More button in dark theme.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			Header header = new Header(driver);
			header.logout();

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		viewerTool=new ViewerToolbar(driver);
		PagesTheme pageTheme = new PagesTheme(driver);

		viewerpage.resizeBrowserWindow(650, 700);
		viewerpage.assertTrue(viewerTool.isElementPresent(viewerTool.moreViewerIcon),"Checkpoint[1/11]", "Verifying the more icon is present when browser is small");
		viewerpage.assertEquals(viewerTool.getText(viewerTool.moreIcon),ViewerPageConstants.MORE,"Checkpoint[2/11]", "Verifying the More icon Name is displayed correctly");

		viewerTool.mouseHover(viewerTool.moreIcon);
		viewerTool.assertTrue(pageTheme.verifyThemeForSectionWithBorder(viewerTool.moreIcon, loadedTheme),"Checkpoint[3/11]","verifying the border is displayed on mouse hover as per theme");
		viewerTool.assertTrue(pageTheme.verifyThemeForText(viewerTool.moreIcon, loadedTheme),"Checkpoint[4/11]","verifying the text is displayed as per theme");
		viewerTool.click(viewerTool.moreIcon);
		viewerTool.assertTrue(pageTheme.verifyThemeForPopup(viewerTool.morePopup, loadedTheme),"Checkpoint[5/11]","verifying theme on more tile displayed");

		viewerTool.assertTrue(pageTheme.verifyButtonIsFilled(viewerTool.activeIconInMoreTile, loadedTheme),"Checkpoint[6/11]","Verifying the icon background color when selected");
		viewerTool.mouseHover(viewerTool.activeIconInMoreTile);
		viewerTool.assertTrue(pageTheme.verifyThemeOnTooltip(viewerTool.tooltip, loadedTheme),"Checkpoint[7/11]","Verifying the tooltip is adhering the theme");


		for(int i = 0 ;i<viewerTool.allInactiveRectIconsInMoreTile.size();i++) {

			viewerTool.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerTool.allInactiveRectIconsInMoreTile.get(i), loadedTheme),"Checkpoint[8/11]","verifying the annotation icon's background");
			viewerTool.mouseHover(viewerTool.allInactiveRectIconsInMoreTile.get(i));
			viewerTool.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerTool.allInactiveRectIconsInMoreTile.get(i), loadedTheme),"Checkpoint[9/11]","verifying that no background is displayed for annotation icons on mouse hover");
			viewerTool.assertTrue(pageTheme.verifyThemeForSectionWithBorder(viewerTool.allInActiveIconsInMoreTile.get(i), loadedTheme),"Checkpoint[10/11]","verifying the border is displayed on mouse hover as per theme");
			viewerTool.assertTrue(pageTheme.verifyThemeOnTooltip(viewerTool.tooltip, loadedTheme),"Checkpoint[11/11]","verifying the theme is applied on tooltip as well");

		}

	}		

	@Test(groups={"Chrome","IE11","Edge","US2146","Positive","F1081"})
	public void test09_US2146_TC9844_verifyOverlaysMenuGetsClosed()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Custom TextOverlayMenu closes when clicked on PDF view box.");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(imbioPatientName, 1);
		overlays = new ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.CUSTOM_ANNOTATION);
		
		overlays.assertTrue(overlays.isElementPresent(overlays.customMenu),"Checkpoint[1/2]", "verifying the overlays menu id displayed");
		
		overlays.click(overlays.getViewPort(2));
		
		overlays.assertFalse(overlays.isElementPresent(overlays.customMenu),"Checkpoint[2/2]", "verifying the overlay menu is closed");
	
	}		



	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

	}


}

