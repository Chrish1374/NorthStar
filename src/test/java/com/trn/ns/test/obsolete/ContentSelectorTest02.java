/*package com.trn.ns.test.obsolete;
import java.sql.SQLException;
import java.util.List;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector_old;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F79_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  -revision-0
//Functional.NS.I15_ContentSelector-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ContentSelectorTest02 extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector_old cs;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private HelperClass helper;

	private String Liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Liver9filePath);
	
	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	String secondSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));	
	private String thirdSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private String fourthSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String pointMuliSeries = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNamePointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, pointMuliSeries);

	String piccOnefilePath = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String piccOnePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, piccOnefilePath);
	String firstSeriesDescriptionPicline = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Picline_filepath"));

	String SQATestingfilepath = Configurations.TEST_PROPERTIES.get("SQA_Testing");
	String patientNameSQATesting = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SQATestingfilepath);

	String terareconBrainTDAFilepath = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String patientNameTerareconBrainTDA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, terareconBrainTDAFilepath);

	String vunoSIIMCaseE032Filepath = Configurations.TEST_PROPERTIES.get("VUNO_SIIM_CASE_032_Filepath");
	String patientNameVUNOSIIMCASE032 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, vunoSIIMCaseE032Filepath);

	String john005S0223Filepath = Configurations.TEST_PROPERTIES.get("John_005_S_0223_Filepath");
	String patientNameJohn005S0223 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, john005S0223Filepath);

	String johnSmith1CORSTAGEoldFilepath = Configurations.TEST_PROPERTIES.get("John_Smith_1_CORSTAGE_old_Filepath");
	String patientNameJohnSmith1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnSmith1CORSTAGEoldFilepath);

	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String PatientNameAidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);
	
	String filePathWJ = Configurations.TEST_PROPERTIES.get("WJ_Filepath");
	String PatientNameGSPSWJ = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathWJ);
	
	String mrLSPfilePath = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
	String patientNameMrLSP = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, mrLSPfilePath);
	
	String imbioFilepath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilepath);
	
	String chestFilePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String chestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, chestFilePath);


	String consoleError = "Cannot read property 'executeCommandWithoutEvent' of null";

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
	}

	@Test(groups = { "Chrome","DE1009"})
	public void test01_DE1009_TC4117_verifyContentSelectorWithAllAnnotationLevel() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify content selector is displayed after clicking on series description when annotation level set to - minimum and full");// Default is covered in above test cases
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);
		patientPage.clickOntheFirstStudy();		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		ContentSelector_old cs = new ContentSelector_old(driver);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		cs.openContentSelector(1, true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify content selector is getting open after clicking on series description when annotation level is set to minimum");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.sourceTab), "Verify Source tab is present on Content Selector", "The Source tab is present on content selector");

		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		cs.openContentSelector(2, true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify content selector is getting open after clicking on series description when annotation level is set to full");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.sourceTab), "Verify Source tab is present on Content Selector", "The Source tab is present on content selector");
	}			

	//US977: Update content selector following new A/R design
	@Test(groups = {"Chrome","US977","positive","BVT"})
	public void test02_US977_TC4377_verifyUIForContentSelector() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify UI of content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		//Click on Series description on fourth View box and select Result Tab
		cs.openContentSelector(1, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify default state of Source and Result tab when content selector open");
		viewerpage.assertTrue(cs.verifyTabIsActiveInContentSelector(cs.resultTab), "Verify Result tab seen first followed by Source", "Verified first tab seen as Result is active in Content selector");
		viewerpage.assertFalse(cs.verifyTabIsActiveInContentSelector(cs.sourceTab), "Verify Source tab in content selector", "Verified that Source tab is not active tab in content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]", "Verify machine and series radio button  present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.machineButtonOnContentSelector), "Verify machine radio button  is present on result tab", "The machine radio button  is present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.seriesButtonOnContentSelector), "Verify series radio button  is present on result tab", "The series radio button  is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]", "Verify Sort by dropdown menu when Group by series is selected");
		viewerpage.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertFalse(viewerpage.isElementPresent(cs.selectionComboBoxOnContentSelector), "Verify sort by dropdown is not present on result tab", "Sort by dropdown not seen when Group by series option selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]", "Verify Sort by dropdown menu when Group by machine is selected");
		viewerpage.click(cs.machineButtonOnContentSelector);
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.selectionComboBoxOnContentSelector), "Verify sort by dropdown is present on result tab", "Sort by dropdown is present on result tab");
		viewerpage.assertEquals(cs.selectionOptionOnContentSelector.size(), 2, "Verify 2 options seen under sort by dropdown ", "Verified that 2 options seen under sort by dropdown menu");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]", "Verify option for sort by drop down");
		viewerpage.assertEquals(cs.getText(cs.selectionOptionOnContentSelector.get(0)),ViewerPageConstants.RECENT_SELECTION,"Verify Recent selection option on sort by drop down","Verified Recent selection option is present on sort by dropdown");
		viewerpage.assertEquals(cs.getText(cs.selectionOptionOnContentSelector.get(1)),ViewerPageConstants.ASCENDING_SELECTION,"Verify Ascending selection option on sort by drop down","Verified Recent selection option is present on sort by dropdown");

	}

	@Test(groups = {"Chrome","US977","positive"})
	public void test03_US977_TC4380_verifyTooltipOnCS() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltip for each entry from content selector");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameTerareconBrainTDA);

		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForTimePeriod(1000);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/9]", "Verify tootip when ,mousehover on source tab");
		cs.openContentSelector(1, false);
		viewerpage.mouseHover(cs.sourceTab);
		viewerpage.assertTrue(cs.verifyTitleOfTabInContentSelector(cs.sourceTab), "Verify tooltip for source tab", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/9]", "Verify tootip when ,mousehover on result tab");
		viewerpage.mouseHover(cs.resultTab);
		viewerpage.assertTrue(cs.verifyTitleOfTabInContentSelector(cs.resultTab), "Verify tooltip for result tab", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/9]", "Verify tootip when ,mousehover on machine label from CS");
		viewerpage.mouseHover(cs.machineLabelOnContentSelector);
		viewerpage.assertEquals(cs.getAttributeValue(cs.machineLabelOnContentSelector, NSGenericConstants.TITLE), ViewerPageConstants.CONTENT_MACHINE_BUTTON, "Verify tootip for machine label", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/9]", "Verify tootip when ,mousehover on series label from CS");
		viewerpage.mouseHover(cs.seriesLabelOnContentSelector);
		viewerpage.assertEquals(cs.getAttributeValue(cs.seriesLabelOnContentSelector, NSGenericConstants.TITLE), PatientXMLConstants.SERIES_TAG, "Verify tootip for series label", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/9]", "Verify tootip when ,mousehover on machine name in CS");
		viewerpage.mouseHover(cs.allContentSelectorMachine.get(0));
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(0)), machineName , "Verify tootip for machine name", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/9]", "Verify tootip when ,mousehover on created date for machine in CS");
		String date=cs.getText(cs.dateCreatedForSeries.get(0));
		viewerpage.mouseHover(cs.dateCreatedForSeries.get(0));
		viewerpage.assertEquals(cs.getAttributeValue(cs.dateCreatedForSeries.get(0), NSGenericConstants.TITLE), date, "Verify tootip for created date", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/9]", "Verify tooltip for each entry in Result tab when machine radio button is selected");
		verifyTootipForEachEntryInResultOrSourceTab(cs.resultFromContentSelector,cs.sourceFromOrCreationDateContentSelector);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/9]", "Verify tooltip for each entry in Result tab when series radio button is selected");
		viewerpage.click(cs.seriesButtonOnContentSelector);
		verifyTootipForEachEntryInResultOrSourceTab(cs.resultFromContentSelector,cs.sourceFromOrCreationDateContentSelector);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/9]", "Verify tooltip for each entry in Source tab.");
		viewerpage.click(cs.sourceTab);
		verifyTootipForEachEntryInResultOrSourceTab(cs.seriesFromContentSelector,cs.dateCreatedForSeries);
	}

	@Test(groups = {"Chrome","US977","positive"})
	public void test04_US977_TC4381_verifyUIClosedFunctionalityForCS() throws TimeoutException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify UI of content selector closed after mouse hover any where within the viewer");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify content selector closed when mouse hover on window centre in viewbox1");
		cs.openContentSelector(1, false);
		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.assertFalse(cs.isElementPresent(cs.resultTab), "Verify content selector after mouse hover action perfrom", "Verified that content selector closed after mouse hover action");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify content selector closed when mouse hover on window width in viewbox1");
		cs.openContentSelector(1, true);
		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.assertFalse(cs.isElementPresent(cs.sourceTab), "Verify content selector after mouse hover action perfrom", "Verified that content selector closed after mouse hover action");

	}

	@Test(groups = {"Chrome","US977","negative"})
	public void test05_US977_TC4382_verifyHighlightEntriesInCS() throws TimeoutException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Highlighted entries in Content Selector");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]", "Verify Result text highlighted on mouse hover on Result tab.");
		cs.openContentSelector(1, false);
		viewerpage.mouseHover(cs.resultTab);
		viewerpage.assertEquals(cs.getCssValue(cs.resultTab,NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Verify result text highlighted or not","Verified that Result text highlighted on mouse hover action perform");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]", "Verify Source text highlighted on mouse hover on Source tab.");
		viewerpage.mouseHover(cs.sourceTab);
		viewerpage.assertEquals(cs.getCssValue(cs.sourceTab, NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Verify Source text highlighted or not", "Verified that Source text highlighted on mouse hover action perform");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]", "Verify Machine text highlighted on mouse hover on Group by section");
		viewerpage.mouseHover(cs.machineLabelOnContentSelector);
		viewerpage.assertEquals(cs.getCssValue(cs.machineLabelOnContentSelector, NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Verify Machine text highlighted or not", "Verified that Machine text highlighted on mouse hover action perform");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]", "Verify Series text highlighted on mouse hover on Group by section");
		viewerpage.mouseHover(cs.seriesLabelOnContentSelector);
		viewerpage.assertEquals(cs.getCssValue(cs.seriesLabelOnContentSelector, NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Verify Series text highlighted or not", "Verified that Series text highlighted on mouse hover action perform");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]", "Verify Series text highlighted on mouse hover on Group by section");
		viewerpage.click(viewerpage.getViewPort(1));
		cs.openContentSelector(1, true);
		viewerpage.mouseHover(cs.seriesFromContentSelector.get(0));
		viewerpage.assertEquals(cs.getCssValue(cs.seriesFromContentSelector.get(0),NSGenericConstants.CSS_PROP_COLOR),ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR,"Verify Series highlighted or not", "Verified that Series highlighted on mouse hover action perform");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]", "Verify series that are displayed in viewer are seen highlighted in content selector ");
		cs.openContentSelector(1, true);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionAH4),"Verifying already visible series on viewbox is higlighted on content selector", "Verified for"+" "+firstSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionAH4),"Verifying already visible series on viewbox is higlighted on content selector", "Verified for"+" "+secondSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, thirdSeriesDescriptionAH4),"Verifying already visible series on viewbox is higlighted on content selector", "Verified for"+" "+thirdSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, fourthSeriesDescriptionAH4),"Verifying already visible series on viewbox is higlighted on content selector","Verified for"+" "+fourthSeriesDescriptionAH4);
	}

	@Test(groups = {"Chrome","US977","positive"})
	public void test06_US977_TC4383_verifyAutoExpandFunctionalityForCS() throws TimeoutException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Auto expand functionality for Series/Results in Content selector");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameTerareconBrainTDA);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify content selector auto expand such that all entries  present in it are viewable in top,middle and botton viewbox");
		cs.openContentSelector(1, false);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify content selector auto-expand or not in top viewbox", "Verified");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(2));
		cs.openContentSelector(4, false);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify content selector auto-expand or not in middle viewbox", "Verified");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(2));
		cs.openContentSelector(7, false);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify content selector auto-expand or not in bottom viewbox", "Verified");

		//draw miltiple findings and reload viewer page
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,1);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,2);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,3);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,4);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,5);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,6);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,7);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,8);
		drawAnnotationAndreloadViewerPage(patientNameTerareconBrainTDA,1,9);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify scroll bar when list of entries go beyond the viewer after auto-expand");
		cs.openContentSelector(7, false);
		viewerpage.mouseHover(cs.resultFromContentSelector.get(0));
		viewerpage.assertTrue(cs.verifyPresenceOfVerticalScrollbar(cs.toggleButton.get(0)), "Verify scroll bar when list of entries go beyond the viewer after auto-expand", "Verified");

		viewerpage.scrollIntoView(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));
		viewerpage.click(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify selected entry loaded in the viewbox");
		cs.openContentSelector(4, false);
		viewerpage.scrollIntoView(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(4, viewerpage.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1))),"Verifying already visible series on viewbox is higlighted on content selector","Verified");


	}

	@Test(groups = {"Chrome","US977","positive"})
	public void test07_US977_TC4385_verifyExpandFunctionalityForCS() throws TimeoutException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify expand functionality of content selector for patient"+" "+patientNameTerareconBrainTDA);

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameTerareconBrainTDA);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]", "Verify all results are visible under that machine when content selector open ");
		cs.openContentSelector(1, false);
		viewerpage.click(cs.machineButtonOnContentSelector);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify content selector auto-expand", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]", "Verify all results are hide under the machine when click on arrow available at the end of the row ");
		viewerpage.click(cs.toggleButton.get(0));
		viewerpage.assertFalse(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify machine results are hide when click on arrow available at the end of the row", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]", "Verify all results are expand under the machine when click on machine name ");
		viewerpage.click(cs.contentSelectorMachine);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify all results are expand under the machine when click on machine name ", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]", "Verify all results are visible under that machine when click on series button from Result tab");
		viewerpage.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify content selector auto-expand", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]", "Verify all results are hide under the series when click on arrow available at the end of the row");
		viewerpage.click(cs.toggleButton.get(0));
		viewerpage.assertFalse(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify series results are hide when click on arrow available at the end of the row", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]", "Verify all results are expand under the machine when click on name");
		viewerpage.click(cs.contentSelectorMachine);
		viewerpage.assertTrue(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE), "Verify all results are expand under the series when click on name", "Verified");


	}

	@Test(groups = { "Chrome","US977","positive"})
	public void test08_US977_TC4473_verifyDateUnknownTextOnCS() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify content selector on layout change");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameVUNOSIIMCASE032+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameVUNOSIIMCASE032);

		patientPage.clickOntheFirstStudy();	      
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);

		cs = new ContentSelector_old(driver);
		cs.openContentSelector(1, false);

		//click on machine button to verify Unknown date
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Empty description replace with text Unknown Date when Result are Group by Machine name");
		cs.click(cs.machineButtonOnContentSelector);
		cs.assertEquals(cs.getText(cs.machineGroupLableheader), ViewerPageConstants.UNKNOWN_DATE, "Verify Empty description replace with Unknown date", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Empty description replace with text Unknown Date when Result are Group by Series name");
		cs.click(cs.seriesButtonOnContentSelector);
		cs.assertEquals(cs.getText(cs.dateCreatedForSeries.get(0)), ViewerPageConstants.UNKNOWN_DATE, "Verify Empty description replace with Unknown date", "Verified");   

	}

	@Test(groups = { "Chrome","US977","negative"})
	public void test09_US977_TC4474_verifyEmptyDescriptionForResultPDF() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Result name(PDF): Verify Empty Description replace with the Machine name which created it");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameJohnSmith1+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameJohnSmith1);

		patientPage.clickOntheFirstStudy();	     
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);

		cs = new ContentSelector_old(driver);
		cs.openContentSelector(2, false);

		//click on machine button to verify Unknown date
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Empty description replace with Created Username when Result are Group by Machine name");
		cs.click(cs.machineButtonOnContentSelector);
		cs.assertTrue((cs.getText(cs.sourceFromOrCreationDateContentSelector.get(0)).contains(cs.getText(cs.contentSelectorMachine))), "Verify Empty description replace with Unknown date", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Empty description replace with text Unknown Reference when Result are Group by Series name");
		cs.click(cs.seriesButtonOnContentSelector);
		cs.assertEquals(cs.getText(cs.contentSelectorMachine), ViewerPageConstants.UNKNOWN_REFERENCE, "Verify Empty description replace with Unknown Reference", "Verified");   

	}

	@Test(groups = { "Chrome","US977","negative"})
	public void test10_US977_TC4475_verifyEmptyDescriptionReplaceWithCreatedUserForDrawnAnnotation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify empty description text replace with Created By user when new annotations drawn");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameJohn005S0223+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameJohn005S0223);

		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForTimePeriod(1000);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);

		patientPage.clickOntheFirstStudy();      
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		cs = new ContentSelector_old(driver);
		cs.openContentSelector(1, false);

		//click on machine button to verify Unknown date
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify empty Description text when Group by machine radio button selected");
		cs.click(cs.machineButtonOnContentSelector);
		verifyEmptyDescriptionText(cs.sourceFromOrCreationDateContentSelector,machineName);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify empty Description text when Group by series radio button selected");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.contentSelectorMachine),ViewerPageConstants.UNKNOWN_REFERENCE,"Verify empty description text replace with Unknown Reference text", "Verified");

		drawAnnotationAndreloadViewerPage(patientNameJohn005S0223,1,2);
		drawAnnotationAndreloadViewerPage(patientNameJohn005S0223,1,3);
		drawAnnotationAndreloadViewerPage(patientNameJohn005S0223,1,4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify empty Description text when Group by machine radio button selected after drawing multiple annotations");
		cs.openContentSelector(1, false);
		cs.click(cs.machineButtonOnContentSelector);

		for(int i=0;i< cs.resultFromContentSelector.size();i++ )
		{
			if(cs.getText(cs.resultFromContentSelector.get(i)).contains(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX))
			{
				viewerpage.mouseHover(cs.sourceFromOrCreationDateContentSelector.get(i));
				viewerpage.assertTrue(cs.getText(cs.sourceFromOrCreationDateContentSelector.get(i)).contains(Configurations.TEST_PROPERTIES.get("nsUserName")),"Verify empty description text replace with Created by user name when Group by machine", "Verified");
			}
			else
			{
				viewerpage.mouseHover(cs.sourceFromOrCreationDateContentSelector.get(i));
				viewerpage.assertTrue(cs.getText(cs.sourceFromOrCreationDateContentSelector.get(i)).contains(cs.getText(cs.contentSelectorMachine)),"Verify empty description text replace with Created by machine name", "Verified");

			}


		}


	}

	@Test(groups = { "Chrome","US977","Negative"})
	public void test11_US977_TC4476_verifyEmptyDescriptionWhenMachineNameUnknown() throws InterruptedException, SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify empty description When Machine name Unknown");

		String MachineUID="neuro-quant";
		DatabaseMethods db=new DatabaseMethods(driver);
		db.updateMachineTitle(MachineUID, "''");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameJohn005S0223+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameJohn005S0223);

		patientPage.clickOnStudy(1);   
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		cs = new ContentSelector_old(driver);

		cs.openContentSelector(1, false);

		//click on machine button to verify Unknown date
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify empty Description text when Group by machine radio button selected and Machine name Unknown");
		cs.click(cs.machineButtonOnContentSelector);
		verifyEmptyDescriptionText(cs.sourceFromOrCreationDateContentSelector,ViewerPageConstants.UNKNOWN_MACHINE_NAME);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify empty Description text when Group by series radio button selected");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.contentSelectorMachine),ViewerPageConstants.UNKNOWN_REFERENCE,"Verify empty description text replace with Unknown Reference text", "Verified");

	}

	//US804 Scrollbar component
	@Test(groups = { "Chrome", "IE11", "Edge", "US804", "Positive" })
	public void test12_TC4449_VerifyScrollbarComponentInContentSelector() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new UI of scrollbar component on Content Selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+piccOnePatient+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(piccOnePatient);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		cs = new ContentSelector_old(driver);
		
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);	
		lineWithUnit=new MeasurementWithUnit(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs.waitForViewerpageToLoad(2);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -50, -50, 100, 0);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 150, 150);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 78, -20, 100, 100);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -120, -150, 0, 150);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-4,-20,134,0);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,66,-50,0,180);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,42,41,134,134);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-385,-20,768,0);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -50, 120, 150);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -10, -30, 90, 80);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 25, -70, 50, 0);
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150) ;
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -50,-70) ;
		helper.browserBackAndReloadViewer(piccOnePatient, 1, 1);

		//Draw a Circle on View Box1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs.closingBannerAndWaterMark();
		cs.click(cs.getViewPort(1));

		for(int i=1;i<=9;i+=4)
		{
		
			cs.openContentSelector(i, false);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify scroll bar component in content selector for Result tab");
			viewerpage.mouseHover(cs.contentSelectorMachines.get(0));
			viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify new UI of Scrollbar component present in content selector for Result tab", "Verified");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify UI of regular scrollbar component in content selector for Result tab");
			viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in content selector for Result tab");
			viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
			viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");

			viewerpage.scrollIntoView(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));
			String ResultName=cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));
			String MachineName=cs.getText(cs.allContentSelectorMachine.get(1));
			viewerpage.click(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1));

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify after clicking on last item in result tab,the corresponding result+source series loaded in viewer");
			//viewerpage.click(cs.bannerCloseIcon);
			viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(i, ResultName,MachineName), "Verify result+source series loaded in viewer", "Verified");

			cs.closingBannerAndWaterMark();
		}
		
		// Similarly check scrollbar in Source tab when entries are more
		viewerpage.navigateBackToStudyListPage();
		studyPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientNameSQATesting);
		studyPage.clickOntheFirstStudy();		
		viewerpage.waitForViewerpageToLoad();

		viewerpage.selectLayout(cs.threeByThreeLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		for(int i=1;i<=viewerpage.getNumberOfCanvasForLayout();i+=4)
		{
			cs.openContentSelector(i, true);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify scroll bar component in content selector for Source tab");
			viewerpage.mouseHover(cs.contentSelectorMachines.get(0));
			viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify Scrollbar component present in content selector for Source tab", "Verified");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify UI of regular scrollbar component in source tab");
			viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in content selector for source tab");
			viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
			viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");

			viewerpage.scrollIntoView(cs.seriesFromContentSelector.get(cs.seriesFromContentSelector.size()-1));
			String SeriesName=cs.getText(cs.seriesFromContentSelector.get(cs.seriesFromContentSelector.size()-1));
			viewerpage.click(cs.seriesFromContentSelector.get(cs.seriesFromContentSelector.size()-1));

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify after clicking on last item in source tab,the corresponding source series loaded in viewer");
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(i, SeriesName), "Verify series is loaded in viewer", "Verified");
		}

	}

	@Test(groups = { "Chrome","DE948","Positive"})
	public void test13_DE948_TC4047_verifyMachineDescriptionWhenNoMachineResults() throws InterruptedException, SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify machine description When no machine result on first place");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	        
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		cs = new ContentSelector_old(driver);
		circle = new CircleAnnotation(driver);

		cs.openContentSelector(1, false);

		String seriesName1=cs.getSeriesDescriptionOverlayText(1);
		String seriesName2=cs.getSeriesDescriptionOverlayText(1);

		String ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + Configurations.TEST_PROPERTIES.get("nsUserName");
		//click on machine button to verify no machine 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify machine description when no machine result ");
		cs.click(cs.machineButtonOnContentSelector);
		viewerpage.assertEquals(cs.allContentSelectorMachine.size(),0,"Verify machine description is empty", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]", "Verify user can see GSPS_scan_1 result along with machine name User Created Result in Result tab");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		cs.openContentSelector(1, false);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(0)),ViewerPageConstants.USER_CREATED_RESULT,"Verify machine name after creating annotation on first viewbox is"+" "+ViewerPageConstants.USER_CREATED_RESULT, "Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(0)),ResultName+"_1","Verify result name after creating annotation is"+" "+ResultName+"_1", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]", "Verify series and new series in result tab when user select group by series option");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(0)),seriesName1,"Verify series name when click on series button in Result tab", "Verified series name"+" "+seriesName1);
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(0)),ResultName+"_1","Verify series name after creating annotation in Source tab is"+" "+ResultName+"_1", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]", "Verify user can see GSPS_scan_2 result along with machine name User Created Result in Result tab");
		circle.closingBannerAndWaterMark();
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		cs.openContentSelector(2, false);
		cs.click(cs.machineButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(0)),ViewerPageConstants.USER_CREATED_RESULT,"Verify machine name after creating annotation on second viewbox is"+" "+ViewerPageConstants.USER_CREATED_RESULT, "Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(1)),ResultName+"_2","Verify result name after creating annotation is"+" "+ResultName+"_2", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]", "Verify series and new series in result tab when user select group by series option");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(1)),seriesName2,"Verify series name when click on series button in Result tab","Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(0)),ResultName+"_2","Verify series name after creating annotation in Source tab is"+" "+ResultName+"_2", "Verified");

	}

	@Test(groups = { "Chrome","DE948","Positive"})
	public void test14_DE948_TC4051_verifyMachineDescriptionWhenMultipleMachineResultsPresent() throws InterruptedException, SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify machine description When multiple machine results are present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientNameAidoc+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientNameAidoc);
		patientPage.clickOntheFirstStudy();	       
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		cs = new ContentSelector_old(driver);
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		cs.openContentSelector(3, false);

		String resultToSelect=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
		String seriesName1=cs.getSeriesDescriptionOverlayText(1);
		String seriesName3=cs.getSeriesDescriptionOverlayText(3);
		String seriesName4=cs.getSeriesDescriptionOverlayText(4);

		String ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + Configurations.TEST_PROPERTIES.get("nsUserName");
		//click on machine button to verify 2 machine (Lesion Finder and Lesion Finder 2)
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/7]", "Verify multiple machine in content seelctor when no annotation is present");
		cs.click(cs.machineButtonOnContentSelector);
		viewerpage.assertEquals(cs.allContentSelectorMachine.size(),2,"Verify 2 machine result seen in content selector", "Verified");

		//draw annotation on Source( SAGITTAL)
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/7]", "Verify content selector after drawing annotation on source");
		circle.closingBannerAndWaterMark();
		circle.selectCircleFromQuickToolbar(4);
		circle.drawCircle(4, 20, 20, -100,-100);	
		cs.openContentSelector(4, false);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(0)),ViewerPageConstants.USER_CREATED_RESULT,"Verify machine name after creating annotation "+" "+ViewerPageConstants.USER_CREATED_RESULT, "Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(0)),ResultName+"_1","Verify result name after creating annotation is"+" "+ResultName+"_1", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/7]", "Verify series and new series in result tab when user select group by series option");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(2)),ViewerPageConstants.UNKNOWN_REFERENCE,"Verify series name when click on series button in Result tab", "Verified series name"+" "+seriesName4);
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-3)),ResultName+"_1","Verify series name after creating annotation in Source tab is"+" "+ResultName+"_1", "Verified");
		cs.click(cs.machineButtonOnContentSelector);

		//draw annotation on machine1 (Lesion Finder 2) ..Resultname :AXIAL
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/7]", "Verify content selector after drawing annotation on Lesion Finder 2");
		cs.mouseHover(cs.getViewPort(3));
		cs.selectResultFromContentSelectorWithMachineName(3, resultToSelect, ViewerPageConstants.AIDOC_MACHINE_NAME2);
		cs.waitForViewerpageToLoad();

		cs.closingBannerAndWaterMark();
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(3, -50, -50, 100, 0);
		cs.openContentSelector(3, false);
		closeAllExpandOneAtTime(2);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(2)),ViewerPageConstants.AIDOC_MACHINE_NAME2,"Verify machine name after creating annotation"+" "+ViewerPageConstants.USER_CREATED_RESULT, "Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1)),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_2","Verify result name after creating annotation is", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/7]", "Verify series and new series in result tab when user select group by series option");
		cs.click(cs.seriesButtonOnContentSelector);
		closeAllExpandOneAtTime(1);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(1)),seriesName3,"Verify series name when click on series button in Result tab","Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1)),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_2","Verify series name after creating annotation in Source tab is", "Verified");


		//draw annotation on machine1 (Lesion Finder) ..Resultname : AIDOC hyperdense 2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/7]", "Verify content selector after drawing annotation on Lesion Finder");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		cs.openContentSelector(1, false);
		cs.click(cs.machineButtonOnContentSelector);
		closeAllExpandOneAtTime(1);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(1)),ViewerPageConstants.AIDOC_MACHINE_NAME1,"Verify machine name after creating annotation "+" "+ViewerPageConstants.USER_CREATED_RESULT, "Verified");
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1)),ResultName+"_3","Verify result name after creating annotation is"+" "+ResultName+"_3", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/7]", "Verify series and new series in result tab when user select group by series option");
		cs.click(cs.seriesButtonOnContentSelector);
		viewerpage.assertEquals(cs.getText(cs.allContentSelectorMachine.get(2)),seriesName1,"Verify series name when click on series button in Result tab","Verified");
		closeAllExpandOneAtTime(2);
		viewerpage.assertEquals(cs.getText(cs.resultFromContentSelector.get(cs.resultFromContentSelector.size()-1)),ResultName+"_3","Verify series name after creating annotation in Source tab is"+" "+ResultName+"_3", "Verified");
		cs.click(cs.machineButtonOnContentSelector);


	}

	//DE1304: Console Error seen when changing content through content selector

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1304", "Positive" })
	public void test15_DE1304_TC5252_VerifyConsoleErrorWhenSourceSeriesRenderedOnViewerAfterDrawingAnnotation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify there should not be any console error when source series is selected through content selector after drawing annotation");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);	
		lineWithUnit=new MeasurementWithUnit(driver);

		String seriesName=viewerpage.getSeriesDescriptionOverlayText(1);

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[1/5]", "Verified that no console error after drawing annotation");

		cs.selectSeriesFromContentSelector(1, seriesName);
		viewerpage.assertFalse(verifyGSPSObjectPresence(1), "Checkpoint[2/5]", "Verified that original source series loaded without any drawn annotation");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[3/5]", "Verified that no console error on rendering of original source series");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		cs.selectSeriesFromContentSelector(1, seriesName);
		viewerpage.assertFalse(verifyGSPSObjectPresence(1), "Checkpoint[4/5]", "Verified that original source series loaded without any drawn annotation after reload");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[5/5]", "Verified that no console error on rendering of original source series after viewer page reload");    
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1304", "Positive" })
	public void test16_DE1304_TC5253_VerifyConsoleErrorWhenSourceSeriesRenderedOnViewerAfterAddingComment() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify there should not be any console error when source series is selected through content selector after adding comment to annotation");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);
		patientPage.clickOntheFirstStudy();		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		cs = new ContentSelector_old(driver);
		ellipse = new EllipseAnnotation(driver);	

		String seriesName=viewerpage.getSeriesDescriptionOverlayText(1);
		String ellipseComment="Ellipse Comment";
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[1/5]", "Verified that no console error after drawing annotation");

		cs.selectSeriesFromContentSelector(1, seriesName);
		viewerpage.assertTrue(ellipse.getAllGSPSObjects(1).isEmpty(), "Checkpoint[2/5]", "Verified that original source series loaded without any drawn annotation");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[3/5]", "Verified that no console error on rendering of original source series");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		cs.selectSeriesFromContentSelector(1, seriesName);
		viewerpage.assertTrue(ellipse.getAllGSPSObjects(1).isEmpty(), "Checkpoint[4/5]", "Verified that original source series loaded without any drawn annotation after reload");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[5/5]", "Verified that no console error on rendering of original source series after viewer page reload");    
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1304", "Negative" })
	public void test17_DE1758_TC7199_VerifyFindingsUnderMachineWhenCreatedOnResult() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user drawn findings are NOT appearing under 'User Created Results' in content selector after drawing annotation on result series");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNamePointMultiSeries+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNamePointMultiSeries);
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();
			
		cs = new ContentSelector_old(driver);
		cs.waitForPatientToLoad(1);
		ellipse = new EllipseAnnotation(driver);	


		List<String> machines = cs.getMachineName(1);
		List<String> results = cs.getAllResultDesciptionFromContentSelector(1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		List<String> machinesPostAnnotationCreation = cs.getMachineName(1);
		List<String> updatedResult = cs.getAllResultDesciptionFromContentSelector(1);	

		cs.assertEquals(machines,machinesPostAnnotationCreation, "Checkpoint[1/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertNotEquals(results.size(),updatedResult.size(), "Checkpoint[2/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertEquals(machines.get(0),machineName, "Checkpoint[3/12]", "Verifying that machine name is same as displayed on study page");
		cs.assertNotEquals(machines.get(0),ViewerPageConstants.USER_CREATED_RESULT, "Checkpoint[4/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNamePointMultiSeries, 1, 1);

		machinesPostAnnotationCreation = cs.getMachineName(1);
		updatedResult = cs.getAllResultDesciptionFromContentSelector(1);	

		cs.assertEquals(machines,machinesPostAnnotationCreation, "Checkpoint[5/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertNotEquals(results.size(),updatedResult.size(), "Checkpoint[6/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertEquals(machines.get(0),machineName, "Checkpoint[7/12]", "Verifying that machine name is same as displayed on study page");
		cs.assertNotEquals(machines.get(0),ViewerPageConstants.USER_CREATED_RESULT, "Checkpoint[8/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");


		cs.selectSeriesFromContentSelector(1, cs.getSeriesDescriptionOverlayText(1));
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		
		updatedResult = cs.getAllResultDesciptionFromContentSelector(1);	
		machinesPostAnnotationCreation = cs.getMachineName(1);
		
		cs.assertNotEquals(machines.size(),machinesPostAnnotationCreation.size(), "Checkpoint[9/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertNotEquals(results.size(),updatedResult.size(), "Checkpoint[10/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertEquals(machinesPostAnnotationCreation.get(1),machineName, "Checkpoint[11/12]", "Verifying that machine name is same as displayed on study page");
		cs.assertEquals(machinesPostAnnotationCreation.get(0),ViewerPageConstants.USER_CREATED_RESULT, "Checkpoint[12/12]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");


	}

	// Boneage, Imbio and John_doe and liver9
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE1304", "Positive" }, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test51_contentSelector"})
	public void test18_DE1758_TC7198_VerifyFindingsUnderMachineWhenCreatedOnSource(String patientFilePath, String source) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user drawn findings are appearing under 'User Created Results' in content selector after drawing annotation on source series");

	
		patientPage = new PatientListPage(driver);
		
		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );

		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();			
		cs = new ContentSelector_old(driver);
		cs.waitForViewerpageToLoad(Integer.parseInt(source));
		
		ellipse = new EllipseAnnotation(driver);	


		List<String> machines = cs.getMachineName(1);
		List<String> results = cs.getAllResultDesciptionFromContentSelector(1);


		cs.selectSeriesFromContentSelector(1, cs.getSeriesDescriptionOverlayText(Integer.parseInt(source)));
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
        ellipse.closingBannerAndWaterMark();
		
		List<String> updatedResult = cs.getAllResultDesciptionFromContentSelector(1);	
		List<String> machinesPostAnnotationCreation = cs.getMachineName(1);
		
		cs.assertNotEquals(machines.size(),machinesPostAnnotationCreation.size(), "Checkpoint[1/4]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertNotEquals(results.size(),updatedResult.size(), "Checkpoint[2/4]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		cs.assertEquals(machinesPostAnnotationCreation.get(0),ViewerPageConstants.USER_CREATED_RESULT, "Checkpoint[3/4]", "Newly added annotation on result series should be listed under Content selector ->  'Result' tab -> 'Machine title (GSPS_11)' and should not be added under 'User Created results'");
		for(int i =1;i<machinesPostAnnotationCreation.size();i++)
		cs.assertEquals(machinesPostAnnotationCreation.get(i),machines.get(i-1), "Checkpoint[4/4]", "Verifying that machine name is same as displayed on study page");
		

	}

	*//**
	 * @throws InterruptedException
	 *//*
	@Test(groups = { "Chrome", "IE11", "Edge", "DE1887", "Positive" })
	public void test20_DE1887_TC7506_VerifyUserAbleToLoadGSPSDemoseriesInviewer() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Reaserch demo series is loaded in the viewer for WJ patient from content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientNameGSPSWJ+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientNameGSPSWJ);
		
		patientPage.clickOntheFirstStudy();	
				
		cs = new ContentSelector_old(driver);
		cs.waitForViewerpageToLoad();
		ViewerSliderAndFindingMenu findingMenu = new ViewerSliderAndFindingMenu(driver);
		cs.closeBanner();
		
		int phaseNumber = cs.getValueOfCurrentPhase(1);
		int sliceNumber = cs.getMaxNumberofScrollForViewbox(1);
		int currentSlice = cs.getCurrentScrollPositionOfViewbox(1);
		
		List<String> prSeriesName = cs.getPREntiresForGivenSeries(1, cs.getSeriesDescriptionOverlayText(1));
		cs.assertEquals(prSeriesName.size(), 1, "Checkpoint[1/7]", "Verifying that there is only one PR entry present");
		cs.assertTrue(findingMenu.getAllGSPSObjects(1).isEmpty(),"Checkpoint[2/7]","Verifying that DICOM image consists no annotations");
			
		//Loading the GSPS demo series into the viewer
		cs.selectPREntryForGivenSeries(1, cs.getSeriesDescriptionOverlayText(1));		
		cs.waitForTimePeriod(2000);
		
		//Verifying overlays and annotations are loaded for the series
		cs.assertTrue(findingMenu.getAllGSPSObjects(1).size()>=1,"Checkpoint[3/7]","Verifying that DICOM image consists of system Annotation");
		cs.assertNotEquals(prSeriesName.get(0), cs.getSeriesDescriptionOverlayText(1),"Checkpoint[4/7]","Verifying the series description of overlay text");
		
		cs.assertNotEquals(phaseNumber,cs.getValueOfCurrentPhase(1),"Checkpoint[5/7]","Verifying that after loading DICOM image in viewer there is a change in the phase number");
		cs.assertNotEquals(sliceNumber,cs.getImageNumberLabelValue(1),"Checkpoint[6/7]","Verifying that after loading DICOM image in viewer there is a change in slice number");
		cs.assertEquals(currentSlice,cs.getCurrentScrollPositionOfViewbox(1),"Checkpoint[7/7]","Verifying the Scroll Position of Image for a ViewBox");
		
		
		
	
	}
		
		
	@Test(groups = { "Chrome", "IE11", "Edge", "DE1925", "Negative","DE1969" })
	public void test21_DE1925_TC7677_DE1969_TC7937_verifyLoadingOfSeriesInEmptyViewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify loading the series in to empty view box after scroll and change layout is performed."
				+ "<br> Re-execute TC7677, Verify loading the series in to empty view box after scroll and change layout is performed.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameMrLSP+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameMrLSP);
		
		patientPage.clickOntheFirstStudy();	
		
		patientPage.clearConsoleLogs();
		
		cs = new ContentSelector_old(driver);
		cs.waitForViewerpageToLoad();
		ViewerLayout layout = new ViewerLayout(driver);
		cs.dragAndReleaseOnViewer(cs.getViewPort(1), 0, 0, 0, 20);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		for(int i =6;i<=cs.getNumberOfCanvasForLayout();i++) {
			cs.selectSeriesFromContentSelectorOnEmptyViewbox(i, cs.getSeriesDescriptionOverlayText(1));		
			cs.assertEquals(cs.getSeriesDescriptionOverlayText(i), cs.getSeriesDescriptionOverlayText(1), "Checkpoint[1/2]", "Verifying that series is loaded in respective viewbox");
			cs.assertFalse(cs.isConsoleErrorPresent(consoleError), "Checkpoint[2/2]", "verifying that there is no such console error");
		}	
		
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE1925", "Negative","DE1931","DE1969" })
	public void test22_DE1925_TC7815_DE1931_DE1969_TC7936_verifyLoadingOfSeriesInEmptyViewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DE1931: Verify the Content Selector is opened on empty view box after drawing an annotation on dicom series and changing the layout."
				+ "<br> Re-execute TC7815 - DE1931: Verify the Content Selector is opened on empty view box after drawing an annotation on dicom series and changing the layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameMrLSP+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameMrLSP);
		
		patientPage.clickOntheFirstStudy();	
				
		patientPage.clearConsoleLogs();
		cs = new ContentSelector_old(driver);
		cs.waitForViewerpageToLoad();
		
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5, 30, 30);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -5, -5, -30, -30);
		ViewerLayout layout = new ViewerLayout(driver);
		List<String> results = cs.getAllResultsFromContentSelector(1);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		for(int i =7;i<=cs.getNumberOfCanvasForLayout();i++) {
			
			cs.selectResultFromContentSelectorOnEmptyViewbox(i,results.get(0));
			cs.assertEquals(cs.getSeriesDescriptionOverlayText(i), cs.getSeriesDescriptionOverlayText(1), "Checkpoint[1/4]", "Verifying that series is loaded in respective viewbox");
			cs.assertFalse(cs.isConsoleErrorPresent(consoleError), "Checkpoint[2/4]", "verifying that there is no such console error");
			circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(i, 1), "Checkpoint[3/4]", "Verifying the circle is present");
			lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(i, 1), "Checkpoint[4/4]", "verifying the measurement is present");
			
		}	
		
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DR2166", "Negative" })
	public void test23_DR2166_TC8658_verifyNoConsoleErrorWhenSeriesLoadedInPdfviewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to load the source series from pdf view box on viewer[Happy Path]");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbioPatientName);
		
		patientPage.clickOntheFirstStudy();	
				
		patientPage.clearConsoleLogs();
		cs = new ContentSelector_old(driver);
		cs.waitForViewerpageToLoad();	
		cs.waitForPdfToRenderInViewbox(2);	
		ViewerLayout layout = new ViewerLayout(driver);
		cs.selectSeriesFromContentSelector(2,cs.getSeriesDescriptionOverlayText(3));
		cs.assertFalse(cs.isConsoleErrorPresent(), "Checkpoint[1/3]", "verifying that there is no such console error");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		for(int i =4;i<=cs.getNumberOfCanvasForLayout();i++) {
			
			cs.selectSeriesFromContentSelector(i,cs.getSeriesDescriptionOverlayText(3));
			cs.assertEquals(cs.getSeriesDescriptionOverlayText(i), cs.getSeriesDescriptionOverlayText(3), "Checkpoint[2/3]", "Verifying that series is loaded in respective viewbox");
			cs.assertFalse(cs.isConsoleErrorPresent(consoleError), "Checkpoint[3/3]", "verifying that there is no such console error");
		}	
		
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DR2166", "Negative" })
	public void test24_DR2166_TC8659_verifyNoConsoleErrorWhenSeriesLoadedInSRviewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to load the source series from SR view box on viewer.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+chestPatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(chestPatientName);
		
		patientPage.clickOntheFirstStudy();
				
		patientPage.clearConsoleLogs();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		cs.waitForViewerpageToLoad(2);		
		cs.closeBanner();
		
		cs.selectSeriesFromContentSelector(1,cs.getSeriesDescriptionOverlayText(2));
		cs.assertFalse(cs.isConsoleErrorPresent(), "Checkpoint[1/3]", "verifying that there is no such console error");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		for(int i =4;i<=cs.getNumberOfCanvasForLayout();i++) {
			
			cs.selectSeriesFromContentSelector(i,cs.getSeriesDescriptionOverlayText(2));
			cs.assertEquals(cs.getSeriesDescriptionOverlayText(i), cs.getSeriesDescriptionOverlayText(2), "Checkpoint[2/3]", "Verifying that series is loaded in respective viewbox");
			cs.assertFalse(cs.isConsoleErrorPresent(consoleError), "Checkpoint[3/3]", "verifying that there is no such console error");
		}	
		
	
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

	public void drawAnnotationAndreloadViewerPage(String patientName,int study,int viewbox) throws InterruptedException
	{
		ellipse.selectEllipseFromQuickToolbar(viewbox);
		ellipse.drawEllipse(viewbox, 100, -50, 40, -50);
		HelperClass helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName, study, viewbox);

		lineWithUnit.selectDistanceFromQuickToolbar(viewbox);
		lineWithUnit.drawLine(viewbox, -70, -80, -120, 90);
		helper.browserBackAndReloadViewer(patientName, study, viewbox);
	}

	public void verifyTootipForEachEntryInResultOrSourceTab(List<WebElement> ListOfResultsOrSeries,List<WebElement> CreatedByUsername) throws TimeoutException, InterruptedException
	{

		for(int i=0;i< ListOfResultsOrSeries.size();i++ )
		{
			viewerpage.mouseHover(ListOfResultsOrSeries.get(i));
			viewerpage.assertEquals(cs.getAttributeValue(ListOfResultsOrSeries.get(i), NSGenericConstants.TITLE),cs.getText(ListOfResultsOrSeries.get(i)),"Verify tooltip for "+" "+ cs.getText(ListOfResultsOrSeries.get(i)), "Verified tooltip for result/Series description");

			viewerpage.mouseHover(CreatedByUsername.get(i));
			viewerpage.assertEquals(cs.getAttributeValue(CreatedByUsername.get(i), NSGenericConstants.TITLE),cs.getText(CreatedByUsername.get(i)),"Verify tooltip of"+ " "+cs.getText(CreatedByUsername.get(i)), "Verified tooltip for createdBy in result/series description ");
		}
	}

	public void verifyEmptyDescriptionText(List<WebElement> ListOfResults,String machineName) throws TimeoutException, InterruptedException
	{ 
		for(int i=0;i< ListOfResults.size();i++ )
		{
			viewerpage.mouseHover(ListOfResults.get(i));
			viewerpage.assertTrue(cs.getText(ListOfResults.get(i)).contains(machineName),"Verify empty description text replace with Created by"+" "+machineName, "Verified");

		}
	}

	public void closeAllExpandOneAtTime(int toggleButton)
	{
		for(int j=0;j<cs.allContentSelectorMachine.size();j++)
		{
			if(cs.getAttributeValue(cs.toggleButton.get(j), NSGenericConstants.TITLE).equalsIgnoreCase(NSGenericConstants.CLOSE))
				cs.click(cs.toggleButton.get(j));
		}
		cs.click(cs.toggleButton.get(toggleButton));
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		String MachineUID="neuro-quant";
		String MachineTitle="'neuro quant'";
		DatabaseMethods db=new DatabaseMethods(driver);
		db.updateMachineTitle(MachineUID,MachineTitle );
	}

}




*/