package com.trn.ns.test.viewer.caching;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.enums.RenderingMode;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LossyCaching extends TestBase {

	private ExtentTest extentTest;	
	private ContentSelector cs;
	private HelperClass helper;
	private String ah4PatientName;
	private String northstarCtNeck;
	private String tcgaRT;
	private String srCtPatientName;
	private String ctPetMultiphase;
	private ViewBoxToolPanel preset;
	private ViewBoxToolPanel viewBoxPanel;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		// get patient names
		String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
		ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		filePath = Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
		northstarCtNeck = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		filePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
		tcgaRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		filePath = Configurations.TEST_PROPERTIES.get("AnonymizedByInferVISION2017-10-17_filepath");
		srCtPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		filePath = Configurations.TEST_PROPERTIES.get("CT_PET_Multiphase_filepath");
		ctPetMultiphase = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		// add a test user with the rendering mode as Lossy
		db = new DatabaseMethods(driver);
		AutoModeRpdCaching.addTestUser(db, RenderingMode.Lossy);
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(AutoModeRpdCaching.userName, AutoModeRpdCaching.password);
		loginPage.clearConsoleLogs();
		helper = new HelperClass(driver);
	}

/**
 ************************************************************************************
 ************************************** TESTS ***************************************
 ************************************************************************************ 
 */

	/**
	 * [Lossy-only mode] Verify caching starts for active viewbox and invalidated when lossy context is changed
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT","E2E", "F195" })
	public void test01_US1899_TC8945_TC8946_TC8948_TC8950_TC8951_TC8952_TC8953_TC8954_TC8962() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy rendering mode] Verify caching starts for active viewbox when viewer is loaded"
				+ "<br> [Lossy rendering mode] Verify caching does not consider non-loaded series"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when WW/WL is updated using mouse or through window width/contrast menu or through keyboard shortcut"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when zoom is updated using mouse or through zoom menu"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when pan is updated using mouse"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when image orientation is updated"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when browser is resized"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when layout is changed"
				+ "<br> [Lossy rendering mode] Verify caching re-starts when image number is updated");

		// load AH.4 study in viewer and wait for all viewboxes to load
		ViewerPage viewerPage = helper.loadViewerPage(ah4PatientName, "", 1, 1);

		viewerPage.waitForTimePeriod(3000);
		
		verifyLossyCachingStarts(viewerPage);
		
		// check after caching completed, scroll should find the images
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);
		
		// check after zoom is changed, caching is restarted and then scroll should find all images in cache
		verifyZoomInvalidatesLossyCache(viewerPage);
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after pan is changed, caching is restarted and then scroll should find all images in cache
		verifyPanInvalidatesLossyCache(viewerPage);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after rotation is changed, caching is restarted and then scroll should find all images in cache
		ViewerOrientation orin = new ViewerOrientation(driver);
		verifyRotationInvalidatesLossyCache(orin);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after orientation is changed, caching is restarted and then scroll should find all images in cache
		verifyFlipInvalidatesLossyCache(orin);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after window width is changed, caching is restarted and then scroll should find all images in cache
		verifyWindowWidthInvalidatesLossyCache(viewerPage);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after window center is changed, caching is restarted and then scroll should find all images in cache
		verifyWindowCenterInvalidatesLossyCache(viewerPage);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after the layout is changed, caching is restarted and then scroll should find all images in cache
		ViewerLayout layout = new ViewerLayout(driver);
		verifyLayoutChangeInvalidatesLossyCache(layout);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);

		// check after window center is changed, caching is restarted and then scroll should find all images in cache
		verifyViewboxSizeInvalidatesLossyCache(viewerPage);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);
	}

	/**
	 * [Lossy-only mode] Verify caching starts for the new active viewbox when active viewbox is changed
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT","E2E", "F195" })
	public void test02_US1899_TC8947() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy rendering mode] Verify caching starts for active viewbox when viewer is loaded");

		// load NorthStar^CT^Neck study in viewer
		ViewerPage viewerPage = helper.loadViewerPage(northstarCtNeck, "", 1, 1);	

		// read console logs and clear
		List<String> logs = viewerPage.getConsoleLogs();
		viewerPage.clearConsoleLogs();
		
		// mouse over on 3rd viewbox to make it active
		viewerPage.mouseHover(viewerPage.getViewPort(3));
		
		// wait for caching to kick in
		viewerPage.waitForTimePeriod(1000);
				
		// read console logs
		logs = viewerPage.getConsoleLogs();
		
		// check that new cache context is made active in worker
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.CHANGE_ACTIVE_VIEWBOX));
		viewerPage.assertTrue(logFound, "Checkpoint[1/3]", "Verifying that new cache context is made active in worker");
		
		// check that worker clear current retrieve instance list
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.CLEAR_RETRIEVE_LIST));
		viewerPage.assertTrue(logFound, "Checkpoint[2/3]", "Verifying that worker clear current retrieve instance list");
		
		// check that worker starts caching new active viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_ACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "Checkpoint[3/3]", "Verifying that worker starts caching new active viewbox");
	}

	/**
	 * [Lossy-only mode] Verify 2 viewboxes loaded with same series but with different context cache separately 
	 * @throws InterruptedException
	 * @throws AWTException 
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT","E2E", "F195" })
	public void test03_US1899_TC8970() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy-only mode] Verify 2 viewboxes loaded with same series but with different context caches separately");

		// load AH.4 study in viewer
		ViewerPage viewerPage = helper.loadViewerPage(ah4PatientName, "", 1, 1);	
		ViewerLayout layout = new ViewerLayout(driver);
		preset= new ViewBoxToolPanel(driver);
		// turn off the sync
		viewerPage.performSyncONorOFF();

		// change the layout to 2x1
		layout.selectLayout(layout.twoByOneLayoutIcon);
		
		// load the same series of viewbox 1 in viewbox 2
		cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(2, viewerPage.getSeriesDescriptionOverlay(1).getText().trim());
		
		// change zoom level of viewbox 2
		int newZoomValue = 150;
		preset.changeZoomNumber(2,newZoomValue);
		
		// turn on the sync
		viewerPage.performSyncONorOFF();
		viewBoxPanel=new ViewBoxToolPanel(driver);
		// verify only viewbox 2 is zoomed and not viewbox 1
		viewerPage.verifyEquals(viewBoxPanel.getZoomValue(2), newZoomValue, "Checkpoint[1/2]", "Verifying viewbox 2 is zoomed to " + newZoomValue);
		viewerPage.verifyFalse(viewBoxPanel.getZoomValue(2) == newZoomValue, "Checkpoint[2/2]", "Verifying viewbox 1 is not zoomed to " + newZoomValue);
				
		// now we have 2 viewboxes loaded with same series but with different zoom value
		// check that after window center is changed, both viewboxes cache separately and then scroll should find all images in cache
		verifyWindowCenterInvalidatesLossyCache(viewerPage);		
		AutoModeRpdCaching.scrollToVerifyImageInCache(viewerPage, 2, 2);
	}
	
	/**
	 * [Lossy-only mode] Verify output panel does not cache in lossy rendering mode
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT","E2E", "F195" })
	public void test04_US1899_TC8965() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy rendering mode] Verify output panel does not cache in lossy rendering mode");

		// load RT TCGA study in viewer
		ViewerPage viewerPage = helper.loadViewerPageForRTUsingSearch(tcgaRT, 1, 1);	
		DICOMRT rt = new DICOMRT(driver);
		
		int findingsCount = rt.getFindingsCountFromFindingTable();
		
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		OutputPanel panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
				
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		// check that output panel does not cache
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.OUTPUT_PANEL_NO_LOSSY_CACHING));
		viewerPage.assertTrue(logFound, "Checkpoint[1/4]", "Verifying that output panel does not cache in lossy rendering mode");
		
		// verify thumbnails and cine
		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/3]", "Output panel findings are equals to findings present in finding menu");		
		panel.assertTrue(panel.verifyAnnotationsPresenceInThumbnail(3),"Checkpoint[2/3]","Verifying the annotations are displayed when cine is played on thumbnail");
		
		logFound = viewerPage.verifyNetworkLogFound(URLConstants.RPD_IMAGE_URL);
		viewerPage.assertFalse(logFound, "Checkpoint[3/3]", "Verifying that no RPD request is sent");
	}
	
	/**
	 * [Lossy-only mode] Verify that caching starts for inactive viewbox if active viewbox has non supported data like PDF/SR
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT" ,"E2E", "F195"})
	public void test05_US1899_TC8968() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy rendering mode] Verify that caching starts for inactive viewbox if active viewbox has non supported data like PDF/SR");

		// load SR study in viewer
		ViewerPage viewerPage = helper.loadViewerPage(srCtPatientName, "", 1, 2);

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(2000);
		
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		// check that SR does not cache
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.NO_CACHE_SERIES("SR")));
		viewerPage.assertTrue(logFound, "Checkpoint[1/3]", "Verifying that SR is not supported");
		
		// check that active viewbox does not start caching
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_ACTIVE_VIEWBOX_CACHE));
		viewerPage.assertFalse(logFound, "Checkpoint[2/3]", "Verifying that active viewbox cannot be cached");
		
		// check that caching starts for inactive viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_INACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "Checkpoint[3/3]", "Verifying that caching starts for inactive viewbox");
		
	}
	
	/**
	 * [Lossy-only mode] Verify that caching is not restarted when phase number is changed
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT","E2E", "F195"})
	public void test06_US1899_TC8964() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Lossy rendering mode] Verify that caching is not restarted when phase number is changed");

		// load SR study in viewer
		ViewerPage viewerPage = helper.loadViewerPage(ctPetMultiphase, "", 1, 1);
		viewerPage.waitForAllImagesToLoad();

		// read console logs and clear
		List<String> logs = viewerPage.getConsoleLogs();
		viewerPage.clearConsoleLogs();
		
		// change phase
		viewerPage.scrollToPhase(1, 2);		
		
		// verify that lossy context is not deleted after changing the phase number
		logs = viewerPage.getConsoleLogs();		
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CONTEXT_CHANGED(1)));
		viewerPage.assertFalse(logFound, "Checkpoint[1/5]", "Verifying that lossy context is not changed for viewbox 1");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CONTEXT_CHANGED(2)));
		viewerPage.assertFalse(logFound, "Checkpoint[2/5]", "Verifying that lossy context is not changed for viewbox 2");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_DELETED_OLD_CONTEXT));
		viewerPage.assertFalse(logFound, "Checkpoint[3/5]", "Verifying that lossy context is not deleted");

		// clear logs and verify all phase images are found in the cache
		viewerPage.clearConsoleLogs();
		
		int maxPhase = viewerPage.getValueOfMaxPhase(1);
		for(int i = 3; i <= maxPhase; i++) {
			viewerPage.scrollToPhase(i, 1);
		}
		logs = viewerPage.getConsoleLogs();
		
		// check that all phase images are found in the cache
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND)).count();
		viewerPage.assertEquals(count, maxPhase - 2, "Checkpoint[4/5]", "Verifying that all phase images are found");
		
	}

/**
 ************************************************************************************
 **************************** PRIVATE METHODS ***************************************
 ************************************************************************************ 
 */


	private void verifyZoomInvalidatesLossyCache(ViewerPage viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// change zoom in viewbox 2
		preset.changeZoomNumber(2, 142);
		
		// wait for caching to kick in
		viewerPage.waitForTimePeriod(500);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyPanInvalidatesLossyCache(ViewerPage viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// select pan tool
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select Pan tool from context Menu" );
		WebElement viewbox = viewerPage.getViewPort(1);
		viewerPage.selectPanFromQuickToolbar(viewbox);
		
		// pan the image
		viewerPage.dragAndReleaseOnViewerQuickVersion(viewbox, 10, 10, 20, 20);

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(1000);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyRotationInvalidatesLossyCache(ViewerOrientation viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// rotate image in viewbox 1
		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(1), viewerPage.topClockwiseRotationMarker(1));

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(1000);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyFlipInvalidatesLossyCache(ViewerOrientation viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// flip image in viewbox 1
		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(2));

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(1000);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyWindowWidthInvalidatesLossyCache(ViewerPage viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// update window width of viewbox 2 // Need fix explicitly
//		viewerPage.inputWWNumber(111, 2);

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(500);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyWindowCenterInvalidatesLossyCache(ViewerPage viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// update window center of viewbox 1// Need fix explicitly
//		viewerPage.inputWCNumber(333, 1);

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(500);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}
	
	private void verifyLayoutChangeInvalidatesLossyCache(ViewerLayout viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// change the layout to 1x2
		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyViewboxSizeInvalidatesLossyCache(ViewerPage viewerPage) throws InterruptedException {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// resize window
		viewerPage.resizeBrowserWindow(800, 600);

		// wait for caching to kick in
		viewerPage.waitForTimePeriod(500);
		
		//check that caching starts for the active viewbox
		verifyCachingRestarted(viewerPage);
	}

	private void verifyLossyCachingStarts(ViewerPage viewerPage) {
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		AutoModeRpdCaching.verifyCachingInitialization(viewerPage, logs);
		
		// check that caching starts for the active viewbox
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_ACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "Checkpoint[1/6]", "Verifying worker started to cache active viewbox");
		
		// check that caching starts for the inactive viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_INACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "Checkpoint[2/6]", "Verifying worker started to cache inactive viewbox");
		
		// check that caching is completed for the active and inactive viewboxes
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
		viewerPage.assertEquals(count, 3, "Checkpoint[3/6]", "Verifying worker completed caching active viewbox");
		
		// check that caching is completed for all loaded viewboxes only
		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.ALL_CACHE_DONE(totalViewboxes)));
		viewerPage.assertTrue(logFound, "Checkpoint[4/6]", "Verifying worker completed caching all loaded viewboxes");
		
		// check that it does not cache not loaded series
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_NO_DISPLAYING_VIEWBOX_CACHE));
		viewerPage.assertFalse(logFound, "Checkpoint[5/6]", "Verifying worker does not cache not loaded series");
		
		logFound = viewerPage.verifyNetworkLogFound(URLConstants.RPD_IMAGE_URL);
		viewerPage.assertFalse(logFound, "Checkpoint[6/6]", "Verifying that no RPD request is sent");
	}

	private void verifyCachingRestarted(ViewerPage viewerPage) {
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		// check that caching starts for the active viewbox
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CONTEXT_CHANGED(1)));
		viewerPage.assertTrue(logFound, "Checkpoint[1/7]", "Verifying panning changed lossy cache context for viewbox 1");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CONTEXT_CHANGED(2)));
		viewerPage.assertTrue(logFound, "Checkpoint[2/7]", "Verifying panning changed lossy cache context for viewbox 2");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CLEARED_CONTEXT(1)));
		viewerPage.assertTrue(logFound, "Checkpoint[3/7]", "Verifying viewbox 1 cleared old cache");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.LOSSY_CLEARED_CONTEXT(2)));
		viewerPage.assertTrue(logFound, "Checkpoint[4/7]", "Verifying viewbox 2 cleared old cache");
		
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_NOT_FOUND)).count();
		viewerPage.assertTrue(count >= 2, "Checkpoint[5/7]", "Verifying RPD/lossy image was not found in the cache for the 2 viewboxes in sync");
		
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.LOSSY_DELETED_OLD_CONTEXT)).count();
		viewerPage.assertTrue(count >= 2, "Checkpoint[6/7]", "Verifying worker deleted old contexts for the 2 viewboxes");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.ALL_CACHE_DONE(-1)));
		viewerPage.assertTrue(logFound, "Checkpoint[7/7]", "Verifying caching is completed");
	}
}
