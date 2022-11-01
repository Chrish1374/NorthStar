package com.trn.ns.test.viewer.caching;

import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.enums.RenderingMode;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AutoModeRpdCaching extends TestBase {
	public static final String userName = "newuser";
	public static final String password = "scan";

	private ExtentTest extentTest;	
	private HelperClass helper;
	private String ah4PatientName;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		// get patient names
		String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
		ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		// add a test user with the rendering mode as Auto
		db = new DatabaseMethods(driver);
		addTestUser(db, RenderingMode.Auto);
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(userName, password);
		loginPage.clearConsoleLogs();
		helper = new HelperClass(driver);
	}

/**
 ************************************************************************************
 ************************************** TESTS ***************************************
 ************************************************************************************ 
 */

	/**
	 * [Auto rendering mode] Verify cache is NOT invalidated when zoom is changed
	 * @throws InterruptedException
	 */
	@Test(groups = { "Chrome", "IE11", "Edge", "US1899", "BVT" ,"E2E", "F195" })
	public void test01_US1899_TC8974() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Auto rendering mode] Verify cache is NOT invalidated when zoom is changed");

		// load AH.4 study in viewer and wait for all viewboxes to load
		ViewerPage viewerPage = helper.loadViewerPage(ah4PatientName, "", 1, 1);
		viewerPage.waitForAllImagesToLoad();

		viewerPage.waitForTimePeriod(2000);
		
		verifyRpdCachingStarts(viewerPage);
		
		// check after caching completed, scroll should find the images
		scrollToVerifyImageInCache(viewerPage, 2, 2);

		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// change zoom in viewbox 2
		ViewBoxToolPanel preset = new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(2,142);
		
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND)).count();
		viewerPage.assertTrue(count >= 2, "verifyCacheNotInvalidated[1/2]", "Verifying RPD/lossy image was found in the cache for the 2 viewboxes in sync");
		
		boolean logFound = logs.stream().anyMatch(e -> e.contains("worker"));
		viewerPage.assertTrue(logFound, "verifyCacheNotInvalidated[2/2]", "Verifying worker was not invoked");
	}

/**
 ************************************************************************************
 **************************** PUBLIC STATIC METHODS ***************************************
 ************************************************************************************ 
 */
	public static void addTestUser(DatabaseMethods db, RenderingMode mode) {
		final String encPassword = "JFlm2Aybzf4K2GtZMVhf0LAgdyh+GJV2";
		final String salt = "pOXKweEGhzpelKkB+28GsB6THYgZJjae";
		db.addUserInDB("", userName, encPassword, salt, "1900-01-01 00:00:00.000","","","","","","","2018-09-10 16:30:28.153","dark","0","","","","");
		try {
			db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, mode.getValue(), userName);
		} catch (Exception e) {
			LOGGER.error("Exception in addTestUser()", e);
		}
	}
	
	public static void verifyCachingInitialization(ViewerPage viewerPage, List<String> logs) {		
		// check that caching starts for the active viewbox
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.SET_TOKEN));
		viewerPage.assertTrue(logFound, "verifyCachingInitialization[1/8]", "Verifying worker set authentication token");
		
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.INIT_CONFIG_SETTINGS));
		viewerPage.assertTrue(logFound, "verifyCachingInitialization[2/8]", "Verifying worker initialized config settings");

		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.ADDED_IMAGE_CACHE)).count();
		viewerPage.assertTrue(count >= totalViewboxes, "verifyCachingInitialization[3/8]", "Verifying image cache is created for each viewbox");
		
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.ADDED_SERIES_INFO)).count();
		viewerPage.assertTrue(count >= totalViewboxes, "verifyCachingInitialization[4/8]", "Verifying series info is added");
		
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.ADDED_CACHE_CONTEXT)).count();
		viewerPage.assertTrue(count >= totalViewboxes, "verifyCachingInitialization[5/8]", "Verifying cache context is added");

		int otSeriesCount = 0, srSeriesCount = 0;
		for(int i = 1; i <= totalViewboxes; i++) {
			if(viewerPage.getPDFViewPort(i) != null) {
				otSeriesCount++;
			}
			if(viewerPage.getSRViewPort(i) != null) {
				srSeriesCount++;				
			}
		}
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.NO_CACHE_SERIES("OT"))).count();
		viewerPage.assertEquals(count, otSeriesCount, "verifyCachingInitialization[6/8]", "Verifying caching is not supported for OT series");

		count = logs.stream().filter(e -> e.contains(CachingLogConstants.NO_CACHE_SERIES("SR"))).count();
		viewerPage.assertEquals(count, srSeriesCount, "verifyCachingInitialization[7/8]", "Verifying caching is not supported for SR series");
		
		// check that caching starts for the inactive viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.SET_ACTIVE_VIEWBOX));
		viewerPage.assertTrue(logFound, "verifyLossyCachingStarts[8/8]", "Verifying worker set active viewbox");
	}
	
	public static void scrollToVerifyImageInCache(ViewerPage viewerPage, int viewboxNumber, int syncViewboxes) {
		// clear console logs before new action
		viewerPage.clearConsoleLogs();
		
		// scroll through all slices in the given viewbox (multiplied by syncViewbox count)
		int totalNumberOfImages = viewerPage.scrollThroughAllSlices(viewboxNumber, 20) * syncViewboxes;

		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		// check that caching starts for the active viewbox
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND)).count();
		viewerPage.assertEquals(count, totalNumberOfImages, "Scroll_To_Verify_Cache_Checkpoint", "Verifying all " + totalNumberOfImages + " slices were found in the cache");
	}
	
/**
 ************************************************************************************
 **************************** PRIVATE METHODS ***************************************
 ************************************************************************************ 
 */

	private void verifyRpdCachingStarts(ViewerPage viewerPage) {
		// read console logs
		List<String> logs = viewerPage.getConsoleLogs();
		
		verifyCachingInitialization(viewerPage, logs);
		
		// check that caching starts for the active viewbox
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_ACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "verifyLossyCachingStarts[1/6]", "Verifying worker started to cache active viewbox");
		
		// check that caching starts for the inactive viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_INACTIVE_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "verifyLossyCachingStarts[2/6]", "Verifying worker started to cache inactive viewbox");

		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		// check that caching is completed for the active and inactive viewboxes
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
		viewerPage.assertTrue(count > totalViewboxes, "verifyLossyCachingStarts[3/6]", "Verifying worker completed caching active viewbox");
		
		// check that caching is completed for all loaded viewboxes only
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.ALL_CACHE_DONE(-1)));
		viewerPage.assertTrue(logFound, "verifyLossyCachingStarts[4/6]", "Verifying worker completed caching");
		
		// check that it caches not loaded series
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_NO_DISPLAYING_VIEWBOX_CACHE));
		viewerPage.assertTrue(logFound, "verifyLossyCachingStarts[5/6]", "Verifying worker caches not-loaded series");
		
		logFound = viewerPage.verifyNetworkLogFound(URLConstants.LOSSY_IMAGE_URL);
		viewerPage.assertFalse(logFound, "Checkpoint[6/6]", "Verifying that no lossy request is sent");
	}
}
