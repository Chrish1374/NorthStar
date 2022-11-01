package com.trn.ns.test.viewer.PMAP;

import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.test.viewer.caching.AutoModeRpdCaching;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPLossySupportTest extends TestBase {

	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private HelperClass helper;

	String filePath1 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);

	String filepath2 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath2);
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String newUser = "abc";

	
	@Test(groups ={"US2039","Positive","F195","E2E"})
	public void test01_US2039_TC9153_verifyLossySupportPMAP() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that lossy mode is always visible for Pmap data for lossy user no matter zoom percentage.");

		RegisterUserPage register = new RegisterUserPage(driver,false);
		register.createNewUserNonUIWay(username, username, newUser, newUser, LoginPageConstants.SUPPORT_EMAIL, newUser, newUser, newUser, false);
		
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, newUser,newUser,1);
		ViewerLayout layout = new ViewerLayout(driver);
		
		layout.closeNotification();
		layout.assertTrue(layout.isElementPresent(layout.getLossyOverlay(1)),"Checkpoint[1/3]","verifying lossy annotation presence in viewbox1 on default load");
		
		ViewBoxToolPanel viewbox = new ViewBoxToolPanel(driver);
		
		viewbox.changeZoomNumber(1, 150);
		layout.assertTrue(layout.isElementPresent(layout.getLossyOverlay(1)),"Checkpoint[2/3]","Lossy tag should be visible always for Pmap no matter zoom percentage.");
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewbox.changeZoomNumber(1, 60);
		for (int i = 1; i <= 2; i++) 		
			layout.assertTrue(layout.isElementPresent(layout.getLossyOverlay(i)),"Checkpoint[3/3]","Lossy tag should be visible always for Pmap no matter zoom percentage.");
	
	}
	
	
	@Test(groups ={"US2039","F195","Positive"})
	public void test02_US2039_TC9154_verifyLossyCaching() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that caching is happening for Pmap overlay when mode is lossy.");

		db = new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, 1, username);
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(covidPatientName,username,username,1);
		viewerpage.closeNotification();
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getLossyOverlay(1)),"Checkpoint[1/6]","verifying lossy annotation presence in viewbox1 on default load");
		
		// wait for caching to be done
		viewerpage.waitForTimePeriod(3000);
		
		List<String> logs = viewerpage.getConsoleLogs();		
		AutoModeRpdCaching.verifyCachingInitialization(viewerpage, logs);
		
		// check that caching starts for the active viewbox
		boolean logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_ACTIVE_VIEWBOX_CACHE));
		viewerpage.assertTrue(logFound, "Checkpoint[2/6]", "Verifying worker started to cache active viewbox");
		
		// check that caching starts for the inactive viewbox
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_INACTIVE_VIEWBOX_CACHE));
		viewerpage.assertTrue(logFound, "Checkpoint[3/6]", "Verifying worker started to cache inactive viewbox");
		
		// check that caching is completed for the active and inactive viewboxes
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
		viewerpage.assertEquals(count, 2, "Checkpoint[4/6]", "Verifying worker completed caching active viewbox");
		
		// check that caching is completed for all loaded viewboxes only
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.ALL_CACHE_DONE(2)));
		viewerpage.assertTrue(logFound, "Checkpoint[5/6]", "Verifying worker completed caching all loaded viewboxes");
		
		// check that it does not cache not loaded series
		logFound = logs.stream().anyMatch(e -> e.contains(CachingLogConstants.START_NO_DISPLAYING_VIEWBOX_CACHE));
		viewerpage.assertFalse(logFound, "Checkpoint[6/6]", "Verifying worker does not cache not loaded series");
		
	}
	
	
	@Test(groups ={"US2039","Positive","F195"})
	public void test03_US2039_TC9156_verifygZipCompressionInrequest() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the gzip compression for Pmap overlays in request header.");

		db = new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, 1, username);
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID, username, username,1);
		viewerpage.closeNotification();
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getLossyOverlay(1)),"Checkpoint[1/2]","verifying lossy annotation presence in viewbox1 on default load");
		
		viewerpage.waitForTimePeriod(3000);
		
		viewerpage.assertEquals(viewerpage.getParamValFromNetworkRequest(OrthancAndAPIConstants.NETWORK_METHOD_2,OrthancAndAPIConstants.ACCEPT_ENCODING,OrthancAndAPIConstants.ACCEPT_ENCODING_VAL),62,"Checkpoint[2/2]","verifying the gzip compression is present in header");
	
	}
	
	
	@AfterMethod
	public void updateMode() throws SQLException {
		
		db = new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, 0, username);
	
		
	}

}
