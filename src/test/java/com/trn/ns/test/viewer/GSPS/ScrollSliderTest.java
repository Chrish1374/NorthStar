package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
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
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ScrollSliderTest extends TestBase {

	private ViewerSliderAndFindingMenu viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;	
	private ExtentTest extentTest;
    private PagesTheme pageTheme;
    private ViewerSliderAndFindingMenu findingMenu;
    
	//Loading the patient on viewer 
	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String ADCphilips_FilePath = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ADCphilips_FilePath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
	String ctNeckPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath2 = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2); 

	String tcgavpFilePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String rtPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcgavpFilePath);

	String lidcFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String lidcPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, lidcFilePath);

	String quanFilePath =Configurations.TEST_PROPERTIES.get("4Quan_LesionDetection_filepath");
	String quanPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, quanFilePath);

	String imbio_filePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbio_filePath);

	String gspsFilePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath);

	String srFilePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String srPatientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, srFilePath);
	
	String ah4PdfFilepath = TEST_PROPERTIES.get("AH4_pdf_filepath");
	String pdfPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ah4PdfFilepath);


	private ContentSelector contentSelector;
	private PointAnnotation point;
	private CircleAnnotation circle;
	private DICOMRT rt;
	private PolyLineAnnotation poly;
	private MeasurementWithUnit lineWithUnit;

	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private TextAnnotation textAnn;
	private PolyLineAnnotation polyLine;
	private EllipseAnnotation ellipse;
	private HelperClass helper;
	private static int seriesLevelID;
	String loadedTheme;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException, SQLException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);

		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);

	}


	@Test(groups ={"IE11","Chrome","Edge","F1093","US808","US2345","Positive","E2E"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_sliderPresence"})
	public void test01_US808_TC5064_US2345_TC9990_verifySliderPresence(String patientFilePath, String whichViewbox, String sliderPresence) throws TimeoutException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of slice bar appears on different data. <br>"+
		"Verify scrollbar gets visible when we hover over the area near right edge of the viewbox ( on all data types) on eureka theme");

		String filepath = Configurations.TEST_PROPERTIES.get(patientFilePath.trim());
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filepath);
		int viewbox = Integer.parseInt(whichViewbox);
		boolean sliderPres = Boolean.parseBoolean(sliderPresence);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName, viewbox);

		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the slider presence");
		viewerpage.assertEquals(viewerpage.verifySliderPresence(viewbox),sliderPres, "Slider's presence ="+sliderPresence, "verified");	

	}

	@Test(groups ={"IE11","Chrome","Edge","F1093","US808","US2345","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US808_TC5065_US2345_TC9990_TC9992_verifyScrollUsingSlider(String theme) throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of scrollbar allow user to use the slide bar to scroll through the slice of the image stack. <br>"+
		"Verify scrollbar gets visible when we hover over the area near right edge of the viewbox ( on all data types) on eureka theme. <br>"+
		"Verify scrollbar gets visible when we hover over the area near right edge of the viewbox ( on all data types) on dark theme");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		
		loginPage = new LoginPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1,username, password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "verifying forward scroll is working fine in all viewbox" );

		int currentImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, 20);
		viewerpage.assertNotEquals(currentImagePos, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying the forward scroll", "verified");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that viewbox 1 and 2 are in sync", "verified");

		viewerpage.openSlider(1);
		Point location = viewerpage.getSlider(1).getLocation();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "verifying backward scroll is working fine in all viewbox" );
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, -20);
		viewerpage.assertEquals(currentImagePos, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying the backward scroll", "verified");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that viewbox 1 and 2 are in sync", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the slider position" );
		viewerpage.assertEquals(location.getX(),viewerpage.getSlider(1).getLocation().getX(),"verifying the slider's x coordinate is not changed","verified");
		viewerpage.assertNotEquals(location.getY(),viewerpage.getSlider(1).getLocation().getY(),"verifying the slider's y coordinate is changes","verified");


//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying the slider on inputing the image number" );
//		viewerpage.inputImageNumber(1, 1);
//		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider is displayed upon inputing the image","verified");
//		viewerpage.assertEquals(location.getX(),viewerpage.getSlider(1).getLocation().getX(),"verifying the slider's x coordinate is not changed","verified");
//		viewerpage.assertTrue(location.getY()>viewerpage.getSlider(1).getLocation().getY(),"verifying the slider's y coordinate is changes","verified");


	}

	@Test(groups ={"IE11","Chrome","Edge","US808","Positive","BVT"})
	public void test03_US808_TC5084_verifySliderPresenceWhenUserChangesOrientation() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Orientation markers and scrollbar functionality not overlap");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ViewerOrientation orin = new ViewerOrientation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that slider is not displayed on mouse hover on markers");
		viewerpage.mouseHover(orin.getRightOrientationMarker(1));
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(1,false)),"verifying the slider is not displayed", "verified");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that rotation markers are not displayed upon hover on slider");
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(orin.rightCounterClockwiseRotationMarker(1)),"verifying the rotation marker absence","verified");


	}

	@Test(groups ={"IE11","Chrome","Edge","US808","Positive"})
	public void test04_US808_TC5085_verifySliderPresenceOnOverlaysChanges() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify slide bar is visible on all mode of text overlays");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		viewerpage.closingConflictMsg();
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "verifying the slider presence on full text overlays");		
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2, false)),"verifying the slider absence in viewbox 2", "verified");	
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "verifying the slider presence on minimum text overlays");	
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2, false)),"verifying the slider absence in viewbox 2", "verified");			

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "verifying the slider presence on default text overlays");	
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2, false)),"verifying the slider absence in viewbox 2", "verified");	

	}

	// need to add the length verification
	@Test(groups ={"IE11","Chrome","Edge","US808","Positive"})
	public void test05_US808_TC5086_verifySliderFunctionWhenViewerResized() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify height of the scrollbar when window resize.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "verifying the slider functionality before resize");

		int currentImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, 20);
		viewerpage.assertNotEquals(currentImagePos, viewerpage.getCurrentScrollPositionOfViewbox(1), "verifying the scroll", "verified");

		//		int sliderHeight = viewerpage.getSliderLine(1).getRect().height;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "verifying the slider functionality when browser is resized");
		viewerpage.resizeBrowserWindow(900,900);		
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, 20);
		viewerpage.assertNotEquals(currentImagePos, viewerpage.getCurrentScrollPositionOfViewbox(1), "verifying the scroll using slider", "verified");


		Point location = viewerpage.getViewPort(1).getLocation();		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "verifying the slider size when browser is resized");
		//		viewerpage.assertTrue(viewerpage.getSliderLine(1).getRect().height < sliderHeight, "", "");
		
		viewerpage.assertTrue((location.getY()<viewerpage.getSliderLine(1).getRect().y)&&
				((location.getY()+viewerpage.getViewPort(1).getRect().height)>(viewerpage.getSliderLine(1).getRect().y+viewerpage.getSliderLine(1).getRect().height))
				, "Verifying that on resize the slider is not coming outside of viewbox", "verified");


	}

	@Test(groups ={"IE11","Chrome","Edge","US808","Negative"})
	public void test06_US808_TC5088_verifySliderFunctionUponDifferentFunctionality() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify different functionality where new range slider doesn't shows up immediately.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "layout change");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(1,false)),"verifying the slider absence in viewbox 1", "verified");	
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2,false)),"verifying the slider absence in viewbox 2", "verified");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "series selected using content selector");
		contentSelector = new ContentSelector(driver);
		contentSelector.selectSeriesFromSeriesTab(2, contentSelector.getSeriesDescriptionOverlayText(1));
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(1,false)),"verifying the slider absence in viewbox 1", "verified");	
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2,false)),"verifying the slider absence in viewbox 2", "verified");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "double click one up");
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(1,false)),"verifying the slider absence in viewbox 1", "verified");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "double click restore" );
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(1,false)),"verifying the slider absence in viewbox 1", "verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(2,false)),"verifying the slider absence in viewbox 2", "verified");




	} 

	@Test(groups ={"IE11","Chrome","Edge","US808","Negative","BVT"})
	public void test07_US808_TC5089_verifySliderFunctionForMultiphase() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of scrollbar appears for images are multiphase");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ADC_philips_Patient+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(ADC_philips_Patient, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Performing the phase scroll forward");
		viewerpage.pressKey(NSGenericConstants.DOT_KEY);
		viewerpage.pressKey(NSGenericConstants.DOT_KEY);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(3, false)),"verifying the slider absence", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Performing the phase scroll backward");
		viewerpage.pressKey(NSGenericConstants.COMMA_KEY);
		viewerpage.pressKey(NSGenericConstants.COMMA_KEY);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSlider(3, false)),"verifying the slider absence", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the slider presence");
		viewerpage.assertTrue(viewerpage.verifySliderPresence(3),"slider should be present", "verified");		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Performing the slider scroll" );
		int currentImagePos = viewerpage.getCurrentScrollPositionOfViewbox(3);
		int phseCount = viewerpage.getValueOfCurrentPhase(3);

		viewerpage.scrollTheSlicesUsingSlider(3, 0, 0, 0, 20);
		viewerpage.assertNotEquals(currentImagePos, viewerpage.getCurrentScrollPositionOfViewbox(3), "Slice should scroll", "verified");
		viewerpage.assertEquals(phseCount, viewerpage.getValueOfCurrentPhase(3), "phase should not scroll", "verified");

	} 

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive","BVT"})
	public void test01_US809_TC5269_verifyMarkersOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that finding marker should be visible on slider (scroll slide bar) when finding is present on any slice");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
//		viewerpage.inputImageNumber(1,1);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-100, -100);

		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/6]","Verifying there is one marker present on slider");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/6]","Verifying the state of markers - green");		


//		viewerpage.inputImageNumber(5,1);
		viewerpage.scrollDownToSliceUsingKeyboard(3);
		point.drawPointAnnotationMarkerOnViewbox(1,-100, -100);

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),2,"Checkpoint[3/6]","Verifying there are 2 markers present on slider as annotation is created on different slices");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[4/6]","Verifying the state of markers - green");		


//		viewerpage.inputImageNumber(10,1);
		viewerpage.scrollDownToSliceUsingKeyboard(3);
		point.drawPointAnnotationMarkerOnViewbox(1,-100, -100);

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),3,"Checkpoint[5/6]","Verifying there are 3 markers present on slider as annotation is created on different slices");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[6/6]","Verifying the state of markers - green");		

	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test02_US809_TC5272_TC5273_verifyGreenAndRedMarkersOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that green bar should be on scroll slider when all findings are in accepted state"
				+ "<br> GSPS: Verify that Red bar should be on scroll slider when all findings are in rejected state");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-80, -100);
		point.drawPointAnnotationMarkerOnViewbox(1,80, 100);
		point.drawPointAnnotationMarkerOnViewbox(1,-50, -100);


		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/4]","Verifying there is one marker present on slider as results are accepted on a slice");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/4]","Verifying the state of markers - green");		

		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu();

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),2,"Checkpoint[3/4]","Verifying there is one marker present on slider as results are rejected on a slice");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[4/4]","Verifying the state of markers - red");



	}

	@Test(groups ={"Chrome","IE11","Edge","F1093","US809","US2345","Positive","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US809_TC5274_US2345_TC9993_TC10008_verifyMixedMarkersOnSlider(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that mix color (green+red) bar should be displayed on scroll slider when mix types of findings (accept, reject, pending) are present. <br>"+
		"Verify track lines are getting displayed near scrollbar for user drawn annotations on eureka theme. <br>"+
		"Verify track lines are getting displayed near scrollbar for user drawn annotations on dark theme");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-80, -100);
		point.drawPointAnnotationMarkerOnViewbox(1,50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1,200, 50);

		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		point.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Checkpoint[1/10]","Verifying the marker presence and its state - mixed");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Checkpoint[2/10]","Verifying the marker presence and its state - mixed");
		
		point.deleteAllAnnotation(1);

		viewerpage.assertTrue(viewerpage.getAllGSPSObjects(1).isEmpty(),"Checkpoint[3/10]","Verifying there is no annotation present after deleting all");
		viewerpage.assertTrue(viewerpage.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[4/10]","Verifying that there is no marker present on slider as there is no annotation");

		point.drawPointAnnotationMarkerOnViewbox(1,-80, -100);
		point.drawPointAnnotationMarkerOnViewbox(1,200, 50);

		point.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Checkpoint[5/10]","Verifying the marker presence and its state - mixed");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),1,"Checkpoint[6/10]","Verifying the marker presence and its state - mixed");

		point.deleteAllAnnotation(1);
		
		point.drawPointAnnotationMarkerOnViewbox(1,-80, -100);
		point.drawPointAnnotationMarkerOnViewbox(1,200, 50);

		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		point.selectRejectfromGSPSRadialMenu();

		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),1,"Checkpoint[7/10]","Verifying the marker presence and its state - mixed");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Checkpoint[8/10]","Verifying the marker presence and its state - mixed");

		point.deleteAllAnnotation(1);
		
		point.drawPointAnnotationMarkerOnViewbox(1,-80, -100);
		point.drawPointAnnotationMarkerOnViewbox(1,200, 50);

		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Checkpoint[9/10]","Verifying the marker presence and its state - mixed");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Checkpoint[10/10]","Verifying the marker presence and its state - mixed");

	}

	@Test(groups ={"Chrome","IE11","Edge","US809","DE1376","Positive"})
	public void test04_US809_TC5289_DE1376_TC5676_verifyMachineFindingsOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that Red marker should be displayed on scroll slider when user delete machine finding");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(ctNeckPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,1),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");

		point.deleteAllAnnotation(1);
		viewerpage.assertFalse(viewerpage.getAllGSPSObjects(1).isEmpty(),"Checkpoint[2/4]","Verifying there is no annotation deleted rather it should have been rejected");
		viewerpage.assertFalse(viewerpage.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[3/4]","Verifying that there are markers are present post deleting the machine findings");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,1),"Checkpoint[4/4]","Verifying the markers when the machine result are deleted - rejected");

	}

	@Test(groups ={"Chrome","IE11","Edge","US809","DE1376","Positive","BVT"})
	public void test05_US809_TC5290_DE1376_TC5676_verifyMachineFindingsEditingOnSliderWhenAccepted() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that state of marker should be changed to \"Green\" on scroll slider when user edits pending machine finding");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(aidocPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		circle = new CircleAnnotation(driver);
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR,2),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");


		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		circle.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertFalse(viewerpage.getAllGSPSObjects(1).isEmpty(),"Checkpoint[2/4]","Verifying the GSPS objects are present post change its status");
		viewerpage.assertFalse(viewerpage.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[3/4]","Verifying the markers are present post change in machine state");

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR,2),"Checkpoint[4.1/4]","Verifying the markers when the machine result changed");
		viewerpage.assertFalse(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR),"Checkpoint[4.2/4]","Verifying the markers when the machine result loaded");

	}

	@Test(groups ={"Chrome","IE11","Edge","US809","DE1376","Positive","BVT"})
	public void test06_US809_TC5291_DE1376_TC5676_verifyMachineFindingsEditingOnSliderWhenRejected() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS: Verify that state of marker should be changed to \"Red\" on scroll slider when user edits rejected machine finding");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(aidocPatientName, 1);
		circle = new CircleAnnotation(driver);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR,2),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");


		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		viewerpage.assertFalse(viewerpage.getAllGSPSObjects(1).isEmpty(),"Checkpoint[2/4]","Verifying the GSPS objects are present post change its status");
		viewerpage.assertFalse(viewerpage.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[3/4]","Verifying the markers are present post change in machine state");

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR,2),"Checkpoint[4.1/4]","Verifying the markers when the machine result changed");
		viewerpage.assertFalse(viewerpage.verifyMarkerColorOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR),"Checkpoint[4.2/4]","Verifying the markers when the machine result loaded");



	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test07_US809_TC5302_verifyRTEditingOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT : Verify that red marker should be turns into green on scroll slider when user edits rejected RT segment");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");

		rt.rejectSegment(5);
		rt.selectPreviousfromGSPSRadialMenu();

		int []coordinates = new int[] {-40, 50};

		poly = new PolyLineAnnotation(driver);
		List<WebElement> handles = poly.getLinesOfPolyLine(1, 1);

		poly.editPolyLine(handles.get(2), coordinates,handles.get(20));
		poly.waitForTimePeriod(6000);
		rt.assertTrue(rt.verifyAcceptedRTSegment(1),"Checkpoint[2/4]","Verifying the contour is turned in accepted");
		rt.assertTrue(rt.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/4]","Verifying the accept gsps radial menu");

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR, 87),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 300),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR, 350),"Checkpoint[4/4]","Verifying the markers when RT data is edited");



	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test08_US809_TC5303_verifyRTEditingOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT :  Verify that pending (white / grey) marker should be turns into green on scroll slider when user edits pending RT segment");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");

		rt.navigateToFirstContourOfSegmentation(5);

		int []coordinates = new int[] {-40, 50};

		poly = new PolyLineAnnotation(driver);
		List<WebElement> handles = poly.getLinesOfPolyLine(1, 1);

		poly.editPolyLine(handles.get(2), coordinates,handles.get(20));
		poly.waitForTimePeriod(3000);
		rt.assertTrue(rt.verifyAcceptedRTSegment(1),"Checkpoint[2/4]","Verifying the contour is turned in accepted");
		rt.assertTrue(rt.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/4]","Verifying the accept gsps radial menu");

		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 300),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,350),"Checkpoint[4/4]","Verifying the markers when RT data is edited");



	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test09_US809_TC5304_TC5305_TC5306_TC5307_verifyRTEditingOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT :  Verify that mix color (green+red) bar should be displayed on scroll slider when mix types of RT segments (accept, reject, pending) are present"
				+ "<br> RT : Verify that Red marker should be displayed on scroll slider when all segment present on slice are in rejected state"
				+ "<br> RT :  Verify that green bar should be on scroll slider when all RT segments of same slice are in accepted state"
				+ "<br> RT : Verify that for every slice there should be finding marker associated with loaded slice (which contains RT segment)");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		
		// reject + accepted + pending
		rt.rejectSegment(1);
		rt.acceptSegment(rt.legendOptionsList.size());
		
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR, 12),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 300),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,350),"Checkpoint[4/4]","Verifying the markers when RT data is edited");

		rt.rejectSegment(1);
		rt.acceptSegment(rt.legendOptionsList.size());
	
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");

		// pending + reject
		rt.rejectSegment(1);
	
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 300),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");


		// pending + accepted
		rt.acceptSegment(1);
		
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 300),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");

		for(int i =2 ;i<=rt.legendOptionsList.size();i++)
			rt.acceptSegment(i);
	
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");

		for(int i =1 ;i<=rt.legendOptionsList.size();i++)
			rt.rejectSegment(i);
			
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).isEmpty(),"Checkpoint[4/4]","Verifying the markers when RT data is edited");



	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test10_US809_TC5308_verifyMarkersOnSliderForGroupsOfFinding() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS Group : Verify that green / red  marker should be displayed for all group findings on scroll slider when user accept / reject any finding from group");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectlyUsingID(lidcPatientID, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		viewerpage.assertTrue(viewerpage.verifyFindingsStateUnderGroup(1,1, ViewerPageConstants.PENDING_FINDING_COLOR),"Checkpoint[2/3]"," Verifying the state of findings under group in finding menu after accepting the finding of group");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),viewerpage.getMaxNumberofScrollForViewbox(1),"Checkpoint[1/3]","Verifying the state, size of markers");	
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR),"Checkpoint[1/3]","Verifying the state, size of markers");	
		viewerpage.selectFindingFromGroupOfTable(1,1, 1);
		viewerpage.selectAcceptfromGSPSRadialMenu();
		viewerpage.assertTrue(viewerpage.verifyFindingsStateUnderGroup(1,1, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/3]"," Verifying the state of findings under group in finding menu after accepting the finding of group");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR, viewerpage.getMaxNumberofScrollForViewbox(1)),"Checkpoint[3/3]","Verifying the markers has mixed colors");
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),viewerpage.getMaxNumberofScrollForViewbox(1),"Checkpoint[1/3]","Verifying the state, size of markers");	
	
	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test11_US809_TC5309_TC5348_verifyNoMoreMarkerWhenThereIsNoSegments() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT : Verify that no finding markers should be present on slider if segment is not present on slice"
				+ "<br> Layout change : Verify that finding markers should not be present on Source dicom images");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		contentSelector = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		contentSelector.selectSeriesFromSeriesTab(1, viewerpage.getSeriesDescriptionOverlayText(1));

		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertTrue(findingMarkersOnslider.isEmpty(),"Checkpoint[1/2]","Verifying there is no marker when source is loaded");

		viewerpage.mouseHover(viewerpage.getViewPort(1));
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertTrue(findingMarkersOnslider.isEmpty(),"Checkpoint[2/2]","Verifying there is no marker post layout change");


	}

	@Test(groups ={"Chrome","IE11","Edge","US809","Positive"})
	public void test12_US809_TC5309_verifyNoMoreMarkerWhenThereIsNoFindings() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS : Verify that no finding markers should be present on slider when there is no finding present on slice");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertTrue(findingMarkersOnslider.isEmpty(),"Checkpoint[1/1]","Verifying there is no marker when no finding present on slice");



	}

	@Test(groups ={"Chrome","IE11","Edge","F1093","US809","US2345","Positive","E2E"})
	public void test13_US809_TC5311_US2345_TC10009_verifyGSPSWithRTOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS + RT : Verify that mix color (green+red) bar should be displayed on scroll slider when mix types of findings (accept, reject, pending).<br>"+
		"Verify track lines are getting displayed near scrollbar for machine drawn annotations");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		
		point = new PointAnnotation(driver);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,350),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR, 1),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		
		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[2/6]","Verifying the markers are of pending color when GSPS (pending) +RT (pending)");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");

		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu();

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[2/6]","Verifying the markers are of pending color when GSPS (pending) +RT (pending)");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,1),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");


		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[2/6]","Verifying the markers are of pending color when GSPS (pending) +RT (pending)");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,1),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");

		// pending + accepted
		rt.acceptSegment(2);
		rt.selectPreviousfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();
		point.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,350),"Checkpoint[5/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,229),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		
		// pending + reject
		rt.rejectSegment(2);	
		rt.selectPreviousfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu();
		point.selectRejectfromGSPSRadialMenu();
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,350),"Checkpoint[5/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,230),"Checkpoint[1/6]","Verifying the markers are of mixed color when GSPS (accepted) +RT (pending)");

	}

	@Test(groups ={"Chrome","IE11","Edge","F1093","US809","US2345","Positive","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test14_US809_TC5341_US2345_TC9993_TC10008_verifyClickOnMarkers(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that associated slice should get rendered on clicking finding marker. <br>"+
		"Verify track lines are getting displayed near scrollbar for user drawn annotations on eureka theme.<br>"+
		"Verify track lines are getting displayed near scrollbar for user drawn annotations on dark theme");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(ctNeckPatientName,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);

		List<Integer> slicesHavingGSPS = viewerpage.getSlicesWhichHasGSPS(2);

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(2,ViewerPageConstants.PENDING_FINDING_COLOR,slicesHavingGSPS.size()),"Checkpoint[1/2]","Verifying the state of markers - pending");	
		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(2);
		viewerpage.click(findingMarkersOnslider.get(1));
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2), slicesHavingGSPS.get(1),"Checkpoint[2/2]","Verifying on click of marker user is navigated to respective slice");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1054","Positive"})
	public void test15_US1054_TC5635_verifyClickOnMarkersForRT() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify selecting finding from the preview on the slider mark.");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
				
		rt = new DICOMRT(driver);

		List<WebElement> markers = rt.getFindingMarkersOnSlider(1);
		rt.mouseHover(markers.get(0));

		String findingName = rt.getText(viewerpage.findings.get(0));
		List<WebElement> findings = rt.findingsText;
		viewerpage.click(findings.get(0));

		rt.assertEquals(rt.getSelectedContourColor(),rt.getLegendOptionColor(findingName),"Checkpoint[1/2]","Verifying the on clicking of finding that contour is highlighted");	
		rt.assertTrue(rt.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/2]", "verifying that pending gsps toolbar is displayed");


	}

	@Test(groups ={"Chrome","IE11","Edge","US810","Positive"})
	public void test16_US810_TC5377_verifyPreviewListOfFindingsOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify there is a preview of list of findings displayed when the user mouse hover over the mark on the scroll bar in the viewer page. (Individual findings)");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);

		point.closingConflictMsg();

		point.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating multiple point annotations");
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 80);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, 80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, 150);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, -150);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the list of findings which are accepted and header from finding menu");
		List<String> findings = viewerpage.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		String header = viewerpage.getHeaderFromFindingTable();
		
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,1),"Checkpoint[1/18]","Verifying the state of markers - green");		
		viewerpage.assertEquals(findings, viewerpage.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[2/18]", "Verifying the name of findings in slider preview panel");
		viewerpage.assertEquals(viewerpage.getHeaderFromSliderTable(), header,"Checkpoint[3/18]", "verifying the header is same as finding menu");
		
		viewerpage.click(viewerpage.getHeaderFromSlider());
		viewerpage.mouseHover(viewerpage.findings.get(0));
		for(int i =0, j=2;i<10&&j<10;i++,j=j+1) {

			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling the finding using mouse wheel - finding"+j);
			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);
			
			int activePoint = point.getPointWhichIsAcceptedAndActive(1);
			for(int k=1;k<=10;k++) {
				
				if(k==activePoint)
					viewerpage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,k),"Checkpoint[4/18]","Verifying the point is focused");
				else
					viewerpage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,k),"Checkpoint[5/18]","Verifying the other points are not focused");
			}
			viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"Checkpoint[6/18]","verifying the GSPS accept reject toolbar is displayed");


		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting all the findings");		
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		for(int i =2;i<=10;i++)
			point.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the list of findings which are rejected from finding menu");
		findings = viewerpage.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
				
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR,1),"Checkpoint[7/18]","Verifying the state of markers - red");		
		viewerpage.assertEquals(findings, viewerpage.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.REJECTED_FINDING_COLOR), "Checkpoint[8/18]", "Verifying the findings name");		

		viewerpage.click(viewerpage.getHeaderFromSlider());
		viewerpage.mouseHover(viewerpage.findings.get(0));
		for(int i =0, j=2;i<10&&j<10;i++,j=j+1) {

			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling the finding using mouse wheel - finding"+j);
			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);	
		
			int activePoint = point.getPointWhichIsRejectedAndActive(1);
			for(int k=1;k<=10;k++) {
				
				if(k==activePoint)
					viewerpage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,k),"Checkpoint[4/18]","Verifying the point is focused");
				else
					viewerpage.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,k),"Checkpoint[5/18]","Verifying the other points are not focused");
			}
			
			viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"Checkpoint[11/18]","verifying the GSPS accept reject toolbar is displayed");


		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the state of points from rejected to pending");
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		for(int i =2;i<=10;i++)
			point.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the list of findings which are pending from finding menu");
		findings = viewerpage.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);		
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,1),"Checkpoint[12/18]","Verifying the state of markers - pending");		
		viewerpage.assertEquals(findings, viewerpage.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.PENDING_FINDING_COLOR), "Checkpoint[13/18]", "Verifying the findings name");		
		
		viewerpage.click(viewerpage.getHeaderFromSlider());
		viewerpage.mouseHover(viewerpage.findings.get(0));

		for(int i =0, j=2;i<10&&j<10;i++,j=j+1) {

			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling the finding using mouse wheel - finding"+j);
			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
			int activePoint = point.getPointWhichIsPendingAndActive(1);
			for(int k=1;k<=10;k++) {
				
				if(k==activePoint)
					viewerpage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,k),"Checkpoint[4/18]","Verifying the point is focused");
				else
					viewerpage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,k),"Checkpoint[5/18]","Verifying the other points are not focused");
			}
			viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[16/18]","verifying the GSPS accept reject toolbar is displayed");

		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on selective finding from slider preview panel");
		viewerpage.click(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).get(0));
		int activePoint = point.getPointWhichIsPendingAndActive(1);
		for(int k=1;k<=10;k++) {
			
			if(k==activePoint)
				viewerpage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,k),"Checkpoint[4/18]","Verifying the point is focused");
			else
				viewerpage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,k),"Checkpoint[5/18]","Verifying the other points are not focused");
		}
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[18/18]","Verifying the pending GSPS toolbar is displayed");
	}

	@Test(groups ={"Chrome","IE11","Edge","US810","US2441","Positive","F1093"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test17_US810_TC5378_TC5379_US2411_TC10209_TC10210_verifyPreviewListOfFindingsOnSliderForGroups(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings list displayed  when the user mouse hover over the mark on the scroll bar which has group findings on the same slice in the viewer page. (findings of the same group but located in different slices)"
				+ "<br> Verify the findings list displayed  when the user mouse hover over the mark on the scroll bar which has group findings on the same slice in the viewer page. (Group findings on the same slice). <br>"+
				"Verify summary pop up for group findings in eureka theme. <br>"+
				"Verify summary pop up for group findings in dark theme");

		patientPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new  HelperClass(driver);
		helper.loadViewerDirectlyUsingID(lidcPatientID,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the Group findings from finding menu");
		List<String> groups = viewerpage.getListOfGroupsInFindingMenu(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the findings from first group");
		List<String> groupFindings = viewerpage.getListOfFindingsFromFindingMenu(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the slice number which has GSPS objects");
		int slices = viewerpage.getMaxNumberofScrollForViewbox(1);
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,slices),"Checkpoint[1/20]","Verifying the state of markers - pending and count");
		viewerpage.assertTrue(viewerpage.verifyGroupOnSliderContainer(1, 17, 1, groups.get(0), groupFindings, ViewerPageConstants.PENDING_FINDING_COLOR),"Checkpoint[2/20]","Verifying the group on slider container");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the scroll in group findings from slider preview panel");
		viewerpage.mouseHover(viewerpage.listOfFindingsFromGroup.get(0));
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,2);		
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),17,"Checkpoint[3/20]","Verifying the current Slice");		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the textannotation");
		ellipse = new EllipseAnnotation(driver);
		viewerpage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[4/20]","Verifying the respective findings is focused");
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[5/20]","Verifying the pending GSPS toolbar is displayed");

		// Need to check the notification message
//		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv), "Checkpoint[6/20]", "Banner display in header after scroll");
//		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT_FOR_CROSS_SLICES),"Checkpoint[7/20]", "verifying the banner text");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the textannotation");		
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		 // need to check the index for annotation
		textAnn = new TextAnnotation(driver);
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),17,"Checkpoint[12/20]","Verifying the current Slice");
		int activeAnn = textAnn.getTextAnnWhichIsPendingAndActive(1, false);
		
		for(int i=1;i<=textAnn.getTextAnnotations(1).size();i++)
		{
			if(i==activeAnn)
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, i, false),"Checkpoint[13/20]","Verifying the respective findings is focused");
			else
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentPendingInactive(1, i, false),"Checkpoint[13/20]","Verifying the respective findings is focused");
			
		}
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[14/20]","Verifying the pending GSPS toolbar is displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the ellipse");		
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),18,"Checkpoint[15/20]","Verifying the current Slice");
		polyLine = new PolyLineAnnotation(driver);
		viewerpage.assertTrue(polyLine.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[10/20]","Verifying the respective findings is focused");
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[11/20]","Verifying the pending GSPS toolbar is displayed");
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the textannotation");		
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		// need to check the index of annotation
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),18,"Checkpoint[18/20]","Verifying the current Slice");
		
		activeAnn = textAnn.getTextAnnWhichIsPendingAndActive(1, false);
		
		for(int i=1;i<=textAnn.getTextAnnotations(1).size();i++)
		{
			if(i==activeAnn)
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, i, false),"Checkpoint[19/20]","Verifying the respective findings is focused");
			else
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentPendingInactive(1, i, false),"Checkpoint[19/20]","Verifying the respective findings is focused");
			
		}
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[20/20]","Verifying the pending GSPS toolbar is displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the ellipse");		
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),19,"Checkpoint[15/20]","Verifying the current Slice");
		viewerpage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[16/20]","Verifying the respective findings is focused");
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[17/20]","Verifying the pending GSPS toolbar is displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the textannotation");		
		viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),19,"Checkpoint[18/20]","Verifying the current Slice");
		activeAnn = textAnn.getTextAnnWhichIsPendingAndActive(1, false);
		
		for(int i=1;i<=textAnn.getTextAnnotations(1).size();i++)
		{
			if(i==activeAnn)
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, i, false),"Checkpoint[19/20]","Verifying the respective findings is focused");
			else
				viewerpage.assertTrue(textAnn.verifyTextAnnotationIsCurrentPendingInactive(1, i, false),"Checkpoint[19/20]","Verifying the respective findings is focused");
			
		}
		
		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"Checkpoint[20/20]","Verifying the pending GSPS toolbar is displayed");


	}

	@Test(groups ={"Chrome","IE11","Edge","US810","Positive","DE1454"})
	public void test18_US810_TC5480_DE1454_TC5890_verifyConsecutiveFindingsOnPreviewPane() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify how the preview box(mouse hover on the finding mark on the slider) is displayed  when the height of the slider is small - large number of slices and viewer is small."
				+ "<br> Verify if synchronized series are getting scrolled when user scrolls the finding from slider preview box using mouse wheel");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(quanPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating 10 points on current loaded slice");
		point.drawPointAnnotationMarkerOnViewbox(2, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(2, 50, -50);
		point.drawPointAnnotationMarkerOnViewbox(2, -50, 50);
		point.drawPointAnnotationMarkerOnViewbox(2, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(2, 60, 60);
		point.drawPointAnnotationMarkerOnViewbox(2, 60, -60);
		point.drawPointAnnotationMarkerOnViewbox(2, -60, 60);
		point.drawPointAnnotationMarkerOnViewbox(2, -60, -60);
		point.drawPointAnnotationMarkerOnViewbox(2, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(2, 70, -70);

		int currentSlice = viewerpage.getCurrentScrollPositionOfViewbox(2);

		viewerpage.scrollDownToSliceUsingKeyboard(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating another 10 points on next slice");
		point.drawPointAnnotationMarkerOnViewbox(2, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(2, 50, -50);
		point.drawPointAnnotationMarkerOnViewbox(2, -50, 50);
		point.drawPointAnnotationMarkerOnViewbox(2, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(2, 60, 60);
		point.drawPointAnnotationMarkerOnViewbox(2, 60, -60);
		point.drawPointAnnotationMarkerOnViewbox(2, -60, 60);
		point.drawPointAnnotationMarkerOnViewbox(2, -60, -60);
		point.drawPointAnnotationMarkerOnViewbox(2, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(2, 70, -70);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the findings from finding menu and its header");
		List<String> findings = viewerpage.getFindingsName(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		String header = viewerpage.getHeaderFromFindingTable();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout to 3x3");
		layout.mouseHover(layout.getViewPort(1));
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.closingConflictMsg();
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR,1),"Checkpoint[1/7]","Verifying the state of markers - green");
		viewerpage.mouseHover(viewerpage.getFindingMarkersOnSlider(2).get(0));
		viewerpage.assertEquals(viewerpage.getHeaderFromSliderTable(), header,"Checkpoint[2/7]", "Verifying the header with findings header with slider header");	


		List<String> allFindings = new ArrayList<String>();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that if images are on higher side and small space in viewbox, then 1 marker is displayed");
//		viewerpage.mouseHover(viewerpage.getFindingMarkersOnSlider(1).get(0));
		viewerpage.mouseHover(viewerpage.acceptedFindings.get(0));
		
		for(int i =1, j=2;i<=findings.size()&&j<=findings.size();i++,j=j+1) {			

			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);	
			if(j==11) {
				viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),currentSlice,"Checkpoint[3/7]","Verifying the slice is changed");
				currentSlice =viewerpage.getCurrentScrollPositionOfViewbox(2);
			}


			allFindings.add(viewerpage.getText(viewerpage.acceptedFindings.get(i-1)));
			viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"Checkpoint[4/7]","Verifying the accept reject toolbar");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),currentSlice,"Checkpoint[5/7]","Verifying the slice remain same on scroll");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),currentSlice,"Checkpoint[6/7]","Verifying the synchonization behavior");

		}
//		viewerpage.assertEquals(findings, allFindings, "Checkpoint[7/7]", "Verifying all the findings name with finding table with slider menu");


	}

	@Test(groups ={"Chrome","IE11","Edge","US810","Positive","DE1454"})
	public void test19_US810_TC5481_DE1454_TC5890_verifyAllTheFindingsAreDisplayedWhenSliceAreLess() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify how the preview box(mouse hover on the finding mark on the slider) is displayed  when the height of the slider is small - less number of slices in series and viewer is small."
				+ "<br> Verify if synchronized series are getting scrolled when user scrolls the finding from slider preview box using mouse wheel");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		viewerpage.doubleClick(viewerpage.getViewPort(1));

		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating 10 points on current loaded slice");
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, 60, 60);
		point.drawPointAnnotationMarkerOnViewbox(1, 60, -60);
		point.drawPointAnnotationMarkerOnViewbox(1, -60, 60);
		point.drawPointAnnotationMarkerOnViewbox(1, -60, -60);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);

		int currentSlice1 = viewerpage.getCurrentScrollPositionOfViewbox(1);

		viewerpage.scrollDownToSliceUsingKeyboard(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating another 10 points on next slice");
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, 60, 60);
		point.drawPointAnnotationMarkerOnViewbox(1, 60, -60);
		point.drawPointAnnotationMarkerOnViewbox(1, -60, 60);
		point.drawPointAnnotationMarkerOnViewbox(1, -60, -60);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);

		int currentSlice2 = viewerpage.getCurrentScrollPositionOfViewbox(1);

		String header = viewerpage.getHeaderFromFindingTable();
		
		viewerpage.scrollUpToSliceUsingKeyboard(1);
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,2),"Checkpoint[1/7]","Verifying the state of markers - green and count");		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout to 3x3");
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR,2),"Checkpoint[2/7]","Verifying the state of markers - green and count");		
		viewerpage.mouseHover(viewerpage.getFindingMarkersOnSlider(1).get(0));
		viewerpage.assertEquals(viewerpage.getHeaderFromSliderTable(), header.replace("20", "10"),"Checkpoint[3/7]", "Verifying the header");	
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that if images are on lower side and small space in viewbox, then all markers are displayed");
		for(int i =0;i<10;i++) {

			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
			viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"Checkpoint[4/7]","Verifying the accept gsps toolbar");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),currentSlice1,"Checkpoint[5/7]","Verifying the slice number is same on creation of first set of points");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),currentSlice1,"Checkpoint[6/7]","Verifying the slice number is same on other synchronized series");

		}

		viewerpage.mouseHover(viewerpage.getFindingMarkersOnSlider(1).get(1));
		viewerpage.mouseHover(viewerpage.acceptedFindings.get(0));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that if images are on lower side and small space in viewbox, then all markers are displayed");
		for(int i =0;i<10;i++) {			
			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);		
			viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"Checkpoint[7/7]","Verifying the accept gsps toolbar");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),currentSlice2,"Checkpoint[8/7]","Verifying the slice number is same on creation of second set of points");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),currentSlice2,"Checkpoint[9/7]","Verifying the slice number is same on other synchronized series");

		}

	}

	@Test(groups ={"Chrome","IE11","Edge","DE1384","Positive"})
	public void test20_DE1384_TC5682_verifyMarkersOnSlider() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding scroll bar shows all finding even though there are some GSPS findings or SC results.");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-150, -150);

		viewerpage.openSlider(1);
		List<WebElement> findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/9]","Verifying there is one marker present on slider");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/9]","Verifying the state of markers - green");
		viewerpage.assertEquals(viewerpage.getAllFindingsFromSliderContainer(1, 1).size(),1,"Checkpoint[3/9]","Verifying there is one finding present on slider");


		viewerpage.scrollDownToSliceUsingKeyboard(5);
		point.drawPointAnnotationMarkerOnViewbox(1,-150, -150);

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),2,"Checkpoint[4/9]","Verifying there are 2 markers present on slider as annotation is created on different slices");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[5/9]","Verifying the state of markers - green");		
		viewerpage.assertEquals(viewerpage.getAllFindingsFromSliderContainer(1, 2).size(),1,"Checkpoint[6/9]","Verifying there is one finding present on slider");


		viewerpage.scrollDownToSliceUsingKeyboard(5);
		point.drawPointAnnotationMarkerOnViewbox(1,-150, -150);

		findingMarkersOnslider = viewerpage.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),3,"Checkpoint[7/9]","Verifying there are 3 markers present on slider as annotation is created on different slices");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[8/9]","Verifying the state of markers - green");		
		viewerpage.assertEquals(viewerpage.getAllFindingsFromSliderContainer(1, 3).size(),1,"Checkpoint[9/9]","Verifying there is one finding present on slider");

	}

	//DE1368:Content slider finding marks not correct in multiphase
	@Test(groups ={"Chrome","IE11","Edge","DE1368","Positive"})
	public void test21_DE1368_TC5685_TC5686_verifySliderMarkerForPhaseWhichIsBeingLoadedInViewbox() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the slider bar is showing findings for that phase only which is currently loaded in view box. <br>"+
				" Verify that sliding button is only on that finding in siding bar which is loaded currently.");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(ADC_philips_Patient, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);

		point.waitForViewerpageToLoad();
		point.click(point.getViewPort(3));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading multiphase patient "+ADC_philips_Patient  +" on viewer page");
	
		viewerpage.pressKey(NSGenericConstants.DOT_KEY);
		point.selectPointFromQuickToolbar(3);
		point.drawPointAnnotationMarkerOnViewbox(3,-50, -50);
		point.selectRejectfromGSPSRadialMenu();

		
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(3);
		circle.drawCircle(3, -30, -40, -40, -30);
		circle.selectRejectfromGSPSRadialMenu();

		List<WebElement> findingMarkersOnslider = point.getFindingMarkersOnSlider(3);
		point.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/6]","Verifying there is one marker present on slider as results are rejected on a slice");
		point.assertTrue(point.verifyMarkerColorOnSlider(3,ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[2/6]","Verifying the state of markers - red");		

		viewerpage.pressKey(NSGenericConstants.COMMA_KEY);
		point.assertTrue(point.getFindingMarkersOnSlider(3).isEmpty(),"Checkpoint[3/6]","Verifying there is no marker on slice after phase change");
		point.assertFalse(point.verifyMarkerColorOnSlider(3,ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[4/6]","Verifying the state of marker as red not visible after changing the phase to 1");

		viewerpage.pressKey(NSGenericConstants.DOT_KEY);
		point.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[5/6]","Verifying that slider bar is showing findings for  phase2 only which is currently loaded in view box.");
		point.assertTrue(point.verifyMarkerColorOnSlider(3,ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[6/6]","Verifying the state of markers - red");		

	}

	//DE1376: [Hardening] : Finding marker scroll bar : Green bars / findings are getting displayed when perform left click on viewer (when there is no finding)
	@Test(groups ={"Chrome","IE11","Edge","DE1376","Positive"})
	public void test22_DE1376_TC5675_verifyFindingMarkerWhenNoFindings() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Green bars / findings should not get displayed when perform left click on viewer after deleting all the annotations");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		lineWithUnit=new MeasurementWithUnit(driver);

		lineWithUnit.waitForViewerpageToLoad();
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		lineWithUnit.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading patient "+patientName1+" on viewer page");
		List<WebElement> findingMarkersOnslider = lineWithUnit.getFindingMarkersOnSlider(1);
		lineWithUnit.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/5]","Verifying there is one marker present on slider as results are accepted on a slice");
		lineWithUnit.assertTrue(lineWithUnit.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/5]","Verifying the state of markers - green");		

		lineWithUnit.deleteAllAnnotation(1);
		lineWithUnit.assertTrue(lineWithUnit.getAllGSPSObjects(1).isEmpty(), "Checkpoint[3/5]", "Verified that all annotation are deleted on viewbox1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform left click on viewbox after deleting all anotation");
		lineWithUnit.assertTrue(lineWithUnit.getAllGSPSObjects(1).isEmpty(), "Checkpoint[4/5]", "Verified that no new annotation is created after performing left click on viewbox1");
		lineWithUnit.assertTrue(lineWithUnit.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[5/5]","Verifying there is no marker present on slider as no findings are present");




	}

	@Test(groups ={"Chrome","IE11","Edge","DE1688","Positive"})
	public void test23_DE1688_TC7130_verifyNoBreaksOnSliderForRT() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT specific: Verify that there are no line breaks on finding bar when user resizes the browser window");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		ViewerLayout layout = new ViewerLayout(driver);
		rt.assertTrue(rt.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[1/4]","Verifying the markers when the machine result loaded");
		rt.mouseHover(rt.getViewPort(1));

		layout.selectLayout(layout.twoByTwoLayoutIcon);	
		rt.assertTrue(rt.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 175),"Checkpoint[2/4]","Verifying the markers when the machine result loaded");

		rt.resizeBrowserWindow(300, 600);
		rt.mouseHover(rt.getViewPort(1));
		rt.assertTrue(rt.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 117),"Checkpoint[3/4]","Verifying the markers when the machine result loaded");

		rt.maximizeWindow();
		rt.mouseHover(rt.getViewPort(1));
		layout.selectLayout(layout.oneByOneLayoutIcon);		
		rt.assertTrue(rt.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR, 350),"Checkpoint[4/4]","Verifying the markers when the machine result loaded");


	}

	@Test(groups ={"Chrome","IE11","Edge","DE1760","Negative"})
	public void test24_DE1760_TC7136_verifySliderOnRT() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the slider displays the finding mark for all the available segments.");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);

		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		poly = new PolyLineAnnotation(driver);

		int contours = poly.getAllPolylines(1).size();
		rt.assertTrue(rt.verifyMarkerColorOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR,350),"Checkpoint[1/2]","Verifying the markers when the machine result loaded");

		rt.assertEquals(poly.getAllPolylines(1).size(), contours,"Checkpoint[2/2]","The contour should be displayed with respect to the slices and no additional contour should be created.");

	}

	@Test(groups ={"IE11","Chrome","Edge","DE1934","DE2063","Positive"})
	public void test25_DE1934_TC7914_DE2063_TC8289_verifyScrollUsingSliderForGSPS() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of findings is updated on slider in the sync view box. <br>"+
		"[Risk and Impact]: Verify the DE1934 test cases");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		contentSelector = new ContentSelector(driver);
		contentSelector.waitForViewerpageToLoad();
		List<String> results = contentSelector.getAllResults();

		int badgeCount = viewerpage.getBadgeCountFromToolbar(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the same series in the 2nd view box like in the 1st view box." );
		for(int i =0;i<results.size();i++) {

			contentSelector.selectResultFromSeriesTab(2, results.get(i),i+1);
			if(viewerpage.getBadgeCountFromToolbar(2)==badgeCount) {
				break;
			}
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select any finding in the 1st view box and click on Accept button on the AR tool bar." );
		int currentSlice = contentSelector.getCurrentScrollPositionOfViewbox(1);
		point = new PointAnnotation(driver);
		int findings = point.getFindingsCountFromFindingTable(1);
		int findingsMarkerOnslider = point.getFindingMarkersOnSlider(1).size();

		point.mouseHover(point.getViewPort(2));
		point.selectPoint(1, 1);
		point.selectAcceptfromGSPSRadialMenu();

		contentSelector.assertNotEquals(currentSlice, contentSelector.getCurrentScrollPositionOfViewbox(1), "Checkpoint[1/23]", "The selected finding should be Accepted and focus should be moved to next finding.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accept the rest of the findings in the 1st view box using the Accept button on the AR tool bar." );
		for(int i=0;i<findings-1;i++)
			point.selectAcceptfromGSPSRadialMenu();	

		for(int i=1;i<=2;i++) {
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), findings, "Checkpoint[2/23]", "All findings should be Accepted in the loaded series.");
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 2nd view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[3/23]", "[Checkpoint #1] All the findings marks should be in green color in the  "+i+" view box.");
		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Again change the status of all findings to 'Pending' from AR tool bar and verify the status of the findings on slider and finding preview box in both the view boxes.");
		point.mouseHover(point.getViewPort(2));
		point.selectPoint(1, 1);
		for(int i=0;i<findings;i++)
			point.selectAcceptfromGSPSRadialMenu();	

		for(int i=1;i<=2;i++) {
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findings, "Checkpoint[4/23]", "All findings should be Accepted in the loaded series. viewbox 1");
			
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 2nd view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[5/23]", "[Checkpoint #1] All the findings marks should be in pending color in the "+i+" view box.");

		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Go to a slice with no findings and draw annotations Ellipse, Circle, Point, Line, Distance in one of the view box and verify the following in both the loaded series:"+
				"1. Finding mark on the slider." + 
				"2. Findings should be reflected in the Finding Preview box on slider." );

		point.mouseHover(point.getViewPort(1));
		point.scrollDownToSliceUsingKeyboard(1);	

		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 10, 10, 30, 50);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -160, 70, 70);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		lineWithUnit.closeWaterMarkIcon(2);

		List<String> acceptedFindings = point.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		point.assertEquals(acceptedFindings.size(), 3, "Checkpoint[6/23]", "All findings should be Accepted in the loaded series under finding menu");
		point.assertEquals(point.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 3, "Checkpoint[7/23]", "All findings should be Accepted in the loaded series under finding menu");

		for(int i=1 ;i<=2;i++) {
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[8/23]", "[Checkpoint #3] All specified annotations should be drawn and finding marks in green color should be seen on the slider.");
		}
		
		List<String> acceptedFindingsSlider  = point.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		point.assertEquals(acceptedFindingsSlider,acceptedFindings, "Checkpoint[9/23]", "[Checkpoint #4] When mouse hovered on the finding mark on the slider, the findings should be seen in the Finding Preview box with their respective state( green dot before the finding).");
		point.assertEquals(point.getFindingsNameFromSliderContainer(2, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindings, "Checkpoint[10/23]", "[Checkpoint #4] When mouse hovered on the finding mark on the slider, the findings should be seen in the Finding Preview box with their respective state( green dot before the finding).");

		// Scenario - 2

		int allGSPSObjects = point.getAllGSPSObjects(1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Delete one finding from one of the view box and verify the slider and finding preview box in both the viewboxes.");
		circle.click(circle.getViewPort(1));
		circle.deleteCircleUsingARBar(1, 1);
		
		for(int i=1 ;i<=2;i++) {
			point.assertEquals(point.getAllGSPSObjects(i).size(), allGSPSObjects-1, "Checkpoint[11/23]", "The selected finding should be deleted from both the series. from viewbox 1");
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), acceptedFindings.size()-1, "Checkpoint[12/23]", "Deleted finding should also be deleted from finding menu from viewbox 1");
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[13/23]", "[Checkpoint #6] The finding mark of the deleted finding should be removed from the slider.");
		}
		acceptedFindings = point.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		acceptedFindingsSlider  = point.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		point.assertEquals(acceptedFindingsSlider,acceptedFindings, "Checkpoint[14/23]", "[Checkpoint #7] If there are other findings are available on the same slice the Finding Preview box should be updated and the deleted annotation should not be seen in the preview box.");
		point.assertEquals(point.getFindingsNameFromSliderContainer(2, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindings, "Checkpoint[15/23]", "[Checkpoint #7] If there are other findings are available on the same slice the Finding Preview box should be updated and the deleted annotation should not be seen in the preview box.");


		circle.deleteAllAnnotation(1);

		for(int i=1 ;i<=2;i++) {
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.REJECTED_FINDING_COLOR).size(), findings, "Checkpoint[16/23]", "[Checkpoint #8] All the user drawn GSPS should be deleted and the finding marks should be deleted on the slider for the user drawn GSPS.");
			point.assertTrue(point.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(), "Checkpoint[17/23]", "[Checkpoint #8] All the user drawn GSPS should be deleted ");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st and 2nd view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.REJECTED_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[18/23]", "[Checkpoint #9] The status of the machine findings should be updated to 'Rejected' and the same should be reflected on the slider and the Finding Preview box.");
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Again change the status of all findings to 'Pending' from AR tool bar and verify the status of the findings on slider and finding preview box in both the view boxes.");
		point.mouseHover(point.getViewPort(2));
		point.selectPoint(1, 1);		
		for(int i=0;i<findings;i++)
			point.selectRejectfromGSPSRadialMenu();	


		//Update the state of annotation by moving/adding comment to the annotation:

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Move the pending finding in one of the view box and verify the status of the finding on viewer and slider in both the viewboxes.");
		point.movePoint(1, 1, 30, 40);
		
		for(int i=1 ;i<=2;i++) 
			point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(i, 1), "Checkpoint[19/23]", "[Checkpoint #10] The selected finding should be moved to the new location and the finding status should be changed to 'Accepted'.");

		for(int i=1 ;i<=2;i++) {
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findings-1, "Checkpoint[20/23]", "Only that finding should be accepted rest findings should remain in pending");
			point.assertEquals(point.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[21/23]", "moved finding should be turned in accepted");


			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findingsMarkerOnslider-1, "Checkpoint[22/23]", "Verifying there is one less pending marks on viewbox - 1 ");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			point.assertEquals(point.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[23/23]", "[Checkpoint #11] The finding mark on the slider should be in green color.");
		}

	}

	@Test(groups ={"IE11","Chrome","Edge","DE1934","DE2063","Positive"})
	public void test26_DE1934_TC7914_DE2063_TC8289_verifyScrollUsingSliderForSR() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of findings is updated on slider in the sync view box.<br>"+
		"[Risk and Impact]: Verify the DE1934 test cases");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+srPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(srPatientName, 2);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		contentSelector = new ContentSelector(driver);
		contentSelector.closeNotification();		

		List<String> results = contentSelector.getAllResults();


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the same series in the 2nd view box like in the 1st view box." );
		for(int i =0;i<results.size();i++) {

			contentSelector.selectResultFromSeriesTab(1, results.get(i),i+1);
			if(contentSelector.isElementPresent(viewerpage.findingBadge)) {
				break;
			}
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select any finding in the 1st view box and click on Accept button on the AR tool bar." );
		int currentSlice = contentSelector.getCurrentScrollPositionOfViewbox(1);

		circle = new CircleAnnotation(driver);
		int findings = circle.getFindingsCountFromFindingTable(1);
		int findingsMarkerOnslider = circle.getFindingMarkersOnSlider(1).size();

		circle.selectCircle(1, 1);
		circle.selectAcceptfromGSPSRadialMenu();

		contentSelector.assertNotEquals(currentSlice, contentSelector.getCurrentScrollPositionOfViewbox(1), "Checkpoint[1/23]", "The selected finding should be Accepted and focus should be moved to next finding.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accept the rest of the findings in the 1st view box using the Accept button on the AR tool bar." );
		for(int i=0;i<findings-1;i++)
			circle.selectAcceptfromGSPSRadialMenu();	
		
		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), findings, "Checkpoint[2/23]", "All findings should be Accepted in the loaded series.");
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[3/23]", "[Checkpoint #1] All the findings marks should be in green color in the  "+i+" view box.");
		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Again change the status of all findings to 'Pending' from AR tool bar and verify the status of the findings on slider and finding preview box in both the view boxes.");
		circle.selectCircle(1, 1);
		for(int i=0;i<findings;i++)
			circle.selectAcceptfromGSPSRadialMenu();	

		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findings, "Checkpoint[4/23]", "All findings should be Accepted in the loaded series. viewbox "+i);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 2nd view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[5/23]", "[Checkpoint #1] All the findings marks should be in pending color in the  "+i+" view box.");
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Go to a slice with no findings and draw annotations Ellipse, Circle, Point, Line, Distance in one of the view box and verify the following in both the loaded series:"+
				"1. Finding mark on the slider." + 
				"2. Findings should be reflected in the Finding Preview box on slider." );

		circle.scrollUpToSliceUsingKeyboard(1);	

		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 10, 10, 30, 50);


		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -160, 70, 70);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		lineWithUnit.closeWaterMarkIcon(2);

		List<String> acceptedFindings = circle.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		circle.assertEquals(acceptedFindings.size(), 3, "Checkpoint[6/23]", "All findings should be Accepted in the loaded series under finding menu");
		circle.assertEquals(circle.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 3, "Checkpoint[7/23]", "All findings should be Accepted in the loaded series under finding menu");

		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[8/23]", "[Checkpoint #3] All specified annotations should be drawn and finding marks in green color should be seen on the slider.");
		}
		List<String> acceptedFindingsSlider  = circle.getFindingsNameFromSliderContainer(1, 10, ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		circle.assertEquals(acceptedFindingsSlider,acceptedFindings, "Checkpoint[9/23]", "[Checkpoint #4] When mouse hovered on the finding mark on the slider, the findings should be seen in the Finding Preview box with their respective state( green dot before the finding).");
		circle.assertEquals(circle.getFindingsNameFromSliderContainer(2, 10, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindings, "Checkpoint[10/23]", "[Checkpoint #4] When mouse hovered on the finding mark on the slider, the findings should be seen in the Finding Preview box with their respective state( green dot before the finding).");

		// Scenario - 2

		int allGSPSObjects = circle.getAllGSPSObjects(1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Delete one finding from one of the view box and verify the slider and finding preview box in both the viewboxes.");
		circle.click(circle.getViewPort(1));
		circle.deleteCircleUsingARBar(1, 1);
				
		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getAllGSPSObjects(i).size(), allGSPSObjects-1, "Checkpoint[11/23]", "The selected finding should be deleted from both the series. from viewbox 1");
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), acceptedFindings.size()-1, "Checkpoint[12/23]", "Deleted finding should also be deleted from finding menu from viewbox 1");
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[13/23]", "[Checkpoint #6] The finding mark of the deleted finding should be removed from the slider.");
		}
		
		acceptedFindings = circle.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		acceptedFindingsSlider  = circle.getFindingsNameFromSliderContainer(1, 10, ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		circle.assertEquals(acceptedFindingsSlider,acceptedFindings, "Checkpoint[14/23]", "[Checkpoint #7] If there are other findings are available on the same slice the Finding Preview box should be updated and the deleted annotation should not be seen in the preview box.");
		circle.assertEquals(circle.getFindingsNameFromSliderContainer(2, 10, ViewerPageConstants.ACCEPTED_FINDING_COLOR),acceptedFindings, "Checkpoint[15/23]", "[Checkpoint #7] If there are other findings are available on the same slice the Finding Preview box should be updated and the deleted annotation should not be seen in the preview box.");


		circle.deleteAllAnnotation(1);

		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.REJECTED_FINDING_COLOR).size(), findings, "Checkpoint[16/23]", "[Checkpoint #8] All the user drawn GSPS should be deleted and the finding marks should be deleted on the slider for the user drawn GSPS.");
			circle.assertTrue(circle.getFindingsName(i, ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(), "Checkpoint[17/23]", "[Checkpoint #8] All the user drawn GSPS should be deleted ");
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the "+i+" view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.REJECTED_FINDING_COLOR).size(), findingsMarkerOnslider, "Checkpoint[18/23]", "[Checkpoint #9] The status of the machine findings should be updated to 'Rejected' and the same should be reflected on the slider and the Finding Preview box.");

		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Again change the status of all findings to 'Pending' from AR tool bar and verify the status of the findings on slider and finding preview box in both the view boxes.");
		circle.selectCircle(1, 1);
		for(int i=0;i<findings;i++)
			circle.selectRejectfromGSPSRadialMenu();		

		//Update the state of annotation by moving/adding comment to the annotation:

//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Move the pending finding in one of the view box and verify the status of the finding on viewer and slider in both the viewboxes.");
//		circle.selectCircle(1, 1);
//		circle.selectAcceptfromGSPSRadialMenu();
//		
//		for(int i=1 ;i<=2;i++) 
//			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(i, 1), "Checkpoint[19/23]", "[Checkpoint #10] The selected finding should be moved to the new location and the finding status should be changed to 'Accepted'.");
//
//		for(int i=1 ;i<=2;i++) {
//			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.PENDING_COLOR).size(), findings-1, "Checkpoint[20/23]", "Only that finding should be accepted rest findings should remain in pending");
//			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.ACCEPTED_COLOR).size(), 1, "Checkpoint[21/23]", "moved finding should be turned in accepted");
//	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
//			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.PENDING_FINDING_COLOR,ViewerPageConstants.PENDING_FINDING_COLOR).size(), findingsMarkerOnslider-1, "Checkpoint[22/23]", "Verifying there is one less pending marks on viewbox -"+i);
//	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
//			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[23/23]", "[Checkpoint #11] The finding mark on the slider should be in green color.");
//
//		}
	}
	
	@Test(groups ={"IE11","Chrome","Edge","DE1934","DE2063","Positive"})
	public void test26_02_DE1934_TC7914_DE2063_TC8289_verifyScrollUsingSliderForSR() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of findings is updated on slider in the sync view box.<br>"+
		"[Risk and Impact]: Verify the DE1934 test cases");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+srPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(srPatientName, 2);	
		circle = new CircleAnnotation(driver);
		contentSelector = new ContentSelector(driver);
		contentSelector.closeNotification();		

		List<String> results = contentSelector.getAllResults();


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the same series in the 2nd view box like in the 1st view box." );
		for(int i =0;i<results.size();i++) {

			contentSelector.selectResultFromSeriesTab(1, results.get(i),i+1);
			if(contentSelector.isElementPresent(circle.findingsBadge)) {
				break;
			}
		}


		
		int findings = circle.getFindingsCountFromFindingTable(1);
		int findingsMarkerOnslider = circle.getFindingMarkersOnSlider(1).size();

		circle.mouseHover(circle.getViewPort(1));
		//Update the state of annotation by moving/adding comment to the annotation:

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Move the pending finding in one of the view box and verify the status of the finding on viewer and slider in both the viewboxes.");
		circle.addResultComment(circle.getAllCircles(1).get(0),"comment");
		
		for(int i=1 ;i<=2;i++) 
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(i, 1), "Checkpoint[19/23]", "[Checkpoint #10] The selected finding should be moved to the new location and the finding status should be changed to 'Accepted'.");

		for(int i=1 ;i<=2;i++) {
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findings-1, "Checkpoint[20/23]", "Only that finding should be accepted rest findings should remain in pending");
			circle.assertEquals(circle.getFindingsName(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[21/23]", "moved finding should be turned in accepted");
	
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.PENDING_FINDING_COLOR).size(), findingsMarkerOnslider-1, "Checkpoint[22/23]", "Verifying there is one less pending marks on viewbox -"+i);
	
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "In the 1st view box, mouse hover in the slider area to display the slider and verify the state of findings marks on the slider displayed." );
			circle.assertEquals(circle.getStateSpecificMarkerOnSlider(i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), 1, "Checkpoint[23/23]", "[Checkpoint #11] The finding mark on the slider should be in green color.");

		}
	}

	@Test(groups ={"Chrome","IE11","Edge","DE1934","DE2063","Positive"})
	public void test27_DE1934_TC7944_DE2063_TC8289_verifySliderOnSyncRT() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of contour in legend and  finding marks, finding  preview box on slider is updated in the sync view box. <br>"+
		"[Risk and Impact]: Verify the DE1934 test cases");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(rtPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();		
		rt.rejectSegment(1);
		viewerpage.assertEquals(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),350,"Checkpoint[1/13]","[Checkpoint #1] The slider should display the finding mark and the finding mark color should indicate the state of the finding and if there are multiple findings with different state on same slice then the finding mark will be in green and red color.");

		List<String> results = rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR);
		rt.assertEquals(results.size(),1,"Checkpoint[2/13]","[Checkpoint #2] The following checkpoints should be met:\r\n" + 
				"The state of the selected segment should be updated on legend.");

		for(int i =200;i<205;i++)
			rt.assertEquals(rt.getFindingsNameFromSliderContainer(1, i, ViewerPageConstants.REJECTED_FINDING_COLOR).get(0),results.get(0),"Checkpoint[3/13]","The finding should have a green dot before finding name in the Finding Preview box.");

		rt.navigateGSPSBackUsingKeyboard();
		rt.selectAcceptfromGSPSRadialMenu();

		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR, 350),"Checkpoint[4/13]","verifying on changing state the slider color also changed");
		results = rt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR);
		rt.assertEquals(results.size(),1,"Checkpoint[5/13]","State of the selected finding should be in 'Accepted' state.");

		for(int i =100;i<105;i++)
			rt.assertEquals(rt.getFindingsNameFromSliderContainer(1, i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0),results.get(0),"Checkpoint[6/13]","The finding should have a green dot before finding name in the Finding Preview box.");

		ViewerLayout layout = new ViewerLayout(driver);
		rt.mouseHover(rt.getViewPort(1));
		layout.selectLayout(layout.twoByOneLayoutIcon);

		contentSelector = new ContentSelector(driver);
		List<String> clones = contentSelector.getAllResults();	
		contentSelector.selectResultFromSeriesTab(2, clones.get(1),2);
		rt.mouseHover(rt.getViewPort(2));

		List<String> resultsViewbx2 = rt.getStateSpecificSegmentNames(2, ThemeConstants.SUCCESS_ICON_COLOR);
		rt.assertEquals(results, resultsViewbx2, "Checkpoint[7/13]", "State of the selected finding should be in 'Accepted' state.");

		rt.clickOnSegementStateIcon(2, 1);
		rt.selectAcceptfromGSPSRadialMenu();

		results = rt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR);
		resultsViewbx2 = rt.getStateSpecificSegmentNames(2, ThemeConstants.SUCCESS_ICON_COLOR);


		rt.assertEquals(results, resultsViewbx2, "Checkpoint[8/13]", "State of the selected finding should be in 'Accepted' state.");

		for(int i =100;i<105;i++)
		{
			for(int j =0;j<results.size();j++) {
				rt.assertTrue(rt.getFindingsNameFromSliderContainer(1, i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).contains(results.get(j)),"Checkpoint[9/13]","The finding should have a green dot before finding name in the Finding Preview box.");
				rt.assertTrue(rt.getFindingsNameFromSliderContainer(2, i, ViewerPageConstants.ACCEPTED_FINDING_COLOR).contains(results.get(j)),"Checkpoint[10/13]","The finding should have a green dot before finding name in the Finding Preview box.");
			}
		}

		rt.click(rt.getViewPort(1));
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		rt.clickOnSegementStateIcon(1, 3);
		rt.selectRejectfromGSPSRadialMenu();

		results = rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR);
		resultsViewbx2 = rt.getStateSpecificSegmentNames(2, ThemeConstants.ERROR_ICON_COLOR);
		List<String> resultsViewbx3 = rt.getStateSpecificSegmentNames(3, ThemeConstants.ERROR_ICON_COLOR);

		rt.assertFalse(resultsViewbx2.equals(resultsViewbx3), "Checkpoint[11/13]", "Verifying the clone are not having impact on original result");
		rt.assertFalse(results.equals(resultsViewbx3), "Checkpoint[12/13]", "Verifying the clone are not having impact on original result");
		rt.assertEquals(results, resultsViewbx2, "Checkpoint[13/13]", "Verifying the clone are not having impact on original result");


	}

	@Test(groups ={"IE11","Chrome","Edge","F1093","US2345","Positive","E2E"})
	public void test28_US2345_TC10013_verifyTrackScrollbarAfterLayoutChange() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify track scrollbar after changing layout and when Output panel tab is open");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ViewerLayout layout=new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying the slider functionality after layout change.");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		layout.mouseHover(layout.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		int beforeImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, 20);
		int afterImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(beforeImagePos, afterImagePos, "verifying the scroll", "verified");

		//		int sliderHeight = viewerpage.getSliderLine(1).getRect().height;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying the slider functionality when Content selector tab is open.");
		ContentSelector cs=new ContentSelector(driver);
		cs.openAndCloseSeriesTab(true);
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"verifying the slider presence", "verified");
		viewerpage.scrollTheSlicesUsingSlider(1, 0, 0, 0, 20);
		viewerpage.assertNotEquals(afterImagePos, viewerpage.getCurrentScrollPositionOfViewbox(1), "verifying the scroll using slider", "verified");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","F1093","US2345","Positive"})
	public void test29_US2345_TC10011_TC10017_verifySliderWhenCloneLoadsOnPDF() throws InterruptedException, AWTException, IOException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify slider bar handle  gets updated as per image number. <br>"+
		"Verify track scrollbar after replacing series from Series tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "verifying the slider position after scroll the slider.");
		viewerpage.assertTrue(viewerpage.verifySliderPresence(1),"Checkpoint[1/7]", "verifying the slider presence");
		int x=viewerpage.getSlider(1).getRect().getX();
		int y=viewerpage.getSlider(1).getRect().getY();
		
		int beforeImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.scrollDownToSliceUsingKeyboard(5);
		int afterImagePos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertEquals(viewerpage.getSlider(1).getRect().getX(), x, "Checkpoint[2/7]", "verified x coordinate for slider are same.");
		viewerpage.assertNotEquals(viewerpage.getSlider(1).getRect().getY(), y, "Checkpoint[3/7]", "verified y coordinate for slider are change after scroll perform.");
		viewerpage.assertNotEquals(beforeImagePos, afterImagePos, "Checkpoint[4/7]", "Verified that slice number change after scroll perform.");
		
		//TC10017
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "verifying the slider when user load clone copy on PDF viewbox.");
		helper.loadViewerDirectly(pdfPatientName, 2);
		viewerpage.waitForPdfToRenderInViewbox(3);

		viewerpage.waitForElementVisibility(viewerpage.getViewboxNotificationCloseIcon(3));
		viewerpage.click(viewerpage.getViewboxNotificationCloseIcon(3));
		viewerpage.scrollDownUsingPerfectScrollbar(viewerpage.lastItemOnPDF(3),viewerpage.verticalScrollBarComponent.get(0),10,viewerpage.getHeightOfWebElement(viewerpage.verticalScrollBarSlider.get(0)));
	    viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.lastItemOnPDF(3)),"Checkpoint[5/7]","Verify last item of PDF visible after scroll");
		
		point=new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 80);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, 80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, 150);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, -150);

		//load clone copy and verify scroll slider
		ContentSelector cs=new ContentSelector(driver);
		cs.selectResultFromSeriesTab(3,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		cs.assertTrue(viewerpage.verifySliderPresence(3), "Checkpoint[6/7]", "Verified presence of slider.");
		viewerpage.assertTrue(viewerpage.verifyMarkerColorOnSlider(3,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[7/7]","Verifying the state of markers - green");	
		
	}
	
	//US2441: Replace the old findings slider summary popup with the new Eureka design and content
	@Test(groups ={"Chrome","IE11","Edge","US2441","F1093","Negative"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test30_US2441_TC10192_TC10199_verifyLookAndFeelOfSummaryPopup(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the new findings slider summary popup in eureka theme. <br>"+
		"Verify look and feel of the new findings slider summary popup in dark theme");

		patientPage = new PatientListPage(driver);
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new  HelperClass(driver);
		helper.loadRTDirectly(rtPatientName,username, password, 1);

		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		poly = new PolyLineAnnotation(driver);

		ViewerLayout layout=new ViewerLayout(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);

		findingMenu.openFindingTableOnBinarySelector(1);
		pageTheme=new PagesTheme(driver);
		
		findingMenu.mouseHover(findingMenu.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).get(0));
	
		findingMenu.assertTrue(pageTheme.verifyThemeForIcon(findingMenu.getFindingTable(1),loadedTheme),"Checkpoint[1/13]","Verified theme for Summary pop up.");
		findingMenu.assertTrue(findingMenu.getCssValue(findingMenu.getFindingTable(1),NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(NSGenericConstants.SLIDER_CORNER_BORDER_RADIUS),"Checkpoint[2/13]","Verified corner border radius for Summary pop up.");
		
		findingMenu.assertEquals(findingMenu.getColorOfRows(findingMenu.findingHeader),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[3/13]","Verified color for Finding header on summary pop up..");
		findingMenu.assertEquals(findingMenu.getColorOfRows(findingMenu.sliceHeader),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[4/13]","Verified color for slice header on summary pop up.");
		
		findingMenu.assertEquals(findingMenu.getColorOfRows(findingMenu.totalAcceptedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[5/13]","Verified color for accepted finding count on finding summary pop up.");
		findingMenu.assertEquals(findingMenu.getColorOfRows(findingMenu.totalRejectedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[6/13]","Verified color for rejected finding count on finding summary pop up.");
		findingMenu.assertEquals(findingMenu.getColorOfRows(findingMenu.totalPendingFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[7/13]","Verified color for pending finding count on finding summary pop up.");
		
		findingMenu.assertEquals(findingMenu.getBackgroundColor(findingMenu.totalAcceptedFindings),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[8/13]","Verified background for accepted finding box on finding summary pop up .");
		findingMenu.assertEquals(findingMenu.getBackgroundColor(findingMenu.totalRejectedFindings),ViewerPageConstants.REJECTED_FINDING_COLOR,"Checkpoint[9/13]","Verified background for rejected finding box on finding summary pop up.");
		findingMenu.assertEquals(findingMenu.getBackgroundColor(findingMenu.totalPendingFindings),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[10/13]","Verified background for pending finding box on finding summary pop up.");
		
		for(int i=0;i<findingMenu.findings.size();i++)
		{
		findingMenu.assertTrue(pageTheme.verifyThemeOnLabel(findingMenu.findingsText.get(i),loadedTheme),"Checkpoint[11/14]","Verified theme for Finding text on finding summary pop up.");
		findingMenu.assertEquals(findingMenu.getBackgroundColor(findingMenu.findingsState.get(i)),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[12/13]","Verified color for finding state on finding summary pop up.");
		}

	
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.waitForAllChangesToLoad();
		findingMenu.mouseHover(findingMenu.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).get(90));
		findingMenu.assertTrue(findingMenu.isElementPresent(findingMenu.verticalScrollBar), "Checkpoint[13/13]", "Verified that vertical scrollbar is visible on finding summary pop up.");
		 
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2441","F1093","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test31_US2411_TC10200_TC10201_verifySymmaryPopUpForAcceptRejectPendingState(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new findings slider summary popup when findings are accepted/rejected/pending in eureka theme. <br>"+
		"Verify new findings slider summary popup when user drawn findings are accepted/rejected/pending in dark theme");

		patientPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName1,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		pageTheme=new PagesTheme(driver);

		point.closingConflictMsg();

		point.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating multiple point annotations");
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);
		point.selectRejectfromGSPSRadialMenu();
		
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		
		point.drawPointAnnotationMarkerOnViewbox(1, -70, -70);
		point.selectRejectfromGSPSRadialMenu();
		
		point.drawPointAnnotationMarkerOnViewbox(1, 80, -80);
		point.selectAcceptfromGSPSRadialMenu();
		
		point.drawPointAnnotationMarkerOnViewbox(1, -80, 80);
		point.selectAcceptfromGSPSRadialMenu();
		
		int findingCount=viewerpage.getFindingsCountFromFindingTable();
		String sliceInfo=ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+viewerpage.getCurrentScrollPositionOfViewbox(1)+" / "+viewerpage.getMaxNumberofScrollForViewbox(1);
		
		//mousehover on accepted finding track
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		viewerpage.assertEquals(viewerpage.getText(viewerpage.sliceHeader), sliceInfo, "Checkpoint[1/15]", "Verified slice info on accepted finding summary pop up.");
		
		for(int i=0;i<findingCount;i++)
			viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(viewerpage.findingsText.get(i),loadedTheme),"Checkpoint[2/12]","Verified theme for Finding text.");
		
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),2,"Checkpoint[3/15]","Verified count of accepted findings.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.REJECTED_FINDING_COLOR),2,"Checkpoint[4/15]","Verified count of rejected finding.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.PENDING_FINDING_COLOR),2,"Checkpoint[5/15]","Verified count of pending finding.");

	    //mousehover on rejected finding track
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR).get(0));
		viewerpage.assertEquals(viewerpage.getText(viewerpage.sliceHeader), sliceInfo, "Checkpoint[6/15]", "Verified slice info on rejected finding summary pop up");
		
		for(int i=0;i<findingCount;i++)
			viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(viewerpage.findingsText.get(i),loadedTheme),"Checkpoint[7/15]","Verified theme for Finding text.");
		
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),2,"Checkpoint[8/15]","Verified count of accepted finding.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.REJECTED_FINDING_COLOR),2,"Checkpoint[9/15]","Verified count of rejected finding.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.PENDING_FINDING_COLOR),2,"Checkpoint[10/15]","Verified count of pending finding.");

		//mousehover on pending finding track
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).get(0));
		viewerpage.assertEquals(viewerpage.getText(viewerpage.sliceHeader), sliceInfo, "Checkpoint[11/15]", "Verified slice info on pending finding summary pop up");
		
		for(int i=0;i<findingCount;i++)
			viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(viewerpage.findingsText.get(i),loadedTheme),"Checkpoint[12/15]","Verified theme for Finding text.");
		
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),2,"Checkpoint[13/15]","Verified count of accepted finding.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.REJECTED_FINDING_COLOR),2,"Checkpoint[14/15]","Verified count of rejected finding.");
		viewerpage.assertEquals(viewerpage.getFindingsFromSliderContainer(1,ViewerPageConstants.PENDING_FINDING_COLOR),2,"Checkpoint[15/15]","Verified count of pending finding.");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2441","F1093","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test32_US2411_TC10202_TC10203_verifySelectedFindingsOnSummaryPopup(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify selected finding is highlighted in summary pop up in eureka theme. <br>"+
		"Verify selected finding is highlighted in summary pop up in dark theme");

		patientPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		pageTheme=new PagesTheme(driver);
		
		//mousehover on accepted finding track
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.PENDING_FINDING_COLOR).get(0));
		viewerpage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerpage.findingRows.get(0),loadedTheme), "Checkpoint[1/3]", "Verified theme that default first row is selected in finding summary pop up.");
	
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 40, 40,40,40);
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		viewerpage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerpage.findingRows.get(1),loadedTheme), "Checkpoint[2/3]", "Verified that second row is selected for user drawn annotation.");
		
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		viewerpage.assertFalse(pageTheme.verifyThemeForFindingRows(viewerpage.findingRows.get(0),loadedTheme), "Checkpoint[3/3]", "Verified that by default first row is selected when no finding is highlighted.");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2441","F1093","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test33_US2411_TC10204_TC10205_verifyThemeForSelectedFindingRow(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding row from summary pop up is getting highlighted on hover in eureka theme. <br>"+
		"Verify finding row from summary pop up is getting highlighted on hover in dark theme.");

		patientPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName,username,password, 1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		pageTheme=new PagesTheme(driver);

		point.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating multiple point annotations");
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, 80);
	
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		viewerpage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerpage.findingRows.get(0),loadedTheme), "Checkpoint[1/2]", "Verified that default first finding row is highlighted.");

		int findingCount=viewerpage.findingsText.size();
		for(int i=1;i<findingCount;i++)
		{
		viewerpage.mouseHover(viewerpage.findingsText.get(i));
		viewerpage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerpage.findingRowsOnMouseHover.get(i),loadedTheme), "Checkpoint[2/2]", "Verified that row is highlighted on mousehover on finding-"+i);
		}
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2441","F1093","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test34_US2293_TC10206_TC10207_TC10208_verifyScrollAndCommentFunctOnSummaryPopup(String theme) throws InterruptedException, IOException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify focus jumps to corresponding finding in viewer when user clicks on that finding from summary pop up. <br>"+
		"Verify 'i' icon is displayed in summary pop up for findings with comment in eureka theme. <br>"+
		"Verify 'i' icon is displayed in summary pop up for findings with comment in dark theme");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName,username, password,1);
		viewerpage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		pageTheme=new PagesTheme(driver);
		
		point.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating multiple point annotations");
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -70);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, -80, 80);
	
		viewerpage.click(viewerpage.getViewPort(1));
		int totalCount=viewerpage.getAllGSPSObjects(1).size();
		viewerpage.openFindingTableOnBinarySelector(1);
		viewerpage.selectFindingFromTable(6);
		
		int currentScroll=viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		//scroll through finding menu and verify
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Scroll functionality through finding menu.");
		for(int i=2;i<(totalCount/2);i++)
		{
			viewerpage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,2);
			viewerpage.waitForTimePeriod(500);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), currentScroll, "Checkpoint[1/6]","Verified that scroll perfrom successfully.");
			viewerpage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerpage.findingRows.get(i),loadedTheme),"Checkpoint[2/6]","Verified theme for selected finding in finding table.");
			point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, point.getPointWhichIsAcceptedAndActive(1)),"Checkpoint[3/6]","Verified that point annotation is selected on viewer when scroll perform .");
		}
		
		//TC10208
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify theme for I icon and tooltip.");
		String comment="Point Comment";
		viewerpage.selectFindingFromTable(4);
		point.addResultComment(1,comment);
		
		viewerpage.mouseHover(viewerpage.getStateSpecificMarkerOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0));
		viewerpage.assertTrue(pageTheme.verifyButtonIsFilled(viewerpage.iIconInfo.get(0),loadedTheme),"Checkpoint[4/6]", "Verified theme for I icon.");
		viewerpage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerpage.getTooltipWebElement(viewerpage.iIconInfo.get(0)),loadedTheme), "Checkpoint[5/6]", "Verified tooltip for Comment.");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.tooltip), comment,"Checkpoint[6/6]", "Verified text of tooltip.");
	}

	
    @AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(username);
		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);

	}



}

