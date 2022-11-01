package com.trn.ns.test.viewer.layout;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LayoutChangeTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector contentSelector;
	private HelperClass helper;
	private ViewerLayout layout;

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);	
	String filePath2 = Configurations.TEST_PROPERTIES.get("AI_Change_MS");
	String AIChangeMs = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);	
	String filePath3 = Configurations.TEST_PROPERTIES.get("AI_Change_Tumor");
	String AIChangeTumor = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);	
	String filePath4 = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String brainTDA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	String filePath5 = Configurations.TEST_PROPERTIES.get("MR_CARDIAC_filepath");
	String MrCardiac = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);


	String liverSeries1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath1);
	String liverSeries2=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath1);
	String liverSeries3=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath1);
	String liverSeries4=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath1);
	String liverSeries5=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath1);

	String MSResult1= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);
	String MSResult2= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath2);
	String MSResult3= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY, filePath2);
	String MSResult4= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT04_TEXTOVERLAY, filePath2);
	String MSResult5= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT05_TEXTOVERLAY, filePath2);
	String MSResult6= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT06_TEXTOVERLAY, filePath2);

	String TumorSeries1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY,filePath3);
	String TumorSeries2=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY,filePath3);
	String TumorSeries3=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY,filePath3);
	String TumorSeries4=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY,filePath3);
	String TumorSeries5=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY,filePath3);
	String TumorSeries6=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES06_TEXTOVERLAY,filePath3);
	String TumorSeries7=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES07_TEXTOVERLAY,filePath3);
	String TumorSeries8=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES08_TEXTOVERLAY,filePath3);
	String TumorSeries9=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES09_TEXTOVERLAY,filePath3);

	String BTdaSeries2=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath4);
	String BTdaSeries5=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT05_TEXTOVERLAY, filePath4);


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		
	}

/*
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US789","Positive"})
	public void test01_US789_TC3032_TC3036_TC3037_TC3038_TC3040_TC3043_ChangeLayoutOneUpClientSideLogic() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verification with change layout One Up and user manual selection"+"<br>Verify with One up and change to same content"+"<br>Verify with 1*1 up not oneup and change to same content"+"<br>Verify layout with auto fill series"+"<br>Verify with Layout memory for same series"
				+"<br>Verification with moving the index in series");

		helper = new HelperClass(driver);
		
		viewerpage = helper.loadViewerDirectly(liver9, 1);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		//Steps 1 Verify with the series changes from content selector.

		// Verifying the Default layout for Liver 9 Data.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/17]", "verify the series are appearing as per the client logic for liver 9 Data on Layout and series change combinations");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "verifying number of rows of layout for liver 9 data", "Number of rows for Layout is 2");

		// Changing the series from the content selector and uploading 5,6,7,8 series

		// Select Original Patient series in original Patient Data series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/17]", "Validating series 5,6,7,8 are displaying in 1st,2nd,3rd,4th viewbox after selecting from content selector");
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries5);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		// Select Original Patient series in original Patient Data series
		System.out.println(liverSeries6);
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries6);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		// Select Result series in ViewBox containing a original Patient series
		contentSelector.selectSeriesFromSeriesTab(3,liverSeries7);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");
		// Select Original Patient series in original Patient Data series
		contentSelector.selectSeriesFromSeriesTab(4,liverSeries8);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8 ), "Verifying series displayed on 4th viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		// One up on 4th view box
		viewerpage.doubleClickOnViewbox(4);
		// Change the Layout into 2*1 and  Verify series 7 and 8 are loaded.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/17]","Validating series 7,8 are displaying after changing layout to 2*1");
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 1st viewbox is higlighted on content selector", "Verified result displayed on 1st viewbox , Series 7 is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 2nd viewbox is higlighted on content selector", "Verified result displayed on 2nd viewbox, Series 8 is higlighted on content selector");

		// Changing series to 1 and 2 series in 2*1 Layout
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries1);
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries2);

		// Change the Layout into 2*2 and  Verify series 5,6,1,2 and are loaded.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/17]","Validating series 5,6,1,2 are displaying after changing layout to 2*2");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying result displayed on 1st viewbox is higlighted on content selector", "Verified result displayed on 1st viewbox, Series 5 is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying result displayed on 2nd viewbox is higlighted on content selector", "Verified result displayed on 2nd viewbox, Series 6 is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 1 is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 2 is higlighted on content selector");


		// Steps 2   Verify with change layout to 1-up 

		// Verifying the default layout for Liver 9 data.
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "verifying number of rows of layout for Liver 9 data", "Number of rows for Layout is 2");
		//  Manually changing the Layout into 1*1 on 3rd viewBox
		viewerpage.doubleClickOnViewbox(3);
		// Change Layout to 2*2 Again.
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		// Verifying the Layout in in 2*2 Now
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/17]","Validating row count for 2*2 layout for liver9 data");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "verifying number of rows of layout for Liver 9 data", "Number of rows for Layout is 2");

		//Implementation of TC3036

		//Going back to Patient list page and again loading the Viewer page for Liver 9 data
		helper.browserBackAndReloadViewer(liver9, 1, 1);
		
		viewerpage.waitForPatientToLoad(1);
		// Verifying the Default layout for Liver 9 Data.
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "verifying number of rows of layout for Liver 9 data", "Number of rows for Layout is 2");
		// Changing the series from the content selector and uploading 5,6,7,8 series
		// Select Original 5th Patient series in 1st view box
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries5);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is  higlighted on content selector");
		// Select Original 6th Patient series in 2nd view box
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries6);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 6 is higlighted on content selector");
		// Select Original 7th Patient series in 3rd view box
		contentSelector.selectSeriesFromSeriesTab(3,liverSeries7);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 7 is higlighted on content selector");
		// Select Original 8th Patient series in 4th view box
		contentSelector.selectSeriesFromSeriesTab(4,liverSeries8);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 8 is higlighted on content selector");
		// One up on 1st view box and verify that series 5 is loaded
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1stviewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is higlighted on content selector");
		// Changing the Series in 1st view box from 5th series to 8th series.
		contentSelector.selectSeriesFromSeriesTab(3,liverSeries8);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 1st viewbox is higlighted on content selector", "Verified result displayed on 1st viewbox, Series 8 is higlighted on content selector");
		// Changing Layout to 2*2 again
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/17]","Validating series 8,7,7,8 are displaying after changing layout to 2*2");
		// Verifying Original 8th Patient series in 1st view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 8 is  higlighted on content selector");
		// Verifying Original 6th Patient series in 2nd view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 6 is higlighted on content selector");
		// Verifying Original 7th Patient series in 3rd view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 7 is higlighted on content selector");
		// Verifying Original 8th Patient series in 4th view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 8 is higlighted on content selector");

		//Implementation of TC3037


		//Going back to Patient list page and again loading the Viewer page for Liver 9 data
		helper.browserBackAndReloadViewer(liver9, 1, 1);
		viewerpage.waitForPatientToLoad(1);
		// Verifying the Default layout for Liver 9 Data.
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "verifying number of rows of layout for liver 9 data", "Number of rows for Layout is 2");
		// Changing the series from the content selector and uploading 5,6,7,8 series
		// Select Original 5th Patient series in 1st view box
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries5);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is  higlighted on content selector");
		// Select Original 6th Patient series in 2nd view box
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries6);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 6 is higlighted on content selector");
		// Select Original 7th Patient series in 3rd view box
		contentSelector.selectSeriesFromSeriesTab(3,liverSeries7);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 7 is higlighted on content selector");
		// Select Original 8th Patient series in 4th view box
		contentSelector.selectSeriesFromSeriesTab(4,liverSeries8);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 8 is higlighted on content selector");
		// Changing layout into 1*1 and verify that series 5 is loaded
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 1stviewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is higlighted on content selector");
		// Changing the Series in 1st view box from 5th series to 8th series.
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries8);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 1st viewbox is higlighted on content selector", "Verified result displayed on 1st viewbox, Series 8 is higlighted on content selector");
		// Changing Layout to 2*2 again
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/17]","Validating series 8,6,7,8 are displaying after changing layout to 2*2");
		// Verifying Original 8th Patient series in 1st view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 8 is  higlighted on content selector");
		// Verifying Original 6th Patient series in 2nd view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 6 is higlighted on content selector");
		// Verifying Original 7th Patient series in 3rd view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 7 is higlighted on content selector");
		// Verifying Original 8th Patient series in 4th view box
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 8 is higlighted on content selector");

		// Implementation of TC3038

		//Going back to Patient list page and again loading the Viewer page for Liver 9 data
		helper.browserBackAndReloadViewer(liver9, 1, 1);
		viewerpage.waitForPatientToLoad(1);
		// Changing Layout to 3*3 again
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/17]","Validating series 1,2,3,4,5,6,7,8,9 are displaying after changing layout to 3*3");
		// Verifying 1st Patient series in 1st view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// Verifying 2nd Patient series in 2nd view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 2 is higlighted on content selector");
		// Verifying 3rd Patient series in 3rd view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 3 is higlighted on content selector");
		// Verifying 4th Patient series in 4th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries4), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 4 is higlighted on content selector");
		// Verifying 5th series in 5th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 5th viewbox is higlighted on content selector", "Verified series displayed on 5th viewbox, Series 5 is higlighted on content selector");
		// Verifying 6th series in 6th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries6), "Verifying result displayed on 6th viewbox is higlighted on content selector", "Verified result displayed on 6th viewbox, Series 6 is higlighted on content selector");
		// Verifying 7th series in 7th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries7), "Verifying result displayed on 7th viewbox is higlighted on content selector", "Verified result displayed on 7th viewbox, Series 7 is higlighted on content selector");
		// Verifying 8th series in 8th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries8), "Verifying result displayed on 8th viewbox is higlighted on content selector", "Verified result displayed on 8th viewbox, Series 8 is higlighted on content selector");
		// Verifying 9th series in 9th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries9), "Verifying result displayed on 9th viewbox is higlighted on content selector", "Verified result displayed on 9th viewbox, Series 9 is higlighted on content selector");
		// Changing Layout to 2*2 again
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/17]","Validating series 1,2,3,4 are displaying after changing layout to 2*2");
		// Verifying 1st Patient series in 1st view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// Verifying 2nd Patient series in 2nd view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 2 is higlighted on content selector");
		// Verifying 3rd Patient series in 3rd view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 3 is higlighted on content selector");
		// Verifying 4th Patient series in 4th view box is loaded
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries4), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 4 is higlighted on content selector");


		//Implementation of TC3040
		//Going back to Patient list page and again loading the Viewer page for Liver 9 data
		helper.browserBackAndReloadViewer(liver9, 1, 1);
		viewerpage.waitForPatientToLoad(1);
		// Changing the series from the content selector and uploading 1,1,1,1 series
		// Select Original 1stPatient series in 1st view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[10/17]","Validating series 1 is displaying in all 1st,2nd,3rd,4th view box.");
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// Select Original 2nd Patient series in 2nd view box
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 1 is higlighted on content selector");
		// Select Original 3rd Patient series in 3rd view box
		contentSelector.selectSeriesFromSeriesTab(3,liverSeries1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying result displayed on 3rd viewbox is higlighted on content selector", "Verified result displayed on 3rd viewbox, Series 1 is higlighted on content selector");
		// Select Original 4th Patient series in 4th view box
		contentSelector.selectSeriesFromSeriesTab(4,liverSeries1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying result displayed on 4th viewbox is higlighted on content selector", "Verified result displayed on 4th viewbox, Series 1 is higlighted on content selector");
		// Changing Layout to 1*2 again
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		// Verifying 1stPatient series in 1st view box loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[11/17]","Validating series 1 is displaying in 1st view box after changing layout to 1*2");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath1)), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// Changing Layout to 2*2 again
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		// Verifying 1stPatient series in 1st, 2nd, 3r&4th view box loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[12/17]","Validating series 1 is displaying 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 3rd viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 4th viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// One up on 3rd View box
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "verifying number of rows of layout for Liver 9 data", "Number of rows for Layout is 1");
		// Changing the series from the content selector and uploading 1,1,1,1 series
		// Select Original 5th patient series in 1st view box
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries5);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon( liverSeries5), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is  higlighted on content selector");
		// Changing Layout to 2*2 again
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		// Verifying 1stPatient series in 1st, 2nd, 3r&4th view box loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[13/17]","Validating series 1,1,5,1 is displaying in 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries5), "Verifying series displayed on 3rd viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 5 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 4th viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");

		// Implementation of TC3043
		helper.browserBackAndReloadViewer(liver9, 1, 1);
		viewerpage.waitForPatientToLoad(1);
		// Apply One up on last view box. and Verify 4th series is loaded in view box.

		viewerpage.doubleClickOnViewbox(4);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries4), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 4 is  higlighted on content selector");
		// Change Layout to 2*1 and Verify that series Displayed is 3 and 4.
		layout.selectLayout(layout.twoByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[14/17]","Validating series 3&4 is displaying 2*1 layout.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 3 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries4), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 4 is  higlighted on content selector");
		// Change the 4th series to 1st series in 2*1 Layout
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 3 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 1 is  higlighted on content selector");
		// One up on 1st View box and Verify that series 3rd is loaded.
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 3 is  higlighted on content selector");
		// Change Layout to 2*1 and Verify that series Displayed is 2 and 3.
		layout.selectLayout(layout.twoByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[15/17]","Validating series 2,3 is displaying in 2*1 layout.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 2 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 3 is  higlighted on content selector");
		// Change 3 series to 2 series and user should see 2, 2 series in both the view box.
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries2);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 2 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 2 is  higlighted on content selector");		
		// One up on the first view box
		viewerpage.doubleClickOnViewbox(1);
		// Change Layout to 2*1 and Verify that series Displayed is 1 and 2.
		layout.selectLayout(layout.twoByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[16/17]","Validating series 1,2 is displaying in 2*1 layout.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 2 is  higlighted on content selector");
		// Change the series 2 to 3
		contentSelector.selectSeriesFromSeriesTab(2,liverSeries3);
		// One up on the first view box
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 1 is  higlighted on content selector");
		// Change 1st series into 4th series
		contentSelector.selectSeriesFromSeriesTab(1,liverSeries4);
		// Change Layout to 2*2 and  series 4,3,2,1 is loaded in 1,2,3,4 view box.
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[17/17]","Validating series 4,3,2,1 is displaying 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries4), "Verifying series displayed on 1st viewbox is higlighted on content selector", "Verified series displayed on 1st viewbox, Series 4 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries3), "Verifying series displayed on 2nd viewbox is higlighted on content selector", "Verified series displayed on 2nd viewbox, Series 3 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries2), "Verifying series displayed on 3rd viewbox is higlighted on content selector", "Verified series displayed on 3rd viewbox, Series 2 is  higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(liverSeries1), "Verifying series displayed on 4th viewbox is higlighted on content selector", "Verified series displayed on 4th viewbox, Series 1 is  higlighted on content selector");

	}
*/
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US789","Positive","Sanity"})
	public void test02_US789_TC3027_TC3039_TC3046_ChangeLayoutOneUpClientSideLogic() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify with change layout back and forth- Covered happy path"+"<br>Verification with layout change then back should keep the order"+"<br>Verify layout change, content selector on blank viewbox");

		helper = new HelperClass(driver);		
		viewerpage = helper.loadViewerDirectly(AIChangeMs, 1);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		viewerpage.closeNotification();
		//Steps 1 Verify with the series changes from content selector..

		// Verifying the Default layout for AI change Data.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "verify the series are appearing as per the client logic for AI change Data on Layout and series chan"
				+ "ge combinations");
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 6, "verifying number of rows of layout for AI Change data", "Number of canvas for Layout is 6");
		// Verifying the default series loaded for AI Change data are 1,2,3,4,5,6)
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/10]","Validating series 1,2,3,4,5,6 aredisplaying 2*3 layout.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult3), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 3rd series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult4), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 4th series displayed on 4th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 5th viewbox is higlighted on content selector", "Verified 5th series displayed on 5th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 6th viewbox is higlighted on content selector", "Verified 6th series displayed on 6th viewbox is higlighted on content selector");

		// In 3rd and 4th view box manually changing the series from 3rd and 4th to 5th and 6th.
		contentSelector.selectResultFromSeriesTab(3,MSResult5);
		contentSelector.selectResultFromSeriesTab(4,MSResult6);
		// Verifying the default series loaded for AI Change data are 1,2,5,6,5,6)
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/10]","Validating series 1,2,5,6,5,6 are displaying 2*3 layout.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 6th series displayed on 4th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 5th viewbox is higlighted on content selector", "Verified 5th series displayed on 5th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 6th viewbox is higlighted on content selector", "Verified 6th series displayed on 6th viewbox is higlighted on content selector");
		// Changing Layout to 2*1
		layout.selectLayout(layout.twoByOneLayoutIcon);
		// Verifying Series 1 and 2 are loaded in 1st and 2nd view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/10]","Validating series 1,2 are displaying 1st,2nd view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");

		// Changing Layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		// Verifying Series 1,2,5&6 are loaded in 1st,2nd,3rd & 4th view box respectively
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/10]","Validating series 1,2,5,6 are displaying 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 6th series displayed on 4th viewbox is higlighted on content selector");

		// Step2 Verify layout with One up and One down.

		// One up on 3rd ViewBox and verify only 5ht series is loaded.
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 5th series displayed on 1st viewbox is higlighted on content selector");
		// One down on view box and verify the layout is 2*2 and series 1,2,5,6 are loaded
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 4, "verifying number of rows of layout for AI Change MS data", "Number of canvas for Layout is 4");
		// Verifying Series 1,2,5&6 are loaded in 1st,2nd,3rd & 4th view box respectively
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/10]","Validating series 1,2,5,6 are displaying 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 6th series displayed on 4th viewbox is higlighted on content selector");


		// Step 3 Verify layout with 1*1.

		// One up on 3rd ViewBox and verify only 5ht series is loaded.
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		// Changing layout to 2*2 and verifying that series 1,2,5,6 are loaded.
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/10]","Validating series 1,2,5,6 are displaying 1st,2nd,3rd,4th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 6th series displayed on 4th viewbox is higlighted on content selector");

		// Go back to patient page and again load the viewer page for AI change MS data.
		helper.browserBackAndReloadViewer(AIChangeMs, 1, 1);

		viewerpage.waitForPatientToLoad(1);
		layout.closeNotification();
		// OneUp on the third view box.
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult3), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 3rd series displayed on 1st viewbox is higlighted on content selector");
		// Change layout to 1*1
		layout.selectLayout(layout.oneByOneLayoutIcon);
		// change layout to 1*2 and verify that series 2 and 3 are loaded.
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 2nd series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult3), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 3rd series displayed on 2nd viewbox is higlighted on content selector");
		// In 3rd and 4th view box manually changing the series from 3rd and 4th to 5th and 6th.
		contentSelector.selectResultFromSeriesTab(1,MSResult2);
		contentSelector.selectResultFromSeriesTab(2,MSResult5);
		// Change layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		layout.closeNotification();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/10]","Validating series 1,2,5,4 are displaying 1st,2nd,3rd,4th view box.");
		// Verifying that series 1,2,5,6 are loaded.
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult4), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 4th series displayed on 4th viewbox is higlighted on content selector");
		// Change layout to 2*3
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		layout.closeNotification();
		// Verifying that series 1,2,5,4,5,6 are loaded.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/10]","Validating series 1,2,5,4,5,6 are displaying 1st,2nd,3rd,4th,5th,6th view box.");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 5th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult4), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 4th series displayed on 4th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 5th viewbox is higlighted on content selector", "Verified 5th series displayed on 5th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 6th viewbox is higlighted on content selector", "Verified 6th series displayed on 6th viewbox is higlighted on content selector");

		// Implementation of TC3046

		// Go back to patient page and again load the viewer page for AI change MS data.
		//navigateToPatientPageAndReload(AiChangeMs);
		// Change layout to 3*3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		//  Select the series in 7,8,9 empty view box
		contentSelector.selectResultFromSeriesTab(7,MSResult1);
		contentSelector.selectResultFromSeriesTab(8,MSResult2);
		contentSelector.selectResultFromSeriesTab(9,MSResult3);
		// Change Layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		// Again Change the Layout into 3*3 
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		// Verifying that series are persisted are loaded.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[10/10]","Validating series are persisting");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 1st series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 2nd series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 3rd series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult4), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 4th series displayed on 4th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult5), "Verifying result series displayed on 5th viewbox is higlighted on content selector", "Verified 5th series displayed on 5th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult6), "Verifying result series displayed on 6th viewbox is higlighted on content selector", "Verified 6th series displayed on 6th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult1), "Verifying result series displayed on 7th viewbox is higlighted on content selector", "Verified 1st series displayed on 7th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult2), "Verifying result series displayed on 8th viewbox is higlighted on content selector", "Verified 2nd series displayed on 8th viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(MSResult3), "Verifying result series displayed on 9th viewbox is higlighted on content selector", "Verified 3rd series displayed on 9th viewbox is higlighted on content selector");

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US789","Positive"})
	public void test03_US789_TC3041_ChangeLayoutOneUpClientSideLogic() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify with the first load, some actions and reload the study");

		helper = new HelperClass(driver);		
		viewerpage = helper.loadViewerDirectly(AIChangeTumor, 1);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		layout.closeNotification();
		// Verifying the Default layout for AI change Tumor Data.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "verify the series are appearing as per the client logic for AI change Tumor Data on Layout and series change combinations");
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 9, "verifying number of rows of layout for AI Change Tumor data", "Number of rows for Layout is 9");

		List<String> series = new ArrayList<String>();
		for(int i=1;i<=viewerpage.totalNumberOfCanvasForLayout.size();i++){
			series.add(viewerpage.getSeriesDescriptionOverlayText(i));
		}
		
		// Manually changing the series in view boxes
		contentSelector.selectResultFromSeriesTab(1,TumorSeries9);
		contentSelector.selectResultFromSeriesTab(2,TumorSeries8);
		contentSelector.selectResultFromSeriesTab(3,TumorSeries7);
		contentSelector.selectResultFromSeriesTab(4,TumorSeries6);
		contentSelector.selectResultFromSeriesTab(5,TumorSeries5);
		contentSelector.selectResultFromSeriesTab(6,TumorSeries4);
		contentSelector.selectResultFromSeriesTab(7,TumorSeries3);
		contentSelector.selectResultFromSeriesTab(8,TumorSeries2);
		contentSelector.selectResultFromSeriesTab(9,TumorSeries1);

		// One up on first series and validate series 9 is opened in 1st view box.
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(TumorSeries9), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 9th series displayed on 1st viewbox is higlighted on content selector");
		// change series from 9 to 8 and change layout to 2*2
		contentSelector.selectResultFromSeriesTab(1,TumorSeries8);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		// verifying series 8,8,7,6 are getting displayed in 2*2 layout
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying series 8,8,7,6 are getting displayed");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(TumorSeries8), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 8th series displayed on 1st viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(TumorSeries8), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 8th series displayed on 2nd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(TumorSeries7), "Verifying result series displayed on 3rd viewbox is higlighted on content selector", "Verified 7th series displayed on 3rd viewbox is higlighted on content selector");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(TumorSeries6), "Verifying result series displayed on 4th viewbox is higlighted on content selector", "Verified 6th series displayed on 4th viewbox is higlighted on content selector");
		helper.browserBackAndReloadViewer(AIChangeTumor, 1, 1);
		layout.closeNotification();
		// Comparing the series loaded with series loaded which loaded in first time.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validating same series name are appearing in order as displayed when user loads the study");
		for(int i=1;i<=viewerpage.totalNumberOfCanvasForLayout.size();i++){
			viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(i),series.get(i-1),"Comparing the series names with the series name when user loaded at first","Both the names are matching i.e same order is appearing");
		}

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US789","Positive","Sanity"})
	public void test04_US789_TC3042_ChangeLayoutOneUpClientSideLogic() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify OneUp, change layout, content selector selection and One Down");

		helper = new HelperClass(driver);		
		viewerpage = helper.loadViewerDirectly(brainTDA, 1);
	
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);

		// Step1  Verify One up, layout change and One down.

		// Verifying the Default layout for AI change Tumor Data.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "verify the series are appearing as per the client logic for Tda Maps Data on Layout and series change combinations");
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 9, "verifying number of rows of layout for AI Change Tumor data", "Number of rows for Layout is 9");

		// Applying One up on 2nd view box and Verifying 2 series is loaded.
		viewerpage.doubleClickOnViewbox(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "validating series 2 is getting displayed");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(BTdaSeries2), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 2nd series displayed on 1st viewbox is higlighted on content selector");
		// Change layout to 1*1
		layout.selectLayout(layout.oneByOneLayoutIcon);
		// Apply now one down on 1st view box
		viewerpage.doubleClickOnViewbox(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validating total number of layout count");
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 9, "verifying number of rows of layout for AI Change Tumor data", "Number of rows for Layout is 9");

		// Step2  Verify One up, change content and One down.
		helper.browserBackAndReloadViewer(brainTDA, 1, 1);
	
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 9, "verifying number of rows of layout for TDA data", "Number of rows for Layout is 9");
		// Applying One up on 2nd view box and Verifying 2 series is loaded.
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(BTdaSeries2), "Verifying result series displayed on 1st viewbox is higlighted on content selector", "Verified 2nd series displayed on 1st viewbox is higlighted on content selector");
		// Changing series from 2nd to 5th
		contentSelector.selectResultFromSeriesTab(2,BTdaSeries5);
		//Verifying on 1st view box Series 5 is present
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validating series 5 is getting displayed in 1st view box");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(BTdaSeries5), "Verifying result series displayed on 2nd viewbox is higlighted on content selector", "Verified 5th series displayed on 2nd viewbox is higlighted on content selector");

		// Apply now one down on 1st view box
		viewerpage.doubleClickOnViewbox(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validating total number of layout count after one down");
		viewerpage.assertEquals(viewerpage.totalNumberOfCanvasForLayout.size(), 9, "verifying number of rows of layout for AI Change Tumor data", "Number of rows for Layout is 9");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","DR2105","DR2176","Negative"})
	public void test05_DR2105_TC8465_DR2176_TC8769_VerifyNoConsoleErrorOnChangingLayout() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that no console errors are displayed when user changes the layout for MR Cardiac patient. <br>"+
		"Verify no console error is  displayed on loading MR cardiac data");

		helper = new HelperClass(driver);		
		viewerpage = helper.loadViewerDirectly(MrCardiac, 2);

		
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify no console errors on default load of viewer.");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Verify no console error when layout is changed", "Verified");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify no console errors when layout is changed");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Verify no console error when layout is changed", "Verified");
	}

	//US2145: Eureka Layout menu
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US2145","Positive","F1081"})
	public void test06_US2145_TC9533_VerifyLayoutBorderOnMouseHover() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Layout menu display.");

        helper=new HelperClass(driver);
    	viewerpage = helper.loadViewerDirectly(liver9, 1);

    	PagesTheme theme=new PagesTheme(driver);
    	
    	//verify default selected layout color
    	layout=new ViewerLayout(driver);
    	layout.openLayoutContainer();
    	viewerpage.assertTrue(theme.verifyThemeForBorder(layout.twoByTwoLayoutIcon,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[1/10]", "Verified fill color for default selected 2*2 icon.");
    	
    	for(int i=0;i<layout.totalNumberOfLayoutIcons.size();i++)
    	{
    		viewerpage.mouseHover(layout.totalNumberOfLayoutIcons.get(i));
    		viewerpage.assertEquals(viewerpage.getBorderColorOfWebElemnt(layout.totalNumberOfLayoutIcons.get(i)), ThemeConstants.EUREKA_BUTTON_BORDER_COLOR, "Checkpoint[2/10]", "Verified Eureka border color for Layout "+(i+i));
  
    	}
    	
    	
    	//change layout 1*1 and verify border color
    	layout.selectLayout(layout.oneByOneLayoutIcon);
    	viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[3/10]", "Verified viewer page is loaded in 1*1 layout.");
    	layout.openLayoutContainer();
    	viewerpage.assertTrue(theme.verifyThemeForBorder(layout.oneByOneLayoutIcon,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[4/10]", "Verified fill color for selected 1*1 layout");
		
    	//change layout 3*3 and verify border color
    	layout.selectLayout(layout.threeByThreeLayoutIcon);
    	viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[5/10]", "Verified viewer page is loaded in 3*3 layout.");
    	layout.openLayoutContainer();
    	viewerpage.assertTrue(theme.verifyThemeForBorder(layout.threeByThreeLayoutIcon,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[6/10]", "Verified fill color for selected 3*3 layout");
    	
    	//change layout 1L*3R and verify border color
    	layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
    	viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_L_AND_THREE_BY_ONE_R_LAYOUT), "Checkpoint[7/10]", "Verified viewer page is loaded in 1x1L-3x1R layout.");
    	layout.openLayoutContainer();
    	viewerpage.assertTrue(theme.verifyThemeForBorder(layout.oneByOneLAndThreeByOneRLayoutIcon,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[8/10]", "Verified fill color for selected 1x1L-3x1R layout");
    	
    	//change layout 1T*2B and verify border color
    	layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);
    	viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_T_AND_ONE_BY_TWO_B_LAYOUT), "Checkpoint[9/10]", "Verified viewer page is loaded in 1x1T-1x2B layout.");
    	layout.openLayoutContainer();
    	viewerpage.assertTrue(theme.verifyThemeForBorder(layout.oneByOneTAndOneByTwoBLayoutIcon,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[10/10]", "Verified fill color for selected 1x1T-1x2B layout");
	}
	
}
