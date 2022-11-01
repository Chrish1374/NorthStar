//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.PASS;
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.HelperClass;
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
//public class ContextMenuTest extends TestBase {
//
//	private ViewerPage viewerPage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//	
//	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);
//
//	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
//	private HelperClass helper;
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username, password);
//
//		patientPage = new PatientListPage(driver);
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","Edge","US243"})
//	public void test01_US243_TC857_TC688_verifyAllIconOnContextMenu() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that \"ALL\" icon is placed in the second arc(last right icon) of radial toolbar");
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//		
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Performing the right click" );
//		viewerPage.openRadialMenu(viewerPage.getViewPort(1));
//
//		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
//		viewerPage.assertTrue(viewerPage.quickToolbarMenu.isDisplayed(), "Verifying the Radial Menu Presence", "Radial Menu is present");
//		viewerPage.assertTrue(viewerPage.threeDotsIcon.isDisplayed(), "Verifying the Three dots icon Presence", "Three dots icon is present");
//
//		viewerPage.selectThreeDotIcon();
//		viewerPage.assertTrue(viewerPage.allIcon.isDisplayed(), "Verifying the All icon Presence", "All icon is present");		
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243","US968","Sanity"})
//	public void test02_US243_TC857_TC689_US968_TC4308_TC4309_TC4310_TC4311_verifyContextMenuPresence() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("verify that context menu/ vertical menu list should get opened when click on the \"ALL\" icon and gets disable when clicked out"
//				+ "<br> Verify - Context Menu Basic Style changes"
//				+ "<br> Verify - Context menu tab selection changes on mouse click"
//				+ "<br> Verfiy context menu closes on clicking anywhere in the viewport"
//				+ "<br> Verify the style changes for tools under context menu tab");
//
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/12]", "Performing the right click" );
//		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
//		
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		viewerPage.assertTrue(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Presence", "Context Menu is present");
//		viewerPage.assertFalse(viewerPage.quickToolbarMenu.isDisplayed(), "Verifying the Radial Menu absence", "Radial Menu is absent");
//		
//		// verifying the context menu has 3 tabs
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.size(),3, "Verifying the tabs present on context menu", "3 tabs are present");
//
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getText().trim(),"Basic", "Verifying the basic tab presence on context menu", "basic tab is present");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getText().trim(),"Measurement", "Verifying the measurement tab presence on context menu", "measurement tab is present");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getText().trim(),"Other", "Verifying the other tab presence on context menu", "other tab is present");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/12]", "Verifying the Tabs are getting highlighted and its colors" );
//		
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getAttribute(NSGenericConstants.CSS_PROP_ARIA_SELECTED), "true", "Verify Context Menu tab options and first tab is highlighted", "verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getAttribute(NSGenericConstants.CSS_PROP_ARIA_SELECTED), "false", "Verify Context Menu tab options and first tab is highlighted", "verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getAttribute(NSGenericConstants.CSS_PROP_ARIA_SELECTED), "false", "Verify Context Menu tab options and first tab is highlighted", "verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/12]", "Verifying there are line dividers and its color" );
//		
//		viewerPage.assertEquals(viewerPage.dividerLine.size(),2, "The tool options under each tab should be separated by a grey line.", "2 lines are present");
//		viewerPage.assertEquals(viewerPage.dividerLine.get(0).getCssValue(NSGenericConstants.CSS_PROP_BORDER_COLOR), ViewerPageConstants.CONTEXT_MENU_DIVIDER_LINE_COLOR, "The tool options under each tab should be separated by a grey line.", "verified");
//		viewerPage.assertEquals(viewerPage.dividerLine.get(1).getCssValue(NSGenericConstants.CSS_PROP_BORDER_COLOR), ViewerPageConstants.CONTEXT_MENU_DIVIDER_LINE_COLOR, "The tool options under each tab should be separated by a grey line.", "verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/12]", "Verifying tabs are rounded and entire context menu has corved corner" );
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS), ViewerPageConstants.CONTEXT_MENU_TAB_PIXELS, "The border should be curved.", "verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS), ViewerPageConstants.CONTEXT_MENU_TAB_PIXELS, "The border should be curved.", "verified");
//		viewerPage.assertEquals(viewerPage.contextMenu.getCssValue(NSGenericConstants.CSS_BORDER_RADIUS), ViewerPageConstants.CONTEXT_MENU_PIXELS, "The border should be curved.", "verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/12]", "Verifying background color of active and inactive tab" );
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BUTTON_DISABLED_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BUTTON_DISABLED_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/12]", "Verifying font color of active and inactive tab" );
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"The tab which is highlighted when context menu opens up should have a background color gray and the text font should be white. Non-highlighted tabs should have a background color black and font color grey.","verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/12]", "Verifying color changes when mousehover on tools" );
//		viewerPage.mouseHover(viewerPage.panIcon);		
//		viewerPage.assertEquals(viewerPage.panIconTitle.getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		
//		viewerPage.mouseHover(viewerPage.windowLevelIcon);		
//		viewerPage.assertEquals(viewerPage.windowingIconTitle.getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		
//		viewerPage.mouseHover(viewerPage.lineIconContextMenu);		
//		viewerPage.assertEquals(viewerPage.lineIconTitle.getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/12]", "Verifying color doesn't change whenever tool is not functional" );
//		viewerPage.mouseHover(viewerPage.penIcon);		
//		viewerPage.assertEquals(viewerPage.penIconTitle.getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/12]", "Verifying tab font color changes on mousehover" );
//		viewerPage.mouseHover(viewerPage.contextMenuTabs.get(1));
//		
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"Mouse hover on the tab options should just change the font color of the text. Font color should changed to White. No changes to the background color.","verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/12]", "Verifying tab's background is changed on selection and other tab is deselected" );
//		viewerPage.click(viewerPage.contextMenuTabs.get(1));
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(1).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BUTTON_DISABLED_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getCssValue(NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BUTTON_DISABLED_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(0).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		viewerPage.assertEquals(viewerPage.contextMenuTabs.get(2).getCssValue(NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_INACTIVE_FONT_COLOR,"On left mouse clicking on the non-highlighted tab, the tab should be highlighted. The background color should change to grey and font color should change to white. Left mouse clicking on the highlighted tab does not change the selection.","verified");
//		
//			
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/12]", "Performing the left click on the second viewbox" );
//		viewerPage.mouseHover(viewerPage.getViewPort(2));
//		viewerPage.assertTrue(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Absence", "Context Menu is absent");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/12]", "context menu is closed on clicking on another viewbox " );
//		viewerPage.click(viewerPage.getViewPort(1));
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Absence", "Context Menu is absent");
//
//
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243"})
//	public void test03_US243_TC857_TC690_TC692_verifyDefaultIconScroll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that clicking on any icon from the \"context menu\" will select the tool");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Performing the right click" );
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying the default icon enabled is scroll" );
//
//		viewerPage.assertEquals(viewerPage.currentSelectedIcon.getAttribute("title"),"Scroll","Verifying the scroll is default enabled icon","Scroll is default icon enabled");
//
//		viewerPage.panIcon.click();
//
//		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		viewerPage.assertEquals(viewerPage.currentSelectedIcon.getAttribute("title"),"Pan","Verifying the pan is enabled icon","Pan icon is enabled");
//		viewerPage.panIcon.click();
//		// TC692	Verify that radial toolbar disappear as soon as the tool is selected and active on the left mouse action.
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "verifying - After click on any icon context menu goes off" );
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Absence", "Context Menu is absent");
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243","US968"})
//	public void test04_US243_TC857_TC691_US968_TC4312_verifyTooltip() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that mouse over on each icon will highlight the tool and display tooltip if stay over for some time"
//				+ "<br> 	Verify tool tip appears for all tools under the Context menu tab");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Opening the context Menu" );
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying the tooltips of all the icons" );
//		viewerPage.assertEquals(viewerPage.scrollIconContextMenu.getAttribute("title"),ViewerPageConstants.SCROLL,"verify scroll icon tooltip","tooltip is "+viewerPage.scrollIconContextMenu.getAttribute("title"));
//		viewerPage.assertEquals(viewerPage.panIcon.getAttribute("title"),ViewerPageConstants.PAN,"Verifying the pan icon tooltip","tooltip is "+viewerPage.panIcon.getAttribute("title"));
//
//		viewerPage.assertEquals(viewerPage.noModeIcon.getAttribute("title"),ViewerPageConstants.NO_MODE,"verify nomode icon tooltip","tooltip is No Mode icon");
//		viewerPage.assertEquals(viewerPage.windowLevelIcon.getAttribute("title"),ViewerPageConstants.WINDOW_LEVEL,"Verifying the window level icon tooltip","tooltip is WW/WL");
//
//		viewerPage.assertEquals(viewerPage.penIcon.getAttribute("title"),ViewerPageConstants.PEN,"verify pen icon tooltip","tooltip is Pen");
//		viewerPage.assertEquals(viewerPage.zoomIconContextMenu.getAttribute("title"),ViewerPageConstants.ZOOM,"Verifying the zoom icon tooltip","tooltip is Zoom");
//
//		viewerPage.assertEquals(viewerPage.lineIconContextMenu.getAttribute("title"),ViewerPageConstants.LINE,"verify text icon tooltip","tooltip is Line");
//		viewerPage.assertEquals(viewerPage.textIcon.getAttribute("title"),ViewerPageConstants.TEXT,"verify text icon tooltip","tooltip is textIcon");
//		viewerPage.assertEquals(viewerPage.textArrowIcon.getAttribute("title"),ViewerPageConstants.TEXT_ARROW,"Verifying the text arrow icon tooltip","tooltip is textArrowIcon");
//
//		viewerPage.assertEquals(viewerPage.arrowIcon.getAttribute("title"),ViewerPageConstants.ARROW,"verify arrow icon tooltip","tooltip is arrowIcon");
//		viewerPage.assertEquals(viewerPage.invertColorIcon.getAttribute("title"),ViewerPageConstants.INVERT_ICON,"Verifying the invertColor icon tooltip","tooltip is invertColorIcon");
//		
//		
//		
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243"})
//	public void test05_US243_TC857_TC692_verifyContextMenuDisapperAfterSelection() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that radial toolbar disappear as soon as the tool is selected and active on the left mouse action.");
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Performing the right click" );
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		viewerPage.assertTrue(viewerPage.contextMenu.isDisplayed(), "Verifying the Context presence", "Context Menu is present");
//		viewerPage.panIcon.click();
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Absence on click of any icon", "Context Menu is absent");
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243","DE353"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=US243_DE353_ContextMenu" })
//	public void test06_US243_TC857_TC812_DE353_TC1554_verifyZoomUsingContextMenu(String patientFilePath) throws  InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the zoom command functionality");
//
//		// Select the data from the Patient List
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Selecting Zoom from context menu on parent screen");
//		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "zoom is enabled" );
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(), "Verifying the Context Absence", "Context Menu is absent");
//
//		viewerPage.doubleClickOnViewbox(1);
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		int beforeZoom = viewerPage.getZoomLevel(1);
//
//		viewerPage.dragAndReleaseOnViewer(0, 0, 0, -10);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate Zoom functionality" );
//		viewerPage.assertTrue(beforeZoom < viewerPage.getZoomLevel(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+viewerPage.getZoomLevel(1));
//		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify the zoom from Context Menu", patientName+"_Viewbox1_Zoom");
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243","DE353"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=US243_DE353_ContextMenu" })
//	public void test07_US243_TC857_TC813_DE353_TC1554_verifyPanUsingContextMenu(String patientFilePath) throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the pan command functionality");
//
//		// Select the data from the Patient List
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Selecting Pan from context menu on parent screen");
//		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
//
//		viewerPage.doubleClickOnViewbox(1);
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		viewerPage.dragAndReleaseOnViewer(0, 0, 0, -300);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Validate Pan functionality" );
//		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify the pan from Context Menu", patientName+"_Viewbox1_Pan");
//
//	}
//	
//	@Test(groups ={"IE11","Chrome","firefox","US243","DE353"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=US243_DE353_ContextMenu" })
//	public void test08_US243_TC857_TC860_DE353_TC1554_part2_verifyScrollUsingContextMenu(String patientFilePath) throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the continuous scroll/repeat functionality");
//
//		// Select the data from the Patient List
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Selecting Scroll from context menu on parent screen");
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		viewerPage.selectScrollFromContextMenu(viewerPage.getViewPort(1));
//		
//		viewerPage.doubleClickOnViewbox(1);
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		int currentScrollPos = Integer.parseInt(viewerPage.currentScrollPositionOfViewbox.getText().trim());
//		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Validate scroll functionality" );
//		viewerPage.assertNotEquals(currentScrollPos,Integer.parseInt(viewerPage.currentScrollPositionOfViewbox.getText()), "Verifying the scroll functionality", "scroll is working fine");
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","DE353"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=US243_DE353_ContextMenu" })
//	public void test09_US243_TC857_TC810_DE353_TC1554_verifyWindowLevelUsingContextMenu(String patientFilePath) throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the window level command functionality");
//
//		// Select the data from the Patient List
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		//Selecting  WW/WL from context Menu
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Selecting WW/WL from context menu on parent screen");
//		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
//		
//		//performing  WW/WL 
//		viewerPage.doubleClickOnViewbox(1);
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Validate WW/WL functionality" );
//		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify the WW/WL from Context Menu", patientName+"_Viewbox1_WWWL");
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US243","DE353"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=US243_DE353_ContextMenu" })
//	public void test10_US243_TC857_TC858_DE353_TC1554_verifyCinePlayContextMenu(String patientFilePath) throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cine functionality");
//
//		// Select the data from the Patient List
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		helper = new HelperClass(driver);
//		viewerPage =helper.loadViewerDirectly(patientName, 1);
//
//		// right clicking on viewbox 1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the cine play from context Menu" );
//
//		viewerPage.doubleClickOnViewbox(1);
//		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
//
//		//playing cine
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify cine is played.");
//		viewerPage.selectCineFromContextMenuOthertab(viewerPage.getViewPort(1));
//
//		int currentImage = Integer.parseInt(viewerPage.currentScrollPositionOfViewbox.getText().trim());
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/goldImages/"+patientName+"_cineplayImage.png");
//
//		//stopping cine
//		viewerPage.selectCineFromContextMenuOthertab(viewerPage.getViewPort(1));
//
//		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/actualImages/"+patientName+"_cineplayImage.png");
//		int imageAfterCine = Integer.parseInt(viewerPage.currentScrollPositionOfViewbox.getText().trim());
//		viewerPage.assertNotEquals(currentImage, imageAfterCine, "verifying the cine play is working", "cine is stopped and working fine");
//
//		String expectedImagePath = newImagePath+"/goldImages/"+patientName+"_cineplayImage.png";
//		String actualImagePath = newImagePath+"/actualImages/"+patientName+"_cineplayImage.png";
//		String diffImagePath = newImagePath+"/actualImages/diffImage_"+patientName+"_cineplayImage.png";
//
//		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		viewerPage.assertFalse(cpStatus, "The actual and Expected image should not same","After cine images are change");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying cine is played successfully", "Successfully verified checkpoint with image comparison.<br>Image name is cineplayImage.png");
//	}
//}
