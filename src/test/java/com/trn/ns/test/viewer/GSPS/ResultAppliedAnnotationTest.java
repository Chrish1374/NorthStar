package com.trn.ns.test.viewer.GSPS;
import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ResultAppliedAnnotationTest extends TestBase{
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;
	private ContentSelector contentSelector;
	
	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName, 1);
		
		
	}

	//TC1731 : Hide/display DICOM non-image annotation results in the viewer - Active Overlay.
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	//Limitation : Also not working because of DE565
	@Test(groups ={"firefox","Chrome","IE11"})
	public void test01_US494_TC1731_verifyHideDisplayAnnotationsByOverlay() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer - Active Overlay");

		viewerPage = new ViewerPage(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		//Step 1
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");
		viewerPage.scrollToImage(1, 5);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the 'Result Applied' is visible on all slices in the series", "'Result Applied' is present on all slices in viewbox1");

		//Step 2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying the 'Result applied' is set to toogle 'Off' and GSPS object is no longer visible on viewbox1");
		viewerPage.pressResultApplied(1);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");

		//Step 3
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying the 'Result applied' is set to toogle 'ON' and GSPS object is present on viewbox2");
		viewerPage.assertTrue(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox2", "GSPS object is present on viewbox2");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(2), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox2", "'Result Applied' is present on viewbox2");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,"ON"), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox2", "Result Applied is toggle On on viewbox2");

		//Step 4
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying the 'Result applied' is set to toogle 'Off' and GSPS object is no longer visible on viewbox2");
		viewerPage.pressResultApplied(2);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,"OFF"), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox2", "Result Applied is toggle Off on viewbox2");
		viewerPage.assertFalse(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox2", "GSPS object is no longer visible on viewbox2");

		//Step 5
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the 'Result applied' is set to toogle 'ON' on viewbox1 and the toggle of the second series is still set to off");
		//Scroll back to the slice having GSPS data
		viewerPage.scrollToImage(1, 1);
		viewerPage.pressResultApplied(1);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,"ON"), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,"OFF"), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox2", "Result Applied is toggle Off on viewbox2");

	}

	//TC1732 : Hide/display DICOM non-image annotation results in the viewer - Keyboard Shortcut
	//TC2070 : Result Applied(Hide/display DICOM non-image annotation results) is not working
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	//Limitation : Also not working because of DE565
	@Test(groups ={"firefox","Chrome","IE11","US494","DE1874","Positive"})
	public void test02_US494_TC1732_DE513_TC2070_DE1874_TC7503_verifyHideDisplayAnnotationsByKeyShortcut() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer - Keyboard Shortcut. <br>"+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");

		viewerPage = new ViewerPage(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		//Step 1
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");
		viewerPage.scrollToImage(1, 5);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the 'Result Applied' is visible on all slices in the series", "'Result Applied' is present on all slices in viewbox1");

		//Step 2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying the 'Result applied' is set to toogle 'Off' and GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");

		//Step 3
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying the 'Result applied' is set to toogle 'ON' and GSPS object is present on viewbox2");
		viewerPage.assertTrue(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox2", "GSPS object is present on viewbox2");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(2), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox2", "'Result Applied' is present on viewbox2");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox2", "Result Applied is toggle On on viewbox2");

		//Step 4
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying the 'Result applied' is set to toogle 'Off' and GSPS object is no longer visible on viewbox2");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox2", "Result Applied is toggle Off on viewbox2");
		viewerPage.assertFalse(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox2", "GSPS object is no longer visible on viewbox2");

		//Step 5
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the 'Result applied' is set to toogle 'ON' on viewbox1 and the toggle of the second series is still set to off");
		//Scroll back to the slice having GSPS data
		viewerPage.scrollToImage(1, 1);
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox2", "Result Applied is toggle Off on viewbox2");

	}

	//TC1734 : Hide/display DICOM non-image annotation results in the viewer - Risk and Impact: Text Overlay
	@Test(groups ={"firefox","Chrome","IE11","Edge","Sanity"})
	public void test03_US494_TC1734_verifyHideDisplayAnnotationsResultTextOverlay() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer - Risk and Impact: Text Overlay");

		viewerPage = new ViewerPage(driver);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying GSPS result with Result applied text in Viewbox1 on selcetion of minimum text overlay");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.waitForMinimumOverlayDisplay(1);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying GSPS result with Result applied text in Viewbox1 on selcetion of default text overlay");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		overlays.waitForDefaultOverlayDisplay(1);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying GSPS result with Result applied text in Viewbox1 on selcetion of full text overlay");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

	}

	//Hide/display DICOM non-image annotation results in the viewer - Layout change.
	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test04_US494_verifyHideDisplayAnnotationsAfterLayoutChange() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer - Layout Change");

		viewerPage = new ViewerPage(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");

		//Change layout and verify the presence of result applied text and GSPS object
		//Layout 1x1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verifying GSPS result with Result applied text presence after changing layout to 1x1");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 2x1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verifying GSPS result with Result applied text presence after changing layout to 2x1");
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 1x2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verifying GSPS result with Result applied text presence after changing layout to 1x2");
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 2x2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verifying GSPS result with Result applied text presence after changing layout to 2x2");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 1x1L-3x1R
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verifying GSPS result with Result applied text presence after changing layout to 1x1L-3x1R");
		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 1x1L-2x1R
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verifying GSPS result with Result applied text presence after changing layout to 1x1L-2x1R");
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 1x1L-1x2R
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verifying GSPS result with Result applied text presence after changing layout to 1x1L-1x2R");
		layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 2x1L-1x1R
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verifying GSPS result with Result applied text presence after changing layout to 2x1L-1x1R");
		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Layout 3x1L-1x1R
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verifying GSPS result with Result applied text presence after changing layout to 3x1L-1x1R");
		layout.selectLayout(layout.threeByOneLAndOneByOneRLayoutIcon);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");


	}

	//Hide/display DICOM non-image annotation results in the viewer - Double click one up.
	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test05_US494_verifyHideDisplayAnnotationsOnDoubleClickOneUp() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer - On double click one up");

		viewerPage = new ViewerPage(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");

		//Perform double click one up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying GSPS result with Result applied text presence after double click one up");
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");

	}

	//Hide/display DICOM non-image annotation results in the viewer after selecting series from content selector in empty viewbox .
	//Limitation : This test script is getting fail due to DE565
	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test06_US494_verifyHideDisplayAnnotationsWithContentSelectorSeriesInEmptyVB() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer after selecting series from content selector in empty viewbox");

		viewerPage = new ViewerPage(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		contentSelector = new ContentSelector(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		layout.selectLayout(layout.threeByTwoLayoutIcon);

		//Select series from content selector in empty viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting series from content selector in empty viewbox");
		contentSelector.selectResultFromSeriesTab(5, "1", 2);
		viewerPage.waitForViewerpageToLoad(5);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying GSPS result with Result applied text in Viewbox3");
		viewerPage.assertTrue(verifyGSPSObjectPresence(5), "Verify the presence of GSPS object in Viewbox3", "GSPS object is present on viewbox3");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(5), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox3", "'Result Applied' is present on viewbox3");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(5,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox3", "Result Applied is toggle On on viewbox3");

	}

	//Hide/display DICOM non-image annotation results in the viewer after selecting series from content selector in non empty viewbox .
	//Limitation : This test script is getting fail due to DE565
	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test07_US494_verifyHideDisplayAnnotationsWithContentSelectorSeries() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Hide/display DICOM non-image annotation results in the viewer after selecting series from content selector in non empty viewbox");

		viewerPage = new ViewerPage(driver);
		
		contentSelector = new ContentSelector(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");

		//Select series from content selector in empty viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting series from content selector in non empty viewbox - Viewbox1");
		contentSelector.selectResultFromSeriesTab(1, "1", 2);
		viewerPage.waitForViewerpageToLoad(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying GSPS result with Result applied text in Viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is present on viewbox1");
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'ON' by default in Viewbox1", "Result Applied is toggle On on viewbox1");

	}


	public boolean verifyGSPSObjectPresence(int whichViewbox){
		int count = 0;
		boolean status = false ;

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		SimpleLine line = new  SimpleLine(driver); 

		count = line.getLinesOrPoints(whichViewbox, true, false).size() + circle.getAllCircles(whichViewbox).size() + ellipse.getEllipses(whichViewbox).size() 
				+ textAn.getTextAnnotations(whichViewbox).size() + point.getAllPoints(whichViewbox).size();

		if(count>0)
			status = true ;

		return status;
	}
}