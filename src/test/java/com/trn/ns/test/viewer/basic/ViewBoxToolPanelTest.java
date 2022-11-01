package com.trn.ns.test.viewer.basic;
import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewBoxToolPanelTest extends TestBase {


	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private LoginPage loginPage;
	private ViewerLayout layout;
	private ContentSelector cs;
	private ViewBoxToolPanel viewBoxPanel;

	private ViewerTextOverlays overlays;


	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String chestFilePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String chestPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, chestFilePath);

	String johnDoe = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnDoe);

	String filePath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_Filepath);


	private HelperClass helper;
	private PatientListPage patientPage;
	private PagesTheme pageTheme;
	private String loadedTheme;


	@Test(groups={"Chrome","Edge","US2204","Positive","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2204_TC9486_TC9487_TC9491_TC9504_TC9503_verifyViewBoxToolBar(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that tool panel is present at center bottom of view box and its design.<br>"
				+"Verify opening of tool panel from bottom center of view box and its design.<br>"
				+"Verify the selection and deselection of the tab on opened tool panel.<br>"
				+"Verify opening of tool panel from bottom center of view box and its design for dark theme.<br>"+"Verify that tool panel is present at center bottom of view box and its design when theme is dark.");


		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		pageTheme = new PagesTheme(driver);

		// TC9486 and TC9503
		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.viewBoxToolPanel), "Checkpoint[1/30]", "Verifying that ViewBox tool panel button is present or not");

		viewBoxPanel.assertEquals(viewBoxPanel.getCssValue(viewBoxPanel.viewBoxToolPanel, ViewerPageConstants.VIEWBOX_LEFT_ALIGNMENT),ViewerPageConstants.VIEWBOX_BUTTON_LEFT_ALIGNMENT_VALUE,"Checkpoint[2/30]","Verifying left alignment of ViewBox tool panel button");

		viewBoxPanel.assertEquals(viewBoxPanel.getCssValue(viewBoxPanel.viewBoxToolPanel, ViewerPageConstants.VIEWBOX_POSITION),ViewerPageConstants.VIEWBOX_POSITION_VALUE,"Checkpoint[3/30]","Verifying position of ViewBox tool panel button");

		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.viewBoxToolPanel,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[4/30]", "verifying the round corners");

		viewerpage.assertTrue(viewBoxPanel .verifyRoundedCorner(viewBoxPanel.viewBoxToolPanel,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[5/30]", "verifying the round corners");


		viewerpage.mouseHover(viewBoxPanel.viewBoxToolPanel);
		viewerpage.assertTrue(viewerpage.verifyAttributePresence(viewBoxPanel.ViewToolBoxButtonSvg, ViewerPageConstants.TRANSFORM), "Checkpoint[6/30]", "Verifying that tranform css value is present or not");
		viewerpage.assertTrue(pageTheme.verifyThemeOnTooltip(viewBoxPanel.ViewToolBoxButtonToolTip, loadedTheme), "Checkpoint[7/30]", "Veirfying theme for tool tip of closed view box tool panel button");

		patientPage.assertTrue(pageTheme.verifyThemeOnToolBoxArrow(viewBoxPanel.ViewToolBoxButtonSvg,loadedTheme),"Checkpoint[8/30]","Verifying theme for arrow color of viewbox tool panel.");


		// TC9487 and TC9504

		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);

		// Verifying color of default active zoom tan and other in active tab.

		viewBoxPanel.assertTrue(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[9/30]", "Verifying view box tool panel is opened or not");

		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.zoomTab, loadedTheme), "Checkpoint[10/30]", "Verifying that Zoom is default selected/highlighed");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[11/30]", "verifying the Plane tab is not highlighted");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.windowLevelTab, loadedTheme), "Checkpoint[12/30]", "verifying the WW/WL tab is not highlighted");

		viewerpage.assertTrue(viewBoxPanel .verifyRoundedCorner(viewBoxPanel.zoomTab,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[13/30]", "verifying the round corners");
		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.zoomTab,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[14/30]", "verifying the round corners");


		// Mouse hover on WW/WL and Plane tab and verifying color.
		viewerpage.mouseHover(viewBoxPanel.windowLevelTab);
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.windowLevelTab, loadedTheme), "Checkpoint[15/30]", "Verifying that WW/WL tab is selected/highlighed");

		viewerpage.mouseHover(viewBoxPanel.planeTab);
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[16/30]", "Verifying that Plane tab is selected/highlighed");

		// clicking on WW/Wl tab and Plane tab one by one and verifying color of active and other inactive tab.
		viewerpage.click(viewBoxPanel.windowLevelTab);

		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.windowLevelTab, loadedTheme), "Checkpoint[17/30]", "Verifying that WW/WL tab is selected/highlighed");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.zoomTab, loadedTheme), "Checkpoint[18/30]", "verifying the zoom tab is not selected");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[19/30]", "verifying the Plane tab is not selected");

		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.windowLevelTab,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[20/30]", "verifying the round corners");
		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.windowLevelTab,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[21/30]", "verifying the round corners");


		viewerpage.click(viewBoxPanel.planeTab);

		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[22/30]", "Verifying that Plane tab is selected/highlighed");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.zoomTab, loadedTheme), "Checkpoint[24/30]", "verifying the zoom tab is not selected");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.windowLevelTab, loadedTheme), "Checkpoint[24/30]", "verifying the WW/WL tab is not selected");

		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.planeTab,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[25/30]", "verifying the round corners");
		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.planeTab,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[26/30]", "verifying the round corners");

		//TC9491
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.CLOSE);
		viewBoxPanel.assertFalse(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[27/30]", "Verifying view box tool panel is opened or not");
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);

		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.zoomTab, loadedTheme), "Checkpoint[28/30]", "Verifying that Zoom is default selected/highlighed");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[29/30]", "verifying the Plane tab is not highlighted");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForInActiveTab(viewBoxPanel.windowLevelTab, loadedTheme), "Checkpoint[30/30]", "verifying the WW/WL tab is not highlighted");
	}

	@Test(groups={"Chrome","Edge","US2204","Positive","F1085"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US2204_TC9492_TC9490_verifyViewBoxToolPanelToolTipAndDirection(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tool tip on hovering on drawer icon.<br>"+"Verify the hovering behavior on closed tool panel.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		pageTheme = new PagesTheme(driver);

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.viewBoxToolPanel), "Checkpoint[1/10]", "Verifying that ViewBox tool panel button is present or not");

		//TC9492-Verifying tool panel button arrow is pointing upward and tool tip.
		viewerpage.mouseHover(viewBoxPanel.viewBoxToolPanel);

		viewerpage.assertEquals(viewerpage.getText(viewerpage.getTooltipWebElement(viewBoxPanel.viewBoxToolPanel)), ViewerPageConstants.VIEWBOX_TOOL_PANEL_CLOSED_BUTTON_TOOLTIP, "Checkpoint[2/10]", "Verifying tool tip for closed viewbox tool panel button");
		viewerpage.assertTrue(viewerpage.verifyAttributePresence(viewBoxPanel.ViewToolBoxButtonSvg, ViewerPageConstants.TRANSFORM), "Checkpoint[3/10]", "Verifying that tranform css value is present or not");

		viewerpage.assertTrue(pageTheme.verifyThemeOnTooltip(viewBoxPanel.ViewToolBoxButtonToolTip, loadedTheme), "Checkpoint[4/10]", "Veirfying theme for tool tip of closed view box tool panel button");

		// Open viewbox tool panel and verifying arrow direction is downward and tool tip.
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);

		viewerpage.assertEquals(viewerpage.getText(viewerpage.getTooltipWebElement(viewBoxPanel.viewBoxToolPanel)), ViewerPageConstants.VIEWBOX_TOOL_PANEL_OPENED_BUTTON_TOOLTIP, "Checkpoint[5/10]", "Verifying tool tip for closed viewbox tool panel button");
		viewerpage.assertFalse(viewerpage.verifyAttributePresence(viewBoxPanel.ViewToolBoxButtonSvg, ViewerPageConstants.TRANSFORM), "Checkpoint[6/10]", "Verifying that tranform css value is present or not");

		viewerpage.assertTrue(pageTheme.verifyThemeOnTooltip(viewBoxPanel.ViewToolBoxButtonToolTip, loadedTheme), "Checkpoint[7/10]", "Veirfying theme for tool tip of opened view box tool panel button");


		// TC9490
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(viewBoxPanel.viewBoxToolPanel, NSGenericConstants.BORDER, loadedTheme),"Checkpoint[8/10]", "Veirfying rounded corner border color on viewbox tool panel");

		viewerpage.assertTrue(viewBoxPanel.verifyRoundedCorner(viewBoxPanel.viewBoxToolPanel,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[9/10]", "verifying the round corners");

		viewerpage.assertTrue(viewBoxPanel .verifyRoundedCorner(viewBoxPanel.viewBoxToolPanel,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[10/10]", "verifying the round corners");
	}

	@Test(groups={"Chrome","IE11","Edge","US2204","Positive","F1085","E2E"})
	public void test03_US2204_TC9495_TC9502_TC9501_verifyPresenceOfToolPanelOnNonDicom() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that view box tool panel is not visible on Empty view box,PDF and SR data.<br>"
				+"Verify that tool panel is visible for JPG/PNG/BMP dataset.<br>"+"Verify that visibility of tool panel is dependent on content which is loaded or being loaded from content selector.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(chestPatient, 2);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		//TC9495

		viewerpage.click(viewerpage.getSRViewPort(1));
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[1/10]", "Verifying that ViewBox tool panel button is not present");

		// Navigating back again visiting viewer page
		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(johnDoePatientName, 1);

		viewerpage.click(viewerpage.getPDFViewPort(3));
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(3)), "Checkpoint[2/10]", "Verifying that ViewBox tool panel button is not present");

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		viewerpage.click(viewerpage.getViewPort(6));
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(3)), "Checkpoint[3/10]", "Verifying that ViewBox tool panel button is not present");

		//TC9502 

		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[4/10]", "Verifying that ViewBox tool panel button is present");

		// Verifying color of default active zoom tab
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[5/10]", "Verifying view box tool panel is opened or not");
		pageTheme = new PagesTheme(driver);
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.zoomTab, ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[6/10]", "Verifying that Zoom is default selected/highlighed");


		//TC9501

		cs=new ContentSelector(driver);
		List<String>resultDesc=cs.getAllResults();
		List<String>seriesDesc=cs.getAllSeries();
		cs.selectResultFromSeriesTab(1, resultDesc.get(2));

		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[7/10]", "Verifying that ViewBox tool panel button is not present");

		cs.selectSeriesFromSeriesTab(1, seriesDesc.get(0));

		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[8/10]", "Verifying that ViewBox tool panel button is present");


		//Navigating back to patient-study page and visiting viewer page with SR patient.

		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(chestPatient, 2);

		viewerpage.click(viewerpage.getSRViewPort(1));

		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[9/10]", "Verifying that ViewBox tool panel button is not present");

		viewerpage.closeWarningNotification();
		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(2));

		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[10/10]", "Verifying that ViewBox tool panel button is not present");

	}

	@Test(groups={"Chrome","IE11","Edge","US2204","Positive","F1085","E2E"})
	public void test04_US2204_TC9494_TC9636_verifyClosingBehaviorOfToolPanel() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the closing behavior of opened viewbox tool panel.<br>"+"Verify that oneUp ,radial menu not working on tool panel popup.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(chestPatient, 2);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		//TC9494

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[1/29]", "Verifying view box tool panel is opened or not");

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.CLOSE);
		viewBoxPanel.assertFalse(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[2/29]", "Verifying view box tool panel is opened or not");

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewerpage.click(viewerpage.getViewPort(2));
		viewBoxPanel.assertFalse(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[3/29]", "Verifying view box tool panel is opened or not");

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.CLOSE);
		viewerpage.mouseHover(15, 15);
		viewBoxPanel.assertFalse(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[4/29]", "Verifying view box tool panel is opened or not");

		//TC9636

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.doubleClick(viewBoxPanel.zoomTab);
		viewerpage.assertEquals(viewerpage.getNuberOfSVGForLayout(), 2, "Checkpoint[5/29]", "Veirfying no of canvas is 2, hence no oneup peformed");

		viewerpage.performMouseRightClick(viewBoxPanel.toolPanel);
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewerpage.quickToolbarMenu), "Checkpoint[6/29]", "Verifying that quick tool bar is not present");

	}

	//US2205:Create Viewbox Zoom tool
	@Test(groups={"Chrome","Edge","US2205","Positive","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test05_US2205_TC9691_verifyLookAndFeelForZoomTool(String theme) throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the design and look and feel of the Zoom tool under the view box tool panel.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(liver9PatientName,username,password, 2);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		PagesTheme pagetheme=new PagesTheme(driver);
		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.sliderThumbLabel), "Checkpoint[1/12]", "Verified element is present for slider thumb .");

		viewBoxPanel.assertEquals(viewBoxPanel.getCssValue(viewBoxPanel.sliderThumbLabel,NSGenericConstants.CSS_BORDER_RADIUS), ViewerPageConstants.SLIDER_THUMB_BORDER_RADIUS, "Checkpoint[2/12]", "Verified border radius for slider thumb.");
		viewBoxPanel.assertTrue(pagetheme.verifyThemeForSlider(viewBoxPanel.sliderThumbLabel,loadedTheme),"Checkpoint[3/12]", "Verified theme for slider thumb label.");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(1), "Checkpoint[4/12]", "Verified default value on viewer.");

		//min and max label
		viewBoxPanel.assertEquals(viewBoxPanel.getText(viewBoxPanel.zoomMaxLabel).toLowerCase(), ViewerPageConstants.MAX,"Checkpoint[5/12]", "Verified Zoom Max label.");
		viewBoxPanel.assertEquals(viewBoxPanel.getText(viewBoxPanel.zoomMinLabel).toLowerCase(), ViewerPageConstants.MIN,"Checkpoint[6/12]", "Verified Zoom Min label.");

		viewBoxPanel.assertEquals(viewBoxPanel.getText(viewBoxPanel.zoomMinIcon), "-","Checkpoint[7/12]", "Verified Zoom Max icon.");
		viewBoxPanel.assertEquals(viewBoxPanel.getText(viewBoxPanel.zoomMaxIcon), "+","Checkpoint[8/12]", "Verified Zoom Min icon.");

		viewBoxPanel.mouseHover(viewBoxPanel.zoomToFit);
		viewBoxPanel.assertTrue(pagetheme.verifyThemeOnTooltip(viewerpage.tooltip, loadedTheme), "Checkpoint[9/12]","Verified theme for zoom to fit .");
		viewBoxPanel.assertTrue(pagetheme.verifyThemeOnLabel(viewBoxPanel.zoomToFit, loadedTheme), "Checkpoint[10/12]","Verified theme for tooltip of zoom to Fit.");

		viewBoxPanel.mouseHover(viewBoxPanel.zoomIn);
		viewBoxPanel.assertTrue(pagetheme.verifyThemeOnTooltip(viewerpage.tooltip,loadedTheme), "Checkpoint[11/12]","Verified theme for tooltip of zoom In.");

		viewBoxPanel.mouseHover(viewBoxPanel.zoomOut);
		viewBoxPanel.assertTrue(pagetheme.verifyThemeOnTooltip(viewerpage.tooltip, loadedTheme), "Checkpoint[12/12]","Verified theme for tooltip of zoom Out.");

	}

	@Test(groups={"Chrome","IE11","Edge","US2205","Positive","F1085","E2E"})
	public void test06_US2205_TC9692_TC9707_verifyMinAndMaxAndZoomToFitFunctionality() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Min and Max value of the  zoom on slider bar under 'Zoom' tab.<br>"+
				"Verify the 'Zoom to fit' functionality on Zoom tool panel.");

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(chestPatient,username,password,2);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		String actualZoom=viewBoxPanel.getZoomLevelValue(2);
		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(),actualZoom, "Checkpoint[1/5]", "Verified default value of zoom on viewbox.");

		viewBoxPanel.changeZoomNumber(2,viewBoxPanel.convertIntoInt(NSGenericConstants.ZOOM_MAX_RANGE) );
		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.click(viewBoxPanel.zoomMaxIcon);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(2), "Checkpoint[2/5]", "Verified max value of zoom is 400 only.");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(),NSGenericConstants.ZOOM_MAX_RANGE, "Checkpoint[3/5]", "Verified max value of zoom is 400 only on viewbox.");

		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.click(viewBoxPanel.zoomToFit);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(),actualZoom, "Checkpoint[4/5]", "Verified zoom value as zoom to fit as "+actualZoom);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(2), "Checkpoint[5/5]", "Verified zoom value as zoom to fit as "+actualZoom+" on viewer as well as tool panel.");

	}

	@Test(groups={"Chrome","IE11","Edge","US2205","Positive","F1085","E2E"})
	public void test07_US2205_TC9708_TC9710_TC9711_TC9867_verifyZoomBySlidingSlider() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to apply zoom by sliding slider on slider bar.<br>"+
				"Verify that user is able to apply zoom by clicking on slider bar.<br>"+
				"Verify that user is able to apply zoom by clicking (-) and (+) icon present slider bar end. <br>"+
				"Verify the default position of slider on slider bar for the patient whose zoom to fit value is not the Min value of zoom on default loading of viewer page.");

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(chestPatient,username,password,2);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		viewerpage.closeNotification();

		viewBoxPanel.openOrCloseViewBoxToolPanel(2, NSGenericConstants.OPEN);
		String actualZoom=viewBoxPanel.getZoomLevelValue(2);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(),actualZoom, "Checkpoint[1/7]", "Verified zoom value on viewer as well as tool panel. ");

		viewBoxPanel.applyZoomUsingIcon(2, 300);
		viewBoxPanel.assertNotEquals(viewBoxPanel.getSliderLevelValue(),actualZoom, "Checkpoint[2/7]", "Verified that zoom applied successfully using max icon.");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(2), "Checkpoint[3/7]", "Verified change zoom value on tool panel as well as slider.");

		String newZoom=viewBoxPanel.getZoomLevelValue(2);
		viewBoxPanel.applyZoomUsingIcon(2, 150);
		viewBoxPanel.assertNotEquals(viewBoxPanel.getSliderLevelValue(),newZoom, "Checkpoint[4/7]","Verified that zoom applied successfully using min icon.");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(2), "Checkpoint[5/7]","Verified change zoom value on tool panel as well as slider.");

		viewBoxPanel.changeZoomNumber(2, 250);
		viewBoxPanel.openOrCloseViewBoxToolPanel(2,NSGenericConstants.OPEN);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(),"250", "Checkpoint[6/7]","Verified that zoom applied successfully using slider.");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),viewBoxPanel.getZoomValue(2), "Checkpoint[7/7]","Verified change zoom value on tool panel as well as slider.");

	}

	@Test(groups={"Chrome","IE11","Edge","US2205","Positive","F1085","E2E"})
	public void test08_US2205_TC9713_TC9718_verifyZoomForSyncViewbox() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that zoom applied through zoom tool panel , is changing the value in active and sync view box both if any sync view box is present for that study.<br>"+
				"Verify that zoom applied through zoom tool panel is for current session only.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(liver9PatientName, 2);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		String actualZoom=viewBoxPanel.getZoomLevelValue(1);
		viewBoxPanel.changeZoomNumber(1, 250);
		for(int i=1;i<=viewerpage.totalNumberOfCanvasForLayout.size();i++)
		{
			viewBoxPanel.click(viewerpage.getViewPort(1));
			viewBoxPanel.openOrCloseViewBoxToolPanel(i,NSGenericConstants.OPEN);
			viewBoxPanel.assertNotEquals(viewBoxPanel.getSliderLevelValue(), actualZoom, "Checkpoint[1/8]","Verified zoom applied successfully on slider for viewbox-"+i);
			viewBoxPanel.assertNotEquals(viewBoxPanel.getZoomValue(i), viewerpage.convertIntoInt(actualZoom), "Checkpoint[2/8]", "Verified change zoom value on viewer for viewbox-"+i);
			viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(), viewBoxPanel.getZoomLevelValue(i), "Checkpoint[3/8]", "Verified change zoom value on viewer toolpanel for viewbox-"+i);
			viewBoxPanel.openOrCloseViewBoxToolPanel(i,NSGenericConstants.CLOSE);
		}

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs=new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(6, cs.getSeriesDescriptionOverlayText(1));
		viewBoxPanel.openOrCloseViewBoxToolPanel(6,NSGenericConstants.OPEN);
		viewBoxPanel.assertNotEquals(viewBoxPanel.getSliderLevelValue(), actualZoom, "Checkpoint[4/8]","Verified zoom applied successfully on slider for viewbox-6 when series is loaded from Series tab.");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getZoomValue(6), viewerpage.convertIntoInt(actualZoom), "Checkpoint[5/8]","Verified change zoom value on viewer for viewbox-6");
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(), viewBoxPanel.getZoomLevelValue(6), "Checkpoint[6/8]","Verified change zoom value on viewer toolpanel for viewbox-6");

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		for(int i=1;i<=viewerpage.totalNumberOfCanvasForLayout.size();i++)
		{
			viewBoxPanel.click(viewerpage.getViewPort(1));
			viewBoxPanel.openOrCloseViewBoxToolPanel(i,NSGenericConstants.OPEN);
			viewBoxPanel.assertEquals(viewBoxPanel.getSliderLevelValue(), actualZoom, "Checkpoint[7/8]","Verified zoom value is reset to default on slider for viewbox-"+i);
			viewBoxPanel.assertEquals(viewBoxPanel.getZoomValue(i), viewerpage.convertIntoInt(actualZoom), "Checkpoint[8/8]", "Verified default zoom value on viewer for viewbox-"+i);
			viewBoxPanel.openOrCloseViewBoxToolPanel(i,NSGenericConstants.CLOSE);
		}

	}

	@Test(groups={"Chrome","IE11","Edge","US2207","Negative","F1085","E2E"})
	public void test09_US2207_TC10222_verifyPlaneTabForNonDicom() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that view box tool panel is not visible on Empty view box,PDF and SR data");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(anonymous_Patient,2);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		//TC10222
		// NonDicom Data

		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[1/5]", "Verifying that ViewBox tool panel button is not present");

		viewBoxPanel.openOrCloseViewBoxToolPanel(1, NSGenericConstants.OPEN);
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.planeTab), "Checkpoint[2/5]", "Verifying that Plane tab is visible or not.");

		// SR data
		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(chestPatient, 2);

		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[3/5]", "Verifying that Plane tab is visible or not.");

		// Imbio Data
		helper.browserBackAndReloadViewer(imbioPatientName, 1, 3);

		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.planeTab), "Checkpoint[4/5]", "Verifying that Plane tab is visible or not.");

		// Ah.4 Data
		helper.browserBackAndReloadViewer(ah4PatientName, 1, 1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);
		viewerpage.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.planeTab), "Checkpoint[5/5]", "Verifying that Plane tab is visible or not.");

	}

	@Test(groups={"Chrome","IE11","Edge","US2207","Positive","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test10_US2207_TC10224_TC10228_verifyPlaneSelection(String theme) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox plane selection tool in eureka theme<br>"
				+"Verify viewbox plane selection tool in dark theme.");


		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(aidocPatientName,username,password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		pageTheme = new PagesTheme(driver);

		//TC10224

		viewerpage.waitForViewerpageToLoad(2);
		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(3)), "Checkpoint[1/18]", "Verifying that ViewBox tool panel button is not present");

		viewerpage.assertTrue(viewBoxPanel.verifyPlaneTabPresence(3), "Checkpoint[2/18]", "Verifying that Plane tab is visible or not.");

		viewBoxPanel.openTabInViewbox(3, viewBoxPanel.planeTab);

		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneTabContentsPresence(3), "Checkpoint[3/18]", "Verifying All contents inside the plane tab");

		for(int i=0; i<=2; i++)
		{

			viewBoxPanel.selectPlane(3, ViewerPageConstants.ALL_PLANES.get(i));
			viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsSelected(3,ViewerPageConstants.ALL_PLANES.get(i),loadedTheme), "Checkpoint[4/18]", "Verifying that Axial plane is visible and selected");

		}

		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsInactive(3,ViewerPageConstants.ALL_PLANES.get(0),loadedTheme), "Checkpoint[5/18]", "Verifying that Axial plane is visible and Inactive");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsInactive(3,ViewerPageConstants.ALL_PLANES.get(1),loadedTheme), "Checkpoint[6/18]", "Verifying that Axial plane is visible and Inactive");


		viewerpage.assertTrue(viewBoxPanel.verifyPlaneTileOnMouserHover(viewBoxPanel.axialBorder),"Checkpoint[7/18]", "verifying the round corners");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(viewBoxPanel.axialBorder,NSGenericConstants.BORDER, loadedTheme), "Checkpoint[8/18]","verifying the color of round corners");

		viewerpage.assertTrue(viewBoxPanel.verifyPlaneTileOnMouserHover(viewBoxPanel.coronalBorder),"Checkpoint[9/18]", "verifying the round corners");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(viewBoxPanel.coronalBorder,NSGenericConstants.BORDER, loadedTheme), "Checkpoint[10/18]","verifying the color of round corners");

		cs=new ContentSelector(driver);

		List<String>seriesDesc=cs.getAllSeries();

		cs.selectSeriesFromSeriesTab(1, seriesDesc.get(1));


		viewerpage.waitForViewerpageToLoad(2);
		viewerpage.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getViewBoxToolPanel(1)), "Checkpoint[11/18]", "Verifying that ViewBox tool panel button is not present");

		viewerpage.assertTrue(viewBoxPanel.verifyPlaneTabPresence(1), "Checkpoint[12/18]", "Verifying that Plane tab is visible or not.");

		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN);
		viewerpage.click(viewBoxPanel.planeTab);
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForActiveTab(viewBoxPanel.planeTab, loadedTheme), "Checkpoint[13/18]", "Verifying that Plane tab is selected/highlighed");

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.planeContainer), "Checkpoint[14/18]", "Verifying that plane container is visible");

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.axialIcon), "Checkpoint[15/18]", "Verifying that Axial plane icon is visible");

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.coronalIcon), "Checkpoint[16/18]", "Verifying that Coronal plane icon is visible");

		viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.sagittalIcon), "Checkpoint[17/18]", "Verifying that Sagittal plane icon is visible");

		viewBoxPanel.assertEquals(viewBoxPanel.getPlaneRadioIcon(1).size(), 3, "Checkpoint[18/18]", "Verifying count of radio button");
	}

	@Test(groups={"Chrome","IE11","Edge","US2207","Positive","F1085","E2E"})
	public void test11_US2207_TC10230_verifyPlaneChangeOnViewBox() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify plane gets updated in viewbox after selecting Axial/Sagital/Coronal options from plane tab");

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(aidocPatientName,username,password,2);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		overlays = new ViewerTextOverlays(driver);
		viewBoxPanel.openTabInViewbox(3,viewBoxPanel.planeTab);


		int i=1;
		while(i<viewBoxPanel.getPlaneLabels(3).size()){
			String currentPlane = viewerpage.getText(overlays.getPlaneInfo(3));

			viewBoxPanel.selectPlane(3, ViewerPageConstants.ALL_PLANES.get(i));
			viewerpage.assertTrue(viewerpage.isElementPresent(overlays.getPlaneByText(3)), "Checkpoint[1."+i+"/7]", "Verifying plane is displayed");
			viewerpage.assertNotEquals(currentPlane,ViewerPageConstants.ALL_PLANES.get(i), "Checkpoint[2."+i+"/7]", "Verifying plane text");
			viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsSelected(3,ViewerPageConstants.ALL_PLANES.get(i),ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[3/7]", "Verifying that Axial plane is visible and selected");
			i++;
		}

		viewBoxPanel.openOrCloseViewBoxToolPanel(3,NSGenericConstants.CLOSE);
		viewBoxPanel.assertFalse(viewBoxPanel.verifyPresenceOfViewBoxToolPanelContainer(), "Checkpoint[7/7]", "Verifying view box tool panel is opened or not");

	}

	@Test(groups={"Chrome","IE11","Edge","US2207","Positive","F1085","E2E"})
	public void test12_US2207_TC10232_verifyPlanePresenceInOtherViewBox() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify plane when same series is loaded into another viewbox from Series tab");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(aidocPatientName,3);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		overlays = new ViewerTextOverlays(driver);

		String currentPlane = viewerpage.getText(overlays.getPlaneInfo(3));

		viewBoxPanel.selectPlane(3, ViewerPageConstants.ALL_PLANES.get(1));
		viewerpage.assertTrue(viewerpage.isElementPresent(overlays.getPlaneByText(3)), "Checkpoint[1/8]", "Verifying plane is displayed");
		viewerpage.assertNotEquals(currentPlane,ViewerPageConstants.ALL_PLANES.get(1), "Checkpoint[2/8]", "Verifying plane text");
		viewerpage.assertEquals(ViewerPageConstants.ALL_PLANES.get(1),viewerpage.getText(overlays.getPlaneInfo(3)), "Checkpoint[3/8]", "Verifying plane text");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsSelected(3,ViewerPageConstants.ALL_PLANES.get(1),ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[4/8]", "Verifying that Axial plane is visible and selected");

		cs=new ContentSelector(driver);

		List<String>seriesDesc=cs.getAllSeries();

		cs.selectSeriesFromSeriesTab(1, seriesDesc.get(1));

		viewerpage.assertEquals(ViewerPageConstants.ALL_PLANES.get(0),viewerpage.getText(overlays.getPlaneInfo(1)), "Checkpoint[5/8]", "Verifying plane text");

		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsSelected(1,ViewerPageConstants.ALL_PLANES.get(0),ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[6/8]", "Verifying that Axial plane is visible and selected");

		helper.browserBackAndReloadViewer(aidocPatientName, 1, 3);

		viewerpage.assertEquals(ViewerPageConstants.ALL_PLANES.get(0),viewerpage.getText(overlays.getPlaneInfo(3)), "Checkpoint[7/8]", "Verifying plane text");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneRadioAndTileIsSelected(3,ViewerPageConstants.ALL_PLANES.get(0),ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[8/8]", "Verifying that Axial plane is visible and selected");		

	}

	//DR2689: Viewer Tool Panel closes when trying to select the WW/WL value.
	@Test(groups={"Chrome","IE11","Edge","DR2689","Positive","F1085"})
	public void test13_DR2689_TC10514_verifyToolPanelWhenMouseHoverInsideWWTab() throws InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that viewer tool panel does not close when user mouse hovers inside the WW/WL tab.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading of "+liver9PatientName+" On viewer.");
		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(liver9PatientName,username,password, 2);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		int windowWidth=viewBoxPanel.getValueOfWindowWidth(1);
		int windowCentre=viewBoxPanel.getValueOfWindowCenter(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify selected WW and WC is visible on viewer.");
		viewBoxPanel.selectPresetValue(1, ViewerPageConstants.HEAD);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyPresetOptionIsSelected(1, ViewerPageConstants.HEAD), "Checkpoint[1/4]", "HEAD is selected on viewer.");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getValueOfWindowWidth(1), windowWidth, "Checkpoint[2/4]", "Verified that window width value is change.");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getValueOfWindowCenter(1), windowCentre, "Checkpoint[3/4]", "Verified that window center value is change");

		viewBoxPanel.openTabInViewbox(1, viewBoxPanel.windowLevelTab);
		for(int i=0;i<viewBoxPanel.presetOptions.size();i++)
		{
			viewBoxPanel.mouseHover(viewBoxPanel.presetOptions.get(i));
			viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.toolBox), "Checkpoint[4."+(i+1)+"/4]", "Verifying that menu is not closed when mousehover on "+viewBoxPanel.getText(viewBoxPanel.presetOptions.get(i)));
		}

	}


	@Test(groups={"Chrome","IE11","Edge","DR2734","Positive","F1085"})
	public void test14_DR2734_TC10632_TC10634_verifyZoomToFitAppliedOnViewbox() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that zoom to fit value is getting applied of view box when user drag the slider to minimum position end perform oneup.<br>"+"Verify that zoom to fit value is getting applied of view box when user clicks on (-) minicon to slide the slider to minimum position end perform oneup.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading of "+ah4PatientName+" On viewer.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);


		helper =new HelperClass(driver); 
		viewerpage=helper.loadViewerDirectly(ah4PatientName, 1);
		viewBoxPanel=new ViewBoxToolPanel(driver);


		// TC10632
		viewBoxPanel.chooseZoomToFit(1);
		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[1/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");


		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[2/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");

		int ZoomValue=viewBoxPanel.getZoomValue(1);
		viewBoxPanel.changeZoomNumber(1, ZoomValue);

		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[3/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getZoomValue(1), ZoomValue, "Checkpoint[4/11]", "Verifying that zoomToFit value is not equals to 2X2 cross layout Zoom to fit value.");



		// TC10634
		helper.browserBackAndReloadViewer(ah4PatientName, 1, 1);


		viewBoxPanel.chooseZoomToFit(1);
		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[5/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");
		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );
		ZoomValue=viewBoxPanel.getZoomValue(1);
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[6/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");

		viewBoxPanel.changeZoomNumber(1, 94);

		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );
		viewBoxPanel.click(viewBoxPanel.zoomMinIcon);

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(),ZoomValue, "Checkpoint[7/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");

		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );

		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[8/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getZoomValue(1), ZoomValue, "Checkpoint[9/11]", "Verifying that zoomToFit value is not equals to 2X2 cross layout Zoom to fit value.");


		viewBoxPanel.changeZoomNumber(1, 250);
		viewBoxPanel.chooseZoomToFit(1);
		ZoomValue=viewBoxPanel.getZoomValue(1);
		viewBoxPanel.doubleClickOnViewbox(1);

		viewBoxPanel.openOrCloseViewBoxToolPanel(1,NSGenericConstants.OPEN );
		viewBoxPanel.assertEquals(viewBoxPanel.getSliderValue(), viewBoxPanel.getZoomValue(1), "Checkpoint[10/11]", "Verifying value in zoom slider bar and zoom overlay on view box are equals");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getZoomValue(1), ZoomValue, "Checkpoint[11/11]", "Verifying that zoomToFit value is not equals to 2X2 cross layout Zoom to fit value.");


	}


	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

	}






}
