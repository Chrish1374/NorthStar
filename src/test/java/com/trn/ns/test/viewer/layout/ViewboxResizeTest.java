package com.trn.ns.test.viewer.layout;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I15_F185_LayoutRefactoring-CF0304ARevD - revision-0 
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewboxResizeTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private OutputPanel panel;
	private ViewerTextOverlays textOverlay;
	private ViewerOrientation orientation;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;


	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String aH4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("AH4_pdf_filepath");
	String aH4PDFPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String Imbio_Texture_FilePath = TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String Imbio_Texture_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Imbio_Texture_FilePath);

	private PointAnnotation point;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private SimpleLine line;
	private TextAnnotation textAn;
	private PolyLineAnnotation Polyline;
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	

	}

	//TC2359- Manage the viewer layout and viewboxe's sizes- DE672
	//	TC1: Manage the viewer layout and viewboxes sizes- DE672 ( (Automated)
	@Test(groups ={"firefox","Chrome","IE11","US591"})
	public void test01_US591_TC2359_verifyMinimizeWindowAndTextOverlay() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Manage the viewer layout and viewboxes sizes- DE672");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerPageUsingSearch(aH4PatientName, 1, 1);
		ContentSelector cs=new ContentSelector(driver);
		viewerPage.closingConflictMsg();
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aH4PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying text overlays on minimising window");
		viewerPage.resizeBrowserWindow(768, 1024);

		textOverlay=new ViewerTextOverlays(driver);
		orientation=new ViewerOrientation(driver);
		cs.openAndCloseSeriesTab(false);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.waitForFullOverlayDisplay(1);

		//Verifying Text overlay labels for Full selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1] Checkpoint[2/3]", "Validate full overlay labels on viebox");
		viewerPage.assertTrue(textOverlay.compareTextOverlayValues(1,filePath), "Checkpoint TC1[1] Verifying selected overlay texts are displaying", "Overlay texts are displaying in Full mode");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1] Checkpoint[3/3]", "Validate orientation markers on viebox");
		viewerPage.assertTrue(orientation.isOrientationDisplayed(1,filePath), "Checkpoint TC1[1] Verifying that orientation are displaying", "Orientation are displaying in Full mode");

	}

	//	TC2: Manage the viewer layout and viewboxes sizes  (Automated) 
	@Test(groups ={"firefox","Chrome","IE11","US591"})
	public void test02_US591_TC2502_verifyMinimiseWindowAndAnnotations() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Manage the viewer layout and viewboxes sizes");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		Polyline = new PolyLineAnnotation(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aH4PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing annotations one of each type (circle, ellipse, polyline, line, distance measurement, text annotation) ");

		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, -100, 50, "Automation_US591");

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 100, 100);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,100,-100);

		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,50,50,60,20);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -100, -80,-50);	

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -100, -100,-50,-50);

		Polyline.selectPolylineFromQuickToolbar(1);
		Polyline.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Minimizing window");
		viewerPage.resizeBrowserWindow(768, 1024);

		//Again maximize window and verify the position of all annotations remain same
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Maximizing window");
		viewerPage.maximizeWindow();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying presence of annotation drawn after restoring window size to original one");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify position of drawn GSPS objects","test02_CheckPoint1");

		//Apply windowing and verify the size and position of GSPS objects
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(10, 10, 50, 60);

		//Apply PAN and verify the size of GSPS annotations
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1]: Checkpoint[2/3]", "Verifying  size and position of each annotation after PAN and windowing");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify position of drawn GSPS objects","test02_CheckPoint2");

		//Apply zoom and then again restore to original value
		preset=new ViewBoxToolPanel(driver);
		int beforeZoomValue = preset.getZoomValue(1);
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(10, 10, 50, 60);

		//Restoring zoom value
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,beforeZoomValue);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[2]: Checkpoint[3/3]", "Verifying  size and position of each annotation after restoring zoom value");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify size of drawn GSPS objects","test02_CheckPoint3");

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US591"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_ResizeViewer"})
	public void test03_US591_TC2362_verifyMinimiseWindowAndAnnotations(String patientFilepath) throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Manage the viewer layout and viewboxes sizes- DE409");

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerPageUsingSearch(patientName, 1, 1);

		viewerPage.waitForPdfToRenderInViewbox(3);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );

		//Change layout to 1x1L - 3x1R  and verify PDF is loaded properly
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying PDF is rendered uniformly and consume full space in viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3), "Verify PDF is rendered properly in viewbox","test03_"+patientName+"_CheckPoint1");

		//Change layout to 2x1L - 1x1R and verify PDF is loaded properly
		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying PDF is rendered uniformly and consume full space in viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3), "Verify PDF is rendered properly in viewbox","test03_"+patientName+"_CheckPoint2");

	}

	//TC2469- Manage the viewer layout and viewboxe's sizes- DE680
	@Test(groups ={"firefox","Chrome","IE11","US591"})
	public void test04_US591_TC2469_verifyMinimizeWindowAndTextOverlay() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Manage the viewer layout and viewboxes sizes- DE680");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(aH4PDFPatientName);	

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForPdfToRenderInViewbox(3);
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aH4PDFPatientName+" in viewer" );

		//select PAN from context menu and perform PAN
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 200, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying PDF is rendered uniformly and consume full space in viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"DICOM image is loaded leaving no empty gaps within the viewbox while the image is Panned","test04_CheckPoint1");

		//Change layout to 1x1L - 3x1R and perform PAN operation on Viewbox1
		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		viewerPage.dragAndReleaseOnViewer(0, 0, 200, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying PDF is rendered uniformly and consume full space in viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"DICOM image is loaded leaving no empty gaps within the viewbox while the image is Panned","test04_CheckPoint2");

		//Change layout to 1x1L - 3x1R and perform PAN operation on Viewbox1
		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerPage.dragAndReleaseOnViewer(0, 0, 200, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying PDF is rendered uniformly and consume full space in viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"DICOM image is loaded leaving no empty gaps within the viewbox while the image is Panned","test04_CheckPoint3");

	}

	@Test(groups ={"Chrome","Edge", "IE11","DE1278"})
	public void test05_DE1278_TC5509_HorizontalScrollBarOnPdf() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying that No Horizontal scroll bar present on pdf view box");

		patientPage = new PatientListPage(driver);
		HelperClass helper=new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(Imbio_Texture_patientName, 1, 3);
	
		viewerPage.waitForPdfToRenderInViewbox(2);
		
		panel=new OutputPanel(driver) ;

		//Loading the patient on viewer

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Resizing the browser and checking the presenece of Horizontal scroll bar.");
		//Resizing the browser and Verifying that scroll  bar on PDF view box.
		viewerPage.resizeBrowserWindow(800, 500);
		viewerPage.maximizeWindow();
		viewerPage.mouseHover(viewerPage.PRESENCE,viewerPage.getViewbox(2), 200, 220);
		viewerPage.assertFalse(viewerPage.verifyPropertyOfRegularHorizontalScrollBar(viewerPage.horizontalScrollBarComponent.get(viewerPage.horizontalScrollBarComponent.size()-1), viewerPage.horizontalScrollBarSlider.get(viewerPage.horizontalScrollBarSlider.size()-1)), "Verifying the presence of Scroll bar", "Scroll bar is not present");

		//Hovering the mouse on the Upper area of the Output panel button.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verifying that user is able to open the Output panel by clicking on upper area of output panel button");
		panel.enableFiltersInOutputPanel(true,true,true);
		viewerPage.assertTrue(panel.verifyOutputPanelIsOpened(), "Verifying the state of Output panel", "OutPut panel is opened");


	}

}

