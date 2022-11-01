package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
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
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LossyOverlayTest extends TestBase{

	
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private CircleAnnotation circle;
	private OutputPanel panel;
	private String protocolName;
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewBoxToolPanel presetMenu;
	private PMAP pmap;
	
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	
	String ChestCT1p25mmFilePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mmFilePath);
	
	String imbioFilepath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilepath);
	
	String gspsFilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath); 

	String pmapFilePath = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientId = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmapFilePath);
	
	String anonymousFilePath = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymousPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymousFilePath);
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException, IOException, InterruptedException {

		db=new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, NSDBDatabaseConstants.ADMINUSER,username);
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US1742","Positive","E2E","F195"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test04_LossyRenderingTest"})
	public void test01_US1452_TC8735_verifyLossyOverlayOnDifferentDataset(String filepath) throws InterruptedException, AWTException

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Lossy' is getting displayed always on viewer for all dataset - RT, PMAP, GSPS, SC , CAD");

		String patientName=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,Configurations.TEST_PROPERTIES.get(filepath) );
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
	
		cs=new ContentSelector(driver);
		List<String> result= cs.getAllResults();
		List<String> series= cs.getAllSeries();

		for(int i=0;i<result.size();i++)
		{
			cs.selectResultFromSeriesTab(1, result.get(i));
			viewerPage.doubleClick(viewerPage.getViewPort(1));
			viewerPage.waitForAllChangesToLoad();
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"Checkpoint[1."+(i+1)+"/2]","Verifying lossy annotation presence in viewbox for resultname "+result.get(i));
			
		}
		
		for(int i=0;i<series.size();i++)
		{
			cs.selectSeriesFromSeriesTab(1, series.get(i));
			viewerPage.doubleClick(viewerPage.getViewPort(1));
			viewerPage.waitForAllImagesToLoad();
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"Checkpoint[2."+(i+1)+"/2]","Verifying lossy annotation presence in viewbox for seriesname "+series.get(i));
			
		}
		

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US1742","Positive","E2E","F195"})
	public void test02_US1452_TC8735_verifyLossyOverlayForSRData() throws InterruptedException, AWTException 

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Lossy' is getting displayed always on viewer for all dataset - RT, PMAP, GSPS, SC , CAD");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Loading the Patient "+ChestCT1p25mmPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ChestCT1p25mmPatientName);

		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
	
		viewerPage.closeNotification();
		cs=new ContentSelector(driver);
		List<String> result= cs.getAllResults();
		List<String> series= cs.getAllSeries();
		
	    cs.selectResultFromSeriesTab(2, result.get(0), 2);
	    viewerPage.doubleClick(viewerPage.getViewPort(2));
	    viewerPage.waitForAllChangesToLoad();
	    viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"Checkpoint[1/2]","Verified lossy annotation text presence on resultname "+result.get(0));
			
		for(int i=0;i<series.size();i++)
		{
			cs.selectSeriesFromSeriesTab(2, series.get(i));
			viewerPage.doubleClick(viewerPage.getViewPort(2));
			viewerPage.waitForAllImagesToLoad();
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"Checkpoint[2."+(i+1)+"/2]","Verifying lossy annotation presence in viewbox for seriesname "+series.get(i));	
		}
	
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1742","Positive","E2E","F195"})
	public void test03_US1452_TC8736_verifyLossyOverlayForSCData() throws InterruptedException, AWTException 

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Lossy' is getting displayed always on viewer after changing layout or selecting series from content selector or after selecting jumpToIcon from output panel");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/]", "Loading the Patient "+ChestCT1p25mmPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbioPatientName);

		patientPage.clickOntheFirstStudy();
		
		panel=new OutputPanel(driver);
		panel.waitForPdfToRenderInViewbox(2);
	
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs=new ContentSelector(driver);
		List<String> series= cs.getAllSeries();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Lossy text on dicom series and SC series result.");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(1)),"Checkpoint[1/5]","Verifying lossy annotation presence in viewbox1");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(3)),"Checkpoint[2/5]","verifying lossy annotation presence in viewbox3");
			
		//select series from CS and verify lossy text
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify lossy text on viewer after selecting series from Content selector.");
		cs.selectSeriesFromSeriesTab(4, series.get(0));
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(3)),"Checkpoint[3/5]","Verifying lossy annotation presence in viewbox4 after selecting series from Content selector.");
		
		//draw annotation and click on jump from OP
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw annotation and perform jump icon form OP and verify Lossy text on viewer.");
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(4);
		circle.drawCircle(4,10, 10, 50, 50);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(4, 1), "Checkpoint[4/5]", "Verified that circle annotation is active accepted GSPS.");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(4)),"Checkpoint[5/5]","Verifying lossy annotation presence after click on jump icon.");
	
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1742","Positive","E2E","F195"})
	public void test04_US1452_TC8743_verifyLossyOverlayForUserDrawnAnnotation() throws InterruptedException

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotations can be drawn successfully when Lossy rendering mode is selected");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(gspsPatientName);

		patientPage.clickOntheFirstStudy();
		
		panel=new OutputPanel(driver);
		panel.waitForViewerpageToLoad(1);
	
		String textAnnot1="ABC1";
				
		int findingCount1=panel.getBadgeCountFromToolbar(1);
		panel.click(panel.getViewPort(1));
		panel.click(panel.getViewPort(2));
		int findingCount2=panel.getBadgeCountFromToolbar(2);
		
		cs=new ContentSelector(driver);
		List<String> result= cs.getAllResults();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify lossy overlay on bot viewbox and verify finding count in Output panel.");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(1)),"Checkpoint[1/10]","Verifying lossy annotation presence in viewbox1");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(2)),"Checkpoint[2/10]","Verifying lossy annotation presence in viewbox2");
		
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), findingCount1+findingCount2, "Checkpoint[3/10]", "Verified finding count from finding menu with Output panel finding menu.");
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw all types of annotation on viewer." );
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		
		int[] coordinates = new int[] {75,75,-75,75,-75,-75,75,-75,75,75};
		poly.drawClosedPolyLine(1, coordinates);
		
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 0, 75, 0);
		
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,10, 10, 25, 25);
		
		
		PointAnnotation point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-150,-150);
		
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -25, -50,-75);
		
		SimpleLine line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,-50,0,-75,0);
		
		TextAnnotation textAn = new TextAnnotation(driver);
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,150,-150,textAnnot1);
		
		panel.assertEquals(cs.getAllResults().size(), result.size()*2, "Checkpoint[4/10]", "Verified that new clone is created after drawing annotations.");
	    int acceptedCount=panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size();
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), acceptedCount ,"Checkpoint[5/10]", "Verified accepted user drawn annotation count in Output panel.");
		
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(gspsPatientName,1,1);
		
		patientPage.clickOntheFirstStudy();
		panel.waitForViewerpageToLoad();
		
		panel.assertEquals(panel.getFindingsCountFromFindingTable(1), acceptedCount+findingCount1, "Checkpoint[6/10]", "Verified count of machine and user drawn annotation in first viewbox.");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(1)), "Checkpoint[7/10]","verifying lossy annotation presence in viewbox-1");
		panel.click(panel.getViewPort(2));
		panel.assertEquals(panel.getFindingsCountFromFindingTable(2), findingCount2, "Checkpoint[8/10]", "Verified count of machine findings on viewbox-2.");
		panel.assertTrue(panel.isElementPresent(panel.getLossyOverlay(2)),"Checkpoint[9/10]","Verifying lossy annotation presence in viewbox-2");
		
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), acceptedCount+findingCount1+findingCount2 ,"Checkpoint[10/10]", "Verified user and machine findings in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
	
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1472"})
	public void test05_US1472_TC8744_verifyZoomPanWWWCForDicom() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all DICOM operations (WWWL/zoom/scroll/cine/orientation change/PAN) can be performed successfully when Lossy rendering mode is selected");
		
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(gspsPatientName);
		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();	
        presetMenu=new ViewBoxToolPanel(driver);
  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+gspsPatientName+" on viewer." );
		
		String zoomValue=presetMenu.getZoomLevelValue(1);
		String windowWidth=viewerPage.getWindowWidthValueOverlayText(1);
		String windowCentre=viewerPage.getWindowCenterValueOverlayText(1);
		String imageNumber=viewerPage.getCurrentScrollPosition(1);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		//verify zoom 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify zoom synchronization for point multiseries data when rendering mode is lossy." );
		viewerPage.takeElementScreenShot(viewerPage.mainViewer, newImagePath+"/goldImages/test05_ImageBeforeZoom.png");
		
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0,0,50,100);
		
	    for(int i=1;i<=viewerPage.getNumberOfCanvasForLayout();i++)
		viewerPage.assertNotEquals(presetMenu.getZoomLevelValue(i), zoomValue,"Verify zoom sync for viewbox-"+i,"Verified");
	    
	    viewerPage.takeElementScreenShot(viewerPage.mainViewer, newImagePath+"/actualImages/test05_ImageAfterZoom.png");

		String expectedImagePath = newImagePath+"/goldImages/test05_ImageBeforeZoom.png";
		String actualImagePath =newImagePath+"/actualImages/test05_ImageAfterZoom.png";
		String diffImagePath = newImagePath+"/actualImages/diff_Image.png";

		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that Zoom applied successfuly on all viewboxes.");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify WWWC value after selection from Preset menu." );
    
		presetMenu.changeZoomNumber(1, 100);
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth,"Verify zoom sync for viewbox-"+1+" for preset value "+ViewerPageConstants.ZOOM_TO_100_PERCENTAGE,"Verified");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCentre,"Verify zoom sync for viewbox-"+2+" for preset value "+ViewerPageConstants.ZOOM_TO_100_PERCENTAGE,"Verified");
		List<String> presetOption=presetMenu.getWWWLPresetOptions(1);
		
		for(int i=presetOption.size()-2;i>0;i--){
		presetMenu.selectPresetValue(1, presetOption.get(i));
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth,"Verify window width for viewbox-1 for preset value "+presetOption.get(i),"Verified");
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(2), windowCentre,"Verify window width  for viewbox-2 for preset value "+presetOption.get(i),"Verified");
		
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1), windowWidth,"Verify window centre  for viewbox-1 for preset value "+presetOption.get(i),"Verified");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCentre,"Verify window centre for viewbox-2 for preset value "+presetOption.get(i),"Verified");
		
		}
		
        presetOption=presetMenu.getWWWLPresetOptions(1);
		
		for(int i=presetOption.size()-2;i>0;i--){
			presetMenu.selectPresetValue(1, presetOption.get(i));
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth,"Verify window width  for viewbox-1 for preset value "+presetOption.get(i),"Verified");
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(2), windowCentre,"Verify window width  for viewbox-2 for preset value "+presetOption.get(i),"Verified");
		
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1), windowWidth,"Verify window centre for viewbox-1 for preset value "+presetOption.get(i),"Verified");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCentre,"Verify window centre for viewbox-2 for preset value "+presetOption.get(i),"Verified");
		
		}
		

		//verify scroll
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify scroll synchronization when rendering mode is lossy.");
		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.scrollDownToSliceUsingKeyboard(3);
		for(int i=1;i<=viewerPage.getNumberOfCanvasForLayout();i++)
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(i), imageNumber,"Verify scroll sync for viewbox-"+i,"Verified");
	
		//verify PAN sync
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify PAMP synchronization when rendering mode is lossy.");
		
		viewerPage.takeElementScreenShot(viewerPage.mainViewer, newImagePath+"/goldImages/test05_ImageWithoutPAN.png");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, -50,-50);
	
		viewerPage.takeElementScreenShot(viewerPage.mainViewer, newImagePath+"/actualImages/test05_ImageWithPAN.png");

		 expectedImagePath = newImagePath+"/goldImages/test05_ImageWithoutPAN.png";
		 actualImagePath = newImagePath+"/actualImages/test05_ImageWithPAN.png";
		 diffImagePath = newImagePath+"/actualImages/diff.png";

		 cpStatus=  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that PAN applied successfuly on all viewboxes.");
	
		//verify cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify cine functionality when rendering mode is lossy.");
		
		int currentImage = viewerPage.convertIntoInt(viewerPage.getCurrentScrollPosition(1));
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.waitForTimePeriod(500);
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));

		viewerPage.assertNotEquals(viewerPage.convertIntoInt(viewerPage.getCurrentScrollPosition(1)), currentImage, "verifying the cine perform on viewbox-1", "Verified");
		viewerPage.assertNotEquals(viewerPage.convertIntoInt(viewerPage.getCurrentScrollPosition(2)), currentImage, "verifying the cine perform on viewbox-2", "Verified");
	
	}
 
	@Test(groups ={"Chrome","Edge","IE11","US1472","Negative"})
	public void test06_US1472_TC8745_verifyPMAPData() throws InterruptedException 
	   {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP is getting applied properly when Lossy rendering mode is selected");
			
		helper=new HelperClass(driver);
		viewerPage = helper.loadViewerDirectlyUsingID(pmapPatientId, 1);
		
		viewerPage.waitForViewerpageToLoad();
		viewerPage.waitForAllImagesToLoad();
		pmap=new PMAP(driver);
			
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"Checkpoint[1/7]","verifying lossy annotation presence in viewbox1 on pmap data default load");
		viewerPage.assertTrue(viewerPage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[2/7]", "Verifying the gradient bar is displayed on default load.");
			
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerPage.assertTrue((pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON)),"Checkpoint[3/7]", "Verifying '"+ViewerPageConstants.HOTIRON+"' color is selected'");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"Checkpoint[4/7]","verifying lossy annotation presence in viewbox1 on pmap data after selecting color from LUT bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[5/7]", "Verifying the gradient bar is displayed after selecting color from LUT bar");
			
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));	
			
		for(int i =0 ;i<4;i++) {
			viewerPage.mouseHover(viewerPage.getViewPort(1));
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"Checkpoint[6/7]","verifying lossy annotation presence in viewbox1 on pmap data");
			viewerPage.assertTrue(viewerPage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[7/7]", "Verifying the gradient bar is displayed");
			}
			viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));	
			

		}	
		
	@Test(groups ={"firefox","Chrome","IE11","Edge","DR2280","Positive"})
	public void test07_DR2280_TC9196_verifyConsoleForNonDicomData() throws InterruptedException, AWTException 

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact] DR2268 - TC9093");
		patientPage = new PatientListPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ChestCT1p25mmPatientName+"in viewer" );
		
		helper=new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(anonymousPatientName,2);
	
		viewerPage.closeNotification();
		cs=new ContentSelector(driver);
		List<String> result= cs.getAllResults();
		List<String> series= cs.getAllSeries();
		
	    cs.selectSeriesFromSeriesTab(1, series.get(0));
	    viewerPage.assertTrue(viewerPage.verifyDicomImageLoadedInViewer(1), "Checkpoint[1/4]"," Verifying series loaded on viewbox-1 after selecting from CS.");
	    viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Checkpoint[2/4]","Verified lossy annotation text presence on seriesname "+series.get(0));
			
		for(int i=0;i<result.size();i++)
		{
			cs.selectResultFromSeriesTab(1, result.get(i));
			viewerPage.assertTrue(viewerPage.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[3/4]"," Verifying non-DICOM image loaded on viewbox-1 after selecting from CS.");
			viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Checkpoint[4."+(i+1)+"/4]","Verifying no console error present for resultName "+result.get(i));	
		}
	
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		db=new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, NSDBDatabaseConstants.NONADMINUSER,username);
	}

}
