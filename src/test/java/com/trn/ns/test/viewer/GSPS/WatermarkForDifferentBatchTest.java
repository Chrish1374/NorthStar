package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class WatermarkForDifferentBatchTest extends TestBase{


	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private ViewerSendToPACS sd;
	private PatientListPage patientPage;

	// Get Patient Name
	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymousPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);

	String ChestCT1p25mm_filePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm_Patient= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mm_filePath);

	String Liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Liver9filePath);

	String imbioFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilePath);
	
	private ContentSelector cs;
	private EllipseAnnotation ellipse;
	private PointAnnotation point;
	private TextAnnotation textAnno;
	private String loadedTheme;
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	// Before method, handles the steps before loading the data for set up.
    @BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
		patientPage=new PatientListPage(driver);
	}

	//US2583: Display Batch Conflict Overlays as per new Eureka design
	@Test(groups={"firefox","Chrome","IE11","Edge","US2583","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2583_TC10463_TC10538_verifyThemeForBatchConflictOverlay(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Batch Conflict Overlays in Eureka theme after changing layout. <br>"+
		"Verify Batch Conflict Overlays in dark theme after changing layout");
		
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

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(anonymousPatient, 2);

		ViewerLayout layout=new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout and verify batch conflict notification." );
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(3), "Checkpoint[1/7]", "Verified batch conflict message for Viewbox-3");
		layout.assertTrue(layout.verifyViewboxBGForConflicts(3), "Checkpoint[2/7]", "Verified Opacity for viewbox-3");
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(4), "Checkpoint[3/7]", "Verified batch conflict message for Viewbox-4");
		layout.assertTrue(layout.verifyViewboxBGForConflicts(4), "Checkpoint[4/7]", "Verified Opacity for viewbox-3");
		
		PagesTheme pagetheme=new PagesTheme(driver);
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(layout.getViewboxNotification(3),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[5/7]", "Verified Theme for viewbox notification for viewbox-3.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(layout.getViewboxNotification(4),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[6/7]", "Verified Theme for viewbox notification for viewbox-4.");

		layout.waitForTimePeriod(3000);
		layout.assertTrue(layout.isElementPresent(layout.getViewboxNotificationMessageBody(3)), "Checkpoint[7/7]", "Verified that message is not autoclosed.");
	}

	@Test(groups={"firefox","Chrome","IE11","Edge","US2583","Positive"})
	public void test02_US2583_TC10539_verifyBatchConflictWhenDataLoadedFromSeries() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Batch Conflict Overlays when data is loaded via content selector. ");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(anonymousPatient, 3);
		
		cs=new ContentSelector(driver);
		String result1=cs.getSeriesDescriptionOverlayText(1);
		String series=cs.getSeriesDescriptionOverlayText(2);
		
		//Scenario 1 - Loading same series into both viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch conflict when same series loaded in both viewbox." );
		cs.selectResultFromSeriesTab(2, cs.getAllResults().get(1));
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(1), "Checkpoint[1/11]", "Verified batch conflict message in first viewbox when result is loaded in 2nd viewbox");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(1), "Checkpoint[2/11]", "Verified opacity for the viewbox-1");
		
		cs.selectResultFromSeriesTab(2,result1);
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(1)), "Checkpoint[3/11]", "Verified that notification closed when another result is loaded in viewbox-1.");
		
		//Scenario 2 -  Drag and drop series into viewbox containing batch conflict overlay 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch conflict when series loaded into viewbox containing batch conflict overlay." );
		helper.browserBackAndReloadViewer(anonymousPatient, 1, 1);
		cs.selectResultFromSeriesTab(2, cs.getAllResults().get(1));
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(1), "Checkpoint[4/11]", "Verified batch conflict message in first viewbox when result is loaded in 2nd viewbox");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(1), "Checkpoint[5/11]", "Verified opacity for the viewbox-1");
		
		cs.selectSeriesFromSeriesTab(1,series);
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(1)), "Checkpoint[6/11]", "Verified that notification closed when series is loaded in viewbox-1");
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(2)), "Checkpoint[7/11]", "Verified that notification closed when series is loaded in viewbox-1");
		
		//Scenario 3 - Warning pop up should not be displayed when source series is loaded 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch conflict when Source series is loaded in empty viewbox." );
		helper.browserBackAndReloadViewer(anonymousPatient, 1, 1);
		ViewerLayout layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(3), "Checkpoint[8/11]", "Verified batch conflict message in third viewbox.");
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(4), "Checkpoint[9/11]", "Verified batch conflict message in fourth viewbox.");
		
		cs.selectSeriesFromSeriesTab(5,series);
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(3)), "Checkpoint[10/11]", "Verified that notification closed when series is loaded in viewbox-5.");
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(4)), "Checkpoint[11/11]", "Verified that notification closed when series is loaded in viewbox-5.");
	
	}
	
	@Test(groups={"firefox","Chrome","IE11","Edge","US2583","Positive","BVT"})
	public void test03_US2583_TC10540_TC10541_TC10545_verifyBatchConflictWhenSameBatchLoaded() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Batch Conflict Overlays when data is loaded via content selector. <br>"+
		"Verify warning up is not displayed in the viewer when only one view box is loaded and no conflicting information is present. <br>"+
		"Verify closing scenarios of batch conflict warning pop up");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(anonymousPatient, 3);
		
		cs=new ContentSelector(driver);
		String result1=cs.getSeriesDescriptionOverlayText(1);
		String series=cs.getSeriesDescriptionOverlayText(2);
		
		cs.selectResultFromSeriesTab(2, cs.getAllResults().get(1));
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(1), "Checkpoint[1/12]", "Verified batch conflict message on viewbox-1.");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(1), "Checkpoint[2/12]", "Verified opacity for viewbox-1.");
		
		cs.selectSeriesFromSeriesTab(1,series);
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(1)), "Checkpoint[3/12]", "Verified that batch conflict not visible in viewbox-1 when series is loaded.");
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(2)), "Checkpoint[4/12]", "Verified that batch conflict not visible in viewbox-2 when series is loaded.");
		
		cs.selectResultFromSeriesTab(2, result1);
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(1)), "Checkpoint[5/12]", "Verified that no conflict message when same batch result is loaded on viewer.");
		
		//TC10541 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		helper.browserBackAndReloadViewer(anonymousPatient, 1, 1);
		ViewerLayout layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(3), "Checkpoint[6/12]", "Verified batch conflict message on viewbox-3 after layout change.");
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(4), "Checkpoint[7/12]", "Verified batch conflict message on viewbox-4 after layout change.");
		
		cs.doubleClick(cs.getViewPort(3));
		cs.waitForAllChangesToLoad();
		cs.assertFalse(cs.isElementPresent(cs.getViewboxNotificationMessageBody(1)), "Checkpoint[8/12]", "Verified that no conflict message when viewer layout is 1*1.");
		cs.doubleClick(cs.getViewPort(3));
		cs.assertEquals(cs.getNumberOfCanvasForLayout(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[9/12]", "Verified that layout is 3*3 .");
	    //wait is added because notification took time to load after double click.
		cs.waitForTimePeriod(2000);
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(1), "Checkpoint[10/12]", "Verified batch conflict message on viewbox-1 after double click perform.");
		layout.assertTrue(layout.verifyViewboxNotificationForConflicts(4), "Checkpoint[11/12]", "Verified batch conflict message on viewbox-4 after double click perform.");
		viewerpage.click(viewerpage.getViewboxNotification(1));
		viewerpage.assertTrue(viewerpage.waterMarkOverlay.isEmpty(), "Checkpoint[12/12]", "Verified that all water mark closed when click inside the viewer notification.");
		
	}
	
	@Test(groups={"firefox","Chrome","IE11","Edge","US2583","Positive"})
	public void test04_US2583_TC10544_verifyBatchConflictOnBrowserResize() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning pop ups after opening right panel and on browser resize.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(anonymousPatient, 1,2);
		
		cs=new ContentSelector(driver);
		
		ViewerLayout layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(3), "Checkpoint[1/6]", "Verified batch conflict message on viewbox-3 after layout change.");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(3), "Checkpoint[2/6]", "Verified opacity for the viewbox-3 after layout change.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification not closed when Output panel or Series tab." );
		cs.openAndCloseSeriesTab(true);
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(3), "Checkpoint[3/6]", "Verified batch conflict message on viewbox-3 when Output panel tab is open.");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(3), "Checkpoint[4/6]", "Verified opacity for the viewbox-3 when Output panel tab is open.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification not closed after browser window resize." );
		cs.openAndCloseSeriesTab(false);
		cs.resizeBrowserWindow(800, 900);
		cs.assertTrue(cs.verifyViewboxNotificationForConflicts(3), "Checkpoint[5/6]", "Verified batch conflict message on viewbox-3 when browser window resize.");
		cs.assertTrue(cs.verifyViewboxBGForConflicts(3), "Checkpoint[6/6]", "Verified opacity for the viewbox-3 when browser window resize.");
			
	}	
		
	//patient data: Liver 9 and Imbio Texture
	@Test(groups={"firefox","Chrome","IE11","Edge","DE1758","Negative"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test16_WaterMarkPresence"})
	public void test05_DE1739_TC7077_US2583_TC10542_TC10545_verifyAllOverlayCloseWhenClickOnAnyOfThem(String patientFilePath, String viewbox,String source) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all the water mark symbols are closed when any one of them is closed. <br>"+
		"Verify batch conflict overlay is displayed in the viewer when different batches are loaded  - When user creates annotations on Source series. <br>"+
		"Verify closing scenarios of batch conflict warning pop up");

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, helper.convertIntoInt(viewbox));		

		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(3);
		ellipse.drawEllipse(3, 0, 0, -100,-100);	
		viewerpage.assertFalse(viewerpage.waterMarkOverlay.isEmpty(), "Checkpoint[1/2]", "Verified water mark overlay visible after drawing annotation on viewer.");
		viewerpage.closeWaterMarkIcon(2);
		viewerpage.assertTrue(viewerpage.waterMarkOverlay.isEmpty(), "Checkpoint[2/2]", "Verified that all water mark closed.");
	}

	@Test(groups={"firefox","Chrome","IE11","Edge","DE1741","US2583","Positive","BVT"})
	public void test06_DE1741_TC7077_US2583_TC10545_verifyViewerAfterClosingWatermark() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no Point annotation is created when User tries to close the water mark in the Viewer. <br>"+
		"Verify closing scenarios of batch conflict warning pop up.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(imbioPatientName, 3);

		point=new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(3);
		point.drawPointAnnotationMarkerOnViewbox(3, 40, 40);
		point.assertEquals(point.getAllGSPSObjects(3).size(), 1, "Checkpoint[1/11]", "Verified that point annotation is drawn successfully on viewer.");

		point.closeWaterMarkIcon(1);
		point.assertEquals(point.getAllGSPSObjects(3).size(), 1, "Checkpoint[2/11]", "Verified that not new point annotation drawn after closing the watermark");
		point.assertTrue(point.waterMarkOverlay.isEmpty(), "Checkpoint[3/11]", "Verified that water mark closed ");

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 3);

		point.selectPointFromQuickToolbar(3);
		point.drawPointAnnotationMarkerOnViewbox(3, 40, 40);
		point.assertEquals(point.getAllGSPSObjects(3).size(), 1, "Checkpoint[4/11]", "Verified that point annotation is drawn successfully on viewer for Liver 9 patient");
		point.assertFalse(point.waterMarkOverlay.isEmpty(), "Checkpoint[5/11]", "Verified that water mark is present after drawing point annotation");

		point.performMouseRightClick(point.getViewPort(1));
		point.assertFalse(point.waterMarkOverlay.isEmpty(), "Checkpoint[6/11]", "Verified that watermark not closed after performing right click");
		point.assertFalse(point.isElementPresent(point.quickToolbarMenu), "Checkpoint[7/11]", "Verified that radial menu not open after performing right click on watermark");

		point.closeWaterMarkIcon(1);
		point.assertEquals(point.getAllGSPSObjects(3).size(), 1, "Checkpoint[8/11]", "Verified that not new point annotation drawn after closing the watermark");
		point.assertTrue(point.waterMarkOverlay.isEmpty(), "Checkpoint[9/11]", "Verified that water mark is closed.");

		String myText ="TextAnnotation";
		textAnno=new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(2);
		textAnno.drawText(2, 10, 10, myText);
		point.assertFalse(point.waterMarkOverlay.isEmpty(), "Checkpoint[10/11]", "Verified that water mark is present after drawing text annotation");

		point.click(point.getViewPort(1));
		point.assertTrue(point.waterMarkOverlay.isEmpty(),"Checkpoint[11/11]","Verified that water mark is closed.");

	}

	@Test(groups ={"Chrome","Edge","US2227","Positive","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test07_US2227_TC9726_TC9742_TC9737_verifyThemeForBatchConflictNotification(String theme) throws  InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that notification 'Warning : Data from different machine runs are  being  displayed' appearing as per the new design of Eureka theme. <br>"+
				"Verify that notification 'Warning : Data from different machine runs are  being  displayed' appearing as per the new design for dark theme.<br>"+
				"Verify that notification 'Some element of this study is for research use only. Not for diagnostic or clinical use.' is appearing as per the new design of Eureka theme.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME, username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			Header header = new Header(driver);
			header.logout();
			loginPage.waitForLoginPageToLoad();
			loginPage.login(username,password);
			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(anonymousPatient, 2);
		ViewerLayout layout = new ViewerLayout(driver);
		sd=new ViewerSendToPACS(driver);
		PagesTheme  pageTheme=new PagesTheme(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv), "Checkpoint[1/4]", "Verified that info notification visible when different machine are loaded on viewer");
		viewerpage.waitForElementVisibility(viewerpage.getWaterMark(3));
		viewerpage.assertTrue(pageTheme.verifyThemeForNotification(viewerpage.getViewboxNotification(3),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[2/4]", "Verified theme for Notification pop up for different batch machine load.");
		viewerpage.assertTrue(pageTheme.verifyThemeForNotification(viewerpage.getViewboxNotification(4),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[2/4]", "Verified theme for Notification pop up for different batch machine load.");

		helper.loadViewerDirectly(ChestCT1p25mm_Patient,2);
		viewerpage.assertTrue(pageTheme.verifyThemeForNotification(sd.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[3/4]", "Verified theme for Notification pop up for warning notification.");
		viewerpage.closeNotification();
		viewerpage.assertFalse(viewerpage.isElementPresent(sd.notificationDiv), "Checkpoint[4/4]", "Verified that notification closed after click on close icon.");



	}



}
