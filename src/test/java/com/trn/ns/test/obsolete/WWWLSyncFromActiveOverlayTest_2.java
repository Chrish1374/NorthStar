package com.trn.ns.test.obsolete;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I29_Viewer-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class WWWLSyncFromActiveOverlayTest_2 extends TestBase{

	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ContentSelector contentSelector;
	private ViewerTextOverlays textOverlay;
	private ViewBoxToolPanel presetMenu;
	private ViewerSliderAndFindingMenu findingMenu;

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4_patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String Anonymize_AH4_filepath=Configurations.TEST_PROPERTIES.get("Anonymize_AH.4_Filepath");
	String Anonymize_AH4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Anonymize_AH4_filepath);
	String Series1ToSelect_AH4= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Anonymize_AH4_filepath);
	String Series2ToSelect_AH4= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Anonymize_AH4_filepath);

	String filePath2 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;


	@Test(groups ={"firefox","Chrome","IE11","Edge","US229","US289","Sanity","BVT"})
	public void test01_US229_TC437_TC553_TC554_TC555_US289_TC2374_verifyWCByActiveOverlay() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Change W/L by Active Overlay of Preconfigured selection"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");

		//Loading the patient on viewer
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, username, password, 1);
		viewerPage.closingConflictMsg();
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//1. Default Overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Loading the Patient data in default overlay" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		//Step 1 - Verify that the C and W displaying default values
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying the default values of C and W" );
		viewerPage.assertEquals(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath), viewerPage.getWindowWidthValText(1), "Verify the default WW value", "WW value is set to default value");
		viewerPage.assertEquals(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath), viewerPage.getWindowCenterText(1), "Verify the default WC value", "WC value is set to default value");	

		//Step 3 - Verify that Center and width values will be set successfully to selected values and menu disappears.
		String beforeWWValue = viewerPage.getWindowWidthValText(1);
		String beforeWCValue = viewerPage.getWindowCenterText(1);

		presetMenu.selectPresetValue(1, ViewerPageConstants.HEAD);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying that selected label is Italic" );
		viewerPage.assertTrue(presetMenu.verifyPresetOptionIsSelected(1, ViewerPageConstants.HEAD), "Verify that the HEAD is displaying in Italic font", "HEAD is displaying in Italic font");

		String afterWWValue = viewerPage.getWindowWidthValText(1);
		String afterWCValue = viewerPage.getWindowCenterText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying that the Center and width values will be set successfully to selected values" );
		viewerPage.assertNotEquals(beforeWWValue, afterWWValue, "Verify that the WW values are changes to new values", "WL values are changes to new values and not same as previous");
		viewerPage.assertNotEquals(beforeWCValue, afterWCValue, "Verify that the WC values are changes to new values", "WC values are changes to new values and not same as previous");

		//Step 4 - Verify that the values will be reset to original states.
		presetMenu.selectPresetValue( 1, ViewerPageConstants.RESET);

		String afterResetWWValue = viewerPage.getWindowWidthValText(1);
		String afterResetWCValue = viewerPage.getWindowCenterText(1);

		//Verifying values after selecting RESET
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying that the WW and WC values will be reset to original states." );
		viewerPage.assertEquals(beforeWWValue, afterResetWWValue, "Verify that the WW value is RESET to original states", "WW value is RESET to original states");
		viewerPage.assertEquals(beforeWCValue, afterResetWCValue, "Verify that the WC value is RESET to original states", "WC value is RESET to original states");
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","DE263"})
	public void test03_DE263_TC1169_DE430_TC1702_verifyWWOverlayByContextMenu() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Values applied from preset do not match menu - Context Menu Coverage - For window width "
				+ "<\br> Invert menu item not working correct with synced series");
		
		//Loading the patient on viewer
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, username, password, 1);
		viewerPage.closingConflictMsg();
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//1. Default Overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in default overlay" );
		textOverlay.selectTextOverlays("default");
		String beforeWWValueFirstViewbox = viewerPage.getWindowWidthValText(1);
		String beforeWCValueFirstViewbox = viewerPage.getWindowCenterText(1);

		String beforeWWValueSecViewbox = viewerPage.getWindowWidthValText(2);
		String beforeWCValueSecViewbox = viewerPage.getWindowCenterText(2);

		//Comparing Preset image
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 1: Verify the preset menu on child window", "test03_DE263_TC1169_checkpoint1");
		viewerPage.mouseHover(viewerPage.getViewPort(1));

		//Step 1 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		List<String> wwwlPresetMenuOptions = presetMenu.getWWWLPresetOptions(1);

		for(int i =1; i<wwwlPresetMenuOptions.size()-1 ;i++){

			viewerPage.assertTrue(presetMenu.verifyWWWCValuesFromPresetContextMenuOption(1 ,wwwlPresetMenuOptions.get(i)),"Verify the WW and WC values with Preset menu for "+wwwlPresetMenuOptions.get(i)+" option","WW and WC values are same as selected");

			List<String> values = presetMenu.getWAndCValuesFromPresetOption(1,wwwlPresetMenuOptions.get(i));
			int windowWidthDiff = viewerPage.convertIntoInt(values.get(0).trim()) - viewerPage.convertIntoInt(beforeWWValueFirstViewbox);
			int windowCenterDiff = viewerPage.convertIntoInt(values.get(1).trim()) - viewerPage.convertIntoInt(beforeWCValueFirstViewbox);

			viewerPage.assertEquals((viewerPage.getValueOfWindowWidth(2)- viewerPage.convertIntoInt(beforeWWValueSecViewbox)) , windowWidthDiff, "Verify the width difference of viewbox2", "verified");		
			viewerPage.assertEquals((viewerPage.getValueOfWindowCenter(2)- viewerPage.convertIntoInt(beforeWCValueSecViewbox)) , windowCenterDiff, "Verify the center difference of viewbox2", "verified");

		}

		//Selecting Reset
		presetMenu.selectPresetValue(1, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify the WW and WC values after selecting Reset in PRESET context menu" );
		viewerPage.assertEquals(viewerPage.getWindowWidthValText(1), beforeWWValueFirstViewbox, "Verifying the WW value in first viewbox", "WW value in first viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowCenterText(1), beforeWCValueFirstViewbox, "Verifying the WC value in first viewbox", "WC value in first viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowWidthValText(2), beforeWWValueSecViewbox, "Verifying the WW value in second viewbox", "WW value in second viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowCenterText(2), beforeWCValueSecViewbox, "Verifying the WC value in second viewbox", "WC value in second viewbox is same as previous value");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify the WW and WC values after selecting INVERT in PRESET context menu" );
		presetMenu.selectPresetValue(1, ViewerPageConstants.INVERT);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying INVERT is applied on images from WW and WC values" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify that the WW values are not same after INVERT selection","test03_DE263_TC1169_checkpoint2" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify that the WW values are not same after INVERT selection","test03_DE430_TC1702_checkpoint3" );


		//Again click on INVERT
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify the WW and WC values after again selecting INVERT in PRESET context menu" );
		presetMenu.selectPresetValue(1, ViewerPageConstants.INVERT);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying INVERT is applied on images from WW and WC values" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify that the WW values are not same after INVERT selection","test03_DE263_TC1169_checkpoint3" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify that the WW values are not same after INVERT selection","test03_DE430_TC1702_checkpoint4" );


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge", "DE263","US289"})
	public void test04_DE263_TC1169_US289_TC2416_verifyWCOverlayByContextMenu() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Values applied from preset do not match menu - Context Menu Coverage- For WindowCenter");

		//Loading the patient on viewer
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, username, password, 1);
		viewerPage.closingConflictMsg();
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//1. Default Overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in default overlay" );
		textOverlay.selectTextOverlays("default");
		String beforeWWValueFirstViewbox = viewerPage.getWindowWidthValText(1);
		String beforeWCValueFirstViewbox = viewerPage.getWindowCenterText(1);
		String beforeWWValueSecViewbox = viewerPage.getWindowWidthValText(2);
		String beforeWCValueSecViewbox = viewerPage.getWindowCenterText(2);

		//Step 3 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1] & Checkpoint TC2[1] & Checkpoint[3/6]", "Verify the WW and WC values after selecting each value in PRESET context menu" );
		List<String> wwwlPresetMenuOptions = presetMenu.getWWWLPresetOptions(1);

		for(int i =1; i<wwwlPresetMenuOptions.size()-1 ;i++){

			viewerPage.assertTrue(presetMenu.verifyWWWCValuesFromPresetContextMenuOption(1 ,wwwlPresetMenuOptions.get(i)),"Verify the WW and WC values with Preset menu for "+wwwlPresetMenuOptions.get(i)+" option","WW and WC values are same as selected");

			List<String> values = presetMenu.getWAndCValuesFromPresetOption(1, wwwlPresetMenuOptions.get(i));
			int windowWidthDiff = viewerPage.convertIntoInt(values.get(0).trim()) - viewerPage.convertIntoInt(beforeWWValueFirstViewbox);
			int windowCenterDiff = viewerPage.convertIntoInt(values.get(1).trim()) - viewerPage.convertIntoInt(beforeWCValueFirstViewbox);

			viewerPage.assertEquals((viewerPage.getValueOfWindowWidth(2)- viewerPage.convertIntoInt(beforeWWValueSecViewbox)) , windowWidthDiff, "Verify the width difference of viewbox2", "verified");		
			viewerPage.assertEquals((viewerPage.getValueOfWindowCenter(2)- viewerPage.convertIntoInt(beforeWCValueSecViewbox)) , windowCenterDiff, "Verify the center difference of viewbox2", "verified");

		}

		//Selecting Reset
		presetMenu.selectPresetValue( 1, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2] & Checkpoint TC2[3] & Checkpoint TC1[2] & Checkpoint[4/6]", "Verify the WW and WC values after selecting Reset in PRESET context menu" );
		viewerPage.assertEquals(viewerPage.getWindowWidthValText(1), beforeWWValueFirstViewbox, "Verifying the WW value in first viewbox", "WW value in first viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowCenterText(1), beforeWCValueFirstViewbox, "Verifying the WC value in first viewbox", "WC value in first viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowWidthValText(2), beforeWWValueSecViewbox, "Verifying the WW value in second viewbox", "WW value in second viewbox is same as previous value");
		viewerPage.assertEquals(viewerPage.getWindowCenterText(2), beforeWCValueSecViewbox, "Verifying the WC value in second viewbox", "WC value in second viewbox is same as previous value");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC2[2] & Checkpoint[5/6]", "Verify the WW and WC values after selecting INVERT in PRESET context menu" );
		presetMenu.selectPresetValue( 1, ViewerPageConstants.INVERT);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying INVERT is applied on images from WW and WC values" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify that the WW values are not same after INVERT selection","test04_DE263_TC1169_checkpoint1" );

		//Again click on INVERT
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify the WW and WC values after again selecting INVERT in PRESET context menu" );
		presetMenu.selectPresetValue( 1, ViewerPageConstants.INVERT);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying INVERT is applied on images from WW and WC values" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify that the WW values are not same after INVERT selection","test04_DE263_TC1169_checkpoint2" );

		viewerPage.performSyncONorOFF();

		for(int i =1; i<wwwlPresetMenuOptions.size()-1 ;i++){

			viewerPage.assertTrue(presetMenu.verifyWWWCValuesFromPresetContextMenuOption(1 ,wwwlPresetMenuOptions.get(i)),"Checkpoint TC3[3] &Verify the WW and WC values with Preset menu for "+wwwlPresetMenuOptions.get(i)+" option","WW and WC values are same as selected");
			viewerPage.assertFalse(presetMenu.compareWindowWidthAndCenter(1, viewerPage.getWindowWidthValText(2), viewerPage.getWindowCenterText(2)), "Checkpoint TC3[3] &Verify the WW and WC values for sync series for "+wwwlPresetMenuOptions.get(i)+" option", "WW and WC values are same for sync series");

		}
		viewerPage.performSyncONorOFF();
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge", "DE263"})
	public void test05_DE263_TC1170_verifyToolCoverage() throws InterruptedException, TimeoutException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Values applied from preset do not match menu - Context Menu Coverage");
		//Loading the patient on viewer

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+liver9Patient+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9Patient, username, password, 1);
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);
		//viewerpage.waitForPdfToRenderInViewbox(3);

		//1. Default Overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Loading the Patient data in default overlay" );
		textOverlay.selectTextOverlays("default");
		String beforeWWValue = viewerPage.getWindowWidthValText(1);
		String beforeWCValue = viewerPage.getWindowCenterText(1);

		//Step 1
		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Performing the right click and enabling the WWWL from Radial Menu for first series" );
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling
		viewerPage.dragAndReleaseOnViewer(10, 10, 100, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify the WW and WC values after Window leveling operation");
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValText(1), beforeWWValue, "Verifying that WW value is changed after applying Window leveling operation", "WW value is changed successfully");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterText(1), beforeWCValue, "Verifying that WC value is changed after applying Window leveling operation", "WC value is changed successfully");
		//disable WWWL icon 
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		//Step 2
		//Window level on other series
		String beforeWWValueSecViewbox = viewerPage.getWindowWidthValText(2);
		String beforeWCValueSecViewbox = viewerPage.getWindowCenterText(2);

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Performing the right click and enabling the WWWL from Radial Menu for second series");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(2));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify WWWL icon is selected and applying the window leveling in second viewbox");
		//Applying the window leveling
		viewerPage.dragAndReleaseOnViewer(20, 20, 150, -80);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify the WW and WC values after Window leveling operation");
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValText(2), beforeWWValueSecViewbox, "Verifying that WW value is changed after applying Window leveling operation", "WW value is changed successfully");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterText(2), beforeWCValueSecViewbox, "Verifying that WC value is changed after applying Window leveling operation", "WC value is changed successfully");
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","DE263","US289"})
	public void test07_US289_TC2375_verifyWCByActiveOverlaySameOrientation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having different FrameReferenceUID and same Orientation.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+ah4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password, 1);
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//1. Default Overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Loading the Patient data in default overlay" );
		textOverlay.selectTextOverlays("default");
		//Step 1 - Verify that the C and W displaying default values
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying the default values of C and W" );
		viewerPage.assertEquals(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath), viewerPage.getWindowWidthValText(1), "Verify the default WW value", "WW value is set to default value");
		viewerPage.assertEquals(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath), viewerPage.getWindowCenterText(1), "Verify the default WC value", "WC value is set to default value");	

	
		//Step 3 - Verify that Center and width values will be set successfully to selected values and menu disappears.
		String beforeWWValue = viewerPage.getWindowWidthValText(1);
		String beforeWCValue = viewerPage.getWindowCenterText(1);

		presetMenu.selectPresetValue( 1, ViewerPageConstants.HEAD);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying that selected label is Italic" );
		viewerPage.assertTrue(presetMenu.verifyPresetOptionIsSelected(1, ViewerPageConstants.HEAD), "Verify that the HEAD is displaying in Italic font", "HEAD is displaying in Italic font");


		String afterWWValue = viewerPage.getWindowWidthValText(1);
		String afterWCValue = viewerPage.getWindowCenterText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying that the Center and width values will be set successfully to selected values" );
		viewerPage.assertNotEquals(beforeWWValue, afterWWValue, "Verify that the WW values are changes to new values", "WL values are changes to new values and not same as previous");
		viewerPage.assertNotEquals(beforeWCValue, afterWCValue, "Verify that the WC values are changes to new values", "WC values are changes to new values and not same as previous");

		//Step 4 - Verify that the values will be reset to original states.
		presetMenu.selectPresetValue( 1, ViewerPageConstants.RESET);

		String afterResetWWValue = viewerPage.getWindowWidthValText(1);
		String afterResetWCValue = viewerPage.getWindowCenterText(1);

		//Verifying values after selecting RESET
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying that the WW and WC values will be reset to original states." );
		viewerPage.assertEquals(beforeWWValue, afterResetWWValue, "Verify that the WW value is RESET to original states", "WW value is RESET to original states");
		viewerPage.assertEquals(beforeWCValue, afterResetWCValue, "Verify that the WC value is RESET to original states", "WC value is RESET to original states");
	}

	@Test(groups ={"Chrome","IE11","Edge", "US1080","Positive"})
	public void test10_US1080_TC5041_verifyWWWCEditUsingRadialBarWindowLevelOption() throws InterruptedException, SQLException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify-Viewer applies the relative WW/WL value to all the images when user edits WW/WL level values (edit using Radial tool bar option)");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_FALSE);		
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);

		viewerPage.scrollToImage(1,1);
		int windowWidth=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int windowCenter=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify window center and Window width value for each slice in the loaded series" );
		verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth,windowCenter,0,0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify window center and Window width value when window leveling option is used");
		viewerPage.scrollToImage(1,1);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -100,-100);
		viewerPage.waitForEndOfAllAjaxes();

		int WWAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int WCAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));

		int diffWidth=WWAfterDrag-windowWidth;
		int diffCenter=WCAfterDrag-windowCenter;
		viewerPage.scrollToImage(1,1);
		verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth,windowCenter,diffWidth,diffCenter);
	}

	@Test(groups ={"Chrome","IE11","Edge", "US1080","Positive"})
	public void test12_US1080_TC5043_TC5135_verifyWWWCSyncWhenDefaultSyncOn() throws SQLException, IOException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify-Viewer applies the relative WW/WL value to all the images when user edits WW/WL level values (edit using Radial tool bar option) - Sync turned on");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);		
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);

		viewerPage.scrollToImage(1,1);
		viewerPage.click(viewerPage.getViewPort(1));

		int windowWidth1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int windowCenter1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify window center and Window width value for each slice in the loaded series for viewbox1" );
		verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth1,windowCenter1,0,0);

		viewerPage.scrollToImage(1,1);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -100,-100);

		int WWAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int WCAfterDrag=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));

		int diffWidth=WWAfterDrag-windowWidth1;
		int diffCenter=WCAfterDrag-windowCenter1;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify window center and Window width value after using window leveling option from radial menu");
		verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(1,maxScrollPos,windowWidth1,windowCenter1,diffWidth,diffCenter);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify window center and Window width value by manually entering the slice number");
		verifyWindowWidthAndCenterForAllSlicesByEnteringvalue(1,maxScrollPos,windowWidth1,windowCenter1,diffWidth,diffCenter);
	}

	@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
	public void test15_US1081_TC5150_TC5136_verifyAbsoluteWWWCSyncUsingResetPresetOptionWhenDefaultSyncOn() throws SQLException, IOException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Absolute and sync is on we reset the WW/WL Values to original values. <br>"+
				"Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Absolute and we use WW/WL preset option");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
		
        presetMenu=new ViewBoxToolPanel(driver);
		viewerPage.scrollToImage(1,1);

		int windowWidth1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int windowCenter1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify window center and Window width value for each slice in the loaded series in viewbox1 and viewbox2" );
		verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,windowWidth1,windowCenter1);
		//preset value 
		viewerPage.scrollToImage(1,1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.HEAD);

		int WWAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int WCAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));

		//verify WWWC after preset value
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify window center and Window width value in viewbox1 and viewbox2 after using preset option from radial menu");
		verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,WWAfterPreset,WCAfterPreset);

		//reset value to original and verify WWWC after reset to original value
		viewerPage.scrollToImage(1,1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify window center and Window width value in viewbox1 and viewbox2 after using preset option from radial menu");
		verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(1,2,maxScrollPos,windowWidth1,windowCenter1);
	}

	@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
	public void test16_US1081_TC5151_TC5137_verifyRelativeWWWCSyncUsingResetPresetOptionWhenDefaultSyncOn() throws SQLException, IOException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Relative and sync is on we reset the WW/WL Values to original values.<BR>"+
				"Verify WW/WL synchronization when DefaultWindowLevelSyncMode is Relative and we use WW/WL preset option");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
		
		presetMenu=new ViewBoxToolPanel(driver);
		viewerPage.scrollToImage(1,1);
		int windowWidth1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int windowCenter1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
		int windowWidth2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(2)));
		int windowCenter2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(2)));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify window center and Window width value for each slice in the loaded series in viewbox1 and viewbox2" );
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);

		viewerPage.scrollToImage(1,1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.HEAD);

		int WWAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int WCAfterPreset=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int diffWidth=WWAfterPreset-windowWidth1;
		int diffCenter=WCAfterPreset-windowCenter1;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify window center and Window width value in viewbox1 and viewbox2 after changing value using preset option");
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,diffWidth,diffCenter);

		viewerPage.scrollToImage(1,1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify window center and Window width value in viewbox1 and viewbox2 after changing value using reset to original option");
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);
	}  

	@Test(groups ={"Chrome","IE11","Edge", "US1081","Positive"})
	public void test17_US1081_TC5139_verifyRelativeWWWCSyncWhenDefaultSyncOnOff() throws SQLException, IOException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL synchronization when DefaultWindowLevelSyncMode is relative and we turn on and off synchronization");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
		db.resetIISPostDBChanges();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Anonymize_AH4_patientName, username, password, 1);
		presetMenu=new ViewBoxToolPanel(driver);

		viewerPage.scrollToImage(1,1);
		int windowWidth1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int windowCenter1=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int maxScrollPos=viewerPage.getMaxNumberofScrollForViewbox(1);
		int windowWidth2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(2)));
		int windowCenter2=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(2)));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify window center and Window width value for each slice in the loaded series in viewbox1 and viewbox2" );
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.scrollToImage(1,1);

		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 50 , 50);

		int WWAfterEdit=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(1)));
		int WCAfterEdit=viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(1)));
		int diffWidth=WWAfterEdit-windowWidth1;
		int diffCenter=WCAfterEdit-windowCenter1;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify window center and Window width value from viewbox1 and viewbox2 after using window leveling option from radial menu");
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,diffWidth,diffCenter);

		db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_FALSE);
		db.resetIISPostDBChanges();

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.scrollToImage(1,1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify window center and Window width value from viewbox1 and viewbox2 after using preset option ");
		verifyWindowWidthAndCenterWhenSyncModeOnForRelative(1,2,maxScrollPos,windowWidth1,windowCenter1,windowWidth2,windowCenter2,0,0);

	}  

	@Test(groups ={"Chrome","IE11","Edge", "DE1426","Negative","BVT"})
	public void test18_DE1426_TC5740_verifyWWWCWhenSameSeriesLoadedOnViewer() throws SQLException, IOException, InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WLWC values when same series is loaded into viewer when it already exists");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+Anonymize_AH4_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password, 1);
		
		contentSelector=new ContentSelector(driver);
		viewerPage.closingConflictMsg();
		presetMenu=new ViewBoxToolPanel(driver);

		String seriesToSelect=viewerPage.getSeriesDescriptionOverlayText(1);
		presetMenu.selectPresetValue(1, ViewerPageConstants.HEAD);
		List<String> wwwlPresetMenuOptions = presetMenu.getWWWLPresetOptions(1);
		List<String> values = presetMenu.getWAndCValuesFromPresetOption(1,wwwlPresetMenuOptions.get(1));

		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), values.get(0).trim(),"Checkpoint[1/4]", "Verified WW value visible on UI with Preset WW value of Head in first viewbox");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), values.get(1).trim(), "Checkpoint[2/4]","Verified WC value visible on UI with Preset WC value of Head in first viewbox");

		contentSelector.selectSeriesFromSeriesTab(2, seriesToSelect);
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), viewerPage.getWindowWidthValueOverlayText(1),"Checkpoint[3/4]", "Verified WW value for the same series loaded on two different viewboxes");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), viewerPage.getWindowWidthValueOverlayText(1), "Checkpoint[4/4]", "Verified WC value for the same series loaded on two different viewboxes");


	}

	@Test(groups ={"Chrome","IE11","Edge","US1410","Positive"})
	public void test19_US1410_TC7586_verifyShortcutsForOverlaysWhenWWWCHasExactValues() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the selection of the WW/WL preset values using keyboard shortcuts.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9Patient+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9Patient, username, password, 1);

		presetMenu=new ViewBoxToolPanel(driver);
		Map<Integer,String> wwwlPresetMenuOptions = presetMenu.getWWWLOverlayOptionsAndShortCuts(1);
		List<Integer> keys = wwwlPresetMenuOptions.keySet().stream().collect(Collectors.toList());

		String originalWindowWidthValue = (viewerPage.getWindowWidthValText(1));
		String originalWindowCenterValue = (viewerPage.getWindowCenterText(1));


		for(int i =0 ;i<keys.size();i++) {			
			viewerPage.pressKeys(viewerPage.getNumberKeys(keys.get(i)));
			for(int j =1;j<=viewerPage.getNumberOfCanvasForLayout();j++) {
				if(!(wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.RESET) ||wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.INVERT)) ) {
					viewerPage.assertEquals(presetMenu.getWAndCValuesFromPresetOption(1,wwwlPresetMenuOptions.get(keys.get(i))).get(0).trim(),viewerPage.getWindowWidthValText(j),"Checkpoint[2/9]","Verifying the window width value after pressing shortcut");
					viewerPage.assertEquals(presetMenu.getWAndCValuesFromPresetOption(1, wwwlPresetMenuOptions.get(keys.get(i))).get(1).trim(),viewerPage.getWindowCenterText(j),"Checkpoint[3/9]","Verifying the window center value after pressing shortcut");
				}else if(wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.RESET))
				{

					viewerPage.assertEquals(viewerPage.getWindowWidthValText(j),originalWindowWidthValue,"Checkpoint[4/9]","Verifying the window width value after pressing shortcut");
					viewerPage.assertEquals(viewerPage.getWindowCenterText(j),originalWindowCenterValue,"Checkpoint[5/9]","Verifying the window center value after pressing shortcut");


				}

				viewerPage.assertTrue(presetMenu.verifyPresetOptionIsSelected(1,wwwlPresetMenuOptions.get(keys.get(i))),"Checkpoint[6/9]", "Verify that option is displaying in Italic font");
			
			}

		}

		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1410","Positive"})
	public void test20_US1410_TC7585_verifyShortcutsForOverlaysWhenWWWCHasNoExactValues() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the short cut keys assigned to WW/WL preset values.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Loading the Patient "+mrLSPPatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(mrLSPPatientName, username, password, 1);	
		presetMenu=new ViewBoxToolPanel(driver);

		Map<Integer,String> wwwlPresetMenuOptions = presetMenu.getWWWLOverlayOptionsAndShortCuts(1);
		
		List<Integer> keys = wwwlPresetMenuOptions.keySet().stream().collect(Collectors.toList());

		List<List<Integer>> wwwcDiff = new ArrayList<List<Integer>>();

		String originalWindowWidthValue = (viewerPage.getWindowWidthValText(1));
		String originalWindowCenterValue = (viewerPage.getWindowCenterText(1));

		for(int j =1;j<=viewerPage.getNumberOfCanvasForLayout();j++) {

			List<Integer> difference = new ArrayList<Integer>();

			difference.add(viewerPage.convertIntoInt(originalWindowWidthValue)-viewerPage.getValueOfWindowWidth(j));
			difference.add(viewerPage.convertIntoInt(originalWindowCenterValue)-viewerPage.getValueOfWindowCenter(j));

			wwwcDiff.add(difference);

		}	

		for(int i =0 ;i<keys.size();i++) {			

			viewerPage.pressKeys(viewerPage.getNumberKeys(keys.get(i)));

			for(int j =1;j<=viewerPage.getNumberOfCanvasForLayout();j++) {
				if(!(wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.RESET) ||wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.INVERT)) ) {


					viewerPage.assertEquals(viewerPage.convertIntoInt(presetMenu.getWAndCValuesFromPresetOption(1, wwwlPresetMenuOptions.get(keys.get(i))).get(0).trim())-viewerPage.getValueOfWindowWidth(j),wwwcDiff.get(j-1).get(0),"Checkpoint[2/9]","Verifying window width difference when shortcut is pressed");
					viewerPage.assertEquals(viewerPage.convertIntoInt(presetMenu.getWAndCValuesFromPresetOption(1, wwwlPresetMenuOptions.get(keys.get(i))).get(1).trim())-viewerPage.getValueOfWindowCenter(j),wwwcDiff.get(j-1).get(1),"Checkpoint[3/9]","Verifying window center difference when shortcut is pressed");
				}else if(wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.RESET))
				{

					viewerPage.assertEquals((viewerPage.getValueOfWindowWidth(1)-viewerPage.getValueOfWindowWidth(j)),wwwcDiff.get(j-1).get(0),"Checkpoint[4/9]","Verifying window width difference when shortcut is pressed");
					viewerPage.assertEquals((viewerPage.getValueOfWindowCenter(1)-viewerPage.getValueOfWindowCenter(j)),wwwcDiff.get(j-1).get(1),"Checkpoint[5/9]","Verifying window center difference when shortcut is pressed");

				}

				if(j<viewerPage.getNumberOfCanvasForLayout()) {
					if(!(wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.RESET) ||wwwlPresetMenuOptions.get(keys.get(i)).contains(ViewerPageConstants.INVERT))){
						viewerPage.assertFalse(presetMenu.verifyPresetOptionIsSelected(1, wwwlPresetMenuOptions.get(keys.get(i))), "Checkpoint[6/9]","Verify that option is not displaying in Italic font if values are not same as preset");
					}else
					{
						viewerPage.assertTrue(presetMenu.verifyPresetOptionIsSelected(1, wwwlPresetMenuOptions.get(keys.get(i))), "Checkpoint[8/9]","Verify that the HEAD is displaying in Italic font if values are  same as preset");
					}
				}

			}

		}


	}

	public void verifyWindowWidthAndCenterForAllSlicesByEnteringvalue(int viewbox,int maxScrollPos,int windowWidth,int WindowCenter,int diffWidth,int diffCenter) throws InterruptedException
	{
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.scrollToImage(i+1,viewbox);
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))),windowWidth+i+diffWidth, "Verify window width for slice"+" "+viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))), "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))),WindowCenter+i+diffCenter, "Verify window center for slice"+" "+viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))), "Verified");

		}
	}

	public void verifyWindowWidthAndCenterForAllSlicesUsingScrollBar(int viewbox,int maxScrollPos,int windowWidth,int WindowCenter,int diffWidth,int diffCenter) throws InterruptedException
	{
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))),windowWidth+i+diffWidth, "Verify window width for slice"+" "+viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))), "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))),WindowCenter+i+diffCenter, "Verify window center for slice"+" "+ viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))), "Verified");
			findingMenu.scrollTheSlicesUsingSlider(viewbox, 0, 0, 0, 20);

		}
	}

	public void verifyWindowWidthAndCenterForAllSlicesUsingMouseWheel(int viewbox,int maxScrollPos,int windowWidth,int WindowCenter,int diffWidth,int diffCenter) throws InterruptedException
	{
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))),windowWidth-i+diffWidth, "Verify window width for slice is"+" "+viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox))), "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))),WindowCenter-i+diffCenter, "Verify window center for slice viewbox is"+" "+viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox))), "Verified");
			viewerPage.mouseWheelScrollInViewer(viewbox, NSGenericConstants.SCROLL_DOWN, 1);

		}
	}

	//pass first viewbox number which having different WWWC for each slice and  second viewbox number which having same WWWC for each slice
	public void verifyWindowWidthAndCenterWhenSyncModeOnForRelative(int viewbox1,int viewbox2,int maxScrollPos,int windowWidth1,int WindowCenter1,int windowWidth2,int WindowCenter2,int diffWidth,int diffCenter) throws InterruptedException
	{
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.assertEquals((viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox1)))-windowWidth1),diffWidth+i, "Verify window width in first viewbox for slice is decreased/increased by"+" "+diffCenter+i, "Verified");
			viewerPage.assertEquals((viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox1)))-WindowCenter1),diffCenter+i, "Verify window center in first viewbox for slice is decreased/increased by"+" "+diffCenter+i, "Verified");
			viewerPage.assertEquals((viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox2)))-windowWidth2),diffWidth, "Verify window width in second viewbox for slice is decreased/increased by"+" "+diffCenter, "Verified");
			viewerPage.assertEquals((viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox2)))-WindowCenter2),diffCenter, "Verify window center in second viewbox for slice is decreased/increased by"+" "+diffCenter, "Verified");
			viewerPage.mouseWheelScrollInViewer(viewbox1, NSGenericConstants.SCROLL_DOWN, 1);

		}
	}

	//WWWC are different for all slice
	public void verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(int viewbox1,int viewbox2,int maxScrollPos,int windowWidth,int WindowCenter) throws InterruptedException
	{
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox1))),windowWidth+i, "Verify window width in first viewbox for slice is decreased/increased by "+" "+windowWidth+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox1))),WindowCenter+i, "Verify window center in first viewbox for slice is decreased/increased by"+" "+WindowCenter+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox2))),windowWidth+i, "Verify window width in second viewbox for slice is decreased/increased by"+" "+windowWidth+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox2))),WindowCenter+i, "Verify window center in second viewbox for slice is decreased/increased by "+" "+WindowCenter+i, "Verified");
			viewerPage.mouseWheelScrollInViewer(viewbox1, "down", 1);

		}
	}

	//WWWC are same for all slice
	public void verifyWindowWidthAndCenterWhenSyncModeOnForAbsolute(int viewbox1,int viewbox2,int maxScrollPos,int windowWidth,int WindowCenter,int diffWW,int diffWC) throws InterruptedException
	{
		for(int i=0;i<maxScrollPos;i++)
		{
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox1))),windowWidth, "Verify window width in first viewbox for slice is decreased/increased by "+" "+windowWidth+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox1))),WindowCenter, "Verify window center in first viewbox for slice is decreased/increased by"+" "+WindowCenter+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowWidthValueOverlay(viewbox2))),windowWidth, "Verify window width in second viewbox for slice is decreased/increased by"+" "+windowWidth+i, "Verified");
			viewerPage.assertEquals(viewerPage.convertIntoInt(viewerPage.getText(viewerPage.getWindowCenterValueOverlay(viewbox2))),WindowCenter, "Verify window center in second viewbox for slice is decreased/increased by "+" "+WindowCenter+i, "Verified");
			viewerPage.mouseWheelScrollInViewer(viewbox1, "down", 1);

		}
	}


	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {

		DatabaseMethods db = new DatabaseMethods(driver);
		if(!db.getWWWLDefaultSyncMode().equalsIgnoreCase(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE)) {			
			db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
			db.resetIISPostDBChanges();
		}
		
		if(!db.getDefaultSyncMode()) {			
			db.updateConfigTable(NSDBDatabaseConstants.DEFAULT_SYNC_MODE,NSGenericConstants.BOOLEAN_TRUE);	
			db.resetIISPostDBChanges();

		}
				
		
	}
}
