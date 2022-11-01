package com.trn.ns.test.obsolete;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
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
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerOPRedesignTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ContentSelector contentSelector;
	private MeasurementWithUnit lineWithUnit;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private OutputPanel panel;
	private PointAnnotation point;
	private ViewerLayout layout;

	String flag = "false";
	DatabaseMethods db= new DatabaseMethods(driver);

	// Get Patient Name

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String liver9Filepath =Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9Filepath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	String TDAMaps = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String TDAMaps_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TDAMaps);
	
	String cpuTestFilePath = Configurations.TEST_PROPERTIES.get("cpu_test_Filepath");
	String cpuTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cpuTestFilePath);

	String webGLConsoleError ="Failed to execute 'shaderSource' on 'WebGLRenderingContext': parameter 1 is not of type 'WebGLShader'";
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,
				password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Negative" })
	public void test01_US1174_TC5755_verifyNoSeriesDescDisplayedWhenNoFindings()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Series Description is not displayed in output panel when there is no finding");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(liver9PatientName);

		patientListPage.clickOntheFirstStudy();

		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		panel.enableFiltersInOutputPanel(true, true, true);

		panel.assertFalse(panel.getText(panel.studySummary).isEmpty(), "Checkpoint[1/2]", "Verifying that there is no series description displayed when there is no finding");
		panel.assertFalse(panel.getText(panel.findingSummaryCount).isEmpty(), "Checkpoint[2/2]", "Verifying that there  is no findings count displayed when there is no finding");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive","BVT","DR2210" })
	public void test02_US1174_TC5755_TC5757_DR2210_TC8840_verifySeriesDescDisplayedWhenFindingsArePresentAndDataWOSeriesDesc()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Series Description summary with finding count in output panel"
				+ "<br> Verify Series number is displayed in output panel if Series Description is blank"
				+ "<br> Verify no webGL related console error is observed and thumbnail images are getting rendered properly");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeries + "in viewer");
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(pointMultiSeries);

		patientListPage.clickOntheFirstStudy();
		
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(1);
		panel.waitForViewerpageToLoad(2);


		int viewboxes = panel.getNumberOfCanvasForLayout();
	
		int totalFindings =0;
		for(int i =1 ; i<= viewboxes;i++)
			totalFindings = totalFindings + (panel.getFindingsCountFromFindingTable(i));

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", totalFindings),"Checkpoint[1/6]","Verifying the total findings are displayed");

		for(int i =1;i<= viewboxes;i++) {
			panel.selectFindingFromTable(1,i);
			panel.selectAcceptfromGSPSRadialMenu();
		}

		List<WebElement> acceptedFindings = panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("",acceptedFindings.size()),"Checkpoint[2/6]","Verifying only accepted findings");

		List<Integer> pendingFindings = new ArrayList<Integer>();
		for(int i =1 ; i<= viewboxes;i++)
			pendingFindings.add(panel.getStateSpecificFindings(i, ViewerPageConstants.PENDING_FINDING_COLOR).size());			
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", pendingFindings.size()),"Checkpoint[3/6]","Verifying only pending findings");


		for(int i =1 ; i<= viewboxes;i++) {
			panel.selectFindingFromTable(1,i);
			panel.selectRejectfromGSPSRadialMenu();
		}
		List<WebElement> rejectedFindings = panel.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR);
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", rejectedFindings.size()),"Checkpoint[4/6]","Verifying only rejected findings");		

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", totalFindings),"Checkpoint[5/6]","Verifying total findings displayed");
		
		panel.assertFalse(panel.isConsoleErrorPresent(webGLConsoleError),"Checkpoint[6/6]","Verify that Output panel should get opened and should not be any console error.");


	}

	//OBSOLETE
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive" })
//	public void test03_US1174_TC5755_verifySeriesDescDisplayedWhenFindingsArePresentForLiver9()
//			throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Series Description summary with finding count in output panel");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(liver9PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad(1);
//		panel.waitForViewerpageToLoad(2);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(1);
//
//		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, 50, -50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -50, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 60);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, -60);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, 60);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -60);
//		point.drawPointAnnotationMarkerOnViewbox(1, -40, 40);
//		point.drawPointAnnotationMarkerOnViewbox(1, 40, -40);		
//
//		point.drawPointAnnotationMarkerOnViewbox(2, 50, 50);
//		point.drawPointAnnotationMarkerOnViewbox(2, 50, -50);
//		point.drawPointAnnotationMarkerOnViewbox(2, -50, 50);
//		point.drawPointAnnotationMarkerOnViewbox(2, -50, -50);
//		point.drawPointAnnotationMarkerOnViewbox(2, 60, 60);
//		point.drawPointAnnotationMarkerOnViewbox(2, 60, -60);
//		point.drawPointAnnotationMarkerOnViewbox(2, -60, 60);
//		point.drawPointAnnotationMarkerOnViewbox(2, -60, -60);
//		point.drawPointAnnotationMarkerOnViewbox(2, -40, 40);
//		point.drawPointAnnotationMarkerOnViewbox(2, 40, -40);
//
//		point.closingWarningAndWaterMark();
//		int viewboxes = 2;
//		List<SeriesSummary> summary = new ArrayList<SeriesSummary>(); 
//
//		for(int i =1 ; i<= viewboxes;i++)		
//			summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(i),panel.getFindingsCountFromFindingTable(i)));		
//		Collections.sort(summary,SeriesSummary.NameComparator);		
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true, summary),"Checkpoint[1/5]","Verifying the order of series and total findings");
//
//		for(int i =1 ; i<= viewboxes;i++) {
//
//			for(int j =1 ; j<= viewboxes;j++) {
//				panel.selectFindingFromTable(i,j);
//				panel.selectAcceptfromGSPSRadialMenu();
//			}
//		}
//
//		summary.clear();
//		for(int i =1 ; i<= viewboxes;i++)
//		{
//			summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(i),panel.getStateSpecificFindings(i, ViewerPageConstants.ACCEPTED_COLOR).size()));
//		}		
//
//		Collections.sort(summary,SeriesSummary.NameComparator);			
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, false, false, summary),"Checkpoint[2/5]","Accepting some findings and validating the accepted findings count");
//
//		summary.clear();
//		for(int i =1 ; i<= viewboxes;i++)
//		{
//			summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(i),panel.getStateSpecificFindings(i, ViewerPageConstants.PENDING_COLOR).size()));
//		}		
//		Collections.sort(summary,SeriesSummary.NameComparator);			
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(false, false, true, summary),"Checkpoint[3/5]","Pending some findings and validating the pending findings count with series");
//
//		for(int i =1 ; i<= viewboxes;i++) {
//
//			for(int j =7 ; j<= 10;j++) {
//				panel.selectFindingFromTable(i,j);
//				panel.selectRejectfromGSPSRadialMenu();
//			}
//		}
//
//		summary.clear();
//		for(int i =1 ; i<= viewboxes;i++)
//		{
//			summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(i),panel.getStateSpecificFindings(i, ViewerPageConstants.REJECTED_COLOR).size()));
//		}		
//		Collections.sort(summary,SeriesSummary.NameComparator);
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(false, true, false, summary),"Checkpoint[4/5]","rejected some findings and validating the rejected findings count with series");		
//
//
//		summary.clear();
//		for(int i =1 ; i<= viewboxes;i++)
//		{
//			summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(i),panel.getFindingsCountFromFindingTable(i)));
//		}		
//		Collections.sort(summary,SeriesSummary.NameComparator);	
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true,summary),"Checkpoint[5/5]","Validating all the findings with series if system has all three findings available");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive" }, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
//	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test04_outputPanel"})
//	public void test04_01_US1174_TC5791_verifySeriesDescDisplayedForDIffTypesOfData(String patientFilePath)
//			throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Series Description summary with finding count in output panel");
//
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		ContentSelector cs = new ContentSelector(driver);
//
//		List<String> results = cs.getAllResults();
//		List<SeriesSummary> summary = new ArrayList<SeriesSummary>(); 
//
//
//		for(int i =0 ; i< results.size();i++)
//		{
//			summary.add(new SeriesSummary(results.get(i),1));
//		}
//		Collections.sort(summary,SeriesSummary.NameComparator);	
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true,summary),"Checkpoint[1/1]","verifying the SC and PDF and JPEGs in output panel for series description with count");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive" , "DR2210"})
//	public void test04_02_US1174_TC5791_DR2210_TC8840_verifySeriesDescDisplayedForRT()
//			throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Series Description summary with finding count in output panel"
//				+ "<br> Verify no webGL related console error is observed and thumbnail images are getting rendered properly");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientNameDICOMRT + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForAllImagesToLoad();
//		panel.waitForRespectedViewboxToLoad(1);
//
//		List<SeriesSummary> summary = new ArrayList<SeriesSummary>(); 
//
//		summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(1),panel.getFindingsCountFromFindingTable()));
//
//		Collections.sort(summary,SeriesSummary.NameComparator);	
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true,summary),"Checkpoint[1/2]","Verifying the RT data with findings in output panel");
//		
//		panel.assertFalse(panel.isConsoleErrorPresent(webGLConsoleError),"Checkpoint[2/2]","Verify that Output panel should get opened and should not be any console error.");
//
//		
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive" })
//	public void test04_03_US1174_TC5791_verifySeriesDescDisplayedForSR()
//			throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Series Description summary with finding count in output panel");
//
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + ChestCT1p25mm + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(ChestCT1p25mm);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForAllImagesToLoad();
//		panel.waitForRespectedViewboxToLoad(2);
//
//		if(panel.isElementPresent(panel.banner))
//			panel.click(panel.bannerCloseIcon);
//		
//		List<SeriesSummary> summary = new ArrayList<SeriesSummary>(); 
//
//		summary.add(new SeriesSummary(panel.getResultDescriptionOverlayText(1),1));
//		summary.add(new SeriesSummary(panel.getSeriesDescriptionOverlayText(2),panel.getFindingsCountFromFindingTable(2)));
//
//		Collections.sort(summary,SeriesSummary.NameComparator);	
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true,summary),"Checkpoint[1/1]","Verifying the SR data with findings in output panel");
//
//	}
//
//	//DE1429: [Hardening]-Output panel is not getting closed on clicking on PDF view box
//	@Test(groups ={"Chrome","IE11","Edge","DE1429","Sanity","DR2210"})
//	public void test05_DE1429_TC5784_TC5827_DR2210_TC8840_VerifyMultipleEntriesForTDAMaps()throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that output panel is not showing duplicate entry for a single finding <br>"+
//				"Verify that output panel is not showing duplicate entry for a single finding."
//				+ "<br> Verify no webGL related console error is observed and thumbnail images are getting rendered properly");
//
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(TDAMaps_PatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad(1);
//		lineWithUnit=new MeasurementWithUnit(driver);		
//		contentSelector = new ContentSelector(driver);
//
//		lineWithUnit.selectDistanceFromQuickToolbar(1);
//		lineWithUnit.drawLine(1, -50, -50, 100, 0);
//
//		panel.enableFiltersInOutputPanel(true, false,false);
//		panel.waitForOutputPanelToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Veriying the count in output panel.");
//		panel.assertEquals(1, panel.thumbnailList.size(), "Verifying the count in thumbnail should be 1 only. It should not be multiplied.", "Thumbnail count is 1 ");
//		panel.openAndCloseOutputPanel(false);
//
//		//Implementing TC5784, Drawing annotation 2nd phase in 1st view box and verifying the series description entries in output panel.
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verifying series descritpions and findings count in output panel");
//		panel.click(panel.getViewPort(1));
//		panel.inputPhaseNumber(2, 1);
//
//		lineWithUnit.selectDistanceFromQuickToolbar(1);
//		lineWithUnit.drawLine(1, -50, -40, 80, 0);
//
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//
//		String Series1=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";
//		contentSelector.selectResultFromSeriesTab(2, Series1, 2);
//
//		int viewboxes = panel.getNumberOfCanvasForLayout();
//		List<String> seriesDesc = new ArrayList<String>();
//		List<Integer> findingsCount = new ArrayList<Integer>();
//		for(int i =1 ; i<= viewboxes;i++)
//		{
//			seriesDesc.add(panel.getSeriesDescriptionOverlayText(i));
//			panel.click(panel.getViewPort(i));
//			findingsCount.add(panel.getFindingsCountFromFindingTable(i));
//
//		}
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(true, true, true,seriesDesc,findingsCount),"Verifying Series descritpions and finding count are same in output panel as in viewer","Series descriptions and count are same.");
//
//		panel.assertFalse(panel.isConsoleErrorPresent(webGLConsoleError),"Checkpoint[3/3]","Verify that Output panel should get opened and should not be any console error.");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1159", "Negative" })
//	public void test06_US1159_TC5747_TC5748_TC5771_TC5783_verifyStylingAndRestructuringOfOutputPanel()throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify 'A/R/P' filters and row indicator colors on output panel window");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(liver9PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		lineWithUnit=new MeasurementWithUnit(driver);
//		ellipse=new EllipseAnnotation(driver);
//		circle=new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1,  100, 100, 100, 100);
//		
//		lineWithUnit.selectDistanceFromQuickToolbar(1);
//		lineWithUnit.drawLine(1, -70, -80, -120, 90);
//		lineWithUnit.selectRejectfromGSPSRadialMenu();
//		
//		lineWithUnit.closingWarningAndWaterMark();
//		ellipse.selectEllipseFromQuickToolbar(2);
//		ellipse.drawEllipse(1, 100, -50, 40, -50);
//		ellipse.selectAcceptfromGSPSRadialMenu();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify if \'A/R/P\' buttons are not selected then only button borders are displayed with the corresponding color");
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.getCssValue(panel.acceptedButton, NSGenericConstants.CSS_PROP_BORDER_COLOR),ViewerPageConstants.ACCEPT_CHECKBOX_COLOR, "Checkpoint[1/16]", "Verified the border color for accepted button");
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.CSS_PROP_BORDER_COLOR), ViewerPageConstants.REJECTED_BORDER_COLOR_OP, "Checkpoint[2/16]", "Verified the border color for rejected button");
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.CSS_PROP_BORDER_COLOR), ViewerPageConstants.PENDING_BORDER_COLOR_OP, "Checkpoint[3/16]", "Verified the border color for pending button");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify if \'A/R/P\' buttons on mouse hover then whole button should display with the corresponding color");
//		panel.assertEquals(panel.getCssValue(panel.acceptedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Checkpoint[4/16]", "Verified the background color for accepted button.");
//		panel.mouseHover(panel.rejectedButton);
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.REJECTED_FINDING_COLOR, "Checkpoint[5/16]",  "Verified the background color for rejected button on mouse hover");
//		panel.mouseHover(panel.pendingButton);
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.PENDING_FINDING_COLOR, "Checkpoint[6/16]",  "Verified the background color for pending button on mouse hover");
//		panel.openAndCloseOutputPanel(false);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify if \'A/R/P\' buttons are selected then whole button should display with the corresponding color.");
//		panel.click(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true, true);
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.REJECTED_FINDING_COLOR, "Checkpoint[7/16]", "Verified the backgroung color for rejected button");
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.PENDING_FINDING_COLOR, "Checkpoint[8/16]", "Verified the backgroung color for pending button");
//		panel.mouseHover(panel.acceptedButton);
//		panel.assertEquals(panel.getCssValue(panel.acceptedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Checkpoint[9/16]","Verified the backgroung color for accepted button on mouse hover");
//		panel.openAndCloseOutputPanel(false);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify row indicator should display right color to know the finding state easily");
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.assertTrue(panel.getAttributeValue(panel.findingStateIndicator.get(0), NSGenericConstants.STYLE).contains(ViewerPageConstants.ACCEPTED_COLOR), "Checkpoint[10/16]", "Verified the state indicator color for accepted finding as Green");
//		panel.assertTrue(panel.getAttributeValue(panel.findingStateIndicator.get(1), NSGenericConstants.STYLE).contains(ViewerPageConstants.REJECTED_COLOR), "Checkpoint[11/16]",  "Verified the state indicator color for rejected finding as Red");
//		panel.assertTrue(panel.getAttributeValue(panel.findingStateIndicator.get(2), NSGenericConstants.STYLE).contains(ViewerPageConstants.PENDING_COLOR), "Checkpoint[12/16]", "Verified the state indicator color for pending finding as Lighblue");
//		
//		//take screenshot for output panel
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		panel.takeElementScreenShot(panel.entireOutputPanel, newImagePath+"/goldImages/test06_OutputPanelUI_expected.png");
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify row indicator should display right color to know the finding state easily");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[13/16]", "Verified that all panel list item are in expanded state");
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify labeling,alignment and structure of the text fields like creator name,Creation date");	
//		
//		panel.takeElementScreenShot(panel.entireOutputPanel, newImagePath+"/actualImages/test06_OutputPanelUI_actual.png");		
//		String expectedImagePath = newImagePath+"/goldImages/test06_OutputPanelUI_expected.png";
//		String actualImagePath = newImagePath+"/actualImages/test06_OutputPanelUI_actual.png";		
//		String diffImagePath = newImagePath+"/actualImages/test06_OutputPanelUI_diff.png";
//
//		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		panel.assertTrue(cpStatus, "Checkpoint[14/16] The actual and Expected image both are same","Verified UI of the Output panel");	
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify Output panel after resize browser window.");
//		panel.resizeBrowserWindow(1200,900);
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[15/16]", "Verified that all panel list item are in expanded state");
//		panel.assertTrue(panel.isElementPresent(panel.entireOutputPanel), "Checkpoint[16/16]", "Verified that OP remain open after resize the browser window.");
//		
//		
//		
//	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1541", "Positive" })
	public void test07_DE1541_TC7358_verifySizeOfButtons() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("output panel");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(liver9PatientName);

		patientListPage.clickOntheFirstStudy();

		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
	
//		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.WIDTH),ViewerPageConstants.OUTPUT_PANEL_OPEN_WIDTH,"Checkpoint[1/6]","Verifying the width of output panel open half circle button");
//		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.HEIGHT),ViewerPageConstants.OUTPUT_PANEL_OPEN_HEIGHT,"Checkpoint[2/6]","Verifying the height of output panel open half circle button");
//		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.CSS_BORDER_RADIUS),ViewerPageConstants.OUTPUT_PANEL_OPEN_BORDER_RADIUS,"Checkpoint[3/6]","Verifying the radius of output panel open half circle button");
//		panel.enableFiltersInOutputPanel(true, false, false);
//		
//		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.WIDTH),ViewerPageConstants.OUTPUT_PANEL_CLOSE_WIDTH,"Checkpoint[4/6]","Verifying the width of output panel close half circle button");
//		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.HEIGHT),ViewerPageConstants.OUTPUT_PANEL_CLOSE_HEIGHT,"Checkpoint[5/6]","Verifying the height of output panel close half circle button");
//		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.CSS_BORDER_RADIUS),ViewerPageConstants.OUTPUT_PANEL_CLOSE_BORDER_RADIUS,"Checkpoint[6/6]","Verifying the radius of output panel close half circle button");
		
		panel.openAndCloseOutputPanel(false);

	

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DR2371","Negative"})
	public void test08_DE2371_TC9360_VerifyMultipleEntriesForCPUTest()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that User created GSPS object on DICOM Stripe machine is not duplicated in output panel");

		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(cpuTestPatientName, 1);
		
		panel = new OutputPanel(driver);
		panel.pressKey(NSGenericConstants.DOT_KEY);
		
		lineWithUnit=new MeasurementWithUnit(driver);		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		panel.enableFiltersInOutputPanel(true, false,false);
		panel.waitForOutputPanelToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Veriying the count in output panel.");
		panel.assertEquals(panel.thumbnailList.size(),1, "Verifying the count in thumbnail should be 1 only. It should not be multiplied.", "Thumbnail count is 1 ");
		
		panel.enableFiltersInOutputPanel(false, true,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Verifying the count in thumbnail. It should not be multiplied.", "Thumbnail count is 0 under rejected tab");
		

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Verifying the count in thumbnail. It should not be multiplied.", "Thumbnail count is 0 under pending tab");
		
		panel.openAndCloseOutputPanel(false);
		
	}
	
}