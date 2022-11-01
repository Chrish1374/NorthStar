
/*
package com.trn.ns.test.obsolete;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0
//Functional.NS.I29_Viewer-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ZoomSyncFromActiveOverlay extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel preset;
	private ViewerLayout layout;
	
	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);

	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	//Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	//TC1065 - Active overlay manual input of zoom percentage or select from zoom options and synchronization - Manual Input Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	@Test(groups ={"firefox","Chrome","IE11","US340","US289","Sanity","BVT"})
	public void test01_US340_TC1065_US289_TC2374_verifyZoomOverlayByManualInput() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of zoom percentage or select from zoom options and synchronization - Manual Input Coverage"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);
		preset=new ViewBoxToolPanel(driver);

		for(int i = 1 ; i<=2 ;i++){
			String beforeZoomValueFirstViewbox = preset.getZoomValue(i);

			//Step 1 - verifying that zoom values are changed for sync series for valid value
			viewerpage.mouseHover(viewerpage.getViewPort(i));
			preset.changeZoomValue(80, i);
			String currentZoomValue = preset.getZoomValue(i);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[1] & Checkpoint[2/5]", "Verifying that zoom values are changed for sync series after inputing values in input box" );
			viewerpage.assertNotEquals(beforeZoomValueFirstViewbox,  preset.getZoomValue(i), "Verify that zoom are changes to new values", "Before zoom value is "+beforeZoomValueFirstViewbox+"  after appying zoom the updated value is "+preset.getZoomValue(i)+"");
			compareZoomValues(i,true);

			//Step 2 - verifying that zoom values are not changed for sync series for invalid value
			preset.changeZoomValue(-10, i);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[2] & Checkpoint[3/5]", "Verifying that zoom values are not changed for any series after inputing invalid values in input box" );
			viewerpage.assertEquals(currentZoomValue,  preset.getZoomValue(i), "Verify that zoom are not changes to new values", "Zoom values are "+currentZoomValue+" and "+preset.getZoomValue(i)+" ");
			compareZoomValues(i,true);

			//Step 3 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Before Sync Values -
			String beforeZoomSyncValue = preset.getZoomValue(i);

			//Perform zoom
			preset.changeZoomValue(i,100);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[3] & Checkpoint[4/5]", "Verifying that the zoom values are not same after inputing different values for zoom input box for images not in sync" );
			//Verifying that the Zoom values are changed only for first viewbox
			viewerpage.assertNotEquals(beforeZoomSyncValue, preset.getZoomValue(i), "Verify that the zoom values are changes to new values", "Zoom values before zoom "+beforeZoomSyncValue+" and after zoom is "+preset.getZoomValue(i)+" ");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[4] & Checkpoint[5/5]", "Verifying that the zoom values are not change for series not in sync" );
			//Verifying that the Zoom values are not changed for other viewbox
			compareZoomValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			preset.changeZoomValue( i,90);
		}
	}

	//TC1066 - Active overlay manual input of zoom percentage or select from zoom options and synchronization - Context Menu Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	@Test(groups ={"firefox","Chrome","IE11","US340","Sanity","BVT"})
	public void test02_US340_TC1066_verifyZoomOverlayByContextMenu() throws InterruptedException {

		

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of zoom percentage or select from zoom options and synchronization - Context Menu Coverage ");
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);
		preset=new ViewBoxToolPanel(driver);

		//Step1 - "Zoom to 100%" option.
		String defaultZoomValueFirstViewbox = preset.getZoomValue(1);

		for(int i = 1 ; i<=2 ;i++){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying the presence of Zoom context table" );
			viewerpage.assertTrue(preset.verifyPresenceOfContextMenu(i, viewerpage.getZoomLabelOverlay(1), preset.OverlayMenuTable), "Verify the presence of Zoom context table", "Zoom context table is displaying");

			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[1] & Checkpoint[3/8]", "Verifying that the Zoom value is set to 'Zoom to 100%' for sync series" );
			viewerpage.assertEquals(preset.getZoomValue(i), "100", "Verify zoom is set to 100%", "Zoom value is updated to 100%");
			compareZoomValues(i,true);

			//Step2 - "Zoom to True Size" option.
			String zoomValueFirstViewboxBeforeZoomTrueSize = preset.getZoomValue(i);
			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_TRUE_SIZE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[2] & Checkpoint[4/8]", "Verifying that the Zoom value is set to 'Zoom to True Size' for sync series" );
			viewerpage.assertEquals(preset.getZoomValue(i), zoomValueFirstViewboxBeforeZoomTrueSize, "Verify that Zoom value does not change ", "Zoom values are "+preset.getZoomValue(i)+" and  "+zoomValueFirstViewboxBeforeZoomTrueSize+" ");
			compareZoomValues(i,true);

			//Step3 - "Zoom to Fit" option.
			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_FIT);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[3] & Checkpoint[5/8]", "Verifying that the Zoom value is set to 'Zoom to Fit' for sync series" );
			viewerpage.assertEquals(preset.getZoomValue(i), defaultZoomValueFirstViewbox, "Verify zoom is set to its default zoom value upon loading the data in a viewer that size.", "Zoom value is updated to default size");
			compareZoomValues(i,true);

			//Step4 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Perform Zoom to 100%
			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[6/8]", "Verifying that the Zoom value is set to 'Zoom to 100%' for series not in sync" );
			viewerpage.assertEquals(preset.getZoomValue(i), "100", "Verify zoom is set to 100%", "Zoom value is updated to 100%");
			compareZoomValues(i,false);

			//Step2 - "Zoom to True Size" option.
			String zoomValueFirstViewboxBeforeZoomTrueSizeForNotSyncSeries = preset.getZoomValue(i);
			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_TRUE_SIZE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[7/8]", "Verifying that the Zoom value is set to 'Zoom to True Size' for series not in sync" );
			viewerpage.assertEquals(preset.getZoomValue(i), zoomValueFirstViewboxBeforeZoomTrueSizeForNotSyncSeries, "Verify that Zoom value does not change ", "Zoom value is not updated");
			compareZoomValues(i,false);

			//Step3 - "Zoom to Fit" option.
			//To check "zoom to Fit" not applied to sync series, change the other viewboxes zoom value to 100%
			viewerpage.performSyncONorOFF();
			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			viewerpage.performSyncONorOFF();

			preset.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_FIT);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[8/8]", "Verifying that the Zoom value is set to 'Zoom to Fit' for series not in sync" );
			viewerpage.assertEquals(preset.getZoomValue(i), defaultZoomValueFirstViewbox, "Verify zoom is set to its default zoom value upon loading the data in a viewer that size.", "Zoom value is updated to default size");
			compareZoomValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			preset.changeZoomValue(90, i);
		}
	}

//Limitation this test script dosn't work in edge
	//	TC 4: Verify the Zoom context menu is opened by selecting the Zoom label (Automated) 
	//	TC 5: Verify that Zoom to 100% option functions properly (Automated) 
	@Test(groups ={"Chrome","internet explorer","firefox","US225","US289"})
	public void test04_US225_TC859_TC886_TC888_TC889_TC890_TC891_US289_TC2413_ZoomImageFromActiveOverlay() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Zoom image from Active Overlay"
				+ "<br> Verify Sync ON/OFF for Zoom.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);
        preset=new ViewBoxToolPanel(driver);

		//Test 01 (TC859)   Verify the Zoom context menu is opened by selecting the Zoom label
        preset.getZoomLabelOverlay(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 01 (TC859): Checkpoint[1/10]", "Verifying the presence of Zoom context table" );
		viewerpage.assertTrue(preset.OverlayMenuTable.isDisplayed(), "Verify the presence of Zoom context table", "Zoom context table is displaying"); 

		int firstViewbox = preset.getZoomLevelValue(1);
		int secondViewbox = preset.getZoomLevelValue(1);
		int ThirdViewbox = preset.getZoomLevelValue(1);
		int fourViewbox = preset.getZoomLevelValue(1);

		//Test 02 (TC888)   Verify that Zoom to 100% option functions properly
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[1] & Checkpoint TC4[1] & Test 02 (TC888): Checkpoint[2/10]", "Verifying that the Zoom value is set to 'Zoom to 100%'" );
		viewerpage.assertEquals(preset.getZoomLevelValue(1), 100, "Verify zoom to 100%", "Zoom value is updated to 100%"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(2), 100, "Verify zoom to 100%", "Zoom value is updated to 100%"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(3), 100, "Verify zoom to 100%", "Zoom value is updated to 100%");
		viewerpage.assertEquals(preset.getZoomLevelValue(4), 100, "Verify zoom to 100%", "Zoom value is updated to 100%");

		//Test 03 (TC889)   Verify that Zoom to Fit  option functions properly
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_FIT); 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC4[1] & Test 03 (TC889): Checkpoint[3/10]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(1), firstViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(2), secondViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(3), ThirdViewbox,"Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
		viewerpage.assertEquals(preset.getZoomLevelValue(4), fourViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");


		//Test 04 (TC890)   Verify that Zoom to True Size  functions as expected
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_TRUE_SIZE); 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Test 04 (TC890): Checkpoint[4/10]", "Verifying that the Zoom value is not changing after clikcing Zoom to true Size ");
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(1), "Verify zoom to True Size not applied ", "Zoom value is not updated "); 

		viewerpage.performSyncONorOFF();
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[2] & Checkpoint TC4[1] & Checkpoint[5/10]", "Verifying that the Zoom value is set to 'Zoom to 100%'" );
		viewerpage.assertEquals(preset.getZoomLevelValue(1), 100, "Verify zoom to 100%", "Zoom value is updated to 100%"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(2), secondViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(3), ThirdViewbox,"Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
		viewerpage.assertEquals(preset.getZoomLevelValue(4), fourViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.performSyncONorOFF();
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_FIT); 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Test 03 (TC889): Checkpoint[6/10]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(1), firstViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(2), secondViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 
		viewerpage.assertEquals(preset.getZoomLevelValue(3), ThirdViewbox,"Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
		viewerpage.assertEquals(preset.getZoomLevelValue(4), fourViewbox, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		//Test 05 (TC891)   Verify that Zoom to Input functions properly
		preset=new ViewBoxToolPanel(driver);
		preset.changeZoomValue(1,500);
		int CurrZoom =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[7/10]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom, 500, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		//Test 06 (TC886)   	Zoom image from Active Overlay - Risk & Impact Coverage 
		String beforeWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		String beforeWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		//preset.inputWWNumber(100, 1);
	//	preset.inputWCNumber(50, 1);
		String currentWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		String currentWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 06 (TC886): Checkpoint[8/10]", "Verifying that  WW and WC values are changed " );
		viewerpage.assertNotEquals(beforeWWValueFirstViewbox, currentWWValueFirstViewbox, "Verify that the WW values are changes to new values", "WW values are not same as previous");
		viewerpage.assertNotEquals(beforeWCValueFirstViewbox, currentWCValueFirstViewbox, "Verify that the WC values  changes to new values", "WC values are not same as previous");

		//selecting 1 x 2 layout
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		//validating new layout is applied
		viewerpage.waitForElementInVisibility(viewerpage.getViewbox(3));
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT), "validating no.of canvas for Layout 1x2", "No.of canvas for layout 1x2 is:"+layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT));

		//Verifying  WW and WC values are same after layout change 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 06 (TC886): Checkpoint[9/10]", "Verifying that the WW and WC values are are same as previous after layout change" );
		viewerpage.assertNotEquals(beforeWWValueFirstViewbox, currentWWValueFirstViewbox, "Verify that the WW values are same after layout change", "WL values are same as previous after layout change");
		viewerpage.assertNotEquals(beforeWCValueFirstViewbox, currentWCValueFirstViewbox, "Verify that the WC values are same after layout change", "WC values are same as previous after layout change");

		//Verifying Zoom is same after layout change 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 06 (TC886): Checkpoint[10/10]", "Verifying that Zoom value is same as previous after layout change" );
		viewerpage.assertEquals(CurrZoom, 500, "Verify that the zoom value is same after layout change ", "Zoom vaue is remaining same as previous after layout change "); 

	} 
	
	//Limitation this test script dosn't work in edge
	@Test(groups ={"Chrome","IE11","firefox"})
	public void test05_US551_TC1657_ZoomImageFromActiveOverlay () throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Zoom image from Active Overlay");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName1,username, password, 1);
        preset=new ViewBoxToolPanel(driver);
        layout=new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1-12", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for "+" data");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomValue(50,1);
		int CurrZoom = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom, 50, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(60,1);
		int CurrZoom1 = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom1, 60, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(70,1);
		int CurrZoom2 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom2, 70, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(100,1);
		int CurrZoom3 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom3, 100, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(110,1);
		int CurrZoom4 = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom4, 110, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(120,1);
		int CurrZoom5 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom5, 120, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(130,1);
		int CurrZoom6 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom6, 130, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(140,1);
		int CurrZoom7 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom7, 140, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(150,1);
		int CurrZoom8 = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom8, 150, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(160,1);
		int CurrZoom9 =preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom9, 160, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.threeByOneLAndOneByOneRLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(170,1);
		int CurrZoom10 = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom10, 170, "Verify that the zoom is updated to new value", "Zoom is update to new value");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		preset.changeZoomValue(180,1);
		int CurrZoom11 = preset.getZoomLevelValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC891): Checkpoint[1/1]", "Verifying that the Zoom value is updated to new value after using zoom input values" );
		viewerpage.assertEquals(CurrZoom11, 180, "Verify that the zoom is updated to new value", "Zoom is update to new value");


	} 


*/