package com.trn.ns.test.viewer.basic;

import java.awt.AWTException;
import java.sql.SQLException;

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
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerTabsTest extends TestBase{


	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private HelperClass helper;
	

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private PagesTheme pageTheme;
	private String loadedTheme;


	
	@Test(groups={"US2151","Positive","E2E","F1082"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2151_TC9714_TC9723_TC9724_TC9730_TC9731_TC9733_verifyOPIsHighLighted(String theme) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that output panel tab is highlighted by default"
				+ "<br> Verify that the last visited tab state is persisted."
				+ "<br> Verify the output panel and series tabs in Eureka and dark theme."
				+ "<br> Verify that the right panel is not triggered when user clicks on the empty area other than the tabs."
				+ "<br> Verify that output panel and series tabs are visible in the maximized right panel when user resizes the window."
				+ "<br> Verify that the output panel is not minimized when user clicks on the viewer area.");
		
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
		viewerpage=helper.loadViewerDirectly(patientName, 1);
		
		viewerpage.assertEquals(viewerpage.tabs.size(), 2, "Checkpoint[1/29]", "Verifying that there are two tabs");

		viewerpage.assertEquals(viewerpage.getText(viewerpage.seriesTab), ViewerPageConstants.SERIES_TAB_NAME, "Checkpoint[2/29]", "Verifying Series tab text");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.opTab), ViewerPageConstants.OP_TAB_NAME, "Checkpoint[3/29]", "Verifying OP text");
		
		pageTheme = new PagesTheme(driver);
		OutputPanel op = new OutputPanel(driver);		
		
		viewerpage.assertTrue(pageTheme.verifyThemeForActiveTab(viewerpage.opTab, loadedTheme), "Checkpoint[4/29]", "Verifying that OP is default selected/highlighed");
		viewerpage.assertTrue(pageTheme.verifyThemeForInActiveTab(viewerpage.seriesTab, loadedTheme), "Checkpoint[5/29]", "verifying the series tab is not highlighted");
		viewerpage.assertTrue(op.verifyRoundedCorner(viewerpage.opTab,NSGenericConstants.CSS_BORDER_RADIUS, ThemeConstants.TAB_ROUNDED_CORNER_POPUP), "Checkpoint[6/29]", "verifying the round corners");
		viewerpage.assertTrue(op.verifyRoundedCorner(viewerpage.seriesTab,NSGenericConstants.CSS_BORDER_RADIUS, ThemeConstants.TAB_ROUNDED_CORNER_POPUP), "Checkpoint[7/29]", "verifying the round corners");
		
						
		viewerpage.mouseHover(viewerpage.opTab);
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(viewerpage.opTab,NSGenericConstants.CSS_PROP_BORDER_COLOR, loadedTheme), "Checkpoint[8/29]", "verifying the border is displayed on mouse hover");
		viewerpage.assertTrue(op.verifyRoundedCorner(viewerpage.opTab,NSGenericConstants.CSS_BORDER_RADIUS, ThemeConstants.TAB_ROUNDED_CORNER_POPUP), "Checkpoint[9/29]", "verifying the rounded corner for tab");
			
		viewerpage.mouseHover(viewerpage.seriesTab);
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(viewerpage.seriesTab, NSGenericConstants.CSS_PROP_BORDER_COLOR, loadedTheme), "Checkpoint[10/29]", "verifying that border is displayed on mouse hover");
		viewerpage.assertTrue(op.verifyRoundedCorner(viewerpage.seriesTab,NSGenericConstants.CSS_BORDER_RADIUS, ThemeConstants.TAB_ROUNDED_CORNER_POPUP), "Checkpoint[11/29]", "verifying the rounded corner for tab");
		
		int x =viewerpage.getXCoordinate(viewerpage.getViewPort(2));
		int width =viewerpage.getXCoordinate(viewerpage.getViewPort(2));
		int y =viewerpage.getYCoordinate(viewerpage.getViewPort(2));
		
		viewerpage.mouseMove(x+width,y+250);
		viewerpage.leftClick();
		
		viewerpage.assertFalse(op.verifyOutputPanelIsOpened(),"Checkpoint[12/29]","Verify that the right panel is not triggered when user clicks on the empty area other than the tabs.");
		
		op.openAndCloseOutputPanel(true);
		viewerpage.assertTrue(pageTheme.verifyThemeForActiveTab(op.opTabOpened, loadedTheme), "Checkpoint[13/29]", "verifying the op tab is highlighted");
		viewerpage.assertTrue(pageTheme.verifyThemeForInActiveTab(op.seriesTabOpened, loadedTheme), "Checkpoint[14/29]", "verifying the series tab is not active");
		
		viewerpage.assertTrue(op.verifyRoundedCorner(op.opTabOpened,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[15/29]", "verifying the rounded corner - op");
		viewerpage.assertTrue(op.verifyRoundedCorner(op.opTabOpened,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[16/29]", "verifying the rounded corner-op");
		
		viewerpage.assertTrue(op.verifyRoundedCorner(op.seriesTabOpened,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[17/29]", "verifying the rounded corner - series tab");
		viewerpage.assertTrue(op.verifyRoundedCorner(op.seriesTabOpened,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP), "Checkpoint[18/29]", "verifying the rounded corner - series tab");

		op.click(op.getViewPort(1));
		viewerpage.assertTrue(op.verifyOutputPanelIsOpened(),"Checkpoint[19/29]","verifying that OP/SEries conatiner is not closed when click outside");
		
		op.resizeBrowserWindow(600, 600);
		viewerpage.assertTrue(op.verifyOutputPanelIsOpened(),"Checkpoint[20/29]","Verify that the output panel is not minimized when user clicks on the viewer area.");
		viewerpage.assertTrue(pageTheme.verifyThemeForActiveTab(op.opTabOpened, loadedTheme), "Checkpoint[21/29]", "Verify that output panel and series tabs are visible in the maximized right panel when user resizes the window.");
		viewerpage.assertTrue(pageTheme.verifyThemeForInActiveTab(op.seriesTabOpened, loadedTheme), "Checkpoint[22/29]", "Verify that output panel and series tabs are visible in the maximized right panel when user resizes the window.");
		op.click(op.getViewPort(1));
		viewerpage.assertTrue(op.verifyOutputPanelIsOpened(),"Checkpoint[23/29]","Verify that the right panel is not triggered when user clicks on the empty area other than the tabs.");
		
		op.maximizeWindow();
		viewerpage.assertTrue(op.verifyOutputPanelIsOpened(),"Checkpoint[24/29]","Verify that output panel and series tabs are visible in the maximized right panel when user resizes the window.");
			
		op.openAndCloseOutputPanel(false);		
		viewerpage.assertFalse(op.verifyOutputPanelIsOpened(),"Checkpoint[25/29]","Verifying that OP Area is not displayed");

		ContentSelector cs = new ContentSelector(driver);
		cs.openAndCloseSeriesTab(true);
				
		viewerpage.assertTrue(pageTheme.verifyThemeForActiveTab(op.seriesTabOpened, loadedTheme), "Checkpoint[26/29]", "verifying that series tab is highlighted when series tab is opened");
		viewerpage.assertTrue(pageTheme.verifyThemeForInActiveTab(op.opTabOpened, loadedTheme), "Checkpoint[27/29]", "verifying that OP tab is not highlighted when series tab is opened");
		
		cs.openAndCloseSeriesTab(false);
				
		viewerpage.assertTrue(pageTheme.verifyThemeForActiveTab(viewerpage.seriesTab, loadedTheme), "Checkpoint[28/29]", "Verify that the last visited tab state is persisted.");
		viewerpage.assertTrue(pageTheme.verifyThemeForInActiveTab(viewerpage.opTab, loadedTheme), "Checkpoint[29/29]", "Verify that the last visited tab state is persisted.");
	
		
		

	}
	
	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

	}


}

