package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.util.List;
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
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.web.page.WebActions;

import gnu.cajo.utils.BaseProxy.Panel;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CutPasteContextMenuTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;

	private ExtentTest extentTest;
	private ViewerPage viewerpage;
	private PointAnnotation point ;
	private ContentSelector cs;
	private CircleAnnotation circle; 
	private TextAnnotation textAnn;
	private EllipseAnnotation ellipse;
	private PolyLineAnnotation polyline;
	private Header header;
	private ViewerARToolbox viewerARToolbox;
	private OutputPanel panel;

	String filePath1 = Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
	String text1PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("NorthStar^Text^NoAnchor_filepath");
	String text2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
	String ellipsePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
	String circlePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath5 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINT_filepath");
	String pointPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	String filePath6 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POLYLINE_filepath");
	String polylinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);

	String filePath7 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^PolyLine^Closed_filepath");
	String closedPolylinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);

	String filePath8 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);

	String filePath9 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String multiPointPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath9); 

	String filePath10 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath10);

	String filePath11 = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath11);

	String filePath12 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath12);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String johnDoeFilePath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnDoeFilePath);

	String cadFilepath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cadFilepath);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private String loadedTheme;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","E2E","F782","US2575","F1085"})
	public void test01_01_US1701_TC8468_TC8469_US2575_TC10439_verifyCutPasteOnSameSliceTextAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS"
				+ "<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice"
				+"<br> [Risk and Impact]: Verify that cut, paste and cancel options are working correctly.");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text1PatientName,  1);

		textAnn = new TextAnnotation(driver);
		cs = new ContentSelector(driver);

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","Verifying the text annotation is pending active");
		int x = textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1));
		int y = textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1));

		textAnn.assertTrue(textAnn.selectCutUsingContextMenu(textAnn.getLineOfTextAnnotations(1, 1)),"Checkpoint[2/8]","Verifying the Cut option is displayed and get closed after selection");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the opacity is changed on cut");
		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[4.1/8]", "verifying the text annotation x location is not changed on cut");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[4.2/8]", "verifying the text annotation y location is not changed on cut");
		List<String> results = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);
		textAnn.assertTrue(textAnn.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","Verifying the paste and cancel options are displayed");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying the state is same as pasted on same slice");

		textAnn.assertTrue(textAnn.verifyAccuracyOfValues(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x,1), "checkpoint[7.1/8]", "verifying the text annotation is pasted on same location");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[7.2/8]", "verifying the text annotation is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying there is no clone generated when copy and paste is done on same slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782","US2575","F1085"})
	public void test01_02_US1701_TC8468_TC8469_US2575_TC10439_verifyCutPasteOnSameSliceTextAnnWOAnchor() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS" + 
				"<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice"
				+"<br> [Risk and Impact]: Verify that cut, paste and cancel options are working correctly.");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text2PatientName,  1);

		textAnn = new TextAnnotation(driver);
		cs = new ContentSelector(driver);

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","Verifying the text annotation is pending active");
		int x = textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1));
		int y = textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1));

		textAnn.click(textAnn.getViewPort(1));
		textAnn.assertTrue(textAnn.selectCutUsingContextMenu(textAnn.getAllTextElementFromTextAnnotation(1, 1).get(0)),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the opacity is changed on cut");
		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[4.1/8]", "verifying the text annotation x location is not changed on cut");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[4.2/8]", "verifying the text annotation y location is not changed on cut");
		List<String> results = cs.getAllResults();

		textAnn.assertTrue(textAnn.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and cancel options are displayed and gets closed after selection");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying the state is same as pasted on same slice");

		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[7.1/8]", "verifying the text annotation is pasted on same location");
		textAnn.assertTrue(textAnn.verifyAccuracyOfValues(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y,1), "checkpoint[7.2/8]", "verifying the text annotation is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying there is no clone generated when copy and paste is done on same slice");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test01_03_US1701_TC8468_TC8469_verifyCutPasteOnSameSlicePoint() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS" + 
				"<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointPatientName,  1);

		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);

		int viewbox = 1;
		int pointNo= point.getPointWhichIsPendingAndActive(viewbox);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying point is pending and focused before cut");
		int x = point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the opacity is changed after cut");
		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0)), x, "checkpoint[4.1/8]", "verifying the point location is not changed post cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0)), y, "checkpoint[4.2/8]", "verifying the point location is not changed post cut");
		List<String> results = cs.getAllResults();

		point.assertTrue(point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and cancel options are displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying point state is same after paste on same slice");

		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0)), x, "checkpoint[7.1/8]", "verifying the point is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0)), y, "checkpoint[7.2/8]", "verifying the point is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "Verifying there is no clone generated post paste on same slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test01_04_US1701_TC8468_TC8469_verifyCutPasteOnSameSliceCircle() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS" + 
				"<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(circlePatientName,  1);

		circle = new CircleAnnotation(driver);
		cs = new ContentSelector(driver);		

		int circleNo= circle.getCircleWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying circle is focused before cut");
		int x = circle.getXCoordinate(circle.getCircle(1, circleNo).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, circleNo).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(1,circleNo),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the circle opacity is changed post cut");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, circleNo).get(0)), x, "checkpoint[4.1/8]", "verifying the circle position is not changed post cut");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, circleNo).get(0)), y, "checkpoint[4.2/8]", "verifying the circle position is not changed post cut");
		List<String> results = cs.getAllResults();

		circle.assertTrue(circle.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and cancel options are displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying the circle is focused after paste and state is same");

		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, circleNo).get(0)), x, "checkpoint[7.1/8]", "verifying the circle is pasted on same location");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, circleNo).get(0)), y, "checkpoint[7.2/8]", "verifying the circle is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test01_05_US1701_TC8468_TC8469_verifyCutPasteOnSameSliceEllipse() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS" + 
				"<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ellipsePatientName,  1);

		ellipse = new EllipseAnnotation(driver);
		cs = new ContentSelector(driver);

		int ellipseNo= ellipse.getEllipseWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);

		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the ellipse is focused before cut");
		int x = ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1));
		int y = ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1));

		ellipse.assertTrue(ellipse.selectCutUsingContextMenu(1,ellipseNo),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");		
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the ellipse opacity is changed after cut");
		ellipse.assertEquals(ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), x, "checkpoint[4.1/8]", "verifying the ellipse location is same after cut");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), y, "checkpoint[4.2/8]", "verifying the ellipse location is same after cut");
		List<String> results = cs.getAllResults();

		ellipse.assertTrue(ellipse.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and cancel options are displayed and gets closed after selection");

		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying the ellipse state is same after paste");

		ellipse.assertTrue(ellipse.verifyAccuracyOfValues(ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), x,1), "checkpoint[7.1/8]", "verifying the ellipse location is same after paste");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), y, "checkpoint[7.2/8]", "verifying the ellipse location is same after paste");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test01_06_US1701_TC8468_TC8469_verifyCutPasteOnSameSliceOpenPoly() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS"
				+ "<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(polylinePatientName,  1);


		polyline = new PolyLineAnnotation(driver);
		cs = new ContentSelector(driver);

		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying open polyline is focused");
		int x = polyline.getXCoordinate(polyline.getPolyLineSvg(1,1));
		int y = polyline.getYCoordinate(polyline.getPolyLineSvg(1,1));

		polyline.assertTrue(polyline.selectCutUsingContextMenu(polyline.getLinesOfPolyLine(1, 1).get(3)),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the open polyline opacity is changed after cut");
		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[4.1/8]", "verifying the polyline position is same after cut");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[4.2/8]", "verifying the polyline position is same after cut");
		List<String> results = cs.getAllResults();

		polyline.assertTrue(polyline.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and canel options are displayed and gets closed after selection");
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying polyline state is same after paste on same slice");

		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[7.1/8]", "verifying the polyline position is same after paste");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[7.2/8]", "verifying the polyline position is same after paste");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test01_07_US1701_TC8468_TC8469_verifyCutPasteOnSameSliceClosedPoly() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with 'Cut' option should be displayed when user right click on GSPS"
				+ "<br> Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on same slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(closedPolylinePatientName,  1);

		polyline = new PolyLineAnnotation(driver);
		cs = new ContentSelector(driver);

		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the closed polyline is focused");
		int x = polyline.getXCoordinate(polyline.getPolyLineSvg(1,1));
		int y = polyline.getYCoordinate(polyline.getPolyLineSvg(1,1));

		polyline.assertTrue(polyline.selectCutUsingContextMenu(polyline.getLinesOfPolyLine(1, 1).get(3)),"Checkpoint[2/8]","verifying the Cut option is displayed and gets closed after selection");
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/8]","verifying the opacity is changed after cut");
		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[4.1/8]", "Location is not changed post cut for closed polyline");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[4.2/8]", "Location is not changed post cut for closed polyline");
		List<String> results = cs.getAllResults();

		polyline.assertTrue(polyline.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/8]","verifying the paste and cancel options are displayed and gets closed after selection");
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying the state remain same post paste of closed polyline ");

		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[7.1/8]", "Location is not changed post paste for closed polyline");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[7.2/8]", "Location is not changed post paste for closed polyline");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","BVT","E2E","F782"})
	public void test02_01_US1701_TC8470_verifyCutPasteMachineFindingOnOtherSliceForSR() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on different slice");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm,  2);

		circle = new CircleAnnotation(driver);
		cs = new ContentSelector(driver);

		circle.closeNotification();
		List<String> results = cs.getAllResults();
		int currentSlice = circle.getCurrentScrollPositionOfViewbox(2);


		int circleNo= 1;
		//		circle.selectCircle(2, circleNo);

		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(2,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/10]","verifying the Annotation is focused - circle SR data");
		int x = circle.getXCoordinate(circle.getCircle(2, circleNo).get(0));
		int y = circle.getYCoordinate(circle.getCircle(2, circleNo).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(2,circleNo),"Checkpoint[2/10]","verifying the Cut option is displayed and gets closed after selection");

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(2,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/10]","verifying the opacity is changed on cut - circle");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(2, circleNo).get(0)), x, "checkpoint[4.1/10]", "verifying the location is same post cut - circle");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(2, circleNo).get(0)), y, "checkpoint[4.2/10]", "verifying the location is same post cut - circle");

		circle.scrollDownToSliceUsingKeyboard(2);

		circle.assertTrue(circle.selectPasteOrCancelUsingContextMenu(2,ViewerPageConstants.PASTE),"Checkpoint[5/10]","verifying the paste and cancel options are displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(2,circleNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/10]","verifying the state is changed to accepted after paste on different slice - circle");

		circle.assertEquals(circle.getXCoordinate(circle.getCircle(2, circleNo).get(0)), x, "checkpoint[7.1/10]", "verifying the location is same post paste on different slice");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(2, circleNo).get(0)), y, "checkpoint[7.2/10]", "verifying the location is same post paste on different slice");


		circle.scrollUpToSliceUsingKeyboard(2);		
		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(2), currentSlice, "Checkpoint[8/10]", "verifying the slice is same after scroll");
		circle.assertTrue(circle.getAllCircles(2).isEmpty(), "Checkpoint[9/10]", "verifying the annotation is not present on earlier slice");

		cs.assertEquals(cs.getAllResults().size(), results.size()+1, "Checkpoint[10/10]", "verifyine the clone is created after pasting on different slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","E2E","F782"})
	public void test02_02_US1701_TC8470_verifyCutPasteMachineFindingOnOtherSliceForPoint() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options Paste and Cancel should be displayed on right click after cut operation is performed on different slice");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName,  1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int currentSlice = point.getCurrentScrollPositionOfViewbox(1);
		point.selectPoint(1, 1);

		int pointNo= 1;

		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/10]","verifying the point is pending focused");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/10]","verifying the Cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/10]","verifying the opacity is changed after cut");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4.1/10]", "verifying the annotation is at same location post cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[4.2/10]", "verifying the annotation is at same location post cut");
		point.scrollDownToSliceUsingKeyboard(1);

		point.assertTrue(point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5/10]","verifying the paste and cancel options are displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/10]","verifying the annotation is pasted on another slice is state is changed");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[7.1/10]", "verifying the annotation is at same location post paste");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[7.2/10]", "verifying the annotation is at same location post paste");

		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[8/10]", "Going back to same slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[9/10]", "verifying the pasted annotation is not present in ealier slice");

		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[10/10]", "verifying the clone is generated");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test03_US1701_TC8471_verifyRadialMenuDisplayed() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click in second viewbox when cut operation is performed in first viewbox.");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName,  1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();

		int currentSlice = point.getCurrentScrollPositionOfViewbox(1);
		point.selectPoint(1, 1);

		int pointNo= 1;
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/7]","Verifying the finding is focused ");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/7]","Verifying the cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/7]","verifying the finding is cut");
		point.scrollDownToSliceUsingKeyboard(1);

		point.assertFalse(point.selectCutUsingContextMenu(point.getViewPort(2)),"Checkpoint[4.1/7]","Verifying the cut option is not displayed");
		point.assertTrue(point.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[4.2/7]", "The Radial menu is visble on ViewBox2");

		point.assertTrue(point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[5.1/7]","verifying the paste and cancel options are displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5.2/7]","verifying the cut is not cancelled after performing the DICOM operation");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[6.1/7]", "verifying the finding is pasted on same location as on previous slice");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[6.2/7]", "verifying the finding is pasted on same location as on previous slice");

		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[7.1/7]", "verifying the previous slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[7.2/7]", "verifying the finding is not present after DICOM operations and cut");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test04_US1701_TC8472_verifyCutPasteInEmptyViewbox() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector should be displayed on click in empty viewbox when cut operation is performed in another viewbox.");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,  1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		point.doubleClickOnViewbox(1);
		cs = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);
		int oneUpx = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int oneUpy = point.getYCoordinate(point.getPoint(1, 1).get(0));

		List<String> results = cs.getAllResults();

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		int x = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int y = point.getYCoordinate(point.getPoint(1, 1).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, 1).get(0)),"Checkpoint[1/9]","Verifying the cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/9]","Verifying the opacity after accepted finding being cut");

		//	point.performMouseRightClick(point.getViewPort(7));
		//	point.assertTrue(cs.isContentSelectorPresent(), "Checkpoint[3/9]", "Verifying the content selector is opened upon right click in empty viewbox");

		point.mouseHover(point.getViewPort(1));
		point.scrollDownToSliceUsingKeyboard(4);
		point.assertTrue(point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[4/9]","verifying the paste and cancel options are displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/9]","verifying the finding being paste after layout change");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), x, "checkpoint[6.1/9]", "verifying the finding location is same after paste on layout change");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), y, "checkpoint[6.2/9]", "verifying the finding location is same after paste on layout change");
		cs.assertEquals(cs.getAllResults().size(), results.size(), "Checkpoint[7/9]", "verifying the clone is not getting generated as cut and paste is happening in same session");

		point.doubleClickOnViewbox(1);
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), oneUpx, "checkpoint[8.1/9]", "verifying the finding location in oneup scenario");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), oneUpy, "checkpoint[8.2/9]", "verifying the finding location in oneup scenario");

		point.scrollUpToSliceUsingKeyboard(4);
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[9/9]", "verifying the finding is not present in previous slice");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test05_US1701_TC8473_verifyCutPasteOnEsc() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click when user pressed ESC key after cut operation");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,  1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -20, -20, -100,-100);	
		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the finding is accpted and focused after selection");
		int x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(1,1),"Checkpoint[2.1/8]","verifying the cut option is displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2.2/8]","verifying the opacity is changed post cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		circle.pressESCKey();			
		circle.openQuickToolbar(circle.getViewPort(1));
		circle.assertTrue(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[3/8]", "The Radial menu is visble on ViewBox1");

		circle.assertFalse(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/8]","verifying that finding is focused again when ESC is pressed and shows the cut is cancelled");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[5/8]", "verifying the finding is present after cancel of cut operation");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[6.1/8]", "Verifying the finding location is same");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[6.2/8]", "Verifying the finding location is same");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[7/8]", "Verifying there is no clone created");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[8/8]", "verifying that finding is present on same slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","E2E","F782"})
	public void test06_US1701_TC8474_verifyCutCancelRadialMenu() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click when user selected Cancel option after cut operation");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName,  1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/9]","Verifying the finding is focused");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/9]","verifying the cut option is displayed and gets closed on selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/9]","verifying the opacity is changed when cut is done using contrl + x");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);

		point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.CANCEL);
		point.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint[4/9]","Verifying on cancel the finding is absent ");				
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[5/9]", "Verifying no clone is generated");

		point.mouseHover(point.getViewPort(2));
		point.mouseHover(point.getViewPort(1));
		point.performMouseRightClick(point.getViewPort(1));
		point.assertTrue(point.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[6/9]", "The Radial menu is visble on ViewBox1 on right click after cancel operation");

		point.scrollUpToSliceUsingKeyboard(2);
		point.assertTrue(point.verifyPointAnnotationIsCurrentInActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/9]","verifying the finding is present on previous slice");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[8/9]", "verifying the finding is present on same location");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[9/9]", "verifying the finding is present on same location");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test07_US1701_TC8475_verifyCutPasteAndreload() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click when user selected paste option after cut operation");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/11]","Verifying the finding is focused");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/11]","verifying the cut option is displayed and closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/11]","verifying the opacity is changed when cut is done using contrl + x");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);

		point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/11]","verifying the state is changed when paste is done using context menu");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[5.1/11]", "verifying the finding location is same");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[5.2/11]", "verifying the finding location is same");
		int slice = point.getCurrentScrollPositionOfViewbox(1);

		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[6/11]", "verifying the clone is generated");
		point.scrollUpToSliceUsingKeyboard(2);
		point.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint[7/11]","verifying finding is not present on previous slice");

		helper.browserBackAndReloadViewer(multiPointPatientName,  1, 1);

		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[8/11]","verifying the finding is displayed on reload");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[9/11]", "verifying the finding location is same");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[10/11]", "verifying the finding location is same");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1),slice, "checkpoint[11/11]", "verifying the updated slice is displayed on reload where paste was done");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test08_US1701_TC8477_verifyCutOnBack() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click when user navigate back to study page and reload viewer after cut operation");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50,-80,-80);
		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/10]","Verifying that finding is selected and focused");
		int x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(1,1),"Checkpoint[2/10]","verifying the cut is displayed on right click");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/10]","verifying the opacity is changed after cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4.1/10]","Verifying that cut operationis cancelled after viewer reload and finding is active and focused");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[4.2/10]", "verifying finding is present");
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[5/10]", "verifying no clone is generated after reload");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[6/10]", "verifying the same slice is being loaded");

		circle.openQuickToolbar(circle.getViewPort(1));
		circle.assertTrue(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[7.1/10]", "The Radial menu is visble on right click on viewer reload");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();
		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[8.1/10]", "Verifying the paste after reload is doing nothing no finding is pasted");
		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8.2/10]", "Verifying on going back to previous slice the finding is still present");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[9/10]", "Verifying the location is not changed after layout change");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[10/10]", "Verifying the location is not changed after layout change");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test09_US1701_TC8476_verifyCutOnLogout() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click when user logs out of Northstar application and reload viewer after cut operation");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50,-80,-80);
		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/10]","Verifying that finding is selected and focused");
		int x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(1,1),"Checkpoint[2/10]","Verifying the cut option is displayed");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/10]","verifying the opacity is changed after cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		header = new Header(driver); 
		header.logout();

		loginPage.login(username, password);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4.1/10]","Verifying that cut operationis cancelled after logout and viewer reload and finding is active and focused");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[4.2/10]", "verifying finding is present");
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[5/10]", "verifying no clone is generated after logout and reload");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[6/10]", "verifying the same slice is being loaded after logout and viewer reload");

		circle.openQuickToolbar(circle.getViewPort(1));
		circle.assertTrue(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[7/10]", "The Radial menu is visble on on right click after logout and reload of viewer");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();
		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[8.1/10]", "Verifying the paste after reload is doing nothing no finding is pasted");
		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8.2/10]", "Verifying on going back to previous slice the finding is still present");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[9/10]", "Verifying the location is not changed after layout change");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[10/10]", "Verifying the location is not changed after layout change");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test10_US1701_TC8478_verifyCutPasteUpdationToNewFindingIsCut() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial menu should be displayed on right click on another viewbox containing same series from where annotation is being cut");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,  1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 60, -100,-10);

		ellipse  = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -50, -50, -20, -30);

		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/14]","Verifying the Circle is selected and focused");
		int circleX = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int circleY = circle.getYCoordinate(circle.getCircle(1, 1).get(0));
		int ellipseX = ellipse.getXCoordinate(ellipse.getAllEllipses(1).get(0));
		int ellipseY = ellipse.getYCoordinate(ellipse.getAllEllipses(1).get(0));

		circle.assertTrue(circle.selectCutUsingContextMenu(1,1),"Checkpoint[2.1/14]","verifying the cut option is displayed when right click on first finding");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2.2/14]","Verifying the Circle opacity is changed as it is being cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		ellipse.selectEllipse(1, 1);
		ellipse.assertTrue(ellipse.selectCutUsingContextMenu(1,1),"Checkpoint[3.1/14]","verifying the cut option is displayed when right click on second finding");
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3.2/14]","Verifying ellipse is cut and opacity is changed");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3.3/14]","Verifying that circle opacity is changed to normal as another finding is cut");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.assertTrue(circle.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[4/14]","Verifying the paste option is displayed on right click");

		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[5/14]", "After paste on another slice circle is not pasted");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1, "Checkpoint[6/14]", "Verifying ellipse is pasted");
		ellipse.assertEquals(ellipse.getXCoordinate(ellipse.getAllEllipses(1).get(0)), ellipseX, "checkpoint[7/14]", "Verifying that ellipse is pasted on same location");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getAllEllipses(1).get(0)), ellipseY, "checkpoint[8/14]", "Verifying that ellipse is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[9/14]", "Verifying that there is no clone generated as cut and paste in same session");

		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[10/14]", "Verifying circle is present on previous slice");
		ellipse.assertTrue(ellipse.getAllEllipses(1).isEmpty(), "Checkpoint[11/14]", "Verifying ellipse is not present as it is pasted on another slice");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), circleX, "checkpoint[12/14]", "Verifying the location of circle is same");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), circleY, "checkpoint[13/14]", "Verifying the location of circle is same");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[14/14]", "Verifying the previous slice");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Positive","F782"})
	public void test11_US1701_TC8479_verifySameSeriesIsLoadedAndRadialMenuDisplayed() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with option Cut should be displayed on right click on second annotation after performing cut operation on first annotation");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName,  1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		List<String> clones = cs.getAllResults();

		int badgeCount = point.getBadgeCountFromToolbar(1);
		for(int i=1;i<=2;i++) {
			cs.selectResultFromSeriesTab(2, clones.get(0), i);
			if(point.getBadgeCountFromToolbar(2)==badgeCount)
				break;
		}

		point.selectPoint(1, 1);

		int pointNo= 1;
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/5]","Verifying that point is pending and focuse");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0)),"Checkpoint[2/5]","verifying that cut option is displayed");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/5]","Verifying opacity is changed after cut operation");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4.1/5]", "verifying location is not changed after cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[4.2/5]", "verifying location is not changed after cut");

		point.performMouseRightClick(point.getViewPort(2));
		point.assertTrue(point.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[5/5]", "The Radial menu is visble on another viewbox when same copy is loaded");




	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","E2E","F782"})
	public void test12_01_US1701_TC8498_verifyNoContextMenuForRT() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options 'Cut' / 'Paste' / 'Cancel'  should not be visible on SC / RT struct / CAD / PMAP / non-DICOM data");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA,  1);

		DICOMRT drt = new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();

		polyline = new PolyLineAnnotation(driver);	
		polyline.assertFalse(polyline.selectCutUsingContextMenu(polyline.getLinesOfPolyLine(1, 2).get(3)),"Checkpoint[1/3]","verifying that no context menu displayed on right click");
		polyline.assertTrue(polyline.verifyPolylineIsInActiveGSPS(1, 2, drt.getColorOfSegment(2).replace(", ", ","), ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/3]","verifying the contour gets deselected");
		polyline.assertFalse(polyline.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[3/3]", "The Radial menu is visble");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","E2E","F782"})
	public void test12_02_US1701_TC8498_verifyNoContextMenuForPMAP() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options 'Cut' / 'Paste' / 'Cancel'  should not be visible on SC / RT struct / CAD / PMAP / non-DICOM data");


		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientName,  1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.assertFalse(circle.selectCutUsingContextMenu(circle.getViewPort(1)),"Checkpoint[1/2]","verifying that no context menu displayed on right click");
		circle.assertTrue(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[2/2]", "The Radial menu is visble on right click");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","E2E","F782"})
	public void test12_03_US1701_TC8498_verifyNoContextMenuForSC() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options 'Cut' / 'Paste' / 'Cancel'  should not be visible on SC / RT struct / CAD / PMAP / non-DICOM data");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbioPatientName,  1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.assertFalse(circle.selectCutUsingContextMenu(circle.getViewPort(1)),"Checkpoint[1/2]","verifying that no context menu displayed on right click");
		circle.assertTrue(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[2/2]", "The Radial menu is visble on right click");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","F782"})
	public void test12_04_US1701_TC8498_verifyNoContextMenuForNONDICOM() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options 'Cut' / 'Paste' / 'Cancel'  should not be visible on SC / RT struct / CAD / PMAP / non-DICOM data");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(johnDoePatientName,  1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad(4);

		for(int i =1;i<=2;i++) {
			circle.assertFalse(circle.selectCutUsingContextMenu(circle.getViewPort(i)),"Checkpoint[1."+i+"/4]","verifying that no context menu displayed on right click");
			circle.assertTrue(circle.verifyCompactQuickToolbar(), "Checkpoint[2."+i+"/4]", "The Radial menu is visble on right click for Image");
		}
		circle.assertFalse(circle.selectCutUsingContextMenu(circle.getViewPort(3)),"Checkpoint[3/4]","verifying that no context menu displayed on right click");
		circle.assertFalse(circle.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[4/4]", "The Radial menu is visble on right click for pdf");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1701","Negative","E2E","F782"})
	public void test12_05_US1701_TC8498_verifyNoContextMenuForCAD() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify context menu with options 'Cut' / 'Paste' / 'Cancel'  should not be visible on SC / RT struct / CAD / PMAP / non-DICOM data");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName,  1);

		polyline = new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();

		cs = new ContentSelector(driver);
		cs.selectResultFromSeriesTab(1, cs.getAllResults().get(0),1);			
		polyline.assertFalse(polyline.selectCutUsingContextMenu(polyline.getLinesOfPolyLine(1, 1).get(0)), "Checkpoint[1]", "verifying the context menu is not displayed on right click");
		polyline.assertTrue(polyline.verifyPolylineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/2]", "verifying polyline is de selected");

	}

	@Test(groups ={"Chrome","Edge","IE11","DR2166","Negative","F249"})
	public void test13_DR2166_TC8660_verifyOptCancelWhenSeriesLoadedCS() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cutting a measurement if content was changed in the viewbox, cut operation should be cancelled.");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		String series = cs.getSeriesDescriptionOverlayText(3);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, 1).get(0)),"Checkpoint[1/8]","Verifying the cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","Verifying the opacity after accepted finding being cut");

		cs.selectSeriesFromSeriesTab(1, series);
		cs.openAndCloseSeriesTab(false);

		point.performMouseRightClick(point.getViewPort(1));
		point.assertTrue(point.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[3/8]", "The Radial menu is visble on ViewBox1 on right click post loading of another source");
		point.performCNTRLV();
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[4/8]", "Verifying the paste after reload is doing nothing no finding is pasted");

		String clone = cs.getAllResults().get(0);

		cs.selectResultFromSeriesTab(1, clone);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the finding is displayed on result loaded using content selector");

		helper.browserBackAndReloadViewer(circlePatientName,  1, 1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();
		int circleNo= circle.getCircleWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);		
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying circle is focused before cut");
		circle.assertTrue(circle.selectCutUsingContextMenu(1,circleNo),"Checkpoint[7/8]","verifying the Cut option is displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[8/8]","verifying the circle opacity is changed post cut");


	}

	@Test(groups ={"Chrome","Edge","IE11","DR2166","Negative","F782"})
	public void test14_DR2166_TC8660_verifyOptCancelWhenSeriesLoadedCS() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cutting a measurement if content was changed in the viewbox, cut operation should be cancelled.");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		String series = cs.getSeriesDescriptionOverlayText(3);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);

		point.assertTrue(point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, 1).get(0)),"Checkpoint[1/8]","Verifying the cut option is displayed and gets closed after selection");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","Verifying the opacity after accepted finding being cut");

		cs.selectSeriesFromSeriesTab(1, series);
		cs.openAndCloseSeriesTab(false);

		point.performMouseRightClick(point.getViewPort(1));
		point.assertTrue(point.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[3/8]", "The Radial menu is visble on ViewBox1 on right click post loading of another source");
		point.performCNTRLV();
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[4/8]", "Verifying the paste after reload is doing nothing no finding is pasted");

		String clone = cs.getAllResults().get(0);

		cs.selectResultFromSeriesTab(1, clone);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the finding is displayed on result loaded using content selector");

		helper.browserBackAndReloadViewer(circlePatientName,  1, 1);

		circle = new CircleAnnotation(driver);
		int circleNo= circle.getCircleWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);		
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/8]","verifying circle is focused before cut");
		circle.assertTrue(circle.selectCutUsingContextMenu(1,circleNo),"Checkpoint[7/8]","verifying the Cut option is displayed and gets closed after selection");
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[8/8]","verifying the circle opacity is changed post cut");


	}
	@Test(groups ={"Chrome","Edge","IE11","US2575","Positive","F1085"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test15_US2575_TC10413_TC10414_verifyCopyCutPasteCancelTextBackgrounds(String theme) throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the cut, copy, paste and cancel menu item texts and text backgrounds in Eureka theme."+
				"<br> Verify the cut, copy, paste and cancel menu item backgrounds in Dark theme.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			Header header = new Header(driver);
			header.logout();

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text1PatientName,  1);

		textAnn = new TextAnnotation(driver);
		viewerARToolbox=new ViewerARToolbox(driver);
		PagesTheme pageTheme = new PagesTheme(driver);

		textAnn.performMouseRightClick(textAnn.getLineOfTextAnnotations(1, 1));
		viewerARToolbox.mouseHover(viewerARToolbox.getCutOption());

		viewerARToolbox.assertEquals(textAnn.getCssValue(viewerARToolbox.getCutOption(), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_ON_MOUSE_HOVERED,"Checkpoint[1/9]","Verifying the opacity of cut text");
		viewerARToolbox.assertTrue(pageTheme.verifyThemeOnLabel(viewerARToolbox.getCutOption(), loadedTheme),"Checkpoint[2/9]","verifying that cut text background is displayed on mouse hover as per theme");

		viewerARToolbox.click(viewerARToolbox.getCutOption());

		viewerARToolbox.performMouseRightClick(viewerARToolbox.getViewPort(1));
		viewerARToolbox.mouseHover(viewerARToolbox.getPasteOption());

		viewerARToolbox.assertEquals(viewerARToolbox.getCssValue(viewerARToolbox.getPasteOption(), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_ON_MOUSE_HOVERED,"Checkpoint[3/9]","Verifying the opacity of paste text");
		viewerARToolbox.assertTrue(pageTheme.verifyThemeOnLabel(viewerARToolbox.getPasteOption(), loadedTheme),"Checkpoint[4/9]","verifying that paste text background is displayed on mouse hover as per theme");

		viewerARToolbox.mouseHover(viewerARToolbox.getCancelOption());

		viewerARToolbox.assertEquals(viewerARToolbox.getCssValue(viewerARToolbox.getCancelOption(), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_ON_MOUSE_HOVERED,"Checkpoint[5/9]","Verifying the opacity of cancel text");
		viewerARToolbox.assertTrue(pageTheme.verifyThemeOnLabel(viewerARToolbox.getCancelOption(), loadedTheme),"Checkpoint[6/9]","verifying that cancel text background is displayed on mouse hover as per theme");


		viewerARToolbox.assertTrue(viewerARToolbox.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE),"Checkpoint[7/9]","Verifying that paste option is clickable");

		helper.browserBackAndReloadViewer(ChestCT1p25mm,  1, 2);

		viewerARToolbox.performMouseRightClick(viewerARToolbox.getViewPort(1));
		viewerARToolbox.mouseHover(viewerARToolbox.getCutOption());

		viewerARToolbox.assertEquals(viewerARToolbox.getCssValue(viewerARToolbox.getCutOption(), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_ON_MOUSE_HOVERED,"Checkpoint[8/9]","Verifying the opacity of copy text");
		viewerARToolbox.assertTrue(pageTheme.verifyThemeOnLabel(viewerARToolbox.getCutOption(), loadedTheme),"Checkpoint[9/9]","verifying that copy text background is displayed on mouse hover as per theme");
	}


	@Test(groups ={"Chrome","Edge","IE11","US2575","Positive","F1085"})
	public void test16_US2575_TC10438_verifyCopyCutPasteCancelTooltips() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that tooltips are not displayed on hovering the cut, copy, paste, cancel menu items.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text1PatientName,  1);

		textAnn = new TextAnnotation(driver);
		viewerARToolbox=new ViewerARToolbox(driver);

		textAnn.performMouseRightClick(textAnn.getLineOfTextAnnotations(1, 1));
		viewerARToolbox.mouseHover(viewerARToolbox.getCutOption());

		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(viewerARToolbox.tooltip), "Checkpoint[1/4]", "Verfiying that no tooltip is displayed on cut text");

		viewerARToolbox.click(viewerARToolbox.getCutOption());

		viewerARToolbox.performMouseRightClick(viewerARToolbox.getViewPort(1));
		viewerARToolbox.mouseHover(viewerARToolbox.getPasteOption());

		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(viewerARToolbox.tooltip), "Checkpoint[2/4]", "Verfiying that no tooltip is displayed on paste text");

		viewerARToolbox.mouseHover(viewerARToolbox.getCancelOption());

		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(viewerARToolbox.tooltip), "Checkpoint[3/4]", "Verfiying that no tooltip is displayed on cancel text");

		helper.browserBackAndReloadViewer(ChestCT1p25mm,  1, 2);

		viewerARToolbox.performMouseRightClick(viewerARToolbox.getViewPort(1));
		viewerARToolbox.mouseHover(viewerARToolbox.getCutOption());

		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(viewerARToolbox.tooltip), "Checkpoint[4/4]", "Verfiying that no tooltip is displayed on copy text");

	}
	@Test(groups ={"Chrome","Edge","IE11","US2575","Positive","F1085"})
	public void test17_US2575_TC10440_verifyPasteCancelAlignment() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that paste and cancel menu items are left aligned.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text1PatientName,  1);

		textAnn = new TextAnnotation(driver);
		viewerARToolbox=new ViewerARToolbox(driver);
		
		textAnn.performMouseRightClick(textAnn.getLineOfTextAnnotations(1, 1));
		viewerARToolbox.mouseHover(viewerARToolbox.getCutOption());

		viewerARToolbox.click(viewerARToolbox.getCutOption());

		viewerARToolbox.performMouseRightClick(viewerARToolbox.getViewPort(1));
		viewerARToolbox.mouseHover(viewerARToolbox.getPasteOption());
		viewerARToolbox.assertTrue((viewerARToolbox.getCssValue(viewerARToolbox.getPasteOption(), NSGenericConstants.TEXT_ALIGN)).contains(ViewerPageConstants.VIEWBOX_LEFT_ALIGNMENT),"Checkpoint[1/2]","Verifying the alignment of paste text");
		viewerARToolbox.assertTrue((viewerARToolbox.getCssValue(viewerARToolbox.getCancelOption(), NSGenericConstants.TEXT_ALIGN)).contains(ViewerPageConstants.VIEWBOX_LEFT_ALIGNMENT),"Checkpoint[2/2]","Verifying the alignment of cancel text");

	}
	@Test(groups ={"Chrome","Edge","IE11","DE2750","Positive","F1125"})
	public void test18_DE2750_TC8470_verifyCopiedToClipboardOnKeyboardShortcut() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Copied to clipboard pop-up is not displayed on view box with SR series when CTRL+C is performed.");
	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm,  2);

		panel =new OutputPanel(driver);
		viewerARToolbox=new ViewerARToolbox(driver);

		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(panel.outputPanelSection), "Checkpoint[1/2]", "Verfiying that output panel is closed.");

		viewerARToolbox.mouseHover(viewerARToolbox.getViewPort(1));
		viewerARToolbox.performCNTRLC();
		
		viewerARToolbox.assertFalse(viewerARToolbox.isElementPresent(viewerARToolbox.copiedToClipboard), "Checkpoint[2/2]", "Verfiying that copied to clipboard text is not displayed at view box-1 when user performs a ctrl c");

	}

}


