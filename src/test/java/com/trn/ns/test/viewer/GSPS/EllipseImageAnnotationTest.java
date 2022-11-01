package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Safety.NS_F169_ImageAnnotationTools-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class EllipseImageAnnotationTest extends TestBase{
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
	String GSPS_ELLIPSE_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	private String firstSeriesDescriptionGSPSEllipse = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath"));

	String filePath1=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);

	String filePath2=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	private EllipseAnnotation ellipse;
	private HelperClass helper;
	private ViewBoxToolPanel presetMenu;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

	}

	//TC1714 : As a NS user I want to see Image annotation- Ellipse on GSPS data
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test01_US524_TC1714_verifyImageAnnotationEllipse() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "As a NS user I want to see Image annotation- Elipse on GSPS data");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify the presence of Image Annotation :Ellipse");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying the presence of Image Annotation :Ellipse", "viewbox1_Ellipse");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

	}

	//TC1715 : As a NS user I want to see Image annotation- Ellipse (GSPS data) to be consistent with change in Layout
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524","BVT"})
	public void test02_US524_TC1715_verifyImageAnnotationEllipseOnLayoutChange() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "As a NS user I want to see Image annotation- Ellipse (GSPS data) to be consistent with change in Layout");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		ellipse = new EllipseAnnotation(driver);
		cs = new ContentSelector(driver);
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the layout change");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		//Select same series in second viewbox
		cs.selectResultFromSeriesTab(2,cs.getAllResults().get(0));
		ellipse.closingConflictMsg();
		
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(2).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(2).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		//Select same series in third viewbox
		cs.selectSeriesFromSeriesTab(3,firstSeriesDescriptionGSPSEllipse);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Ellipses are located in the same region in both view boxes 2 and 3");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying that GSPS annotation Ellipses are located in the same region in both view boxes 2 and 3", "Ellipse_layoutChange_Viewbox1");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(2), "Checkpoint 2: Verifying that GSPS annotation Ellipses are located in the same region in both view boxes 2 and 3", "Ellipse_layoutChange_Viewbox2");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(3), "Checkpoint 3: Verifying that GSPS annotation Ellipses are located in the same region in both view boxes 2 and 3", "Ellipse_layoutChange_Viewbox3");
	}

	//TC1717 : Image annotation: Ellipse- Annotation Level Change
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test04_US524_TC1717_verifyImageAnnotationEllipseOnAnnotationLevelChange() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Ellipse- Annotation Level Change");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		ellipse = new EllipseAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Selecting minimum text overlay" );
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.waitForMinimumOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that the GSPS annotation Ellipses remains consistent after applying minimum text overlay");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying that the GSPS annotation Ellipses remains consistent after applying minimum text overlay", "Ellipse_MinimumOverlay");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Selecting Full text overlay" );
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify that the GSPS annotation Ellipses remains consistent after applying full text overlay");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 2: Verifying that the GSPS annotation Ellipses remains consistent after applying full text overlay", "Ellipse_FullOverlay");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
		
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
	}

	//TC1718 : Image annotation: Ellipse- With Pan
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test05_US524_TC1718_verifyImageAnnotationEllipseWithPan() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Ellipse- With Pan");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectPanFromQuickToolbar(ellipse.getViewPort(1));
		ellipse.dragAndReleaseOnViewer(ellipse.getViewPort(1), 0, 0, 300, 0);

				
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Ellipses remains consistent after applying PAN");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying that the GSPS annotation Ellipses remains consistent after PAN", "Ellipse_Pan");

		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1719 : Image annotation: Ellipse- With Zoom
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test06_US524_TC1719_verifyImageAnnotationEllipseWithZoom() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Ellipse- With Zoom");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectZoomFromQuickToolbar(ellipse.getViewPort(1));
		ellipse.dragAndReleaseOnViewer(ellipse.getViewPort(1), 0, 0, 0, 10);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Ellipses remains consistent after applying Zoom");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying that the GSPS annotation Ellipses remains consistent after Zoom", "Ellipse_Zoom");

		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1720 : Image annotation: Ellipse- Rotation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test07_US524_TC1720_verifyImageAnnotationEllipseRotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Ellipse- Rotation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		ViewerOrientation orin = new ViewerOrientation(driver);
		ellipse = new EllipseAnnotation(driver);
		ellipse.waitForTimePeriod(6000);
//		viewerPage.click(viewerPage.getViewPort(1));
		orin.flipSeries(orin.getTopOrientationMarker(1));
//		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(1), viewerPage.topClockwiseRotationMarker(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Ellipses remains consistent while image is rotated");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint 1: Verifying that the GSPS annotation Ellipses remains consistent while image is rotated", "Ellipse_FlipImage");

		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1770 : Image annotation: Ellipse- Rotation
	//Limitation : Not work in firefox because of DE456
//	TC2: Correctness of GSPS ellipse annotation (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US524"})
	public void test08_US524_TC1770_verifyImageAnnotationEllipseRotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: ellipse- Accuracy Test");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );
	
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(GSPS_ELLIPSE_PatientName, 1, 1);
		
		ellipse = new EllipseAnnotation(driver);
		String CX1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);
		String CY1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY);
		String RX1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RX);
		String RY1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RY);
		String STROKE_WIDTH1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH);
		String FILL_OPACITY1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.FILL_OPACITY);
		String STROKE1=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.STROKE);
		
		String CX2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.CX);
		String CY2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.CY);
		String RX2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.RX);
		String RY2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.RY);
		String STROKE_WIDTH2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH);
		String FILL_OPACITY2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.FILL_OPACITY);
		String STROKE2=ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.STROKE);

		helper.browserBackAndReloadViewer(GSPS_ELLIPSE_PatientName,1, 1);
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] : Checkpoint[1/1]", "Verify the presence of Image Annotation :Ellipse");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1), "Checkpoint TC2[1] : Checkpoint 1: Verifying the presence of Image Annotation :Ellipse", "viewbox1_Ellipse");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	
		//Accuracy test
	
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX),CX1,"verifying cx","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY),CY1,"verifying cy","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RX),RX1,"verifying rx","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RY),RY1,"verifying ry","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),STROKE_WIDTH1,"verifying strokewidth","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.FILL_OPACITY),FILL_OPACITY1,"verifying fill-opacity","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.STROKE),STROKE1,"verifying stroke","verified");


		//Accuracy test
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.CX),CX2,"verifying cx","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.CY),CY2,"verifying cy","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.RX),RX2,"verifying rx","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.RY),RY2,"verifying ry","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),STROKE_WIDTH2,"verifying strokewidth","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.FILL_OPACITY),FILL_OPACITY2,"verifying fill-opacity","verified");
		ellipse.assertEquals(ellipse.getEllipses(1).get(1).getAttribute(NSGenericConstants.STROKE),STROKE2,"verifying stroke","verified");
	}

	//TC1825:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","US2325","F1090","E2E"})
	public void test09_US585_TC1825_TC1826_US2325_TC9777_verifyEllipseAnnotationIcon() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Ellipse Annotation Icon on Context Menu.<br/>Verify that on clicking annotaion tool icon first time, the icon should be enabled."
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, 1);		
		ellipse = new EllipseAnnotation(driver);
		
		//Perform a right click and validate ellipse icon on Radial Menu.
		ellipse.openQuickToolbar(ellipse.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that Ellipse Annotation Icon is present on outer arch of radial bar");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.ellipseIcon),"Verify that Ellipse Annotation Icon is present on outer arch of radial bar","Ellipse Annotation Icon is present on outer arch of radial bar");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.assertTrue(ellipse.checkCurrentSelectedIcon(ViewerPageConstants.ELLIPSE),"checkpoint[2/3]","Verifying ellipse icon is selected in quickbar");
		
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ELLIPSE), "checkpoint[3/3]","Verifying ellipse icon is selected in viewe bar");
	
	
	}

	//TC1827:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test10_US585_TC1827_TC1828_TC1832_verifyDrawingOfEllipseAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to draw Ellipse annotation on DICOM.<br/>Verify user is unable to draw Ellipse on PDF.<br/>Verify that annotaion tool keep selected even after annoation drawn in viewbox.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	
		ellipse.closingConflictMsg();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());

		//Draw a Ellipse on PDF rendered on View box 4
		ellipse.drawEllipse(2, 0, 0, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is unable to draw a Ellipse annotation on PDF");
		ellipse.assertEquals(ellipse.getEllipses(2).size(),0, "Verify Ellipse is not drawn on PDF", "No of Ellipse on Viewbox4  :"+ellipse.getEllipses(4).size());

		//Verify circle annotation is selected even after annotation is drawn
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify that Ellipse annotation icon is selected");
		ellipse.assertTrue(ellipse.checkCurrentSelectedIcon(ViewerPageConstants.ELLIPSE),"Verifying Ellipse annotation icon is selected", "Ellipse annotation icon is selected");
	} 

	//TC1829:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","DE902","DE1329","Positive","Sanity","BVT"})
	public void test11_US585_TC1829_DE902_DE1329_TC5646_verifyUserCanMoveEllipseAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to move Ellipse annotation."
				+ "<br> Verify that ellipse position should get save after moving");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerPage(patientName, "", 1, 1);
		ellipse = new EllipseAnnotation(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0,-100,-150);	
		ellipse.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1: "+ellipse.getEllipses(1).size());

		//Variable to store position on Center of Ellipse before moving
		String  beforeCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY);	

		//Move Ellipse to the Left
//		ellipse.selectEllipse(1, 1);
		ellipse.mouseHover(ellipse.getViewPort(2));
//		ellipse.selectEllipse(1, 1);
		ellipse.moveEllipse(1,1,100,120);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to move Ellipse from one point to another");
		ellipse.assertNotEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse changes from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertNotEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse change om moving Ellipse", "The Y Co-ordinate of Center of Ellipse changes from "+beforeCY+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY));	
		
		helper.browserBackAndReloadViewer(patientName, "", 1, 1);
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ellipse = new EllipseAnnotation(driver);
		String afterCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);   
		String afterCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY); 
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the state is saved on viewer reload");
		ellipse.assertEquals( afterCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX),"Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse is same before and after reload of viewer from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertEquals(afterCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse change om moving Ellipse", "The Y Co-ordinate of Center of Ellipse is same before and after reload of viewer from "+beforeCY+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY));	
	
		Header header = new Header(driver);
		header.logout();
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		helper.loadViewerPage(patientName, "", 1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify the state is saved post logout and on viewer reload");
		ellipse.assertEquals(afterCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse is same before and after reload of viewer from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertEquals(afterCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse change om moving Ellipse", "The Y Co-ordinate of Center of Ellipse is same before and after reload of viewer from "+beforeCY+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY));	
	
		
	}


	//TC1830:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","US2329","Positive","F1090","E2E"}) 
	//- commented since it is started showing notification
	public void test12_US585_TC1830_US2329_TC10165_verifyDeletionOfEllipseAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to delete Ellipse annotation. <br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	
		for(int i=2;i<5;i++)
			ellipse.closeWaterMarkIcon(i);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//Draw a Ellipse on VIewbox1
		ellipse.drawEllipse(1, 50, 50, 110,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to draw a multiple Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify multiple Ellipse annotation is drawn on Viewbox1", "Multiple Ellipse annotation is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),2, "Verify no of Ellipse annotation on View box1", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//select the first Ellipse and press delete button
		ellipse.selectEllipse(1, 1);
		ellipse.deleteSelectedEllipse();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that selected Ellipse is deleted");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify no of Ellipse annotation on View box1", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//select the second Ellipse and press delete button
		ellipse.selectEllipse(1, 1);
		ellipse.deleteSelectedEllipse();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify that selected Ellipse is deleted");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),0, "Verify no of Ellipse annotation on View box1", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

	} 

	//TC1899:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test13_US585_TC1899_verifyEllipseAnnotationAreNotPannedOnRightClick() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Cricle Annotation are not getting panned on right click");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//Draw a Ellipse on Viewbox1
		ellipse.drawEllipse(1, 50, 50, 10,180);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to draw a multiple Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify multiple Ellipse annotation is drawn on Viewbox1", "Multiple Ellipse annotation is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),2, "Verify no of Ellipse annotation on View box1", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//Perform a Right Click on Ellipse 1
		ellipse.performMouseRightClick(ellipse.getEllipses(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		ellipse.assertFalse(ellipse.verifyAcceptGSPSRadialMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
//		viewerPage.assertFalse(viewerPage.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
//		viewerPage.assertFalse(viewerPage.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		//hover mouse to some other location and verify image is not getting panned
		ellipse.mouseHover(ellipse.getEllipses(1).get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify DICOM images are not getting panned");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify DICOM images are not getting panned","TC1899_Ellipses_CheckPoint4");

	} 


	//TC1833:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test14_US585_TC1833_verifyOnZoomEllipseAnnotationChangesPropotionally() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of Ellipse annotation will be change proportionally with image");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 130,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());

		// get Zoom level for Canvas 0 before Zoom
		presetMenu=new ViewBoxToolPanel(driver);
		
		int intialZoomLevel1 = presetMenu.getZoomValue(1);

		//Select a Zoom from radial bar and perform Zoom down
		ellipse.selectZoomFromQuickToolbar(1);
		ellipse.dragAndReleaseOnViewer(ellipse.getViewbox(1), 0, 0, 50,50);

		// get Zoom level for Canvas 0 before Zoom
		int finalZoomLevel1 = presetMenu.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		ellipse.assertTrue(finalZoomLevel1 < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the size of Ellipse annotation will be change proportionally with image");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify the size of Ellipse annotation will be change proportionally with image on Zoom down","TC1833_CheckPoint3");

		//Perform Zoom Up
		ellipse.dragAndReleaseOnViewer(ellipse.getViewbox(1), 0, 0, -50,-50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the size of Ellipse annotation will be change proportionally with image");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify the size of Ellipse annotation will be change proportionally with image on Zoom down","TC1833_CheckPoint4");

	} 


	//TC1835:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test15_US585_TC1835_verifyOnPanEllipseAnnotationMovesAlongWithImage() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on pan the Ellipse annotation move along with image");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 130,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());

		//variable to store radius of Ellipse annotation
		String beforeRadius=ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.R);

		//Perform Pan on Viewbox1
		ellipse.selectPanFromQuickToolbar(ellipse.getViewPort(1));
		ellipse.dragAndReleaseOnViewer(ellipse.getViewbox(1), 0, 0, 50,50);

		//verify size of radius remain same on panning 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify on pan the Ellipse annotation move along with image");
		ellipse.assertEquals(beforeRadius,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.R), "Verify size of radius remain after pan", "The Size of radius remain same ans is :"+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.R));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify on pan the Ellipse annotation move along with image");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify on pan the Ellipse annotation move along with image","TC1835_CheckPoint3");

	} 

	//TC1836:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test16_US585_TC1836_verifyUserCannotMoveEllipseAnnotationToOtherViewbox() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is unable to move Ellipse annotation to other viewbox");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0,150,100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1: "+ellipse.getEllipses(1).size());

		//Variable to store position on Center of Ellipse before moving
		String  beforeCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY);	

		//Move Ellipse to the Left
		ellipse.moveSelectedEllipse(1,170,170);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to move Ellipse from one point to another with Viewbox");
		ellipse.assertNotEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse changes from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertNotEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse change om moving Ellipse", "The Y Co-ordinate of Center of Ellipse changes from "+beforeCY+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that user is unable to move Ellipse annotation in other Viewbox1");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify that user is unable to move Ellipse annotation in other Viewbox1","TC1836_CheckPoint3");

	}



	//TC1892:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test22_US585_TC1892_verifyPositionOfEllipseAnnotationOnLayoutChange() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify position of Ellipse annotation remains same on layout change");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 20, 20,250,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1: "+ellipse.getEllipses(1).size());

		//Variable to store position on Center of Ellipse before moving
		String  beforeCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY);	

		//Change layout to 1X1
		layout.selectLayout(layout.oneByOneLayoutIcon);

		//Verify Center position change after layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to move Ellipse from one point to another with Viewbox");
		ellipse.assertNotEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse changes from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertNotEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse change om moving Ellipse", "The Y Co-ordinate of Center of Ellipse changes from "+beforeCY+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify position of Ellipse annotation remains same on layout change");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify position of Ellipse annotation remains same on layout change","TC1892_CheckPoint3");

	}

	
	//TC1835:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","DE1329"})
	public void test18_US585_TC1831_DE1329_TC5646_verifyResizingOfEllipseAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to resize the circle annotation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 20, 20, 150,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());

		//Variable to store position on Center of Ellipse before moving
		String  beforeCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY);	

		//resize Ellipse Annotation
		ellipse.resizeEllipse(1,1,50,0);

		//Verify Center position change after resize
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that position of center changes on resizing");
		ellipse.assertNotEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of Ellipse change om moving Ellipse", "The X Co-ordinate of Center of Ellipse changes from "+beforeCX+" to "+ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CX));
		ellipse.assertEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of Ellipse not getting change as its resizing horizontally", "verified");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify that user is able to resize the circle annotation");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify that user is able to resize the circle annotation","TC1831_CheckPoint3");

	} 


	//TC1837 : As a NS user I want to see Image annotation- Ellipse on GSPS data
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test19_US585_TC1837_verifyEllipseAnnotationBehaviourForGSPSData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Ellipse Annotation behaviour for GSPS Data");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify the presence of Image Annotation :Ellipse");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		//select the first ellipse annotation and perform resize
		ellipse.resizeEllipse(1, 2, 100,0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to resize the ellipse annotation");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify that user is able to resize the ellipse annotation","TC1837_CheckPoint2");

		//select the second ellipse annotation and perform move
		ellipse.selectEllipse(1, 1);	
		ellipse.moveSelectedEllipse(1, 100, 150);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that user is able to move the ellipse annotation");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify that user is able to move the ellipse annotation","TC1837_CheckPoint3");

		//delete current active ellipse annotation
		ellipse.deleteSelectedEllipse();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to delete a Ellipse annotation");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),2, "Verify no of ellipse annotation on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());

	} 


	//TC1844 : As a NS user I want to see Image annotation- Ellipse on GSPS data
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","US950"})
	public void test20_US585_TC1844_TC1845_TC1846_TC1847_US950_TC4300_TC4303_verifyAcceptRejectRadialMenuForGSPSDataWithEllipseAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Accept/Reject radial menu should be displayed when GSPS eclipse data displayed in viewers page."
				+ "<br/>Verify that circle should be accepted or Rejected rejected on clicking cross button of accept/reject radial context menu"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify the presence of Image Annotation :Ellipse");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		ellipse.assertTrue(ellipse.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		ellipse.assertTrue(ellipse.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		ellipse.assertTrue(ellipse.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify one Ellipse GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Verify first Ellipse annotation in slice is current active GSPS object and accepted by default", "The first Ellipse annotation is current active GSPS object and accepted by default");

		//Click on the Reject button for a Current Active GSPS object
		ellipse.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify on clicking Reject button current GSPS circle annotation is rejected");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1), "Verify current ellipse annotation is rejected", "The current ellipse annotation is rejected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify on clicking Reject button GSPS is automatically shifted to the next GSPS result on the slice");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "GSPS is automatically shifted to the next GSPS result on the slice", "The current ellipse annotation is rejected");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint5");

		//Click on Next button for current active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify on clicking next button GSPS is automatically shifted to the next GSPS result on the slice");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1,1), "GSPS is automatically shifted to the next GSPS result on the slice", "GSPS is automatically shifted to the next GSPS result on the slice");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint6");

		//Click on the Accept button for a Current Active GSPS object

		ellipse.selectAcceptfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify on clicking Accept button current GSPS circle annotation is accepted");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Verify current ellipse annotation is accepted", "The current ellipse annotation is accepted");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify on clicking accept button GSPS is automatically shifted to the next GSPS result on the slice");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1), "GSPS is automatically shifted to the next GSPS result on the slice", "GSPS is automatically shifted to the next GSPS result on the slice");
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint8");
	}

	//TC1881:Image annotation - Ellipse/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","BVT"})
	public void test21_US585_TC1881_verifyOverlappingEllipseAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when eclipse annotation overlaps each other than only one should get selected.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 10,90,100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());

		//Draw a Ellipse on Viewbox1
		ellipse.drawEllipse(1, 0, 0,100,120);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to draw a multiple Ellipse annotation on DICOM");	
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),2, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 : "+ellipse.getEllipses(1).size());				

		//select the first ellipse annotation and perform resize
		ellipse.selectEllipse(1, 1);
		ellipse.compareElementImage(protocolName, ellipse.getViewbox(1),"Verify only one Ellipse is getting selected","TC1881_CheckPoint3");

	}

	@Test(groups ={"Chrome","IE11","US986","positive","BVT"})
	public void test22_US986_TC4199_TC4206_verifySelectionForEllipse() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that ellipse annotation should not get highlighted when user perform right click on ellipse annotation"+
		"<br> Verify that A/R should get display when user perform left click on ellipse annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		 ellipse=new EllipseAnnotation(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify drawn annotaion is current active GSPS" );
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -90, 140, -100,-180);
		ellipse.closingConflictMsg();
		
		ellipse.selectEllipseWithClick(1, 1);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify ellipse annotation is  active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		ellipse.performMouseRightClick(ellipse.getAllEllipses(1).get(0));
		ellipse.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify ellipse annotation is not active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify drawn annotaion is active GSPS after editing annotation" );
		ellipse.moveSelectedEllipse(1, -30, -40);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify ellipse annotation is active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify A/R when user perform left click on drawn annotation ");
		ellipse.selectEllipseWithClick(1, 1);
		ellipse.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on ellipse annotation", "Verified");
	}
	
	//DE987:[Hardening] Ellipse size is getting changed when user clicks inside ellipse
	@Test(groups ={"Chrome","IE11","Edge","DE987"})
	public void test23_DE987_TC4070_TC4071_verifyEllipseSizeWhenClickInsideAndOnCircumference() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify position of Ellipse annotation remains same on layout change <br>"+
		" Verify that Ellipse size should not get changed when user clicks on circumference of ellipse");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_ELLIPSE_PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 20, 20,250,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		ellipse.assertTrue(ellipse.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1) ,"Verify a single Ellipse is drawn on Viewbox", "Verified");

		//Variable to store position Ellipse before clicking inside ellipse
		String  beforeCX = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RX);   
		String  beforeCY = ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RY);	

		//click inside ellipse and verify ellipse annotation state
		ellipse.mouseHoverWithClick("Presence", ellipse.getEllipses(1).get(0), 20, 30);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1) ,"Verify ellipse annotation is accepted GSPS on viewer after click inside ellipse", "Verified");
		
		//Verify ellipse not resize when click inside the ellipse
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify ellipse not resize when click inside the ellipse annotation");
		ellipse.assertEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RX), "Verify RX Co-ordinate of Ellipse", "RX co-ordinate of ellipse are same when click inside ellipse");
		ellipse.assertEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RY), "Verify RY Co-ordinate of Ellipse", "RY Co-ordinate of Ellipse are same when click inside ellipse");
	
		//click on circumference of ellipse and verify ellipse annotation state
		ellipse.selectEllipseWithClick(1,1);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1) ,"Verify ellipse annotation is current active accepted GSPS on viewer when click on circumference", "Verified");
	
		//Verify ellipse not resize when click on circumference of circle
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify ellipse not resize when click on circumference of ellipse");
		ellipse.assertEquals(beforeCX,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RX), "Verify RX Co-ordinate of Ellipse", "RX co-ordinate of ellipse are same when click on circumference of ellipse");
		ellipse.assertEquals(beforeCY,ellipse.getEllipses(1).get(0).getAttribute(NSGenericConstants.RY), "Verify RY Co-ordinate of Ellipse", "RY co-ordinate of ellipse are same when click on circumference of ellipse");
		
	}

	

}



