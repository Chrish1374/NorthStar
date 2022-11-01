package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
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
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MultipleGSPSResultTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	
	private ExtentTest extentTest;
	private ContentSelector cs;
	
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private OutputPanel panel;
	private SimpleLine line;
	private PolyLineAnnotation poly;
	

	
	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String filePathGSPSCircle = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
	String GSPS_PatientName_Circle = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathGSPSCircle); 
	
	String filePath2 = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
	String dxMultipleResultPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	
	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	String secondSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));	
	String firstResultDescriptionAH4 = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));

	String gspsFilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath); 
	
	String icoMetrixFilePath = Configurations.TEST_PROPERTIES.get("Icometrix");
	String icoMetrixPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, icoMetrixFilePath); 
	String icoMetrixNumberOfImages = DataReader.getStudyDetails(PatientXMLConstants.STUDY01_TEXTOVERLAY,PatientXMLConstants.NUMBER_OF_IMAGES, icoMetrixFilePath); 
	

	String firstSeriesDescriptionMultiSeries ="",secondSeriesDescriptionMultiSeries ="";
	String firstGSPSResult = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);
	String secondGSPSResult = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath2);		
	String source = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
	private HelperClass helper;		

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}
	
	@Test(groups = {"Chrome","US688"})
	public void test01_US688_TC2610_TC2730_TC2731_verifyGSPSLoadedonViewboxForSeriesAlreadyHavingGSPS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS series is loaded into viewbox containing series already having GSPS annotation when GSPS result is selected through content selector"
				+ "</br>Verify that annotation and Result applied should not displayed on original series"
				+ "</br>Verify that GSPS annotation, Accept/Reject context menu, Result applied should get displayed when GSPS series selected from content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(PatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		
		//validate Result applied toggle is not present on text overlay
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify Result Applied toggle is not present on viewbox");
        viewerpage.assertFalse(viewerpage.verifyResultAppliedTextPresence(1), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox1", "'Result Applied' is not present on viewbox1");
        viewerpage.assertFalse(viewerpage.verifyResultAppliedTextPresence(2), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox2", "'Result Applied' is not present on viewbox2");
		
        viewerpage.scrollToImage(2, 4);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2, -100, -50);

		//navigate to seventh slice on viewbox2 and draw a ellipse annotation
		viewerpage.scrollToImage(2, 7);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		
		ellipse.closingConflictMsg();
		
        //navigate to third slice on viewbox1 and draw a linear measurement
		viewerpage.scrollToImage(1, 3);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		
		//navigate to fifth slice on viewbox1 and draw a circle annotation
		viewerpage.scrollToImage(1, 5);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 100, 100);

		//navigate to fourth slice on viewbox2 and draw a point annotation
		
		cs = new ContentSelector(driver);
		//select original series from content selector and validate GSPS object is not present on view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]", "Verify GSPS object are not present on viewbox on selecting series from content selector");
		cs.selectSeriesFromSeriesTab(1, firstSeriesDescriptionAH4);
		viewerpage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		
		//navigate back to study page and reload viewer
		helper.browserBackAndReloadViewer(PatientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ellipse.closingConflictMsg();
		
		//verify series open with first slice containing GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]", "Verify viewer open with first slice containing GSPS object");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),3,"Verifying the slice position of Viewbox1","Viewbox1 open on slice no:3 containing linear measurement");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),3,"Verifying the slice position of Viewbox2","Viewbox2 open on slice no:3 on sync with viewbox1");
		
		//select GSPS drawn on Viewbox2 in viewbox1 using content selector and verify series is rendered on viewbox1.
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]", "Verify second series open with same content on viewbox1 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),secondSeriesDescriptionAH4,"Verify second series is rendered on viewbox1","Verified second series is rendered on viewbox1");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),3,"Verifying the slice position of Viewbox1","Viewbox1 open on slice no:3 containing linear measurement");
		
		//select GSPS drawn on Viewbox1 in viewbox2 using content selector and verify series is rendered on viewbox1.
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]", "Verify first series with linear measurement open on viewbox2 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2),firstSeriesDescriptionAH4,"Verify first series is rendered on viewbox2","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),3,"Verifying the slice position of Viewbox2","Second series rendered Viewbox2 open on slice no:3 containing linear measurement");
		viewerpage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Verifying linear measurement is present on viewbox 2","A linear measurement is present on viewbox 2");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedTextPresence(2), "Verify the presence of the label 'Result Applied' on the text overlay in Viewbox2", "'Result Applied' is not present on viewbox2");
	}
		
	@Test(groups = {"Chrome","US688"})
	public void test02_US688_TC2611_verifyGSPSLoadedonViewboxForSeriesAlreadyHavingGSPS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS series is loaded into viewbox containing series having same GSPS annotation when GSPS result is selected through content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,1);

		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		//navigate to third slice on viewbox1 and draw a linear measurement
		viewerpage.scrollToImage(1, 3);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		
		//navigate to fifth slice on viewbox1 and draw a circle annotation
		viewerpage.scrollToImage(1, 5);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 100, 100);

		ellipse.closingConflictMsg();
		//navigate to fourth slice on viewbox2 and draw a point annotation
		viewerpage.scrollToImage(2, 4);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2, -100, -50);

		//navigate to seventh slice on viewbox2 and draw a ellipse annotation
		viewerpage.scrollToImage(2, 7);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		ellipse.closingConflictMsg();
		
		cs = new ContentSelector(driver);
		
		//select GSPS  drawn on Viewbox1 in viewbox1 using content selector and verify series along with GSPS is rendered on view box.
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify first series open with same content on viewbox1 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),firstSeriesDescriptionAH4,"Verify first series is rendered on viewbox1","Verified first series is rendered on viewbox1");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),7,"Verifying the slice position of Viewbox1","The Current slice position in viewbox 1 is 5");
		
		//select GSPS drawn on Viewbox2 in viewbox2 using content selector and verify series is rendered on view box.
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");
		ellipse.closingConflictMsg();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify second series open with same content on viewbox2 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2),secondSeriesDescriptionAH4,"Verify first series is rendered on viewbox2","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),7,"Verifying the slice position of Viewbox2","The Current slice position in viewbox 2 is 7");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify ellipse annotation is present on viewbox2");
		viewerpage.assertEquals(ellipse.getEllipses(2).size(),1,"Verifying ellipse annotation is present on viewbox 2","The ellipse annotation is present on viewbox 2");
	    
	}
	
	@Test(groups = {"Chrome","US688"})
	public void test03_US688_TC2612_verifyGSPSSeriesLoadedonEmptyViewboxSelectingFromContentSelector() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS series is loaded into empty viewbox when GSPS result is selected through content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,1);

		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
				
		//navigate to third slice on viewbox1 and draw a linear measurement
		viewerpage.scrollToImage(1, 3);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		
		//navigate to fifth slice on viewbox1 and draw a circle annotation
		viewerpage.scrollToImage(1, 5);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -50, 100, 100);
		ellipse.closingConflictMsg();

		//navigate to fourth slice on viewbox2 and draw a point annotation
		viewerpage.scrollToImage(2, 4);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2, -100, -50);

		//navigate to seventh slice on viewbox2 and draw a ellipse annotation
		viewerpage.scrollToImage(2, 7);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		ellipse.closingConflictMsg();
		//change layout to 3X3
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs = new ContentSelector(driver);
		ellipse.closingConflictMsg();
		
		//select GSPS  drawn on Viewbox1 in empty viewbox6 using content selector and verify series along with GSPS is rendered on viewbox1.
		cs.selectResultFromSeriesTab(6, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify first series open with same content on viewbox6 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(6),firstSeriesDescriptionAH4,"Verify first series is rendered on viewbox6","Verified first series is rendered on viewbox6");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),7,"Verifying the slice position of Viewbox6","The Current slice position in viewbox 1 is 7");
		ellipse.closingConflictMsg();
		
		//select GSPS drawn on Viewbox1 in empty viewbox7 using content selector and verify series is rendered on view box.
		cs.selectResultFromSeriesTab(7, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify second series open with same content on empty viewbox7 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(7),secondSeriesDescriptionAH4,"Verify second series is rendered on viewbox7","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(7),7,"Verifying the slice position of Viewbox7","The Current slice position in viewbox7 is 7");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify ellipse annotation is present on viewbox7");
		viewerpage.assertEquals(ellipse.getEllipses(7).size(),1,"Verifying ellipse annotation is present on viewbox 7","The ellipse annotation is present on viewbox 7");
	}
		
	@Test(groups = {"Chrome","US688"})
	public void test04_US688_TC2613_verifyMachineDrawnGSPSLoadedOnViewboxOnSelectingDifferentSeriesFromContentSelector() throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify machine drawn GSPS series is loaded into viewbox containing series already having GSPS annotation when GSPS result is selected through content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(gspsPatientName,1);

		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
			
		String machineName=cs.getMachineName().get(0);
		firstSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(1);
		secondSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(2);
		//draw a Circle measurement on view box 		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -50, 100, 100);
		circle.closingConflictMsg();
		//navigate to fourth slice on viewbox2 and draw a point annotation
		viewerpage.scrollToImage(2, 4);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2, 100, 50);
		point.closingConflictMsg();
		
		cs = new ContentSelector(driver);
		//select GSPS drawn on Viewbox2 in viewbox1 using content selector and verify series is rendered on viewbox1.
		cs.selectResultCloneFromSeriesTabForGivenResult(1, 1, machineName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify second series open with same content on viewbox1 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),secondSeriesDescriptionMultiSeries,"Verify second series is rendered on viewbox1","Verified second series is rendered on viewbox1");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),4,"Verifying the slice position of Viewbox1","Viewbox1 open on fourth slice containing point annotation");
		circle.closingConflictMsg();
		
		//select GSPS drawn on Viewbox1 in viewbox2 using content selector and verify series is rendered on viewbox1.
		cs.selectResultCloneFromSeriesTabForGivenResult(2,1,machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify first series with Circle annotation open on viewbox2 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2),firstSeriesDescriptionMultiSeries,"Verify first series is rendered on viewbox2","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),1,"Verifying the slice position of Viewbox2","First series rendered Viewbox2 open on first slice containing circle annotation");
		viewerpage.assertEquals(circle.getAllCircles(2).size(),1,"Verifying circle annotation is present on viewbox 2","A circle annotation is present on viewbox 2");
	}
	
	@Test(groups = {"Chrome","US688"})
	public void test05_US688_TC2616_verifyMachineDrawnGSPSLoadedOnViewboxOnSelectingSameSeriesFromContentSelector() throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify machine drawn GSPS series is loaded into viewbox containing series having same GSPS annotation when GSPS result is selected through content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(gspsPatientName,1);
		
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		String machineName=cs.getMachineName().get(0);
		firstSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(1);
		secondSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(2);
		
		//draw a Circle measurement on view box 		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 100, 100);
		circle.closingConflictMsg();
		//navigate to fourth slice on viewbox2 and draw a linear measurement annotation
		viewerpage.scrollToImage(2, 4);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);
		circle.closingConflictMsg();
		cs = new ContentSelector(driver);
		//select GSPS drawn on Viewbox2 in viewbox1 using content selector and verify series is rendered on viewbox1.
		cs.selectResultCloneFromSeriesTabForGivenResult(1, 2, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify first series open with same content on viewbox1 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),firstSeriesDescriptionMultiSeries,"Verify first series is rendered on viewbox1","Verified first series is rendered on viewbox1");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),1,"Verifying the slice position of Viewbox1","Viewbox1 open on first slice containing machine drawn point annotation");
		circle.closingConflictMsg();
		
		//select GSPS drawn on Viewbox1 in viewbox2 using content selector and verify series is rendered on viewbox1.
		cs.selectResultCloneFromSeriesTabForGivenResult(2,1,machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify second series with linear measurement open on viewbox2 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2),secondSeriesDescriptionMultiSeries,"Verify second series is rendered on viewbox2","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),4,"Verifying the slice position of Viewbox2","Second series rendered Viewbox2 open on fourth slice containing linear measurement");
		viewerpage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Verifying linear measurement annotation is present on viewbox 2","A linear measurement is present on viewbox 2");
	}
	
	@Test(groups = {"Chrome","US688"})
	public void test06_US688_TC2619_TC2685_verifyMachineDrawnGSPSLoadedOnEmptyViewboxOnSelectingSeriesFromContentSelector() throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify machine drawn GSPS series is loaded into empty viewbox when GSPS result is selected through content selector"
				+ "</br>Verify that source name should be displayed as series name when user clicks GSPS series from content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(gspsPatientName,1);

		
		firstSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(1);
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		secondSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(2);
		
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		String machineName=cs.getMachineName().get(0);
		//draw a Circle measurement on view box 	
		viewerpage.scrollToImage(1, 4);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -50, 100, 100);
		circle.closingConflictMsg();
		//navigate to fourth slice on viewbox2 and draw a linear measurement annotation
		viewerpage.scrollToImage(2, 5);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);	
		
		circle.closingConflictMsg();
		ViewerLayout layout = new ViewerLayout(driver);
		//change layout to 3X3
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		circle.closingConflictMsg();
		cs = new ContentSelector(driver);
		//select GSPS drawn on Viewbox1 on empty view box using content selector and verify series is rendered on viewbox.
		cs.selectResultCloneFromSeriesTabForGivenResult(5,2,machineName ,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify first series open with same content on viewbox3 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(5),firstSeriesDescriptionMultiSeries,"Verify first series is rendered on viewbox3","Verified first series is rendered on viewbox3");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(5),4,"Verifying the slice position of Viewbox3","Viewbox3 open on fourth slice containing machine drawn point annotation");
		circle.closingConflictMsg();
		//select GSPS drawn on Viewbox2 on empty View box using content selector and verify series is rendered on viewbox1.
		cs.selectResultCloneFromSeriesTabForGivenResult(6,1,machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify second series with linear measurement open on viewbox4 when selected from content selector");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(6),secondSeriesDescriptionMultiSeries,"Verify second series is rendered on viewbox4","Verified second series is rendered on viewbox2");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(6),3,"Verifying the slice position of Viewbox4","Second series rendered Viewbox4 open on fourth slice containing linear measurement");
		viewerpage.assertEquals(lineWithUnit.getAllLinearMeasurements(6).size(),0,"Verifying linear measurement annotation is present on viewbox 6","A linear measurement is present on viewbox 4");
	}

	@Test(groups = {"Chrome","US688"})
	public void test07_US688_TC2684_verifyLoadedSeriesAndCorrespondingGSPSAreHighlightedOnContentSelector() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that only loaded series in viewer should be checked/marked in content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(gspsPatientName);

		
		patientPage.clickOntheFirstStudy();
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();		
		cs = new ContentSelector(driver);

		firstSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(1);
		secondSeriesDescriptionMultiSeries= viewerpage.getSeriesDescriptionOverlayText(2);
		
		//change layout to 1X1
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.closingConflictMsg();
		
		//verify series loaded on any view box and its GSPS are highlighted on content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify only first series is highlighted content selector");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(cs.getAllSeries().get(0)), "Verify only first series is highlighted content selector", "Verified only first series is highlighted content selector");
		viewerpage.assertFalse(cs.verifyPresenceOfEyeIcon(cs.getAllSeries().get(1)), "Verify second series is not highlighted content selector", "Verified second series is not highlighted content selector");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify only first series GSPS is highlighted content selector");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(cs.getAllResults().get(1)), "Verify only first series GSPS result is highlighted content selector", "Verified only first series GSPS result is highlighted content selector");
		viewerpage.assertFalse(cs.verifyPresenceOfEyeIcon(cs.getAllResults().get(0)), "Verify second series GSPS result is not highlighted content selector", "Verified second series GSPS result is not highlighted content selector");
		

	}

	@Test(groups = {"Chrome","DE751"})
	public void test08_DE751_TC2871_TC2870_TC2869_TC2868_VerifyMultipleGSPSResultsUsingCS() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to select and load all result from Result tab in already filled viewbox using content selector"
				+ "<br> Verify that clicking any GSPS result desired result should be loaded"
				+ "<br> Verify that user should be able to select and load result in empty viewbox using content selector"
				+ "<br> Verify that user should be able select and load any result in any viewbox after changing the layout");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dxMultipleResultPatient+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(dxMultipleResultPatient,1);

		cs = new ContentSelector(driver);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "checkpoint[1/6]: Verifying the source", "test08_checkpoint1");	
		
		cs.selectResultFromSeriesTab(1, secondGSPSResult);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "checkpoint[2/6]: Verifying the second result", "test08_checkpoint2");
	
		cs.selectResultFromSeriesTab(1, firstGSPSResult);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "checkpoint[3/6]: Verifying the first result", "test08_checkpoint3");
		
		//change layout to 3X3
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.closingConflictMsg();
		
		cs.selectSeriesFromSeriesTab(2, source);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "checkpoint[4/6]: Verifying the source in viewbox 2 ", "test08_checkpoint4");	
		viewerpage.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(3, secondGSPSResult);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(3), "checkpoint[5/6]: Verifying the second result in viewbox 3", "test08_checkpoint5");
		viewerpage.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(4, firstGSPSResult);
//		viewerpage.closingBannerAndWaterMark();
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4), "checkpoint[6/6]: Verifying the first result in viewbox 4", "test08_checkpoint6");
		
		
		}
	
	//DE1390: [Hardening]-Viewer page is broken with linear measurement on resizing the browser when output panel is opened.
	@Test(groups = {"Chrome","Edge","DE1390","positive"})
	public void test09_DE1390_TC5735_VerifyViewerPageNotBrokenOnResizingTheBrowser() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  viewer page is  not broken and responding on resizing the browser.");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,1);

		
		panel=new OutputPanel(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		point=new PointAnnotation(driver);
		line=new SimpleLine(driver);
		poly=new PolyLineAnnotation(driver);
				
		String seriesName=panel.getSeriesDescriptionOverlayText(1);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1,50, -50, 40, -50);
		ellipse.closingConflictMsg();
		
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[1."+i+"/6]",ViewerPageConstants.ELLIPSE);
		panel.openAndCloseOutputPanel(false);
		
		panel.mouseHover(panel.getViewPort(1));
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,-10,10,250,10);
		line.closingConflictMsg();
		
		panel.mouseHover(panel.getViewPort(1));
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[2."+i+"/6]",ViewerPageConstants.LINE);
		panel.openAndCloseOutputPanel(false);
		
		panel.mouseHover(panel.getViewPort(1));
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1,abc);
		poly.closingConflictMsg();
		
		panel.mouseHover(panel.getViewPort(1));
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[3."+i+"/6]",ViewerPageConstants.POLYLINE_FINDING_NAME);
		panel.openAndCloseOutputPanel(false);
		
		panel.mouseHover(panel.getViewPort(1));
		circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 70, 70, 80, 80);
	    circle.closingConflictMsg();
	    
	    panel.mouseHover(panel.getViewPort(1));
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[4."+i+"/6]",ViewerPageConstants.CIRCLE);
		panel.openAndCloseOutputPanel(false);
		
		panel.mouseHover(panel.getViewPort(1));
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 20, 20);
		point.closingConflictMsg();
		
		panel.mouseHover(panel.getViewPort(1));
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[5."+i+"/6]",ViewerPageConstants.POINT);
		panel.openAndCloseOutputPanel(false);
		
		panel.mouseHover(panel.getViewPort(1));
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1, -50, -50, 100, 0);
		lineWithUnit.closingConflictMsg();
		
		panel.mouseHover(panel.getViewPort(1));
		panel.openAndCloseOutputPanel(true);
		for(int i=0;i<=2;i++)
		verifyViewerPageOnBrowserResize(1,seriesName,"Checkpoint[6."+i+"/6]",ViewerPageConstants.LINEAR_MEASUREMENT);
		panel.openAndCloseOutputPanel(false);
		
		
		
		}
	
	@Test(groups = {"Chrome","Edge","IE","Negative", "DE1708"})
	public void test10_DE1708_TC6984_VerifyAnnotationOnIcoMetrix() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify that error banner is not displayed when user adds annotation to the DICOM series");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(icoMetrixPatientName,  icoMetrixNumberOfImages, 1);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit.waitForViewerpageToLoad();

		lineWithUnit.selectDistanceFromQuickToolbar(5);
		lineWithUnit.drawLine(5, -50, -50, 150, 150);
		helper.browserBackAndReloadViewer(icoMetrixPatientName,  icoMetrixNumberOfImages, 1);

		lineWithUnit.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify linear measurement annotation is present on reloading the viewer");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verifying linear measurement is present","A linear measurement is present");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify ellipse annotation is present");
		ellipse.assertEquals(ellipse.getEllipses(1).size(),1,"Verifying ellipse annotation is present","The ellipse annotation is present");
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

	public void verifyViewerPageOnBrowserResize(int whichViewbox,String element,String checkpoint,String annotationType) throws InterruptedException
	{
		panel.resizeBrowserWindow(1200, 900);
		panel.waitForOutputPanelToLoad();
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(whichViewbox),element,checkpoint,"Verified that viewer page not broken after window resize to minimum for "+annotationType);
		panel.assertTrue(panel.isElementPresent(panel.outputPanelSection), checkpoint, "Verified that output panel remain open after window resize to minimum for "+annotationType);
		panel.maximizeWindow();
		panel.waitForOutputPanelToLoad();
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(whichViewbox),element,checkpoint,"Verified that viewer page not broken after window resize to maximum for  "+annotationType);
		panel.assertTrue(panel.isElementPresent(panel.outputPanelSection), checkpoint, "Verified that output panel remain open after window resize to maximum for "+annotationType);
		
		
	}
}


