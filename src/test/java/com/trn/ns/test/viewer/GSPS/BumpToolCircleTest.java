package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.BumpToolCircle;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class BumpToolCircleTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private DICOMRT drt;
	private static int seriesLevelID;
	String patientID ="";
	
	private PolyLineAnnotation polylines;
	private ContentSelector cs;
	private BumpToolCircle bumpToolCircle;
	private PolyLineAnnotation polyline;
	private OutputPanel panel;


	String filePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POLYLINE_filepath");
	String gspsPolyline = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String filePath2 =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	
	String filePath3 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	
	String filePath4 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	
	String filePath5=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath5);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String pmapFilepath = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmapFilepath);

	String lungFilepath = Configurations.TEST_PROPERTIES.get("Lung_LIDC_0405_Filepath");
	String lungPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, lungFilepath);

	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username,password);
		
		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);

	}

	@Test(groups = {"Chrome","IE11","Edge","US1412","Positive","BVT"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_bumpCircle"})
	public void test01_US1412_TC7743_verifyActivationAndUIForBumpTool(String patientFilePath, String contentSelectorFlag) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify activation and the UI of the 'Bump tool' on viewer.");

		String filepath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath);
		
		if(patientName.isEmpty())
			patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filepath);
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		if(patientName.isEmpty()) {			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientID);
			helper.loadViewerDirectlyUsingID(patientID, 1);
		}
		else { 
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientName);
			helper.loadViewerDirectly(patientName, 1);
		}
		
		drt =new DICOMRT(driver);
		if(drt.isElementPresent(drt.legendOptions))
			drt.waitForDICOMRTToLoad();
		else
			drt.waitForViewerpageToLoad();

		polylines = new PolyLineAnnotation(driver);
		bumpToolCircle = new BumpToolCircle(driver);

		if(polylines.getPolylineCount(1)>=1)
			bumpToolCircle.assertTrue(bumpToolCircle.enableBumpTool(1),"Checkpoint[1/4]","Verifying that bump tool is enabled when there are polyline present and focused");
		else
			bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(1),"Checkpoint[1/4]","Verifying that bump tool is not enabled when there are no polylines present");
		
		
		if(contentSelectorFlag.equalsIgnoreCase("Y")) {
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the result from content selector which has polyline");
			cs = new ContentSelector(driver);	
			List<String> results = cs.getAllResults();
			cs.selectResultFromSeriesTab(1, results.get(0));
		}
		int count = polylines.getPolylineCount(1);

		if(count==0) {
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating the polyline if result / series have none");
			int[] abc = new int[] {-50,-50,50,-50,50,50,-50,50,-50,-50};
			
			polylines.selectPolylineFromQuickToolbar(1);
			polylines.drawFreehandPolyLine(1, abc);

		}
		bumpToolCircle.assertTrue(bumpToolCircle.enableBumpToolWithOffset(1,-100,10),"Checkpoint[2/4]","Verifying the bump tool is enabled and white circle is displayed");
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -100,10 ,-150, 10),"Checkpoint[3/4]","Verifying that on mouse movement bump tool remains enabled and circle remains of same size");

		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		if(!polylines.isFreeHandPolylineSelected(1,1)){
			if(drt.isElementPresent(drt.legendOptions))
				drt.click(drt.segmentstateicon.get(1));
			else
				polylines.selectPolyline(1, 1);
			
		}
		bumpToolCircle.assertTrue(bumpToolCircle.enableBumpToolWithOffset(1,-100,10),"Checkpoint[4/4]","Verifying that after changing layout the bump tool is getting enabled");

	}

	@Test(groups = {  "Chrome", "IE11", "Edge" , "US1412","Positive","BVT"})
	public void test02_US1412_TC7748_verifySizeOfBumpToolBasedOnLocation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the size of the 'Bump tool'");

		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();

		polylines = new PolyLineAnnotation(driver);
		bumpToolCircle = new BumpToolCircle(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Enabling the bump tool near contour and retrieving the radius and coordinates");
		HashMap<String, Double> bumpCircle = bumpToolCircle.enableBumpTool(1, 10, 10);

		Double radius = (bumpCircle.get(NSGenericConstants.R));
		Double xCoordinate = (bumpCircle.get(NSGenericConstants.CX));
		Double yCoordinate = (bumpCircle.get(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Enabling the bump tool away from contour and retrieving the radius and coordinates");
		HashMap<String, Double> awaybumpCircle = bumpToolCircle.enableBumpTool(1, 300, 300);

		Double radiusA = (awaybumpCircle.get(NSGenericConstants.R));
		Double xCoordinateA =(awaybumpCircle.get(NSGenericConstants.CX));
		Double yCoordinateA =(awaybumpCircle.get(NSGenericConstants.CY));
		
		bumpToolCircle.assertNotEquals(radius, radiusA, "Checkpoint[1/4]","Verifying that bump tool circle is radius different when enabled from different location");
		bumpToolCircle.assertTrue(radius<radiusA, "Checkpoint[2/4]","verifying the bump tool circle size is big which is not near to contour");
		bumpToolCircle.assertNotEquals(xCoordinate, xCoordinateA, "Checkpoint[3/4]","verifying the x coordinates is not same");
		bumpToolCircle.assertNotEquals(yCoordinate, yCoordinateA, "Checkpoint[4/4]","verifying the y coordinates is not same");
				
		
	}
	
	@Test(groups = {  "Chrome", "IE11", "Edge" , "US1412","Negative"})
	public void test03_US1412_TC7766_verifyBumpCircleIsNotEnabledOnNonDICOM() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Bump tool' is not activated on annotations other than polylines(machine results and User drawn polylines).");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(IBL_JohnDoe_PatientName,  1, 4);
		
		bumpToolCircle = new BumpToolCircle(driver);
		for(int i =1; i<=3;i++)
			bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(i),"Checkpoint[1/3]","verifying the bump tool can't be enabled on non DICOm(JPG+ pdf)");
		
		helper.browserBackAndReloadViewer(ChestCT1p25mm,  1, 2);
		bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(1),"Checkpoint[2/3]","Verifying the bump tool can't be enabled on SR report");		
		
		CircleAnnotation circle = new CircleAnnotation(driver);
		
		circle.selectCircle(2, 1);
		bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(2),"Checkpoint[3/3]","Verifying that bump tool is not enabled when there is another annotation than polyline");		
		
	}

	@Test(groups = {  "Chrome", "IE11", "Edge" , "US1412","Positive"})
	public void test04_US1412_TC7797_verifyMovementOfAnnotation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify moving the annotation.");

		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();

		polylines = new PolyLineAnnotation(driver);
		
		int polylineX=polylines.getValueOfXCoordinate(polylines.getLinesOfPolyLine(1, 1).get(1));
		int polylineY= polylines.getValueOfYCoordinate(polylines.getLinesOfPolyLine(1, 1).get(1));

		polylines.moveFreePolyLine(1,1,50,50);

		polylines.assertNotEquals(polylines.getValueOfXCoordinate(polylines.getLinesOfPolyLine(1, 1).get(1)), polylineX, "Checkpoint[1/2]", "verifying post movement the x co-ordinate is changed");
		polylines.assertNotEquals(polylines.getValueOfYCoordinate(polylines.getLinesOfPolyLine(1, 1).get(1)), polylineY, "Checkpoint[2/2]", "verifying post movement the y co-ordinate is changed");

		
	}
	
	//US1413 - bump tool editor	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive","BVT"})
	public void test05_US1413_TC7864_verifyBumpToolEditingForRT() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on RT contours and Mammo CAD");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		polyline = new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		drt.clickOnSegementStateIcon(1,1);
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
			
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/10]","Performing and Verifying the bump tool and checking for bump circle");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/10]","Verifying post editing bump circle is not present");
		bumpToolCircle.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/10]","Verifying after bump editor the accept AR toolbar is displayed");
		bumpToolCircle.assertTrue(polyline.isElementPresent(polyline.getSelectedRTPolyLine(1)),"Checkpoint[4/10]","Verifying after bump editor the accept AR toolbar is displayed");
		
		
		int linesAfterEdit1 = polyline.getLinesOfPolyLine(1, 1).size();			
		polyline.assertTrue(linesBeforeEdit1 > linesAfterEdit1,"Checkpoint[5/10]" ,"Verifying the polyline is edited after use of bump tool");
			
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(segments.size(), 1, "Checkpoint[5.2/10]", "Verifying the segment state is turned green");
		bumpToolCircle.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/10]","Verifying the state of segment in finding menu table");		
		
		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 10; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[7/10]","Verifying the slider where mixed color is displayed");

		markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 2; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[8/10]","Verifying finding is present as accepted in slider where accepted color is displayed");

		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[9/10]","Verifying that clone is getting created in content selector");
//		bumpToolCircle.mouseHover(drt.getViewPort(1));
//		
//		OutputPanel_New panel = new OutputPanel_New(driver);		
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[10/10]","verifying that finding is turned as accepted");	
//		panel.openAndCloseOutputPanel(false);
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1412","Positive","BVT"})
	public void test06_US1413_TC7864_verifyBumpToolWhenSegmentIsAccepted() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on RT contours and Mammo CAD");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);
		
		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		drt.acceptSegment(1);
		drt.clickOnSegementStateIcon(1,1);
		
		polyline = new PolyLineAnnotation(driver);
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, -200, 100 , 200, 100),"Checkpoint[1/14]","Verifying that on mouse movement bump tool remains enabled and circle remains of same size");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/14]","Verifying post editing bump circle is not present");
		bumpToolCircle.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/14]","Verifying after bump editor the accept AR toolbar is displayed");
		bumpToolCircle.assertTrue(polyline.isElementPresent(polyline.getSelectedRTPolyLine(1)),"Checkpoint[4/14]","Verifying after bump editor the accept AR toolbar is displayed");
		
		
		int linesAfterEdit1 = polyline.getLinesOfPolyLine(1, 1).size();			
		polyline.assertTrue(linesBeforeEdit1 > linesAfterEdit1,"Checkpoint[5.1/14]" ,"Verifying the polyline is edited after use of bump tool");
			
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		polyline.takeElementScreenShot(polyline.getViewbox(1), newImagePath+"/goldImages/bumpedImage.png");

		
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(segments.size(), 1, "Checkpoint[5.2/14]", "Verifying the segment state is turned green");
		bumpToolCircle.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/14]","Verifying the state of segment in finding menu table");		
		
		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 10; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[7/14]","Verifying the slider where mixed color is displayed");

		markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 2; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[8/14]","Verifying finding is present as accepted in slider where accepted color is displayed");

		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[9/14]","Verifying that clone is getting created in content selector");
//		bumpToolCircle.mouseHover(drt.getViewPort(1));
//		
//		panel = new OutputPanel_New(driver);		
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[10/14]","verifying that finding is turned as accepted");	
//		panel.openAndCloseOutputPanel(false);
		
		
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNameTCGA,  1, 1);
		drt.waitForDICOMRTToLoad();
		
		drt.clickOnSegementStateIcon(1,1);
		segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		
		drt.assertEquals(segments.size(),1,"Checkpoint[11/14]","Verifying the state is retained on reload");
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[12/14]","Verifying the segment state is retained in finding menu");
//		drt.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit1, "Checkpoint[13/14]","");
		
		drt.takeElementScreenShot(drt.getViewbox(1), newImagePath+"/actualImages/bumpedImage.png");
		
		String expectedImagePath = newImagePath+"/goldImages/bumpedImage.png";
		String actualImagePath = newImagePath+"/actualImages/bumpedImage.png";
		String diffImagePath = newImagePath+"/actualImages/bumpedImage.png";

		boolean cpStatus =  drt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		drt.assertTrue(cpStatus, "The actual and Expected image are same.","");
		
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[14/14]","verifying that finding is retained in output panel on reload");	
//		panel.openAndCloseOutputPanel(false);
	
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Negative","BVT"})
	public void test07_US1413_TC7864_verifyBumpToolWhenSegmentIsRejected() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on RT contours and Mammo CAD");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);
		List<String> resultsBeforeBump = cs.getAllResults();
		
		drt.rejectSegment(1);
		drt.clickOnSegementStateIcon(1,1);
		
		polyline = new PolyLineAnnotation(driver);
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, -200, 100 , 200, 100),"Checkpoint[1/14]","Verifying that on mouse movement bump tool remains enabled and circle remains of same size");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/14]","Verifying post editing bump circle is not present");
		bumpToolCircle.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/14]","Verifying after bump editor the accept AR toolbar is displayed");
		bumpToolCircle.assertTrue(polyline.isElementPresent(polyline.getSelectedRTPolyLine(1)),"Checkpoint[4/14]","Verifying after bump editor the accept AR toolbar is displayed");
		
		
		int linesAfterEdit1 = polyline.getLinesOfPolyLine(1, 1).size();			
		polyline.assertTrue(linesBeforeEdit1 > linesAfterEdit1,"Checkpoint[5.1/14]" ,"Verifying the polyline is edited after use of bump tool");
			
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		polyline.takeElementScreenShot(polyline.getViewbox(1), newImagePath+"/goldImages/bumpedImage.png");

		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(segments.size(), 1, "Checkpoint[5.2/14]", "Verifying the segment state is turned green");
		bumpToolCircle.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/14]","Verifying the state of segment in finding menu table");		
		
		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 10; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[7/14]","Verifying the slider where mixed color is displayed");

		markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 2; i++) 
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[8/14]","Verifying finding is present as accepted in slider where accepted color is displayed");

		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[9/14]","Verifying that clone is getting created in content selector");
//		bumpToolCircle.mouseHover(drt.getViewPort(1));
//		
//		panel = new OutputPanel_New(driver);		
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[10/14]","verifying that finding is turned as accepted");	
//		panel.openAndCloseOutputPanel(false);
		
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNameTCGA,  1, 1);
		
		drt.clickOnSegementStateIcon(1,1);
		segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		
		drt.assertEquals(segments.size(),1,"Checkpoint[11/14]","Verifying the state is retained on reload");
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[12/14]","Verifying the segment state is retained in finding menu");
//		drt.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit1, "Checkpoint[13/14]","");
		
		drt.takeElementScreenShot(drt.getViewbox(1), newImagePath+"/actualImages/bumpedImage.png");
		
		String expectedImagePath = newImagePath+"/goldImages/bumpedImage.png";
		String actualImagePath = newImagePath+"/actualImages/bumpedImage.png";
		String diffImagePath = newImagePath+"/actualImages/bumpedImage.png";

		boolean cpStatus =  drt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		drt.assertTrue(cpStatus, "The actual and Expected image are same.","");

//		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[14/14]","verifying that finding is retained in output panel on reload");	
//		panel.openAndCloseOutputPanel(false);
//		
		
	}
		
	// mamo CAD
	@Test(groups = {"Chrome","IE11","Edge","US1412","Positive","BVT"})
	public void test08_US1413_TC7864_verifyBumpToolForCAD() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on RT contours and Mammo CAD");
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+IHEMammoTestPatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(IHEMammoTestPatientName,  1, 1);

		polyline =new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		cs.selectResultFromSeriesTab(1, resultsBeforeBump.get(0));	
		
		int linesBeforeEdit  = polyline.getLinesOfPolyLine(1, 1).size();
		
		int[] coordinates = new int[]{60,50 ,50, 50,30,0};
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/24]","Editing the loaded CAD finding using Bump tool editor");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/24]","Post editing no bump tool is activated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/24]","Accepted AR tool bar is displayed");		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/24]", "Accepted Polyline should be displayed");
		
		int linesAfterEdit  = polyline.getLinesOfPolyLine(1, 1).size();
		polyline.assertNotEquals(linesBeforeEdit,linesAfterEdit,"Checkpoint[5/24]","Polyline lines should be changed");
		
		List<String> findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[6/24]", "Finding state should be changed to accepted infinding menu");
			
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+4,"Checkpoint[7/24]","Clone should be created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[8/24]","Finding should be accepted now");
		polyline.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[9/24]","Lines should be same after editing of polyline using bump tool editor");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName,  1, 1);
		cs.selectResultFromSeriesTab(1, resultsBeforeBump.get(0)+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+"1");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[10/24]", "Polyline state is retained post reload");
		polyline.assertEquals(findingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[11/24]", "Polyline state is also retained in finding menu post reload");
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[12/24]","Finding should be displayed as accepted in OP after reload");
		polyline.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[13/24]","Lines should be same after editing of polyline using bump tool editor");
		panel.openAndCloseOutputPanel(false);
		
		cs.selectResultFromSeriesTab(1, resultsBeforeBump.get(0));
		polyline.selectRejectfromGSPSRadialMenu();
//		bumpToolCircle.mouseHover(polyline.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true, false);			
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[14/24]","Verifying the rejected finding in OP");
//		panel.openAndCloseOutputPanel(false);
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		polyline.selectFindingFromTable(1);
		
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[15/24]","Performing the bump tool on rejected polyline");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[16/24]","Bump tool should get deactivate");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[17/24]","Accepted AR tool bar should be displayed");		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[18/24]", "Polyline is accepted focused after editing");
		
		linesAfterEdit  = polyline.getLinesOfPolyLine(1, 1).size();
		polyline.assertNotEquals(linesBeforeEdit,linesAfterEdit,"Checkpoint[19/24]","Polyline should be edited");
		
		findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[20/24]", "Finding state should be change to accepted in finding menu");
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+8,"Checkpoint[21/24]","clone should be generated");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[22/24]","Finding should be turned to accepted");
		polyline.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[23/24]","Edited line should be same as viewer");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[24/24]","No finding should be displayed under rejected tab");	
		panel.openAndCloseOutputPanel(false);
		
		
	}
	
	// accepted
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test09_US1413_TC7874_verifyBumpToolOnUserCreatedPolylinesAccepted() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on User created polylines for Pending/Accepted/Rejected findings.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		polyline =new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates);
		
		int linesBeforeEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -100,-120 ,100, 100),"Checkpoint[1/15]","Performing the Bump tool on user created polyline");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/15]","Verifying that bump tool is deactivated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/15]","Verifying the AR tool bar has latest accept button highlighted");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/15]", "Verifying the polyline is accepted and focused after editing");
		
		int linesAfterEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		polyline.assertNotEquals(linesBeforeEdit,linesAfterEdit, "Checkpoint[5/15]", "Verifying the polyline is accepted and focused after editing");
		
		List<String> findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[6/15]", "verifying that state is also changed to accepted in finding menu");
		
		List<WebElement> markers = polyline.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : markers)
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),findingName,"Checkpoint[7/15]","verifying the slider container also has the finding");
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/15]","Verifying the clone is created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[9/15]","verifying the output panel for edited polyline");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[10/15]","verifying the output panel for edited polyline");
				
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
			
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[11/15]", "Verifying on reload the state of polyline is retained");
		polyline.assertEquals(findingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[12/15]", "verifying the finding menu on reload");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit, "Checkpoint[13/15]", "Verifying the polyline is accepted and focused after editing");
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[14/15]","verifying the polyline in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[15/15]","verifying the output panel for edited polyline");
		panel.openAndCloseOutputPanel(false);
		
	}
	
	// rejected
	@Test(groups = {"Chrome","IE11","Edge","US1413","Negative"})
	public void test10_US1413_TC7874_verifyBumpToolOnUserCreatedPolylinesRejected() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on User created polylines for Pending/Accepted/Rejected findings.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 2);
		
		polyline =new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates);
		polyline.selectRejectfromGSPSRadialMenu();
		
		int linesBeforeEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[1.1/15]","verifying the output panel for edited polyline");
		panel.clickOnJumpIcon(1);
				
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -100,-120 ,100, 100),"Checkpoint[1.2/15]","Performing the Bump tool on user created polyline");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/15]","Verifying that bump tool is deactivated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/15]","Verifying the AR tool bar has latest accept button highlighted");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/15]", "Verifying the polyline is accepted and focused after editing");
		
		int linesAfterEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		polyline.assertNotEquals(linesBeforeEdit,linesAfterEdit, "Checkpoint[5/15]", "Verifying the polyline is accepted and focused after editing");
		
		List<String> findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[6/15]", "verifying that state is also changed to accepted in finding menu");
		
		List<WebElement> markers = polyline.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : markers)
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),findingName,"Checkpoint[7/15]","verifying the slider container also has the finding");
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/15]","Verifying the clone is created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[9/15]","verifying the output panel for edited polyline");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[10.1/15]","verifying the output panel for edited polyline");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[10.2/15]","verifying the output panel for edited polyline");
		panel.openAndCloseOutputPanel(false);
			
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
			
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[11/15]", "Verifying on reload the state of polyline is retained");
		polyline.assertEquals(findingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[12/15]", "verifying the finding menu on reload");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit, "Checkpoint[13/15]", "Verifying the polyline is accepted and focused after editing");
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[14/15]","verifying the polyline in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[15.1/15]","verifying the output panel for edited polyline");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[15.2/15]","verifying the output panel for edited polyline");
		panel.openAndCloseOutputPanel(false);
		
	}
	
	// pending
	@Test(groups = {"Chrome","IE11","Edge","US1413","Negative"})
	public void test11_US1413_TC7874_verifyBumpToolOnUserCreatedPolylinesPending() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on User created polylines for Pending/Accepted/Rejected findings.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		polyline =new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates);
		polyline.selectAcceptfromGSPSRadialMenu();
		
		int linesBeforeEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -100,-120 ,100, 100),"Checkpoint[1/15]","Performing the Bump tool on user created polyline");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/15]","Verifying that bump tool is deactivated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/15]","Verifying the AR tool bar has latest accept button highlighted");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/15]", "Verifying the polyline is accepted and focused after editing");
		
		int linesAfterEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		polyline.assertNotEquals(linesBeforeEdit,linesAfterEdit, "Checkpoint[5/15]", "Verifying the polyline is accepted and focused after editing");
		
		List<String> findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[6/15]", "verifying that state is also changed to accepted in finding menu");
		
		List<WebElement> markers = polyline.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : markers)
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),findingName,"Checkpoint[7/15]","verifying the slider container also has the finding");
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/15]","Verifying the clone is created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[9/15]","verifying the output panel for edited polyline");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[10.1/15]","verifying the output panel for edited polyline");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[10.2/15]","verifying no pending polyline in output panel");			
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);			
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[11/15]", "Verifying on reload the state of polyline is retained");
		polyline.assertEquals(findingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[12/15]", "verifying the finding menu on reload");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit, "Checkpoint[13/15]", "Verifying the polyline is accepted and focused after editing");
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[14/15]","verifying the polyline in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit,"Checkpoint[15.1/15]","verifying the output panel for edited polyline");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[15.2/15]","verifying no polyline in output panel");			
		panel.openAndCloseOutputPanel(false);
		
	}
		
	//sc and PMAP
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test12_bumpEditor"})
	public void test12_US1413_TC7874_verifyBumpToolOnUserCreatedPolylinesOnSC(String patientFilePath) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on User created polylines for Pending/Accepted/Rejected findings.");
		
		String filepath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath);
		
		if(patientName.isEmpty())
			patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filepath);
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		if(patientName.isEmpty()) {			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientID);
			helper.loadViewerDirectlyUsingID(patientID,1);
		}
		else { 
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientName);
			helper.loadViewerDirectly(patientName,1);
		}
		
		
		polyline =new PolyLineAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};
		int[] coordinates2 = new int[] {49,47,-27,3,-24,2,-26,-1,-27,6,-27,20,-5,25,5,34,28,13,34,2,21,-9,21,-20,13,-19,10,-25,4, -27};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates);
		polyline.drawFreehandPolyLine(1, coordinates2);
		polyline.selectRejectfromGSPSRadialMenu();
		polyline.selectNextfromGSPSRadialMenu();
		
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
		int linesBeforeEdit2 = polyline.getLinesOfPolyLine(1, 2).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -70,-70 ,40, 40),"Checkpoint[1/43]","Editing the polyline  using bump tool");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/43]","After editing bump tool should be deactivate");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/43]","AR tool bar should show state as accepted");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[4/43]", "Verifying that first polyline state is accepted and focused");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[5/43]","verifying another polyline has no impact - it is in rejected state");
		
		int linesAfterEdit1 = polyline.getLinesOfPolyLine(1, 2).size();
		int linesAfterEdit2 = polyline.getLinesOfPolyLine(1, 1).size();
		
		polyline.assertNotEquals(linesBeforeEdit1,linesAfterEdit1,"Checkpoint[6/43]","verifying polyline 1 is edited");
		polyline.assertEquals(linesBeforeEdit2,linesAfterEdit2,"Checkpoint[7/43]"," No impact on polyline 2");

		
		List<String> acceptedFindingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(acceptedFindingName.size(), 1, "Checkpoint[8/43]", "verifying that state is changed to green after editing");
		
		List<String> rejectedFindingName = polyline.getFindingsName(1, ViewerPageConstants.REJECTED_FINDING_COLOR);
		polyline.assertEquals(rejectedFindingName.size(), 1, "Checkpoint[9/43]", "verifying that state is not changed for polyline 2 - remained rejected");
		
		List<WebElement> acceptedMarkers = polyline.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : acceptedMarkers) {
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindingName,"Checkpoint[10/43]","verifying slider is also updated for polyline 1 with green state");
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.REJECTED_FINDING_COLOR),rejectedFindingName,"Checkpoint[11/43]","verifying slider is not updated for poline 2");
			
		}
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[12/43]","verifying that after editing new clone is created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+acceptedFindingName.get(0),"Checkpoint[13/43]","verifying that polyline is shown under accept tab");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit1,"Checkpoint[14/43]","verifying that editing is reflected in thumbnail");	
		
		panel.enableFiltersInOutputPanel(false, true, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+rejectedFindingName.get(0),"Checkpoint[15/43]","verifying that rejected polyline state is same in output panel");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit2,"Checkpoint[16/43]","verifying no editing is done for polyline 2");	
		
		panel.openAndCloseOutputPanel(false);
		
		polyline.browserBackWebPage();
		patientListPage.waitForPatientPageToLoad();
		
		if(patientName.isEmpty()) {			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientID);
			patientListPage.clickOnPatientUsingID(patientID);
		}
		else { 
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientName);
			patientListPage.clickOnPatientRow(patientName);
		}
		patientListPage.clickOntheFirstStudy();		
		polyline.waitForViewerpageToLoad();		
			
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[17/43]", "verifying on reload polyline 1 state is retained");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[18/43]","verifying polyline 2 state is retained as rejected");
		
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit2,"Checkpoint[19/43]","verifying editing is retained after reload");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 2).size(),linesAfterEdit1,"Checkpoint[20/43]","verifying no change in polyline 2 on reload");

		
		polyline.assertEquals(acceptedFindingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[21/43]", "verifying the finding state in finding menu");
		polyline.assertEquals(acceptedFindingName, polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[22/43]", "verifying the finding state in finding menu");
		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+acceptedFindingName.get(0),"Checkpoint[23/43]","verifying the finding in OP on reload");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit1,"Checkpoint[24/43]","verifying the editing in OP on reload of viewer");
		
		panel.enableFiltersInOutputPanel(false, true, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+rejectedFindingName.get(0),"Checkpoint[25/43]","verifying the finding state is same on reload  of viewer");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesAfterEdit2,"Checkpoint[26/43]","verifying there is no editing in OP on viewer reload");
		panel.openAndCloseOutputPanel(false);
		
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit2,"Checkpoint[27/43]","verifying the polyline editing on viewer is retained");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 2).size(),linesAfterEdit1,"Checkpoint[28/43]","verifying the polyline should not edit on viewer reload");
		
		polyline.selectPolyline(1, 1);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, 90,90 ,-40, -40),"Checkpoint[29/43]","Performing the editing on rejected polyline");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[30/43]","Verifying that bump tool is deactivated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[31/43]","AR tool bar shows the latest state - accepted");
		
		polyline.assertNotEquals(polyline.getLinesOfPolyLine(1, 2).size(),linesAfterEdit2,"Checkpoint[32/43]","verifying the rejected polyline is edited");
		polyline.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesAfterEdit1,"Checkpoint[33/43]","verifying there is no change in first polyline");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[34/43]", "verifying polyline is turned to accepted post editing");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[35/43]","verifying polyline 1 is in accepted state");
		
		acceptedFindingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(acceptedFindingName.size(), 2, "Checkpoint[36/43]", "verifying that finding menu for state change");
	
		
		panel.mouseHover(panel.getViewPort(1));
		panel.scrollDownToSliceUsingKeyboard(1);
		
		polyline.selectPolylineFromQuickToolbar(1);
		polyline.drawFreehandPolyLine(1, coordinates);
		polyline.selectAcceptfromGSPSRadialMenu();
		polyline.selectPreviousfromGSPSRadialMenu();
	
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -70,-70 ,40, 40),"Checkpoint[37/43]","Verifying the editing on pending polyline");
		
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[38/43]","Verifying bump tool is deactivated post editing");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[39/43]","verifying the state is reflected in AR toolbar");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[40/43]", "verifying the polyline is turned in accepted post editing");
		
		acceptedFindingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(acceptedFindingName.size(), 3, "Checkpoint[41/43]", "verifying the findings state in finding menu");
		
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		for(int i =acceptedFindingName.size()-1,j=1  ; i >0;i--,j++)
//			panel.assertEquals(panel.getTextOfFindingFromOutputPanel(j),ViewerPageConstants.FINDING_NAME+": "+acceptedFindingName.get(i),"Checkpoint[42/43]","verifying the output panel for accepted findings");
		
		panel.enableFiltersInOutputPanel(false,true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[43/43]", "verifying the finding is moved from rejected to accepted");

		panel.openAndCloseOutputPanel(false);
		
	}
				
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test13_US1413_TC7875_verifyBumpToolWhenSegmentSelectedFromFindingMenu() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using bump tool on contour selected from finding dropdown.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline = new PolyLineAnnotation(driver);
		drt.selectFindingFromTable(1);
			
		bumpToolCircle = new BumpToolCircle(driver);
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		int linesbeforeEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/10]","Verifying editing using bump tool for polyline selected from finding table");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/10]","bump tool is activated");
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/10]","AR tool bar is showing state");		
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[4/10]","verifying the finding menu for finding state is changed");
		
		drt.assertNotEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesbeforeEdit,"Checkpoint[5/10]","verifying the polyline is changed/edited");
		
		
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/10]","verifying the segment state is changed");

		drt.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[7/10]","verifying the clone is created");
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		drt.click(drt.getViewPort(1));
		
		drt.clickOnSegementStateIcon(1,9);
		coordinates = new int[]{-100,90 , 100, 50, 150,50,200,50,250,50};
		drt.assertFalse(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[8/10]","verifying the edting for another segment");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[9/10]","verifing bump tool is deactivated if editing goes out of viewbox");
		drt.assertTrue(drt.verifyBorderForActiveViewbox(2,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[10/10]", "verifying another viewbox is active");
		
		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive","BVT"})
	public void test14_US1413_TC7876_verifyBumpToolWhenSegmentSelectedFromSlider() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on contour selected from finding preview box on slider.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);
		polyline = new PolyLineAnnotation(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		List<String> options = drt.getLegendOptions(1);
		drt.selectFindingFromSlider(1, 1, options.get(0));
	
		bumpToolCircle = new BumpToolCircle(driver);
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		int linesbeforeEdit = polyline.getLinesOfPolyLine(1, 1).size();
		
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/10]","Verifying editing using bump tool for polyline selected from finding table");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/10]","bump tool is activated");
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/10]","AR tool bar is showing state");		
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[4/10]","verifying the finding menu for finding state is changed");
		
		drt.assertNotEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesbeforeEdit,"Checkpoint[5/10]","verifying the polyline is changed/edited");
		
		
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/10]","verifying the segment state is changed");

		drt.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[7/10]","verifying the clone is created");
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		drt.click(drt.getViewPort(1));
		
		drt.clickOnSegementStateIcon(1,9);
		coordinates = new int[]{-100,90 , 100, 50, 150,50,200,50,250,50};
		drt.assertFalse(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[8/10]","verifying the edting for another segment");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[9/10]","verifing bump tool is deactivated if editing goes out of viewbox");
		drt.assertTrue(drt.verifyBorderForActiveViewbox(2, ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[10/10]", "verifying another viewbox is active");

		
		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test15_US1413_TC7881_verifyBumpToolWhenSegmentSelectedFromOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using bump tool on contour selected from Output Panel.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);
		
		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);
		polyline = new PolyLineAnnotation(driver);
		panel = new OutputPanel(driver);
		
		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(1);
		int linesOfPolyline = polyline.getLinesOfPolyLine(1, 6).size();
				
		bumpToolCircle = new BumpToolCircle(driver);
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/10]","Editing of polyline when selected using output panel");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/10]","Verifying bump tool is deactivated");
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/10]","verifying AR toolbar shows the latest state after editing");		
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[4/10]","verifying the state is also changed in legend");
		
		int updatedLines = polyline.getLinesOfPolyLine(1, 6).size();
		drt.assertNotEquals(linesOfPolyline,updatedLines,"Checkpoint[5/10]","verifying polyline is edited");
		
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/10]","verifying the finding state in finding menu");

		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : markers)
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[7/10]","verifying the state is changed in slider too");

		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/10]","verifying the clone is created too");		
		bumpToolCircle.mouseHover(drt.getViewPort(1));
		
		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[9/10]","verifying the state is changed in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),updatedLines,"Checkpoint[10/10]","verifying the edited polyline is shown in thumbnail");
		panel.openAndCloseOutputPanel(false);
		
				
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test16_US1413_TC7881_verifyBumpToolPolylineSelectedFromOPCAD() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using bump tool on contour selected from Output Panel.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+IHEMammoTestPatientName);
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		polyline =new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();
		
		ContentSelector cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		cs.selectResultFromSeriesTab(1, resultsBeforeBump.get(0));
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(1);
		int linesOfPolyline = polyline.getLinesOfPolyLine(1, 1).size();
				
		bumpToolCircle = new BumpToolCircle(driver);
		int[] coordinates = new int[]{60,50 ,50, 50,30,0};
		
		polyline.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/9]","verifying the editing of polyline");
		polyline.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/9]","verifying tool is deactivated");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[3/9]","Verifying polyline state is changed to accepted");		
		List<String> findingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		polyline.assertEquals(findingName.size(), 1, "Checkpoint[4/9]", "verifying the state changed in finding menu");
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+4,"Checkpoint[5/9]","verifying the clone is created");
		
		int updatedLines = polyline.getLinesOfPolyLine(1, 1).size();
		polyline.assertNotEquals(linesOfPolyline,updatedLines,"Checkpoint[6/9]","verifying the polyline is edited");
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+4,"Checkpoint[7/9]","verifying the clone is created");		
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
			
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+findingName.get(0),"Checkpoint[8/9]","verifying the state is changed in OP");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),updatedLines,"Checkpoint[9/9]","verifying the polyline is edited in thumbnail too");
		panel.openAndCloseOutputPanel(false);
		
				
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive","DE2018"})
	public void test17_US1413_TC7881_TC7881_DE2018_TC8256_verifyBumpToolPolylineSelectedFromOPLiver9() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using bump tool on contour selected from Output Panel."
				+ "<br> Verify using the bump tool on User created closed polylines.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);


		polyline =new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();
		
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> resultsBeforeBump = cs.getAllResults();
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};
			
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates);
				
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true,false, false);
		panel.clickOnJumpIcon(1);
		
		int linesOfPolyline = polyline.getLinesOfPolyLine(1, 1).size();
		
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertTrue(bumpToolCircle.moveBumpTool(1, -70,-70 ,40, 40),"Checkpoint[1/10]","verifying the editing for user created polylines");
		bumpToolCircle.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/10]","Verifying tool is deactivated");
		bumpToolCircle.assertTrue(polyline.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/10]","verifying the AR toolbar is showing latest state");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/10]", "verifying the polyline is accepted and focused");
		int linesOfUpdatedPolyline = polyline.getLinesOfPolyLine(1, 1).size();		
		polyline.assertNotEquals(linesOfPolyline, linesOfUpdatedPolyline, "Checkpoint[5/10]", "verifying the polyline is edited");
		
		List<String> acceptedFindingName = polyline.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		polyline.assertEquals(acceptedFindingName.size(), 1, "Checkpoint[6/10]", "verifying the state is changed in finding menu");
		
		List<WebElement> acceptedMarkers = polyline.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(WebElement mark : acceptedMarkers) {
			polyline.assertEquals(polyline.getFindingsNameFromSliderContainer(mark, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindingName,"Checkpoint[7/10]","verifying the state is changed in slider");
			
		}
		
		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/10]","verifying clone is created");
		bumpToolCircle.mouseHover(polyline.getViewPort(1));
		
				
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+acceptedFindingName.get(0),"Checkpoint[9/10]","verifying the accepted finding is displayed in OP");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),linesOfUpdatedPolyline,"Checkpoint[10/10]","Verifying edited polyline is displayed in thumbnail");	
		panel.openAndCloseOutputPanel(false);
		
	
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test18_US1413_TC8008_verifyBumpToolNotImpactEditingMoving() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify editing and moving the polylines functionality.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		polyline = new PolyLineAnnotation(driver);
		
		int[] coordinates = new int[] {49,47,-27,3,-24,2,-26,-1,-27,6,-27,20,-5,25,5,34,28,13,34,2,21,-9,21,-20};
		
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
		int linesBeforeEdit2 = polyline.getLinesOfPolyLine(1, 2).size();

		polyline.editPolyLine(polyline.getLinesOfPolyLine(1, 1).get(10), coordinates, polyline.getLinesOfPolyLine(1, 1).get(60));
		
		int linesAfterEdit1 = polyline.getLinesOfPolyLine(1, 1).size();
		int linesAfterEdit2 = polyline.getLinesOfPolyLine(1, 2).size();
		
		polyline.assertTrue(linesBeforeEdit1 > linesAfterEdit2,"Checkpoint[1/16]" ,"Verifying that edit contour is done successful" );
		polyline.assertEquals(linesBeforeEdit2 , linesAfterEdit1,"Checkpoint[2/16]" ,"Verifying that edit contour is done successful" );
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/16]","verifying AR tool bar is showing latest state");		
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[4/16]","Verifying the segment's state is also changed to accepted ");
		
		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for (int i = 0; i < 5; i++) 
			drt.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[5/16]","verifying the slider is also changed");
		
		
		int polylineX=polyline.getValueOfXCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1));
		int polylineY= polyline.getValueOfYCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1));

		polyline.moveFreePolyLine(1, 1, 30, 30);
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[6/16]","After movement of polyline AR tool bar showing the latest state");	
		polyline.click(polyline.getViewPort(1));
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		polyline.takeElementScreenShot(polyline.getViewPort(1), newImagePath+"/goldImages/viewer.png");
		
			
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[7/16]","verifying the state is changed in lagend");
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(1),drt.getLegendOptions(1).get(1),"Checkpoint[8/16]","verifying the state is changed in lagend");

		polyline.assertNotEquals(polyline.getValueOfXCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1)), polylineX, "Checkpoint[9/16]", "verifying post movement the x co-ordinate is changed");
		polyline.assertNotEquals(polyline.getValueOfYCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1)), polylineY, "Checkpoint[10/16]", "verifying post movement the y co-ordinate is changed");

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNameTCGA,  1, 1);	
		drt.waitForDICOMRTToLoad();
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[12/16]","verifying on reload the viewer state is retained");	
		
		polyline.click(polyline.getViewPort(1));
		
	
		polyline.takeElementScreenShot(polyline.getViewPort(1), newImagePath+"/actualImages/viewer.png");
		String expectedImagePath = newImagePath+"/goldImages/viewer.png";
		String actualImagePath = newImagePath+"/actualImages/viewer.png";
		String diffImagePath = newImagePath+"/diffImages/viewer.png";
		boolean cpStatus =  polyline.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		polyline.assertTrue(cpStatus, "Checkpoint[11/16]","On reload bothe polyline state are same");
				
			
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),drt.getLegendOptions(1).get(0),"Checkpoint[13/16]","verifying the state is same in legend on reload");
		drt.assertEquals(drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(1),drt.getLegendOptions(1).get(1),"Checkpoint[14/16]","verifying the state is same in legend on reload");
		
		polyline.assertNotEquals(polyline.getValueOfXCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1)), polylineX, "Checkpoint[15/16]", "verifying post movement the x co-ordinate is changed");
		polyline.assertNotEquals(polyline.getValueOfYCoordinate(polyline.getLinesOfPolyLine(1, 1).get(1)), polylineY, "Checkpoint[16/16]", "verifying post movement the y co-ordinate is changed");

		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1413","Negative"})
	public void test19_US1413_TC8012_verifyBumpToolNotForOpenPolylines() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no bump tool is activated on the open Polylines.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		polyline =new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();
		polyline.doubleClickOnViewbox(1);
		polyline.selectPolylineFromQuickToolbar(1);	
		
		int[] coordinates1 = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		int[] coordinates2 = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the free hand polyline on viewbox 1");
		polyline.drawFreehandPolyLine(1, coordinates1);
		polyline.drawPolyLineExitUsingESCKey(1, coordinates2);				
		
		bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(1),"Checkpoint[1/2]","verifying bump tool is not activated for open free hand polyline");
		
		polyline.selectPolyline(1, 1, 10);
		bumpToolCircle.assertFalse(bumpToolCircle.enableBumpTool(1),"Checkpoint[2/2]","verifying bump tool is not activated for open classic polyline");

		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1413","Positive"})
	public void test20_US1413_TC8013_verifySpiltingThePolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify splitting the polyline using the bump tool.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+patientNameTCGA);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		polyline = new PolyLineAnnotation(driver);
						
		int linesBeforeEdit1 = polyline.getLinesOfPolyLine(1, 1).size(); 
		int linesBeforeEdit2 = polyline.getLinesOfPolyLine(1, 2).size(); 
		
		bumpToolCircle = new BumpToolCircle(driver);
		polyline.selectPolyline(1, 1, 30);
		bumpToolCircle.moveBumpTool(1, 450, -50,-1000, 50);
		
		drt.assertTrue(polyline.getLinesOfPolyLine(1, 2).size()>(linesBeforeEdit1/2), "Checkpoint[1/2]", "Verifying on splitting The smaller part of the contour should be removed from the viewer and only the  bigger part of the contour should be available.");
		drt.assertEquals(polyline.getLinesOfPolyLine(1, 1).size(),linesBeforeEdit2, "Checkpoint[2/2]", "Veriying there is no change in another polyline");
		
	}
			
	@Test(groups = {"Chrome","IE11","Edge","DE2018","Positive"})
	public void test21_DE2018_TC7881_verifyBumpToolForLungLIDC() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify using the bump tool on RT contours");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+lungPatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(lungPatientName, 1);

		drt =new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		ContentSelector cs = new ContentSelector(driver);
		polyline = new PolyLineAnnotation(driver);
		panel = new OutputPanel(driver);

		drt.closeNotification();
		
		List<String> resultsBeforeBump = cs.getAllResults();
		
		panel.enableFiltersInOutputPanel(false, false, true);
//		String segmentName = panel.getText(panel.findingsNameTitleList.get(0));
		panel.clickOnJumpIcon(1);
		
		int whichPolyline = polyline.getPolylineWhichIsFocused(1);
		int linesOfPolyline = polyline.getLinesOfPolyLine(1, whichPolyline).size();
		
				
		bumpToolCircle = new BumpToolCircle(driver);
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0};
		
		drt.assertTrue(bumpToolCircle.moveBumpTool(1, coordinates),"Checkpoint[1/13]","Editing of polyline when selected using output panel");
		drt.assertFalse(bumpToolCircle.isElementPresent(bumpToolCircle.bumpCircle),"Checkpoint[2/13]","Verifying bump tool is deactivated");
		
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/13]","verifying AR toolbar shows the latest state after editing");		
//		drt.assertEquals(drt.getSegmentsName(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0),segmentName,"Checkpoint[4/13]","verifying the state is also changed in legend");
		
		int updatedLines = polyline.getLinesOfPolyLine(1, polyline.getPolylineWhichIsFocused(1)).size();
		drt.assertNotEquals(linesOfPolyline,updatedLines,"Checkpoint[5/13]","verifying polyline is edited");
		
		List<String> segments = drt.getStateSpecificSegmentNames(1, ViewerPageConstants.ACCEPTED_COLOR);
		drt.assertEquals(drt.getFindingState(segments.get(0)),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/13]","verifying the finding state in finding menu");

		List<WebElement> markers = drt.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		
		for(int i=0;i<3;i++)
			bumpToolCircle.assertEquals(drt.getFindingsNameFromSliderContainer(markers.get(i), ViewerPageConstants.ACCEPTED_FINDING_COLOR),segments,"Checkpoint[7/13]","verifying the state is changed in slider too");

		bumpToolCircle.assertEquals(cs.getAllResults().size(),resultsBeforeBump.size()+1,"Checkpoint[8/13]","verifying the clone is created too");		
		bumpToolCircle.mouseHover(drt.getViewPort(1));		
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[9/13]","verifying the state is changed in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),updatedLines,"Checkpoint[10/13]","verifying the edited polyline is shown in thumbnail");
	
		polyline.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/thumbnail.png");
		panel.openAndCloseOutputPanel(false);
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(lungPatientName,  1, 1);
		drt.waitForDICOMRTToLoad();
		drt.closeNotification();
		
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getTextOfFindingFromOutputPanel(1),ViewerPageConstants.FINDING_NAME+": "+segments.get(0),"Checkpoint[11/13]","verifying the state is changed in output panel");	
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),updatedLines,"Checkpoint[12/13]","verifying the edited polyline is shown in thumbnail");
	
		polyline.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/thumbnail.png");
		String expectedImagePath = newImagePath+"/goldImages/thumbnail.png";
		String actualImagePath = newImagePath+"/actualImages/thumbnail.png";
		String diffImagePath = newImagePath+"/diffImages/thumbnail.png";
		boolean cpStatus =  polyline.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		polyline.assertTrue(cpStatus, "Checkpoint[13/13]","On reload bothe polyline state are same in thumbnail");		
		panel.openAndCloseOutputPanel(false);

		
		
				
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);
		
	}
	
		
}
