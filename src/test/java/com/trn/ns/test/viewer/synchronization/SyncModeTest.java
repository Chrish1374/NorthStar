package com.trn.ns.test.viewer.synchronization;

import java.io.IOException;
import java.sql.SQLException;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SyncModeTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private Header HeaderPage;
	private ViewBoxToolPanel preset;

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath2=Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String tDA_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);
	private HelperClass helper;
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	//Removed BeforeMethod as restarting IIS server

	//TC2451 - Verify the default synchronization mode value
	//TC2452 - Verify the toggling of synchronization
	@Test(groups ={"firefox","Chrome","IE11","US666"})
	public void test01_US666_TC2451_TC2452_verifyDefaultSyncMode() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("With Sync On || Verify the default synchronization mode value || Verify the toggling of synchronization");

	
		DatabaseMethods db = new DatabaseMethods(driver);
		//Verifying default sync mode value from DB
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that default value for 'defaultSyncMode' in DB");
		db.assertTrue(db.getDefaultSyncMode(), "Verify the default value for 'defaultSyncMode'", "Default value is true");
		
		helper = new HelperClass(driver);
		viewerPage =  helper.loadViewerDirectly(liver9_PatientName, username, password, 1);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");

		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);

		verifyImagesInSync(2,3,NSGenericConstants.BOOLEAN_TRUE);

		//Toggle off the sync and verify
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are NOT in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_FALSE);


	}

	//TC2451 - Verify the default synchronization mode value	
	//TC2452 - Verify the toggling of synchronization
	@Test(groups ={"firefox","Chrome","IE11","US666","Sanity"})
	public void test02_US666_TC2451_TC2452_verifyOffSyncMode() throws SQLException, IOException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("With Sync Off || Verify the default synchronization mode value || Verify the toggling of synchronization");

		DatabaseMethods db = new DatabaseMethods(driver);
		//Changing the default sync mode in configSetting table
		db.updateDefaultSyncMode(NSGenericConstants.BOOLEAN_FALSE);
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that the Default Sync Mode value is :"+NSGenericConstants.BOOLEAN_FALSE);
		db.assertFalse(db.getDefaultSyncMode(), "verifying the default sync mode is :"+NSGenericConstants.BOOLEAN_FALSE,"verified");

		helper = new HelperClass(driver);
		viewerPage =  helper.loadViewerDirectly(liver9_PatientName, username, password, 1);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that images are NOT in sync with zoom, windowing, scroll, Pan");

		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_FALSE);

		verifyImagesInSync(2,3,NSGenericConstants.BOOLEAN_FALSE);

		//Toggle on the sync and verify
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);

	}

	//TC2461 - Verify the sync state in single login session and then after re-login
	@Test(groups ={"firefox","Chrome","IE11","US666","Sanity"})
	public void test03_US666_TC2461_verifySyncStateAfterReLogin() throws  InterruptedException, SQLException, IOException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the latest state of the user defined sync mode status is maintained even when reloading a different study in the viewer");

		//Verifying default sync mode in configSetting table
		DatabaseMethods db = new DatabaseMethods(driver);
		if(db.getDefaultSyncMode() == false){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Updating the default sync mode value to :"+NSGenericConstants.BOOLEAN_TRUE);
			db.updateDefaultSyncMode(NSGenericConstants.BOOLEAN_TRUE);
			db.resetIISPostDBChanges();
		}							   
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that the Default Sync Mode value is :"+NSGenericConstants.BOOLEAN_TRUE);
		db.assertTrue(db.getDefaultSyncMode(), "verifying the default sync mode is :"+NSGenericConstants.BOOLEAN_TRUE,"verified");

		helper = new HelperClass(driver);
		viewerPage =  helper.loadViewerDirectly(liver9_PatientName, username, password, 1);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");

		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);

		//Toggle Off the sync and verify
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying that images are NOT in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_FALSE);

		//Logout and relogin to the application
		HeaderPage = new Header(driver);
		HeaderPage.logout();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying that user logs-out successfully");
		HeaderPage.openNewWindow();
		HeaderPage.switchToNewWindow(2);
		//loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),  "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());
		helper.loadViewerDirectly(liver9_PatientName, username, password, 1);

		//Verifying that the sync is ON by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");

		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);
	}

	//TC2459 - Verify that the latest state of the user defined sync mode status is maintained even when reloading a different study in the viewer.
	@Test(groups ={"firefox","Chrome","IE11","US666"})
	public void test04_US666_TC2459_verifySyncStateAfterBrowserBack() throws SQLException, IOException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the latest state of the user defined sync mode status is maintained even when reloading a different study in the viewer");

		//Changing the default sync mode in configSetting table
		DatabaseMethods db = new DatabaseMethods(driver);
		if(db.getDefaultSyncMode() == false){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Updating the default sync mode value to :"+NSGenericConstants.BOOLEAN_TRUE);
			db.updateDefaultSyncMode(NSGenericConstants.BOOLEAN_TRUE);
			db.assertTrue(db.getDefaultSyncMode(), "verifying the default sync mode is :"+NSGenericConstants.BOOLEAN_TRUE,"verified");

			db.resetIISPostDBChanges();
		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that the Default Sync Mode value is :"+NSGenericConstants.BOOLEAN_TRUE);
		db.assertTrue(db.getDefaultSyncMode(), "verifying the default sync mode is :"+NSGenericConstants.BOOLEAN_TRUE,"verified");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9_PatientName, 1, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");

		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);

		//Toggle Off the sync and verify
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are NOT in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_FALSE);

		viewerPage.navigateToBack();
		PatientListPage patientList=new PatientListPage(driver);
		helper.loadViewerPageUsingSearch(tDA_PatientName, 1, 1);
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are NOT in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_FALSE);
		//Toggle Off the sync and verify
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(2,3,NSGenericConstants.BOOLEAN_TRUE);
		
		viewerPage.navigateToBack();
		patientList.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9_PatientName, 1, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that images are in sync with zoom, windowing, scroll, Pan");
		verifyImagesInSync(1,2,NSGenericConstants.BOOLEAN_TRUE);
	}
	//Verifying sync On and symc Off
	private void verifyImagesInSync(int VB1, int VB2, String syncMode) throws  TimeoutException, InterruptedException{

		//Verify zoom
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(VB1));
		preset=new ViewBoxToolPanel(driver);
		int beforeZoom = preset.getZoomValue(VB1);

		//Applying the zoom
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(VB1),0, 0, 0, -10);
		viewerPage.assertNotEquals(beforeZoom, preset.getZoomValue(VB1), "Verify that the zoom value get change", "Zoom operation applied successfully");
		if(syncMode.equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE))
			viewerPage.assertEquals(preset.getZoomLevelValue(VB1), preset.getZoomLevelValue(VB2), "Verify that the viewbox"+VB1+" and viewbox"+VB2+" are in "
					+ "sync while applying zoom", "Viewboxes are in sync");
		else
			viewerPage.assertNotEquals(preset.getZoomLevelValue(VB1), preset.getZoomLevelValue(VB2), "Verify that the viewbox"+VB1+" and viewbox"+VB2+" are NOT in "
					+ "sync while applying zoom", "Viewboxes are NOT in sync");

		//Applying windowing
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(VB1));
		String beforeWWValue = viewerPage.getWindowWidthValText(VB1);
		String beforeWCValue = viewerPage.getWindowCenterText(VB1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(VB1),10, 10, 100, -50);
		viewerPage.assertNotEquals(beforeWWValue, viewerPage.getWindowWidthValText(VB1), "Verify that the window width value get change", "Windowing operation applied successfully");
		viewerPage.assertNotEquals(beforeWCValue, viewerPage.getWindowCenterText(VB1), "Verify that the window center value get change", "Windowing operation applied successfully");
		if(syncMode.equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE)){
			viewerPage.assertEquals(viewerPage.getWindowWidthValText(VB1), viewerPage.getWindowWidthValText(VB2), "Verify that the viewbox"+VB1+" and viewbox"+VB2+" "
					+ "are in sync while applying windowing - Window width", "Viewboxes are in sync");
			viewerPage.assertEquals(viewerPage.getWindowCenterText(VB1), viewerPage.getWindowCenterText(VB2), "Verify that the viewbox"+VB1+" and viewbox"+VB2+" "
					+ "are in sync while applying windowing - Window Center", "Viewboxes are in sync");
		}else{
			viewerPage.assertNotEquals(viewerPage.getWindowWidthValText(VB1), viewerPage.getWindowWidthValText(VB2), "Vserify that the viewbox"+VB1+" and viewbox"+VB2+" "
					+ "are NOT in sync while applying windowing", "Viewboxes are NOT in sync");
			viewerPage.assertNotEquals(viewerPage.getWindowCenterText(VB1), viewerPage.getWindowCenterText(VB2), "Verify that the viewbox"+VB1+" and viewbox"+VB2+" "
					+ "are NOT in sync while applying windowing - Window Center", "Viewboxes are NOT in sync");
		}

		//Applying scroll
		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(VB1));
		int beforeScroll = viewerPage.getCurrentScrollPositionOfViewbox(VB1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(VB1), 0, 0, 0, 50);
		viewerPage.assertNotEquals(beforeScroll, viewerPage.getCurrentScrollPositionOfViewbox(VB1), "Verify that the image number get change", "Scroll operation applied successfully");
		if(syncMode.equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE))
			viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(VB1), viewerPage.getCurrentScrollPositionOfViewbox(VB2), "Verify that the "
					+ "viewbox"+VB1+" and viewbox"+VB2+" are in sync while applying scroll", "Viewboxes are in sync");
		else
			viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(VB1), viewerPage.getCurrentScrollPositionOfViewbox(VB2), "Verify that the "
					+ "viewbox"+VB1+" and viewbox"+VB2+" are NOT in sync while applying scroll", "Viewboxes are NOT in sync");

	}

	@AfterMethod(alwaysRun=true)
	public void resetDBSettings() throws SQLException, IOException, InterruptedException {

		DatabaseMethods dba = new DatabaseMethods(driver);
		//Changing the default sync mode in configSetting table
		dba.updateDefaultSyncMode(NSGenericConstants.BOOLEAN_TRUE);
		dba.resetIISPostDBChanges();

	}
}
