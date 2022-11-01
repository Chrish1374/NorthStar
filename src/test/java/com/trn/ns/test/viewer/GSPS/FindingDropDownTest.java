package com.trn.ns.test.viewer.GSPS;
import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class FindingDropDownTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerSliderAndFindingMenu viewerPage;
	private ExtentTest extentTest;
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;	
	private SimpleLine line;
	private PolyLineAnnotation polyLineAnn;
	private ContentSelector cs;
	private DatabaseMethods db;
	private PagesTheme pageTheme;

	String loadedTheme ;
	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String piccLinefilePath = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String piccLinePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, piccLinefilePath);

	String firstSeriesDescriptionMultiSeries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
	String secondSeriesDescriptionMultiSeries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath);		

	String imbioFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilePath);

	String firstSeriesDescriptionImbio = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, imbioFilePath);
	String rtStruct =Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String rtStructPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, rtStruct);

	String gaelKuhnFilePath =Configurations.TEST_PROPERTIES.get("GAEL^KUHN_filepath");
	String GaelPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gaelKuhnFilePath);
	String GaelPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, gaelKuhnFilePath);

	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);

	String filePath_AH4=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_AH4);
	private PolyLineAnnotation polyline;
	private HelperClass helper; 

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"Chrome","IE11","Edge","US733","US2293","DR2592","Positive","BVT","E2E","F1091"})
	public void test01_US733_US783_TC2820_TC2821_TC2882_TC3102_US2293_TC10116_DR2592_TC10281_verifyPendingFindingStateCount() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify pending state count on Badge"
				+ "</br>Verify pending state count and series description on finding table"
				+"</br>Verify the color of Pointer for each finding. <br>"+
				"Verify the count of findings are getting increased or decreased in finding status box for accepted, pending , rejected findings in new finding menu.<br>"+
				"Risk and Impact - TC10116");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/13]", "Verify badge is visible on A/R button with total number of Pending finding");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(), 5, "Verify number of pending finding on badge", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar());

	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/13]", "Verify number of finding on header of finding table");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(5), "Verify number of pending finding on badge", "The number of pending finding on header of table is "+viewerPage.getBadgeCountFromToolbar());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/13]", "Verify pointer color for pending finding on table");
		viewerPage.assertEquals(point.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),5, "Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");

		cs = new ContentSelector(driver);		
		List<String> results = cs.getAllResults();

		//draw a circle annotation on view box and verify content of finding table
		circle.mouseHover(circle.getViewPort(2));
		circle.mouseHover(circle.getViewPort(1));
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 50, 50);

		//		List<WebElement> myPoint = point.getPoint(1, 1);
		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		//verify no of pending finding on badge after drawing annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/13]", "Verify number of pending finding on badge remains same as newly drawn annotation is in accepted state");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(), 5, "Verify number of pending finding on badge", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar());

		//verify no of finding on finding table header
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/13]", "Verify number of finding on header of finding table");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(6), "Verify number of pending finding on badge", "The number of pending finding on header of table is 6");

		//verify color of pointer for accepted finding
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/13]", "Verify pointer color for accepted finding on table");
		viewerPage.assertEquals(point.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1, "Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");
		viewerPage.assertEquals(point.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),5, "Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");

		//change the state of first point annotation
		viewerPage.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		//verify no of pending finding on badge after changing state of annotation from pending to rejected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/13]", "Verify number of pending finding on badge remains same as newly drawn annotation is in accepted state");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(), 4, "Verify number of pending finding on badge", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar());

		//verify no of finding on finding table header remains same
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/13]", "Verify number of finding on header of finding table");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(6), "Verify number of pending finding on badge", "The number of pending finding on header of table is 6");

		//verify color of pointer for accepted finding
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/13]", "Verify pointer color for rejected finding on table");
		viewerPage.assertEquals(point.getFindingsName(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1, "Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");
		viewerPage.assertEquals(point.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),4, "Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");

		//select second series in content selector

		cs.selectResultFromSeriesTab(1, results.get(1),1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/13]", "Verify badge is visible on A/R button with total number of Pending finding");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(), 13, "Verify number of pending finding on badge", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/13]", "Verify number of finding on header of finding table");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(13), "Verify number of pending finding on badge", "The number of pending finding on header of table is "+viewerPage.getBadgeCountFromToolbar());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/13]", "Verify series description on header of finding table");
		viewerPage.assertTrue(viewerPage.verifySeriesDescriptionOnFindingTable(secondSeriesDescriptionMultiSeries), "Verify series description on finding table for series in viewbox1", "The series description on finding table is "+firstSeriesDescriptionMultiSeries);


	}

	@Test(groups ={"Chrome","IE11","Edge","US733","Positive"})
	public void test02_US733_TC2823_TC2395_TC2824_verifyScrollBarInsideFindingDropDown() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll bar inside a finding drop down"
				+ "</br Verify scroll bar is not present if number of finding is withing aviable space limit"
				+ "</br> Verify navigation on selecting finding from drop down menu");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,1);
		ViewerLayout layout = new ViewerLayout(driver);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);

		//change a layout to 2 x 3 
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		//open a finding table on second view box and verify that drop down menu is present
		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(2,1).get(0));
		viewerPage.click(viewerPage.gspsFinding);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify drop down is present on finding table");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findingHeader, NSGenericConstants.DISPLAY_PROPERTY),NSGenericConstants.DISPLAY_BLOCK_VALUE, "Verify CSS property for scroll bar in finding table", "The CSS property of scroll bar is present on finding table");
		//		viewerPage.compareElementImage(protocolName,viewerPage.findingHeader,"Verify Scroll bar appear on Content selector","test01_CheckPoint2");

		//double click on view box 2
		viewerPage.doubleClickOnViewbox(2);

		//open a finding table on second view box and verify that drop down menu is present
		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(2,1).get(0));
		viewerPage.click(viewerPage.gspsFinding);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify drop down is present on finding table");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findingHeader, NSGenericConstants.DISPLAY_PROPERTY),NSGenericConstants.DISPLAY_BLOCK_VALUE, "Verify CSS property for scroll bar in finding table", "The CSS property of scroll bar is present on finding table");
		//		viewerPage.compareElementImage(protocolName,viewerPage.findingHeader,"Verify Scroll bar appear on Content selector","test01_CheckPoint3");

		//select different finding from table and verify navigation on selection
//		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.selectFindingFromTable(2,8);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify slice changes to next slice having GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 8, "Verify slice in the viewbox shifts to the slice containing the finding", "The current slice position is: "+viewerPage.getCurrentScrollPositionOfViewbox(2));

		//verify selected finding is highlighted on finding table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify selected finding is highlighted on table");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(8), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");

		//draw a circle annotation on view box and verify content of finding table
		circle.mouseHover(circle.getViewPort(2));
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, -10, -10, 50, 50);
		circle.openGSPSRadialMenu(circle.getAllCircles(2).get(0));

		//select different finding from table and verify navigation on selection
		viewerPage.selectFindingFromTable(2,2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify slice changes to next slice having GSPS object on selecting slice having Machine drawn annotation");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),3, "Verify slice in the viewbox shifts to the slice containing the finding", "The current slice position is: "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");

		//open radial menu for other point annotation
		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(2,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify finding is highlighted in table on annotation selection");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");

	}

	@Test(groups ={"Chrome","IE11","Edge","US733","Negative"})
	public void test03_US733_TC2825_verifyFindingDropDownCloseOnMouseHover() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify closing of finding drop down menu");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);

		point = new PointAnnotation(driver);

		//click on GSPS finding icon and verify finding table is present
		viewerPage.click(viewerPage.gspsFinding);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify finding drop down on clicking finding icon on A/R bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//hover mouse away from drop down menu
		viewerPage.mouseHover(viewerPage.EurekaLogo);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify finding drop down close on mosue hover away from table");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is not present on view box1", "The finding drop down is not present on viewbox");
		//		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsAccept), "Verify Accept/Reject GSPS bar is absent", "The Accept/Reject bar is not present on viewbox");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");

		//click on GSPS finding icon and verify finding table is present

		viewerPage.click(viewerPage.gspsFinding);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify finding drop down on clicking finding icon on A/R bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//change the active view box by mouse hover on second view box
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify finding drop down close on changing active viewbox");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is not present on view box1","The finding drop down is not present on viewbox");
		//		viewerPage.assertTrue(viewerPage.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject GSPS bar is present", "The Accept/Reject bar is not present on viewbox");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");

		//hover mouse over bottom on view box to display A/R radial bar and click on finding table
		viewerPage.mouseHover(viewerPage.acceptRejectToolbar);
		viewerPage.click(viewerPage.gspsFinding);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify finding drop down on clicking finding icon on A/R bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//hover mouse away from drop down menu
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify finding drop down close on mosue hover away from table");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify Accept/Reject GSPS bar is closes on mouse over", "The Accept/Reject radial bar is not displayed on viewbox ");
		viewerPage.assertFalse(viewerPage.isAcceptRejectToolBarPresent(1), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//hover mouse over bottom on view box to display A/R radial bar and click on finding table
		viewerPage.mouseHover(viewerPage.acceptRejectToolbar);
		viewerPage.click(viewerPage.gspsFinding);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify finding drop down on clicking finding icon on A/R bar");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//change the active view box by mouse hover on second view box
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify finding drop down close on mosue hover away from table");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is not present on view box1", "The finding drop down is not present on viewbox");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify Accept/Reject GSPS bar is closes on mouse over", "The Accept/Reject radial bar is not displayed on viewbox ");
	}

	@Test(groups ={"IE11","Chrome","Edge","US733","US950","Sanity"})
	public void test04_US733_TC2934_US950_TC4318_TC4319_verifyNameOfFindingOnTable() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the name of finding on table"
				+ "<br> Verify that when user select annotation from finding list drop down (from A/R bar) yellow shadow should be displayed around selected annotation and it should not get bold"
				+ "<br> Reg - E2E : Verify that when user accept / reject / any annotation then next annotation should highlighted with yellow shadow and annotation should not get bold /thick");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		polyLineAnn = new PolyLineAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//navigate to next slice and draw a polyline annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		polyLineAnn.selectPolylineFromQuickToolbar(1);		
		polyLineAnn.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, -70, -80, -120, 90);

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, 50, 50, "ABC");

		//hover mouse over bottom on view box to display A/R radial bar.
		viewerPage.mouseHover(viewerPage.acceptRejectToolbar);
		viewerPage.click(viewerPage.gspsFinding);

		//verify name of each findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify name of each type of finding");
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(1),ViewerPageConstants.LINEAR_FINDING_NAME, "Verify name of first finding on table", "The name of first finding is: "+viewerPage.getTextOfFindingFromTable(1));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(2),ViewerPageConstants.POINT_FINDING_NAME, "Verify name of second finding on table", "The name of second finding is: "+viewerPage.getTextOfFindingFromTable(2));	
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(3),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Verify name of third finding on table", "The name of third finding is: "+viewerPage.getTextOfFindingFromTable(3));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(4),ViewerPageConstants.CIRCLE_FINDING_NAME, "Verify name of fourth finding on table", "The name of fourth finding is: "+viewerPage.getTextOfFindingFromTable(4));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(5),ViewerPageConstants.POLYLINE_FINDING_NAME, "Verify name of five finding on table", "The name of fifth finding is: "+viewerPage.getTextOfFindingFromTable(5));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(6),ViewerPageConstants.DISTANCE_FINDING_NAME, "Verify name of sixth finding on table", "The name of sixth finding is: "+viewerPage.getTextOfFindingFromTable(6));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(7),ViewerPageConstants.TEXT_FINDING_NAME, "Verify name of seventh finding on table", "The name of seventh finding is: "+viewerPage.getTextOfFindingFromTable(7));


		viewerPage.selectFindingFromTable(ViewerPageConstants.LINEAR_FINDING_NAME);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");
		viewerPage.selectRejectfromGSPSRadialMenu();
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");


		viewerPage.selectFindingFromTable(ViewerPageConstants.POINT_FINDING_NAME);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");

		viewerPage.selectFindingFromTable(ViewerPageConstants.ELLIPSE_FINDING_NAME);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");

		viewerPage.selectFindingFromTable(ViewerPageConstants.CIRCLE_FINDING_NAME);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");


		viewerPage.selectFindingFromTable(ViewerPageConstants.POLYLINE_FINDING_NAME);
		viewerPage.assertTrue(polyLineAnn.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");

		viewerPage.selectFindingFromTable(ViewerPageConstants.DISTANCE_FINDING_NAME);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");

		viewerPage.selectFindingFromTable(ViewerPageConstants.TEXT_FINDING_NAME);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true), "","Verify that when user clicks on any annotation name from finding list dropdown it should not get bold. Also, verify that yellow shadow around selected annotation should be displayed");


	}

	@Test(groups ={"IE11","Chrome","Edge","US783","Sanity"})
	public void test05_US783_TC3078_verifyFindingDropDownOnJPEGDataWithBinarySelector() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding drop down on binary selector with JPEG");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(piccLinePatient,2);
		viewerPage = new ViewerSliderAndFindingMenu(driver);

		lineWithUnit = new MeasurementWithUnit(driver);
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		lineWithUnit.mouseHover(lineWithUnit.getViewPort(1));
		//hover mouse over bottom on view box to display binary selector and verify envoy AI finding icon
		viewerPage.mouseHover(viewerPage.getAcceptRejectToolBar(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify Envoy AI Finding icon do not appear on JPEG data with no GSPS object");
		viewerPage.assertFalse(viewerPage.verifyFindingIconOnBinarySelector(1), "Verify Envoy AI finding icon do not appear on JPEG data with no GSPS object", "The Envoy AI finding icon do not appear on binary selector");

	}	

	@Test(groups ={"IE11","Chrome","Edge","US783","Positive"})
	public void test06_US783_TC3080_TC3089_TC3100_verifyBadgeCountOnFindingTableForDataWithBinarySelector() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify badge count on finding table"
				+ "</br>Verify Envoy AI finding icon on binary selector"
				+ "</br>Verify switching of binary selector and accept reject radial bar");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(3);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//hover over binary selector and verify envoy AI finding icon is not visible
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify finding icon is not present on binary selector,when no GSPS object is present");
		viewerPage.assertTrue(viewerPage.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is not present on binary selector", "The finding icon is not present on binary selector");

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -170, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//		viewerPage.selectScrollFromQuickToolbar(1);

		//hover over binary selector and verify envoy AI finding icon is visible after used draw annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify finding icon is present on binary selector,when GSPS object is present on UI");
		viewerPage.assertTrue(viewerPage.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is present on binary selector", "The finding icon is present on binary selector");

		//change the state Point and ellipse annotation
		viewerPage.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		ellipse.selectAcceptfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));

		//hover mouse over bottom on view box to display binary selector and verify pending annotation count
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify badge count for pending finding on binary selector");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(1),3, "Verify number of pending finding on badge for binary selector", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar(1));

		//change the circle annotation and verify badge count increase
		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.mouseHover(viewerPage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify badge count for pending finding on binary selector increase on user interaction");
		viewerPage.assertEquals(viewerPage.getBadgeCountFromToolbar(1),4, "Verify number of pending finding on badge for binary increase on user interaction", "The number of pending finding is "+viewerPage.getBadgeCountFromToolbar(1));
		cs = new ContentSelector(driver);
		//select series from content selector and verify finding icon on binary selector
		cs.selectResultFromSeriesTab(3, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		//		viewerPage.click(viewerPage..getViewPort(3));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify finding icon is present on binary selector on selecting series from content selector");
		viewerPage.assertTrue(viewerPage.verifyFindingIconOnBinarySelector(3) ,"Verify finding icon is present on binary selector", "The finding icon is present on binary selector");

		//delete all the annotation and verify finding icon is not present on UI.
		viewerPage.deleteAllAnnotation(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify finding icon is not present on binary selector,when GSPS object is present on UI");
		viewerPage.assertTrue(viewerPage.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is not present on binary selector", "The finding icon is not present on binary selector");

	}

	@Test(groups ={"IE11","Chrome","Edge","US783","Positive"})
	public void test07_US783_TC3101_TC3107_verifySeriesDescriptionOnFindingHeaderforDataWithBinarySelector() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding drop down along with series description and pending finding count"
				+ "</br>Verify scroll bar is not present on finding count,if count of finding is withing the limit");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(3);
				
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//		viewerPage.selectScrollFromQuickToolbar(1);

		//hover over binary selector and click envoy AI finding
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify series description on header of finding table from binary selector");
		viewerPage.assertTrue(viewerPage.verifySeriesDescFindingTableHeader(1, firstSeriesDescriptionImbio) ,"Verify series description on header of finding table from finding header", "The series description on header of finding table "+firstSeriesDescriptionImbio);

		//hover over binary selector and click envoy AI finding
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify total finding count on header of finding table from binary selector");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(5) ,"Verify total finding count on header of finding table from binary selector", "The total finding count on header is 4");

		//hover over binary selector and click envoy AI finding
		viewerPage.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify drop down is not present on finding table");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findingHeader, NSGenericConstants.DISPLAY_PROPERTY),NSGenericConstants.DISPLAY_BLOCK_VALUE, "Verify CSS property for scroll bar in finding table", "The CSS property of scroll bar is present on finding table");
		//		viewerPage.compareElementImage(protocolName,viewerPage.findingHeader,"Verify scroll bar do not appear on finding table","test07_CheckPoint1");

	}

	@Test(groups ={"IE11","Chrome","Edge","US783","US2293","Positive","F1091"})
	public void test08_US783_TC3103_US2293_TC9935_verifyScrollBarInsideFindingDropDown() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding drop down along with series description and pending finding count. <br>"+
		"Verify that vertical scroll bar is present in finding menu popup if number of findings are more than the maximum height size limit of finding menu,");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);	
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(3);
		
		point = new PointAnnotation(driver);

		//select Point annotation from radial menu and draw a multiple point annotation
		point.mouseHover(point.getViewPort(1));
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -120, -40);
		point.drawPointAnnotationMarkerOnViewbox(1, -130, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -180, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, 130, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 170, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 20, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 30, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, -80);
		point.drawPointAnnotationMarkerOnViewbox(1, 160, -70);

		//hover over binary selector and click envoy AI finding
		viewerPage.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify drop down is present on finding table");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findingHeader, NSGenericConstants.DISPLAY_PROPERTY),NSGenericConstants.DISPLAY_BLOCK_VALUE, "Verify CSS property for scroll bar in finding table", "The CSS property of scroll bar is present on finding table");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.verticalScrollBar), "Verify vertical scrollbar in finding menu.", "Verified");

	
		//		viewerPage.compareElementImage(protocolName,viewerPage.findingHeader,"Verify Scroll bar appear on Content selector","test08_CheckPoint1");

		//change layout to 3x3 and open a finding header
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify drop down is present on finding table");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findingHeader, NSGenericConstants.DISPLAY_PROPERTY),NSGenericConstants.DISPLAY_BLOCK_VALUE, "Verify CSS property for scroll bar in finding table", "The CSS property of scroll bar is present on finding table");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.verticalScrollBar),  "Verify vertical scrollbar in finding menu after layout change.", "Verified");
		//		viewerPage.compareElementImage(protocolName,viewerPage.findingHeader,"Verify Scroll bar appear on Content selector","test08_CheckPoint2");
	}

	@Test(groups ={"IE11","Chrome","Edge","US783","Positive","BVT"})
	public void test09_US783_TC3106_verifyNameOfFindingOnTable() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the name of finding on table");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		polyLineAnn = new PolyLineAnnotation(driver);
		line = new SimpleLine(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//navigate to next slice and draw a polyline annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		polyLineAnn.selectPolylineFromQuickToolbar(1);		
		polyLineAnn.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, -70, -80, -120, 90);

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, 50, 50, "ABC");

		//hover over binary selector and click envoy AI finding
		//		viewerPage.openFindingTableOnBinarySelector(1);

		//verify name of each findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify name of each type of finding");
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(2),ViewerPageConstants.LINEAR_FINDING_NAME, "Verify name of first finding on table", "The name of first finding is: "+viewerPage.getTextOfFindingFromTable(1));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(3),ViewerPageConstants.POINT_FINDING_NAME, "Verify name of second finding on table", "The name of second finding is: "+viewerPage.getTextOfFindingFromTable(2));	
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(4),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Verify name of third finding on table", "The name of third finding is: "+viewerPage.getTextOfFindingFromTable(3));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(5),ViewerPageConstants.CIRCLE_FINDING_NAME, "Verify name of fourth finding on table", "The name of fourth finding is: "+viewerPage.getTextOfFindingFromTable(4));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(6),ViewerPageConstants.POLYLINE_FINDING_NAME, "Verify name of five finding on table", "The name of fifth finding is: "+viewerPage.getTextOfFindingFromTable(5));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(7),ViewerPageConstants.DISTANCE_FINDING_NAME, "Verify name of sixth finding on table", "The name of sixth finding is: "+viewerPage.getTextOfFindingFromTable(6));
		viewerPage.assertEquals(viewerPage.getTextOfFindingFromTable(8),ViewerPageConstants.TEXT_FINDING_NAME, "Verify name of seventh finding on table", "The name of seventh finding is: "+viewerPage.getTextOfFindingFromTable(7));

	}

	@Test(groups ={"IE11","Chrome","Edge","US783","Sanity","BVT"})
	public void test10_US783_TC3104_verifyNavigationFromFindingDropDown() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify navigation from finding table");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		ViewerLayout layout = new ViewerLayout(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//navigate to next 2 slice and select Point annotation from radial menu and draw a point annotation
		point.scrollDownToSliceUsingKeyboard(2);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);
//		viewerPage.mouseHover(viewerPage.getViewPort(1));

		//hover over binary selector and click envoy AI finding
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify total finding count on header of finding table from binary selector");
		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(5) ,"Verify total finding count on header of finding table from binary selector", "The total finding count on header is 4");

		//open a finding table and select the Point finding from table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify on selecting finding from table it is highligted");
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.selectFindingFromTable(3);

		//verify application moves to slice having selected finding
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify point annotation is current active GSPS object","Point annotation is current active GSPS");

		//		if(!viewerPage.isElementPresent(viewerPage.getAcceptRejectToolBar(1)))
		//		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));


		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(3), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),115, "Verify slice in the viewbox shifts to the slice containing the finding", "The current slice position is: "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//open a finding table and select the Ellipse finding from table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify on selecting finding from table it is highligted");
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.selectFindingFromTable(4);

		//verify application moves to slice having selected finding
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Ellipse annotation is current active GSPS object","Ellipse annotation is current active GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),117, "Verify slice in the viewbox shifts to the slice containing the finding", "The current slice position is: "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//change layout to 3x3 and select linear from finding 
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		//open a finding table and select the Ellipse finding from table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify on selecting finding from table it is highligted");
		viewerPage.selectFindingFromTable(1,2);

		//verify application moves to slice having selected finding
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verify Linear measurement annotation is current active GSPS object","Linear annotation is current active GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),113, "Verify slice in the viewbox shifts to the slice containing the finding", "The current slice position is: "+viewerPage.getCurrentScrollPositionOfViewbox(1));


	}

	@Test(groups ={"IE11","Chrome","Edge","US733","Negative"})
	public void test11_US783_TC3105_verifyFindingDropDownCloseFromBinarySelector() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding drop down closes on mouse hover from binary selector");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatient,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);


		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//click on finding icon binary selector and verify finding table is present
		viewerPage.openFindingTableOnBinarySelector(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify finding drop down on clicking finding icon from binary selector");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//hover mouse away from drop down menu
		viewerPage.mouseHover(viewerPage.EurekaLogo);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify finding drop down close on mosue hover away from table");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is not present on view box1", "The finding drop down is not present on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getAcceptIcon()), "Verify binary selector bar is not present", "The binary selector bar is not present on viewbox");

		//click on finding icon binary selector and verify finding table is present
		viewerPage.openFindingTableOnBinarySelector(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify finding drop down on clicking finding icon from binary selector");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is present", "The finding drop down is present on viewbox");

		//hover mouse away from drop down menu
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify finding drop down close on changing active viewbox");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTable), "Verify finding drop down is not present on view box1", "The finding drop down is not present on viewbox");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getAcceptIcon()), "Verify binary selector bar is not present", "The binary selector bar is not present on viewbox");

	}

	// US978: Display groups in the findings list and cross-slice warnings

	@Test(groups ={"IE11","Chrome","Edge","US978","US2293","positive","E2E","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test18_US978_TC4396_US2293_TC9940_verifyCrossSliceWarningMessage(String theme) throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of cross-slice warning Icon and message. <br>"+
		"Verify the design , look and feel of the group finding in finding menu.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,username, password, 1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		cs=new ContentSelector(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);

		viewerPage.openFindingTableOnBinarySelector(1);

		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//Verify warning Icon for the Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify warning Icon for groups.");

		for(int i=0;i<viewerPage.groupInfo.size();i++)
		{
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.warningIcon.get(i)), "Verify warning icon seen for Group"+(i+1), "Verified");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.warningIcon.get(i), NSGenericConstants.FILL), ViewerPageConstants.SHADOW_COLOR, "Verify color seen for warning icon","Verified yellow color seen forwarning icon");
		}

		//Verify banner when finding selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify banner display after selecting finding from the group");
		viewerPage.selectFindingFromGroupOfTable(1, 1,3);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.notificationDiv), "Verify banner after selecting finding from the group", "Banner display in header after selecting finding from the group");
	
		//Reload another patient
		helper.loadViewerDirectly(GaelPatient, 1);

		//verify banner and warning message when annotation are on same slice
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify banner not display when annotations are on same slice of "+" "+GaelPatient);
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 1, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
		viewerPage.assertFalse((viewerPage.warningIcon.size()) > 0, "Verify warning icon not seen for "+ viewerPage.getText(viewerPage.groupInfo.get(0)), "Verified");
	} 	

	@Test(groups ={"IE11","Chrome","Edge","US978","Sanity"})
	public void test19_US978_TC4402_verifyOverlayShapeAndGroupsTreeStructure() throws InterruptedException, SQLException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification overlay shape and groups tree structure in Findings");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,  1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		cs=new ContentSelector(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);
		db = new DatabaseMethods(driver);

		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify name of each type of finding");
		viewerPage.openFindingTableOnBinarySelector(1);

		//Verify Finding menu overlay shape 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Findings menu overlay shape and available groups on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//verify tree structure by expanding Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify tree structure in groups and findings underneath");
		viewerPage.click(viewerPage.toggleButton.get(1));
		int findingListOnUI=viewerPage.listOfFindingsFromGroup.size();
		int findingListInDb=db.getCountOfGraphicObjectIDAndType(LIDCPatientID,viewerPage.getText(viewerPage.groupInfo.get(1)));
		viewerPage.assertEquals(findingListOnUI, findingListInDb, "Verify list of findings for Group 2"+" "+"is"+" " +findingListOnUI+".", "Findings seen under group are"+" "+findingListInDb);

		//Reload study page again and Check state of Finding Menu 
		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "verify no saving of the finding group expand state");
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

	}

	@Test(groups ={"IE11","Chrome","Edge","US978","Sanity"})
	public void test20_US978_TC4403_verifyGroupsAndFindingSelections() throws Exception, Throwable     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification selection of Groups and Findings on UI as well as DB");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GaelPatient,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		circle =new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify name of each type of finding");
		viewerPage.openFindingTableOnBinarySelector(1);

		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify available groups on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 1, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//select Group then check default first finding is selected from Group also state of annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify group will jump to and select the first finding/annotation within it once click on Group and check state of GSPS");
		viewerPage.click(viewerPage.groupInfo.get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1),"Verify state of annotation as Pending when user select first annotation from the Group","Verified");

		//Verify background color for the selected finding in finding Toolbar
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify the background of the finding is in grey color when finding is selected on finding toolbar ");
		viewerPage.selectFindingFromGroupOfTable(1, 1, 1);
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.click(viewerPage.toggleButton.get(0));
//		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.findings.get(0), NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR,"Verify Background color as Gray for the first finding when it is selected","Verified");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "", "");
	}			

	@Test(groups ={"IE11","Chrome","Edge","US978","positive"})
	public void test21_US978_TC4384_verifyGroupsOnViewerFindingAndDB() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of groups on Viewer and DB for"+" "+ LIDCPatientID + " "+ "and"+" "+GaelPatient);

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		lineWithUnit = new MeasurementWithUnit(driver);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		db = new DatabaseMethods(driver);

		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", " Verify count of groups seen in finding toolbar for "+LIDCPatientID);
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//Verify Group lable in DB 
		//Verify GraphicGroupID seen in GSPSGraphicObject and GraphicType
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", " Verify groups are listed in database table of GraphicGroupLabel");		

		for(int i=0;i<viewerPage.groupInfo.size();i++)
		{
			viewerPage.assertTrue(db.verifyGroupLabelInDB(viewerPage.getText(viewerPage.groupInfo.get(i))),"Verify "+" "+viewerPage.getText(viewerPage.groupInfo.get(i))+" as group visible on finding toolbar as well as in Database ","Verified");
			viewerPage.click(viewerPage.toggleButton.get(i));
			int findingListOnUI=viewerPage.listOfFindingsFromGroup.size();
			int findingListInDb=db.getCountOfGraphicObjectIDAndType(LIDCPatientID,viewerPage.getText(viewerPage.groupInfo.get(i)));
			viewerPage.assertEquals(findingListOnUI, findingListInDb, "Verify list of findings for Group"+" "+(i+1)+" "+"is"+" " +findingListOnUI+".", "Findings seen under group are"+" "+findingListInDb);
			viewerPage.click(viewerPage.toggleButton.get(i));
		}

		//navigate back and Load another patient gael
		extentTest.setDescription("Verification of groups on Viewer and DB for"+" "+GaelPatient );
		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GaelPatient, 1);
		
		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", " Verify count of groups seen in finding toolbar for "+GaelPatient);
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 1, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//Verify Group lable in DB 
		//Verify GraphicGroupID seen in GSPSGraphicObject and GraphicType
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", " Verify groups are listed in database table of GraphicGroupLabel");

		for(int i=0;i<viewerPage.groupInfo.size();i++)
		{
			viewerPage.assertTrue(db.verifyGroupLabelInDB(viewerPage.getText(viewerPage.groupInfo.get(i))),"Verify "+" "+viewerPage.getText(viewerPage.groupInfo.get(i))+" as group visible on finding toolbar as well as in Database ","Verified");
			viewerPage.click(viewerPage.toggleButton.get(i));
			int findingListOnUI=viewerPage.listOfFindingsFromGroup.size();
			int findingListInDb=db.getCountOfGraphicObjectIDAndType(GaelPatientID,viewerPage.getText(viewerPage.groupInfo.get(i)));
			viewerPage.assertEquals(findingListOnUI, findingListInDb,  "Verify list of findings for Group"+" "+(i+1)+" "+"is"+" " +findingListOnUI+".",  "Findings seen under group are"+" "+findingListInDb);
			viewerPage.click(viewerPage.toggleButton.get(i));
		}
	}

	//Accept/Reject at the group level for GSPS

	@Test(groups ={"Chrome","IE11","Edge","Positive","DE1654"})
	public void test22_DE1654_TC6838_verifyFindingStateCountOnBadgeAndTable() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding count on A/R toolbar Badge and finding table");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify the finding count on A/R toolbar badge and finding table with viewbox 1");
		List<WebElement> pendingFindings_viewbox1 = point.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		point.assertEquals(point.getBadgeCountFromToolbar(), pendingFindings_viewbox1.size(), "Verify number of pending finding on badge and finding table", "The number of pending finding on badge is "+point.getBadgeCountFromToolbar()+"</br>The number of pending finding on finding table is"+pendingFindings_viewbox1.size());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify the finding count on A/R toolbar badge and finding table with viewbox 2");
		List<WebElement> pendingFindings_viewbox2 = point.getStateSpecificFindings(2,ViewerPageConstants.PENDING_FINDING_COLOR);
		int viewbox2_pendingCount=pendingFindings_viewbox2.size();
		point.assertEquals(point.getBadgeCountFromToolbar(2), pendingFindings_viewbox2.size(), "Verify number of pending finding on badge and finding table", "The number of pending finding on badge is "+point.getBadgeCountFromToolbar()+"</br>The number of pending finding on finding table is"+pendingFindings_viewbox2.size());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify the count on A/R toolbar badge and finding table after rejecting a finding");
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		pendingFindings_viewbox1 = point.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		point.assertEquals(point.getBadgeCountFromToolbar(), pendingFindings_viewbox1.size(), "Verify number of pending finding on badge and finding table", "The number of pending finding on badge is "+point.getBadgeCountFromToolbar()+"</br>The number of pending finding on finding table is"+pendingFindings_viewbox1.size());

		point.assertEquals(viewbox2_pendingCount, point.getStateSpecificFindings(2,ViewerPageConstants.PENDING_FINDING_COLOR).size(), "Verify number of pending finding on badge", "The number of pending finding is "+point.getBadgeCountFromToolbar());	

	}

	@Test(groups ={"Chrome","IE11","Edge","DE1802","Positive"})
	public void test24_DE1802_TC7384_verifyPendingFindingStateCountInOneUp() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding count mentioned in the finding menu after double click one-up");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		polyline = new PolyLineAnnotation(driver);

		
		//draw a circle annotation on view box and verify content of finding table
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 50, 50);
		circle.assertEquals(circle.getBadgeCountFromToolbar(1), 0, "Checkpoint[1/9]","Verify number of pending finding on badge - The number of pending finding is "+circle.getBadgeCountFromToolbar(1));
		circle.doubleClickOnViewbox(1);
		circle.assertEquals(circle.getBadgeCountFromToolbar(1), 0, "Checkpoint[2/9]","Verify number of pending finding on badge - The number of pending finding is "+circle.getBadgeCountFromToolbar(1));
		circle.doubleClickOnViewbox(1);
		circle.assertEquals(circle.getBadgeCountFromToolbar(1), 0, "Checkpoint[3/9]","Verify number of pending finding on badge - The number of pending finding is "+circle.getBadgeCountFromToolbar(1));


		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 0, 0, -100,-100);	

		ellipse.assertEquals(ellipse.getBadgeCountFromToolbar(2), 0,"Checkpoint[4/9]", "Verify number of pending finding on badge - The number of pending finding is "+ellipse.getBadgeCountFromToolbar(2));
		ellipse.doubleClickOnViewbox(2);
		ellipse.assertEquals(ellipse.getBadgeCountFromToolbar(2), 0, "Checkpoint[5/9]","Verify number of pending finding on badge - The number of pending finding is "+ellipse.getBadgeCountFromToolbar(2));
		ellipse.doubleClickOnViewbox(2);
		ellipse.assertEquals(ellipse.getBadgeCountFromToolbar(2), 0, "Checkpoint[6/9]","Verify number of pending finding on badge - The number of pending finding is "+ellipse.getBadgeCountFromToolbar(2));



		polyline.selectPolylineFromQuickToolbar(3);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		polyline.drawFreehandPolyLine(3, abc);

		polyline.assertEquals(polyline.getBadgeCountFromToolbar(3), 0, "Checkpoint[7/9]","Verify number of pending finding on badge - The number of pending finding is "+polyline.getBadgeCountFromToolbar(3));
		polyline.doubleClickOnViewbox(3);
		polyline.assertEquals(ellipse.getBadgeCountFromToolbar(3), 0, "Checkpoint[8/9]","Verify number of pending finding on badge - The number of pending finding is "+polyline.getBadgeCountFromToolbar(3));
		polyline.doubleClickOnViewbox(3);
		polyline.assertEquals(ellipse.getBadgeCountFromToolbar(3), 0,"Checkpoint[9/9]", "Verify number of pending finding on badge - The number of pending finding is "+polyline.getBadgeCountFromToolbar(3));


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1942","DR2592", "Positive","BVT","F1091"})
	public void test25_DE1942_TC7831_DR2592_TC10279_VerifyCountOnFindingTable()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the count on finding table for Memo CAD data. <br>"+
		"Verify finding list functionality on CAD");

		// Loading the patient on viewer
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		cs = new ContentSelector(driver);
		cs.waitForViewerpageToLoad();

		List<String> results = cs.getAllResults();

		cs.selectResultFromSeriesTab(1, results.get(1),4);

		viewerPage.openFindingTableOnBinarySelector(1);
		int badgeCount=viewerPage.getBadgeCountFromToolbar(1);
		
		viewerPage.selectRejectfromGSPSRadialMenu();
		cs.assertEquals(viewerPage.getBadgeCountFromToolbar(1), badgeCount-1, "Checkpoint[1/2]", "Verifying that the badge count is reduced after rejecting a contour");

		viewerPage.selectAcceptfromGSPSRadialMenu();

		cs.assertEquals(viewerPage.getBadgeCountFromToolbar(1), badgeCount-2, "Checkpoint[2/2]", "Verifying that the badge count is reduced after accepting a contour");


	}

    // US2293: New findings popup for Accept-Reject toolbar as per Eureka UI redesign

	@Test(groups ={"Chrome","IE11","Edge","Positive","US2293","E2E","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test22_US2293_TC9929_TC9930_TC10115_verifyThemeForFindingMenu(String theme) throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new design, look and feel of the new finding menu pop up of AR tool bar for Eureka theme. <br>"+
		"Verify the new design, look and feel of the new finding menu pop up of AR tool bar for dark theme.<br>"+
		"Verify the responsiveness of the finding menu, on resizing the browser size to various screen sizes.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,username, password,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);

		viewerPage.openFindingTableOnBinarySelector(1);
		pageTheme=new PagesTheme(driver);
		viewerPage.assertTrue(pageTheme.verifyThemeForIcon(viewerPage.getFindingTable(1),loadedTheme ),"Checkpoint[1/12]","Verified theme for finding Menu popup.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.findingHeader),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[2/12]","Verified color for Finding header.");
		
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalAcceptedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[3/12]","Verified color for accepted finding count.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalRejectedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[4/12]","Verified color for rejected finding count.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalPendingFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[5/12]","Verified color for pending finding count.");
		
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalAcceptedFindings),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/12]","Verified background for accepted finding box.");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalRejectedFindings),ViewerPageConstants.REJECTED_FINDING_COLOR,"Checkpoint[7/12]","Verified background for rejected finding box");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalPendingFindings),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[8/12]","Verified background for pending finding box");
		
		for(int i=0;i<viewerPage.findings.size();i++)
		{
		viewerPage.assertTrue(pageTheme.verifyThemeOnLabel(viewerPage.findingsText.get(i),loadedTheme),"Checkpoint[9/12]","Verified theme for Finding text.");
		viewerPage.assertTrue(pageTheme.verifyThemeOnLabel(viewerPage.findingsCreatedByUser.get(i),loadedTheme),"Checkpoint[10/12]","Verified theme for Created by user name.");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.findingsState.get(i)),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[11/12]","Verified color for finding state.");
		
		}
	
		viewerPage.resizeBrowserWindow(500, 500);
		viewerPage.openFindingTableOnBinarySelector(2);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.verticalScrollBar),"Checkpoint[12/12]","Verified that vertical scrollbar is present on browser resize.");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2293","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test22_US2293_TC9931_TC9932_verifyThemeWhenFindingIsSelected(String theme) throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that color changing on hovering mouse on finding and user is able to select finding from finding menu popup for Eureka theme. <br>"+
		"Verify that color changing on hovering mouse on finding and user is able to select finding from finding menu popup for Dark theme..");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,username, password,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);

		viewerPage.openFindingTableOnBinarySelector(1);
		pageTheme=new PagesTheme(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify theme when finding row is selected and aMouse hover.");
		viewerPage.mouseHover(viewerPage.findingsText.get(2));
		viewerPage.assertFalse(pageTheme.verifyThemeForFindingRows(viewerPage.findingRows.get(2),loadedTheme), "Checkpoint[1/4]","Verified theme for color on mouse hover on finding.");
		viewerPage.selectFindingFromTable(3);
		point=new PointAnnotation(driver);
		point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1), "Checkpoint[2/4]","Verified that point annotation is active pending GSPS.");
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerPage.findingRows.get(2),loadedTheme), "Checkpoint[3/4]","Verified theme for color on when finding is selected.");
	
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertFalse(pageTheme.verifyThemeForFindingRows(viewerPage.findingRows.get(2),loadedTheme),"Checkpoint[4/4]","Verified that finding row is not highlighted when click on viewer.");
	
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2293","US2411","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test23_US2293_TC9933_TC9934_TC9936_TC9937_TC9938_TC9939_US2411_TC10348_verifyScrollEllipsesAndCommentFunct(String theme) throws InterruptedException, IOException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that navigation happening on view box on navigating over the finding row in finding menu popup by mouse wheel rotation for Eureka theme.<br>"+
		"Verify that navigation happening on view box on navigating over the finding row in finding menu popup by mouse wheel rotation for Dark theme. <br>"+
		"Verify that ellipses(....) is visible and on hovering tool tip is visible in finding menu for Eureka theme. <br>"+
		"Verify that ellipses(....) is visible and on hovering tool tip is visible in finding menu for dark theme.<br>"+
		"Verify that (i) icon is visible against finding for which comment has been added for Eureka theme. <br>"+
		"Verify that (i) icon is visible against finding for which comment has been added for dark theme. <br>"+
		"Risk and Impact - TC9933");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName,username, password,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		pageTheme=new PagesTheme(driver);
		
		viewerPage.openFindingTableOnBinarySelector(1);
		int totalCount=viewerPage.findingsText.size();
		viewerPage.selectFindingFromTable(2);
			
		//scroll through finding menu and verify
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Scroll functionality through finding menu.");
		for(int i=2;i<totalCount;i++)
		{
			int currentScroll=viewerPage.getCurrentScrollPositionOfViewbox(1);
			viewerPage.openFindingTableOnBinarySelector(1);
			viewerPage.mouseHover(viewerPage.findingRows.get(1));
			viewerPage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);
			viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScroll, "Checkpoint[1/12]","Verified that scroll perfrom successfuuly.");
			viewerPage.assertTrue(pageTheme.verifyThemeForFindingRows(viewerPage.findingRows.get(i),loadedTheme),"Checkpoint[2/12]","Verified theme for selected finding in finding table.");
			point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[3/12]","Verified that point annotation is selected on viewer when scroll perform .");
		}
		
		//TC9936 and TC9937
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify ellipses for Finding header,Text and Created user.");
		helper.loadViewerDirectly(imbioPatient, 1);
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.verifyTextOverFlowForDataWraping(viewerPage.findingHeader), "Checkpoint[4/12]", "Verified ellipses for Finding header");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.findingHeader),loadedTheme), "Checkpoint[5/12]", "Verified theme for Finding header Tooltip");
		
		viewerPage.assertTrue(viewerPage.verifyTextOverFlowForDataWraping(viewerPage.findingsCreatedByUser.get(0)), "Checkpoint[6/12]", "Verified ellipses for Created user.");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.findingsCreatedByUser.get(0)),loadedTheme), "Checkpoint[7/12]", "Verified theme for Created user Tooltip");
		
		viewerPage.assertTrue(viewerPage.verifyTextOverFlowForDataWraping(viewerPage.findingsText.get(0)), "Checkpoint[8/12]", "Verified ellipses for Finding text");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.findingsText.get(0)),loadedTheme), "Checkpoint[9/12]", "Verified theme for Finding text Tooltip");

		//TC9938 and TC9939
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify theme for I icon and tooltip.");
		String comment="Point Comment";
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 55);
		point.addResultComment(1,comment);
		
		point.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(pageTheme.verifyButtonIsFilled(viewerPage.iIconInfo.get(0),loadedTheme),"Checkpoint[10/12]", "Verified theme for I icon.");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.iIconInfo.get(0)),loadedTheme), "Checkpoint[11/12]", "Verified tooltip for Comment.");
		viewerPage.assertEquals(viewerPage.getText(viewerPage.tooltip), comment,"Checkpoint[12/12]", "Verified text of tooltip.");
	}

	@Test(groups ={"Chrome","IE11","Edge","Positive","US2293","DR2592","F1091"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test24_US2293_TC9940_DR2592_TC10280_verifyThemeForGroupFindingMenu(String theme) throws InterruptedException, IOException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the design , look and feel of the group finding in finding menu. <br>"+
		"Risk and Impact - TC9940");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,username, password,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
	
		pageTheme=new PagesTheme(driver);
		
		String comment="Point Comment";
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify theme for Group finding.");
		viewerPage.openFindingTableOnBinarySelector(1);
		pageTheme=new PagesTheme(driver);
		viewerPage.assertTrue(pageTheme.verifyThemeForIcon(viewerPage.getFindingTable(1),loadedTheme),"Checkpoint[1/13]","Verify theme for Finding table.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.findingHeader),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[2/13]","Verify color for Finding header.");
		
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalAcceptedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[3/13]","Verify color for accepted finding count.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalRejectedFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[4/13]","Verify color for rejected finding count.");
		viewerPage.assertEquals(viewerPage.getColorOfRows(viewerPage.totalPendingFindings),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[5/13]","Verify color for pending finding count.");
		
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalAcceptedFindings),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[6/13]","Verified background color for accepted finding box.");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalRejectedFindings),ViewerPageConstants.REJECTED_FINDING_COLOR,"Checkpoint[7/13]","Verified background color for rejected finding box.");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.totalPendingFindings),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[8/13]","Verified background color for pending finding box.");
		
		for(int i=0;i<2;i++)
		{
		viewerPage.assertTrue(pageTheme.verifyThemeOnLabel(viewerPage.findingsText.get(i),loadedTheme),"Checkpoint[9/13]","Verified theme for Finding text.");
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.findingsState.get(i)),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[10/13]","Verified theme for Findings within the group.");
		}
		
		viewerPage.selectFindingFromGroupOfTable(1, 1, 3);
		viewerPage.addResultComment(1,comment);
		
		viewerPage.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(pageTheme.verifyButtonIsFilled(viewerPage.iIconInfo.get(0),loadedTheme),"Checkpoint[11/13]", "Verified theme for I icon.");
		viewerPage.mouseHover(viewerPage.iIconInfo.get(0));
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.tooltip,loadedTheme), "Checkpoint[12/13]", "Verified tooltip for Group finding comment.");
		viewerPage.assertEquals(viewerPage.getText(viewerPage.tooltip), comment+" from "+ ViewerPageConstants.POLYLINE_FINDING_NAME,"Checkpoint[13/13]", "Verified text of comment for Group finding.");

	}

	//Verify finding list functionality on CAD
	@Test(groups = { "Chrome", "IE11", "Edge", "DR2592", "Positive","BVT","F1091"})
	public void test25_DR2592_TC10279_VerifyCountOnFindingTable()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding list functionality on CAD");

		// Loading the patient on viewer
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName,1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		cs = new ContentSelector(driver);
		cs.waitForViewerpageToLoad();

		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		
		List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Checkpoint[1/5]","Verifying there is 1 GSPS objects present");
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/5]", "Verifying the poly is present on first result");
		poly.openFindingTableOnBinarySelector(1);
		poly.assertEquals(poly.findings.size(), 1, "Checkpoint[3/5]", "Verified that finding visible in finding menu for first result is 1.");
		
		contentSelector.selectResultFromSeriesTab(2, results.get(results.size()-1),4);
		poly.assertEquals(poly.getAllGSPSObjects(2).size(),3,"Checkpoint[4/5]","Verifying there are 3 GSPS objects present");
		poly.openFindingTableOnBinarySelector(2);
		poly.assertEquals(poly.findings.size(), 3, "Checkpoint[5/5]", "Verified that finding visible in finding menu for fourth result is 3.");
	
		
	}
}














