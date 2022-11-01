package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Safety.NS_F169_ImageAnnotationTools-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CircleAnnotationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ViewBoxToolPanel presetMenu;
	private ExtentTest extentTest;
	private CircleAnnotation circle;
	private ContentSelector cs;
	private OutputPanel panel;

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
	String GSPS_CIRCLE_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	private String firstSeriesDescriptionGSPSCircle = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath"));

	String filePath1=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);

	String filePath2=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	private HelperClass helper;
	private ViewerLayout layout;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	//	TC1707	Image annotation: circle
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test01_US522_TC1707_verifyCircleAnnotation() throws InterruptedException, Exception 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: circle");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Two GSPS circle annotations should be visible on the DICOM data" );
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
		
		circle = new CircleAnnotation(driver);
		viewerPage.waitForElementVisibility(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "validate circle 1 is visible on viewer", "circle 1 is displayed in viewbox-1");
		viewerPage.waitForElementVisibility(circle.getAllCircles(1).get(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)), "validate circle 2 is visible on viewer", "circle 2 is displayed in viewbox-1");

		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verifying circular annotations are intact","viewbox1_Circle");


	}

	//	TC1708	Image annotation: circle- Layout Change
	@Test(groups ={"Chrome","IE11","Edge","US522","BVT"})
	public void test02_US522_TC1708_verifyCircleAnnotationOnLayoutChange() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: circle- Layout Change");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Data is loaded into view boxes 2 and 3. The GSPS annotation circles are located in the same region in both view boxes 2 and 3. " );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
	

		circle = new CircleAnnotation(driver);
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs = new ContentSelector(driver);
		viewerPage.waitForElementVisibility(viewerPage.getViewbox(1));

		cs.selectResultFromSeriesTab(2, cs.getAllResults().get(0));
		viewerPage.closingConflictMsg();

		viewerPage.waitForElementVisibility(circle.getAllCircles(2).get(0));
		viewerPage.assertTrue(circle.getAllCircles(2).get(0).isDisplayed(), "validate circle 1 is visible in viewbox 2", "circle 1 is displayed in viewbox-2");
		viewerPage.waitForElementVisibility(circle.getAllCircles(2).get(1));
		viewerPage.assertTrue(circle.getAllCircles(2).get(1).isDisplayed(), "validate circle 2 is visible in viewbox 2", "circle 2 is displayed in viewbox-2");

		// add image comparison for viewbox 2, not sure if it can be added 
		// (can't be done as radial menu keeps shifting form 1 viewbox to other )

		viewerPage.waitForElementVisibility(viewerPage.getViewbox(2));
		cs.selectSeriesFromSeriesTab(3, firstSeriesDescriptionGSPSCircle);
		viewerPage.closingConflictMsg();

		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 1: Verifying that GSPS annotation Circles are located in the same region in both view boxes 2 and 3", "Circle_layoutChange_Viewbox1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 2: Verifying that GSPS annotation Circles are located in the same region in both view boxes 2 and 3", "Circle_layoutChange_Viewbox2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(3), "Checkpoint 3: Verifying that GSPS annotation Circles are located in the same region in both view boxes 2 and 3", "Circle_layoutChange_Viewbox3");

	}

	//TC1710	Image annotation: circle- Annotation Level Change
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test04_US522_TC1710_verifyImageAnnotationCircleOnAnnotationLevelChange() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Circle- Annotation Level Change");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
	
		circle = new CircleAnnotation(driver);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Selecting minimum text overlay" );
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.waitForMinimumOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify that the GSPS annotation Circles remains consistent after applying minimum text overlay");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 1: Verifying that the GSPS annotation Circles remains consistent after applying minimum text overlay", "Circle_MinimumOverlay");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Selecting Full text overlay" );
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify that the GSPS annotation Circles remains consistent after applying full text overlay");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 2: Verifying that the GSPS annotation Circles remains consistent after applying full text overlay", "Circle_FullOverlay");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Selecting Full text overlay" );
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		overlays.waitForDefaultOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify that the GSPS annotation Circles remains consistent after applying default text overlay");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 3: Verifying that the GSPS annotation Circles remains consistent after applying default text overlay", "Circle_DefaultOverlay");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1711	Image annotation: circle- With Pan
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test05_US522_TC1711_verifyImageAnnotationCircleWithPan() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Circle- With Pan");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
	
		circle = new CircleAnnotation(driver);
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 300, 100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Circles remains consistent after applying PAN");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 1: Verifying that the GSPS annotation Circles remains consistent after PAN", "Circle_Pan");

		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1712	Image annotation: circle- With Zoom
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test06_US522_TC1712_verifyImageAnnotationCircleWithZoom() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: Circle- With Zoom");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
	
		circle = new CircleAnnotation(driver);
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 0, 10);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Circles remains consistent after applying Zoom");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 1: Verifying that the GSPS annotation Circles remains consistent after Zoom", "Circle_Zoom");

		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1713	Image annotation: circle- Rotation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test07_US522_TC1713_verifyImageAnnotationCircleRotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Image annotation: Circle- Rotation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
	
		circle = new CircleAnnotation(driver);
		ViewerOrientation orin = new ViewerOrientation(driver);
		orin.flipSeries(orin.getTopOrientationMarker(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the GSPS annotation Circles remains consistent while image is rotated");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 1: Verifying that the GSPS annotation Circles remains consistent while image is rotated", "Circle_FlipImage");

		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");
	}

	//TC1768	Image annotation: circle- Accuracy Test
	//Limitation : Not work in firefox because of DE456
	//	TC1: Correctness of GSPS circle annotation (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US522"})
	public void test08_US522_TC1768_verifyImageAnnotationCircleRotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image annotation: ellipse- Accuracy Test");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_CIRCLE_patientName,  1, 1);

		circle = new CircleAnnotation(driver);

		String CX1=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.CX);
		String CY1=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.CY);
		String RX1=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.R);
		String STROKE_WIDTH1=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH);
		String FILL_OPACITY1=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.FILL_OPACITY);

		String CX2=circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.CX);
		String CY2=circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.CY);
		String RX2=circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.R);
		String STROKE_WIDTH2=circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH);
		String FILL_OPACITY2=circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.FILL_OPACITY);

		helper.browserBackAndReloadViewer(GSPS_CIRCLE_patientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[1/1]", "Verify the presence of Image Annotation :Circle");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint TC1[1]: Checkpoint 1: Verifying the presence of Image Annotation :Circle", "viewbox1_Circle");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first ellipse presence","first ellipse is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(1)),"verifying Second ellipse presence","Second ellipse is present");

		//Accuracy test
		viewerPage.assertEquals(circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.CX),CX1,"verifying cx","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.CY),CY1,"verifying cy","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.R),RX1,"verifying radius","verified");

		viewerPage.assertEquals(circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),STROKE_WIDTH1,"verifying strokewidth","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.FILL_OPACITY),FILL_OPACITY1,"verifying fill-opacity","verified");
		viewerPage.assertEquals(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"verifying stroke","verified");


		//Accuracy test
		viewerPage.assertEquals(circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.CX),CX2,"verifying cx","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.CY),CY2,"verifying cy","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.R),RX2,"verifying radius","verified");

		viewerPage.assertEquals(circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),STROKE_WIDTH2,"verifying strokewidth","verified");
		viewerPage.assertEquals(circle.getAllCircles(1).get(1).getAttribute(NSGenericConstants.FILL_OPACITY),FILL_OPACITY2,"verifying fill-opacity","verified");
		viewerPage.assertEquals(circle.getCircle(1,2).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"verifying stroke","verified");
	}

	//TC1825:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","US2325","Positive","F1090","E2E"})
	public void test09_US585_TC1825_TC1826_US2325_TC9777_verifyCircleAnnotationIcon() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Circle Annotation Icon on Context Menu.<br/>Verify on clicking Circle Annotation, icon should be enable."
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
	
		circle = new CircleAnnotation(driver);

		//Perform a right click and validate ellipse icon on Radial Menu.
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that Circle Annotation Icon is present on outer arch of radial bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.circleIcon),"Verify that Circle Annotation Icon is present on outer arch of radial bar","Circle Annotation Icon is present on outer arch of radial bar");
		
		circle.selectCircleFromQuickToolbar(1);
		circle.assertTrue(circle.checkCurrentSelectedIcon(ViewerPageConstants.CIRCLE), "Verifying Circle icon is selected", "Point circle is selected in toolbar");
		
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.CIRCLE), "Verifying Circle icon is selected", "Point circle is selected in toolbar");
	
		
	}

	//TC1827:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test10_US585_TC1827_TC1828_TC1832_verifyDrawingOfCricleAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to draw Circle annotation on DICOM.<br/>Verify user is unable to draw Circle annotation on PDF.<br/>Verify that annotaion tool keep selected even after annoation drawn in viewbox.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbio_PatientName, 1);
	
		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 :"+circle.getAllCircles(1).size());

		//Draw a Circle on PDF rendered on View box 4
		circle.drawCircle(2, 0, 0, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that user is unable to draw a circle annotation on PDF");
		viewerPage.assertEquals(circle.getAllCircles(2).size(),0, "Verify Circle is not drawn on PDF", "No of Circle on Viewbox4  :"+circle.getAllCircles(2).size());

		viewerPage.click(viewerPage.getViewPort(1));
		//Verify circle annotation is selected even after annotation is drawn
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify that Circle annotation icon is selected");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.CIRCLE),"Verifying Circle annotation icon is selected", "Circle annotation icon is selected");

	} 

	//TC1829:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","DE902","DE1329","Positive","Sanity","BVT"})
	public void test11_US585_TC1829_DE902_TC3502_DE1329_TC5646_verifyUserCanMoveCircleAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to move Circle annotation"
				+ "<br> Verify that circle position should get save after moving"
				+"<br> Verify that moving functionality of circle annotation should not get break");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName,  1, 1);
		circle = new CircleAnnotation(driver);
		layout = new ViewerLayout(driver);
		
		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,-150,-100);	

		circle.closingConflictMsg();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1: "+circle.getAllCircles(1).size());

		//Variable to store position on Center of Circle before moving
		String  beforeCX = circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY);	

		//Move circle to the Left
		circle.moveSelectedCircle(1,70,60);

		circle.mouseHover(circle.getViewPort(2));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify that user is able to move circle from one point to another");
		viewerPage.assertNotEquals(beforeCX,circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of circle change om moving circle", "The X Co-ordinate of Center of Circle changes from "+beforeCX+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		viewerPage.assertNotEquals(beforeCY,circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of circle change om moving circle", "The Y Co-ordinate of Center of Circle changes from "+beforeCY+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));	

		Float cx = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		Float cy = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Reloading the viewerpage");
		helper.browserBackAndReloadViewer(patientName,  1, 1);

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(1);

		Float afterReloadCx = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		Float afterReloadCy = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying that on reloading the state is not changed");
		viewerPage.assertEquals(cx.intValue(),afterReloadCx.intValue(), "Verify X Co-ordinate of center of circle", "State of circle is saved");
		viewerPage.assertEquals(cy.intValue(),afterReloadCy.intValue(), "Verify Y Co-ordinate of center of circle", "State of circle is saved");	

		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Moving the circle in same session");
		//		//			circle.selectCircle(1, 1);
		//		circle.moveSelectedCircle(1,70,70);
		//
		//		cx = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		//		cy = Float.parseFloat(circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));
		//
		//		viewerPage.assertNotEquals(cx.intValue(),afterReloadCx.intValue(), "Verify X Co-ordinate of center of circle change om moving circle", "The X Co-ordinate of Center of Circle changes from "+beforeCX+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		//		viewerPage.assertNotEquals(cy.intValue(),afterReloadCy.intValue(), "Verify Y Co-ordinate of center of circle change om moving circle", "The Y Co-ordinate of Center of Circle changes from "+beforeCY+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));	
	}

	//TC1830:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585","US2329","Positive","F1090","E2E"})
	public void test12_US585_TC1830_US2329_TC10165_verifyDeletionOfCricleAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to delete Circle annotation. <br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
			
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on View Box1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//Draw a Circle on VIewbox1
		circle.drawCircle(1, 50, 50, -30,-30);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to draw a multiple circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify multiple circle annotation is drawn on Viewbox1", "Multiple Circle annotation is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2, "Verify no of circle annotation on View box1", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//select the first circle and press delete button
		circle.selectCircle(1, 1);
		circle.deleteSelectedCircle();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that selected circle is deleted");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify no of circle annotation on View box1", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//select the second circle and press delete button
		circle.selectCircle(1, 1);
		circle.deleteSelectedCircle();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify that selected circle is deleted");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),0, "Verify no of circle annotation on View box1", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

	} 

	//TC1899:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test13_US585_TC1899_verifyCircleAnnotationAreNotPannedOnRightClick() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify Cricle Annotation are not getting panned on right click");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		circle = new CircleAnnotation(driver);

		//Draw a Circle on View Box1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//Draw a Circle on Viewbox1
		circle.drawCircle(1, 50, 50, 120,120);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that user is able to draw a multiple circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify multiple circle annotation is drawn on Viewbox1", "Multiple Circle annotation is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2, "Verify no of circle annotation on View box1", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//Perform a Right Click on Circle 1
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(circle.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		//hover mouse to some other location and verify image is not getting panned
		viewerPage.mouseHover(circle.getAllCircles(1).get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify DICOM images are not getting panned");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify DICOM images are not getting panned","TC1899_CheckPoint4");

	} 

	//TC1833:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test14_US585_TC1833_verifyOnZoomCircleAnnotationChangesPropotionally() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of circle annotation will be change proportionally with image");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 100,100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 :"+circle.getAllCircles(1).size());

		// get Zoom level for Canvas 0 before Zoom
		presetMenu=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = presetMenu.getZoomValue(1);

		//Variable to store position on Center of Circle before moving


		//Select a Zoom from radial bar and perform Zoom down
		viewerPage.selectZoomFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -50,-50);

		// get Zoom level for Canvas 0 before Zoom
		int finalZoomLevel1 = presetMenu.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerPage.assertTrue(finalZoomLevel1 > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the size of circle annotation will be change proportionally with image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify the size of circle annotation will be change proportionally with image on Zoom down","TC1833_CheckPoint3");

		//Perform Zoom Up
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 50,50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the size of circle annotation will be change proportionally with image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify the size of circle annotation will be change proportionally with image on Zoom down","TC1833_CheckPoint4");
	} 


	//TC1835:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test15_US585_TC1835_verifyOnPanCircleAnnotationMovesAlongWithImage() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on pan the circle annotation move along with image");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 80,80);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 :"+circle.getAllCircles(1).size());

		//variable to store radius of circle annotation
		String beforeRadius=circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.R);

		//Perform Pan on Viewbox1
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 10, 10, 50,50);

		//verify size of radius remain same on panning 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify on pan the circle annotation move along with image");
		viewerPage.assertEquals(beforeRadius,circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.R), "Verify size of radius remain after pan", "The Size of radius remain same ans is :"+circle.getAllCircles(1).get(0).getAttribute(NSGenericConstants.R));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify on pan the circle annotation move along with image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify on pan the circle annotation move along with image","TC1835_CheckPoint3");

	} 

	//TC1836:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test16_US585_TC1836_verifyUserCannotMoveCircleAnnotationToOtherViewbox() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is unable to move circle annotation to other viewbox");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		circle = new CircleAnnotation(driver);

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,80,80);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1: "+circle.getAllCircles(1).size());

		//Variable to store position on Center of Circle before moving
		String  beforeCX = circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CY);	

		//Move circle to the Left
		circle.moveSelectedCircle(1,70,70);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to move circle from one point to another with Viewbox");
		viewerPage.assertNotEquals(beforeCX,circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of circle change om moving circle", "The X Co-ordinate of Center of Circle changes from "+beforeCX+" to "+circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CX));
		viewerPage.assertNotEquals(beforeCY,circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of circle change om moving circle", "The Y Co-ordinate of Center of Circle changes from "+beforeCY+" to "+circle.getCircle(1,1).get(2).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that user is unable to move circle annotation in other Viewbox1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify that user is unable to move circle annotation in other Viewbox1","TC1836_CheckPoint3");

	}


	//TC1892:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test20_US585_TC1892_verifyPositionOfCircleAnnotationOnLayoutChange() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify position of Circle annotation remains same on layout change");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		layout = new ViewerLayout(driver);
		circle = new CircleAnnotation(driver);
		
		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,150,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1: "+circle.getAllCircles(1).size());

		//Variable to store position on Center of Circle before moving
		String  beforeCX = circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.CX);
		String  beforeCY = circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.CY);	

		//Change layout to 1X1
		layout.selectLayout(layout.oneByOneLayoutIcon);

		//Verify Center position change after layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to move circle from one point to another with Viewbox");
		viewerPage.assertNotEquals(beforeCX,circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of circle change om moving circle", "The X Co-ordinate of Center of Circle changes from "+beforeCX+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		viewerPage.assertNotEquals(beforeCY,circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of circle change om moving circle", "The Y Co-ordinate of Center of Circle changes from "+beforeCY+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify position of Circle annotation remains same on layout change");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify position of Circle annotation remains same on layout change","TC1892_CheckPoint3");

	}

	//TC1833:Image annotation - circle/ellipse edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test17_US585_TC1883_verifyUserCannotPerformDicomOperationInsideCircleAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of circle annotation will be change proportionally with image");
		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 100,100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 :"+circle.getAllCircles(1).size());

		// get Zoom level for Canvas 0 before Zoom
		int intialZoomLevel1 = presetMenu.getZoomValue(1);


		//Select a Zoom from radial bar
		viewerPage.selectZoomFromQuickToolbar(1);
		//Perform mouse drag inside circumference of a Circle
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 60, 60, 90,90);

		// get Zoom level for Canvas 0 before Zoom
		int finalZoomLevel1 = presetMenu.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verify user is not able to perform zoom inside a circle annotation");
		viewerPage.assertTrue(finalZoomLevel1<intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);

	} 

	@Test(groups ={"Chrome","IE11","Edge","US585","US950","Sanity","BVT"})
	public void test18_US585_TC1844_TC1845_TC1846_TC1847_US950_TC4300_TC4303_verifyAcceptRejectRadialMenuForGSPSDataWithCircleAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Accept/Reject radial menu should be displayed when GSPS eclipse data displayed in viewers page"
				+ "<br> Verify that accept/reject context menu should be displayed when GSPS circle/eclipse displayed in viewers page"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_CIRCLE_patientName, 1);
		
		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify the presence of Image Annotation :Circle");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying first Circle presence","First Circle is present");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)),"verifying second Circle presence","Second Circle is present");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(circle.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify one Circle GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1,2), "Verify first Circle annotation in slice is current active GSPS object and accepted by default", "The first Circle annotation is current active GSPS object and accepted by default");

		//Click on the Reject button for a Current Active GSPS object
		circle.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify on clicking Reject button current GSPS circle annotation is rejected");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1), "Verify current Circle annotation is rejected", "The current Circle annotation is rejected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify on clicking Reject button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1,2), "GSPS is automatically shifted to the next GSPS result on the slice", "The current Circle annotation is rejected");
		//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint5");

		//Click on Next button for current active GSPS object
		circle.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify on clicking next button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1,2), "GSPS is automatically shifted to the next GSPS result on the slice", "GSPS is automatically shifted to the next GSPS result on the slice");
		//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint6");

		//Click on the Accept button for a Current Active GSPS object

		circle.selectAcceptfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify on clicking Accept button current GSPS circle annotation is accepted");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1,2), "Verify current Circle annotation is accepted", "The current Circle annotation is accepted");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify on clicking accept button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1), "GSPS is automatically shifted to the next GSPS result on the slice", "GSPS is automatically shifted to the next GSPS result on the slice");
		//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Accept/Reject Radial bar appear above current active GSPS circle annotation","TC1844_CheckPoint8");
	}

	//TC1881:Image annotation - Circle/Circle edition and manipulation
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US585"})
	public void test19_US585_TC1881_verifyOverlappingCircleAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when eclipse annotation overlaps each other than only one should get selected.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		circle = new CircleAnnotation(driver);

		//Draw a Circle on View Box1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 10,90,100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a Circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single Circle is drawn on Viewbox", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//Draw a Circle on Viewbox1
		circle.drawCircle(1, 0, 0,100,120);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to draw a multiple Circle annotation on DICOM");	
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a Circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2, "Verify a single Circle is drawn on Viewbox", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());				

		//select the first Circle annotation and perform resize
		circle.selectCircle(1,2);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify only one Circle is getting selected","TC1881_CheckPoint3");

	}


	@Test(groups ={"Chrome","IE11","US986","positive","US950"})
	public void test20_US986_TC4196_TC4205_TC4212_TC4294_US950_TC4300_TC4303_verifySelectionForCircle() throws InterruptedException, IOException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that circle annotation should not highlighted when user perform right click on Circle annotation"+"<br> Verify that A/R should get display when user perform left click on circle annotation"
				+ "<br> Verify that radial menu should not get open when user performs right click on any annotation"+
				"<br> 	Verify that annotation should get highlighted on finishing drawing or editing of any annotation"
				+ "<br> Verify that annotation should get deselect when user perform any action (PAN, ZOOM, WL)"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify drawn annotaion is current active GSPS" );
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,-100,-100);
		circle.selectCircleWithClick(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation gets highlighted when perform left click on annotation", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify drawn annotaion is deselect after perform scroll" );
		int defaultSliceNumber=viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.mouseWheelScrollInViewer(1, NSGenericConstants.SCROLL_UP, 3);
		viewerPage.scrollToImage(1, defaultSliceNumber);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation gets deselect when perform scroll", "Verified");

		circle.selectCircleWithClick(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation gets highlighted when perform left click on annotation", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify drawn annotaion is not active GSPS after playing cine using keyboard");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.scrollToImage(defaultSliceNumber, 1);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is not active GSPS object", "Verified");

		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		//		viewerPage.performMouseRightClick(circle.getAllCircles(1).get(0));
		//		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is not active GSPS object", "Verified");

		circle.moveSelectedCircle(1, 70, 70);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is current active GSPS object after editing annotation", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify radial menu not open when user perform right click on annotation");
		viewerPage.performMouseRightClickOnGSPS(circle.getAllCircles(1).get(0));
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify radial menu not open", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify radial menu  open when user perform right click on anywhere on viewbox");
		viewerPage.click(viewerPage.getViewPort(2));
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify radial menu open", "Verified");

	}

	@Test(groups ={"Chrome","IE11","US986","positive"})
	public void test21_US986_TC4271_TC4272_TC4273_TC4276_TC4277_TC4278_verifyAnnotationDeselectFunctionality() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that annotation should get deselect when user perform left click any where in viewbox except annotation"+ 
				"<br> Verify that annotation should get deselect when user perform mouse hover in other viewbox"
				+"<br> Verify that annotation should get deselect when user perform left click on text overlays"
				+"<br> Verify that annotation should get deselect when user open content selector"
				+"<br> Verify that annotation should get deselect when user clicks on orientation markers (to rotate or flip)"
				+"<br> 	Verify that annotation should get deselect when user clicks NS logo");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		circle = new CircleAnnotation(driver);
		cs=new ContentSelector(driver);
		ViewerOrientation orin = new ViewerOrientation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,-100,-100);
		for(int i=2;i<5;i++)
			circle.closeWaterMarkIcon(i);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify annotation select when perform left click on annotation.");
		circle.selectCircleWithClick(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is current active GSPS object", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify annotation deselect when perform left click any where in viewbox.");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.waitForTimePeriod(5000);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify annotation deselect when perform mouse hover on other viewbox.");
		circle.selectCircleWithClick(1, 1);
		viewerPage.mouseHover(viewerPage.getViewPort(3));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when perform mouse hover on other viewbox", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify annotation deselect when perform left click on text overlays ( W, C, Z, Slice input)");
		circle.selectCircleWithClick(1, 1);
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.waitForTimePeriod(5000);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when perform left click on text overlays (W)", "Verified");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when perform left click on text overlays (Z)", "Verified");
		viewerPage.click(viewerPage.getSliceInfo(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when perform left click on text overlays (slice)", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify annotation deselect when content selector open");
		circle.selectCircleWithClick(1, 1);
		cs.openAndCloseSeriesTab(true);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when series is rotated", "Verified");
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify annotation deselect when rotate the series");
		circle.selectCircleWithClick(1, 1);
		orin.flipSeries(orin.getTopOrientationMarker(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when rotate the series", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify annotation deselect when click on NS logo");
		circle.selectCircleWithClick(1, 1);
		viewerPage.click(viewerPage.EurekaLogo);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation is deselect when click on NS logo", "Verified");
	}

	@Test(groups ={"Chrome","IE11","positive","US585","DE1329"})
	public void test22_US585_TC1831_DE1329_TC5646_verifyResizeOfCircle() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotation can edit(resize) circle/eclipse <BR>"+
				"Verify that resizing functionality of circle annotation should not get break");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,-100,-100);
		circle.closingConflictMsg();
		String radius = circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R);
		circle.mouseHover(circle.getViewPort(1));
		circle.resizeCircle(-50, -50);
		circle.assertNotEquals(circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R), radius, "Checkpoint[1/2]", "Reducing the size");

		radius = circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R);
		circle.resizeCircle(100, 0);
		circle.assertNotEquals(circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R), radius, "Checkpoint[2/2]", "Increasing the size");


	}

	@Test(groups ={"Chrome","IE11","positive","DE1333"})
	public void test23_DE1333_TC5609_verifyFindingSliderBarOnLeftClick() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the thumbnail, Slider bar on pressing the mouse left  button on view box after selecting the Circle annotation.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		panel= new OutputPanel(driver);
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0,-100,-100);

		panel.enableFiltersInOutputPanel(true, true, true);

		int thumbnailCount=panel.thumbnailList.size();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that no another finding mark is getting added on slider baron clicking");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertEquals(circle.getAllCircles(1).size(), circle.getFindingMarkersOnSlider(1).size(),"Verifying the circle count should be same on view box and on Finding sliding bar.", "Verified that finding count is same");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that no thumbnail is getting added on clicking");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),thumbnailCount ,"Verifying the thumbnail count in output panle.", "Thumbnail count isequal to the number of findign count on view box");

	}

	@Test(groups ={"Edge","Chrome","IE11","Negative","DE1831"})
	public void test24_DE1831_TC7404_verifyFindingOnLeftClick() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that annotations are cleaned up from viewer as well as finding list if they are empty.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbio_PatientName, 1);

		circle = new CircleAnnotation(driver);

		int findingCount = circle.getFindingsCountFromFindingTable();

		circle.selectCircleFromQuickToolbar(1);
		circle.clickAt(-100,-100);

		circle.assertEquals(circle.getFindingsCountFromFindingTable(),findingCount, "Checkpoint[1/2]", "Verified that no findings are added");

		circle.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint[2/2]", "Verified No circle is created on click");


	}


}



