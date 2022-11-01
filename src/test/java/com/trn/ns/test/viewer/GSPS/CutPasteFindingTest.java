package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
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
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CutPasteFindingTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private ContentSelector cs;
	private CircleAnnotation circle; 
	private TextAnnotation textAnn;
	private EllipseAnnotation ellipse;
	private PolyLineAnnotation polyline;
	private MeasurementWithUnit distance;
	private SimpleLine line;
	private Header header;
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
	
	String filePath11 = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String adcPilipsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath11);

	String filePath12 =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String lidcPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath12);

	String filePath13 =Configurations.TEST_PROPERTIES.get("BrainPerfusion_EAI_Filepath");
	String brainPerfusionPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath13);
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Positive","BVT","E2E","F782"})
	public void test01_01_US1613_TC8433_verifyCutPasteOnSameSliceTextAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text1PatientName, 1);
	
		textAnn = new TextAnnotation(driver);
		textAnn.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","Verifying the text annotation is pending active");
		int x = textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1));
		int y = textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1));

		textAnn.performCNTRLX();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the opacity is changed on cut");
		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[3/8]", "verifying the text annotation x location is not changed on cut");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[4/8]", "verifying the text annotation y location is not changed on cut");
		List<String> results = cs.getAllResults();

		textAnn.performCNTRLV();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the state is same as pasted on same slice");

		textAnn.assertTrue(textAnn.verifyAccuracyOfValues(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x,1), "checkpoint[6/8]", "verifying the text annotation is pasted on same location");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[7/8]", "verifying the text annotation is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying there is no clone generated when copy and paste is done on same slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Positive","F782"})
	public void test01_02_US1613_TC8433_verifyCutPasteOnSameSliceTextAnnWOAnchor() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(text2PatientName, 1);
	

		textAnn = new TextAnnotation(driver);
		textAnn.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","Verifying the text annotation is pending active");
		int x = textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1));
		int y = textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1));

		textAnn.performCNTRLX();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the opacity is changed on cut");
		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[3/8]", "verifying the text annotation x location is not changed on cut");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[4/8]", "verifying the text annotation y location is not changed on cut");
		List<String> results = cs.getAllResults();

		textAnn.performCNTRLV();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,false,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the state is same as pasted on same slice");

		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[6/8]", "verifying the text annotation is pasted on same location");
		textAnn.assertTrue(textAnn.verifyAccuracyOfValues(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y,1), "checkpoint[7/8]", "verifying the text annotation is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying there is no clone generated when copy and paste is done on same slice");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test01_03_US1613_TC8433_verifyCutPasteOnSameSlicePoint() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointPatientName, 1);
	

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		int viewbox = 1;
		int pointNo= point.getPointWhichIsPendingAndActive(viewbox);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying point is pending and focused before cut");
		int x = point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the opacity is changed after cut");
		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0)), x, "checkpoint[3/8]", "verifying the point location is not changed post cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0)), y, "checkpoint[4/8]", "verifying the point location is not changed post cut");
		List<String> results = cs.getAllResults();

		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying point state is same after paste on same slice");

		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, pointNo).get(0)), x, "checkpoint[6/8]", "verifying the point is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, pointNo).get(0)), y, "checkpoint[7/8]", "verifying the point is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "Verifying there is no clone generated post paste on same slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test01_04_US1613_TC8433_verifyCutPasteOnSameSliceCircle() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(circlePatientName, 1);
	

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		int circleNo= circle.getCircleWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);
		
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying circle is focused before cut");
		int x = circle.getXCoordinate(circle.getCircle(1, circleNo).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, circleNo).get(0));

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the circle opacity is changed post cut");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, circleNo).get(0)), x, "checkpoint[3/8]", "verifying the circle position is not changed post cut");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, circleNo).get(0)), y, "checkpoint[4/8]", "verifying the circle position is not changed post cut");
		List<String> results = cs.getAllResults();

		circle.performCNTRLV();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the circle is focused after paste and state is same");

		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, circleNo).get(0)), x, "checkpoint[6/8]", "verifying the circle is pasted on same location");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, circleNo).get(0)), y, "checkpoint[7/8]", "verifying the circle is pasted on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test01_05_US1613_TC8433_verifyCutPasteOnSameSliceEllipse() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ellipsePatientName, 1);
	

		ellipse = new EllipseAnnotation(driver);
		ellipse.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		int ellipseNo= ellipse.getEllipseWhichIsActive(1, ViewerPageConstants.PENDING_COLOR);
		
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the ellipse is focused before cut");
		int x = ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1));
		int y = ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1));

		ellipse.performCNTRLX();
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the ellipse opacity is changed after cut");
		ellipse.assertEquals(ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), x, "checkpoint[3/8]", "verifying the ellipse location is same after cut");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), y, "checkpoint[4/8]", "verifying the ellipse location is same after cut");
		List<String> results = cs.getAllResults();

		ellipse.performCNTRLV();
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,ellipseNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the ellipse state is same after paste");

		ellipse.assertTrue(ellipse.verifyAccuracyOfValues(ellipse.getXCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), x,1), "checkpoint[6/8]", "verifying the ellipse location is same after paste");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getEllipses(1).get(ellipseNo-1)), y, "checkpoint[7/8]", "verifying the ellipse location is same after paste");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test01_06_US1613_TC8433_verifyCutPasteOnSameSliceOpenPoly() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(polylinePatientName, 1);
	

		polyline = new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying open polyline is focused");
		int x = polyline.getXCoordinate(polyline.getPolyLineSvg(1,1));
		int y = polyline.getYCoordinate(polyline.getPolyLineSvg(1,1));

		polyline.performCNTRLX();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the open polyline opacity is changed after cut");
		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[3/8]", "verifying the polyline position is same after cut");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[4/8]", "verifying the polyline position is same after cut");
		List<String> results = cs.getAllResults();

		polyline.performCNTRLV();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying polyline state is same after paste on same slice");

		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[6/8]", "verifying the polyline position is same after paste");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[7/8]", "verifying the polyline position is same after paste");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test01_07_US1613_TC8433_verifyCutPasteOnSameSliceClosedPoly() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on same slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(closedPolylinePatientName, 1);
	

		polyline = new PolyLineAnnotation(driver);
		polyline.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the closed polyline is focused");
		int x = polyline.getXCoordinate(polyline.getPolyLineSvg(1,1));
		int y = polyline.getYCoordinate(polyline.getPolyLineSvg(1,1));

		polyline.performCNTRLX();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the opacity is changed after cut");
		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[3/8]", "Location is not changed post cut for closed polyline");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[4/8]", "Location is not changed post cut for closed polyline");
		List<String> results = cs.getAllResults();

		polyline.performCNTRLV();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/8]","verifying the state remain same post paste of closed polyline ");

		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[6/8]", "Location is not changed post paste for closed polyline");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[7/8]", "Location is not changed post paste for closed polyline");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[8/8]", "verifying that no clone is created post paste onsame slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive","BVT","E2E","F782"})
	public void test02_01_US1613_TC8437_verifyCutPasteMachineFindingOnOtherSliceForSR() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on differenent slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 1);
	

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad(2);
		cs = new ContentSelector(driver);
		cs.closeNotification();
		List<String> results = cs.getAllResults();
		int currentSlice = circle.getCurrentScrollPositionOfViewbox(2);

		int circleNo= 1;
		circle.selectCircle(2, circleNo);
		
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(2,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/10]","verifying the Annotation is focused - circle SR data");
		int x = circle.getXCoordinate(circle.getCircle(2, circleNo).get(0));
		int y = circle.getYCoordinate(circle.getCircle(2, circleNo).get(0));

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(2,circleNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/10]","verifying the opacity is changed on cut - circle");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(2, circleNo).get(0)), x, "checkpoint[3/10]", "verifying the location is same post cut - circle");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(2, circleNo).get(0)), y, "checkpoint[4/10]", "verifying the location is same post cut - circle");

		circle.scrollDownToSliceUsingKeyboard(2);

		circle.performCNTRLV();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(2,circleNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/10]","verifying the state is changed to accepted after paste on different slice - circle");

		circle.assertEquals(circle.getXCoordinate(circle.getCircle(2, circleNo).get(0)), x, "checkpoint[6/10]", "verifying the location is same post paste on different slice");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(2, circleNo).get(0)), y, "checkpoint[7/10]", "verifying the location is same post paste on different slice");

		cs.assertEquals(cs.getAllResults().size(), results.size()+1, "Checkpoint[8/10]", "verifyine the clone is created after pasting on different slice");

		circle.scrollUpToSliceUsingKeyboard(2);		
		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(2), currentSlice, "Checkpoint[9/10]", "verifying the slice is same after scroll");
		circle.assertTrue(circle.getAllCircles(2).isEmpty(), "Checkpoint[10/10]", "verifying the annotation is not present on earlier slice");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive","E2E","F782"})
	public void test02_02_US1613_TC8437_verifyCutPasteMachineFindingOnOtherSliceForPoint() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on machine drawn annotations on differenent slice");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName, 1);	

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

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/10]","verifying the opacity is changed after cut");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[3/10]", "verifying the annotation is at same location post cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[4/10]", "verifying the annotation is at same location post cut");
		point.scrollDownToSliceUsingKeyboard(1);

		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/10]","verifying the annotation is pasted on another slice is state is changed");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[6/10]", "verifying the annotation is at same location post paste");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[7/10]", "verifying the annotation is at same location post paste");

		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[8/10]", "Going back to same slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[9/10]", "verifying the pasted annotation is not present in ealier slice");

		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[10/10]", "verifying the clone is generated");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test03_US1613_TC8439_TC8449_verifyCutPasteUserFindingsOnSameAndDiffSession() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste options on user drawn annotations in same + different sessions"
				+ "<br> Verify cut + paste options will update annotation state to accepted from rejected or pending");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		point = new PointAnnotation(driver);
	
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
		point.selectAcceptfromGSPSRadialMenu();

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 300, 150,-80,-80);
		circle.selectAcceptfromGSPSRadialMenu();

		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);

		polyline = new PolyLineAnnotation(driver);
		polyline.selectPolylineFromQuickToolbar(1);
		int[] coordinates = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};
		polyline.drawFreehandPolyLine(1, coordinates);
		polyline.selectRejectfromGSPSRadialMenu();

		textAnn = new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -200, 50, "sample");
		textAnn.selectRejectfromGSPSRadialMenu();

		distance = new MeasurementWithUnit(driver);
		distance.selectDistanceFromQuickToolbar(1);
		distance.drawLine(1, 60, 60, 50, 50);
		distance.selectRejectfromGSPSRadialMenu();

		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, 250,-50, 60,0); 

		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		point.selectPoint(1, 1);
		int x = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int y = point.getYCoordinate(point.getPoint(1, 1).get(0));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing cntrl +x and contrl+v for multiple annotation in same session");
		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[1/37]","verifying the point opacity is changed on cut");

		point.scrollDownToSliceUsingKeyboard(4);

		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/37]","verifying the annotation state is tured to green after paste");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), x, "checkpoint[3/37]", "verifying the location is not changed for point after paste");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), y, "checkpoint[4/37]", "verifying the location is not changed for point after paste");
		cs.assertEquals(cs.getAllResults().size(), results.size(), "Checkpoint[5/37]", "No clone is generated after paste");

		point.scrollUpToSliceUsingKeyboard(4);
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[6/37]", "going back to previous slice and checking point is absent");

		distance.selectLinearMeasurement(1, 1);
		x = distance.getXCoordinate(distance.getLinearMeasurements(1, 1).get(0));
		y = distance.getYCoordinate(distance.getLinearMeasurements(1, 1).get(0));

		distance.performCNTRLX();
		distance.assertTrue(distance.verifyLinearMeasurementIsActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[7/37]","verifying the rejected distance measurement opacity is changed after cut");

		distance.scrollDownToSliceUsingKeyboard(4);

		distance.performCNTRLV();
		distance.assertTrue(distance.verifyLinearMeasurementIsActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[8/37]","Verifying the measurement is turned in green post paste");
		distance.assertEquals(distance.getXCoordinate(distance.getLinearMeasurements(1, 1).get(0)), x, "checkpoint[9/37]", "measurement is pasted on same location");
		distance.assertEquals(distance.getYCoordinate(distance.getLinearMeasurements(1, 1).get(0)), y, "checkpoint[10/37]", "measurement is pasted on same location");
		cs.assertEquals(cs.getAllResults().size(), results.size(), "Checkpoint[11/37]", "No clone is generated");

		distance.scrollUpToSliceUsingKeyboard(4);
		distance.assertTrue(distance.getAllLinearMeasurements(1).isEmpty(), "Checkpoint[12/37]", "measurement is not present on previous slice");

		line.selectLine(1, 1);
		x = line.getXCoordinate(line.getLine(1, 1).get(0));
		y = line.getYCoordinate(line.getLine(1, 1).get(0));

		line.performCNTRLX();
		line.assertTrue(line.verifyLineIsActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[13/37]","verifying the accepted line's opacity is changed on cut");

		line.scrollDownToSliceUsingKeyboard(4);

		line.performCNTRLV();
		line.assertTrue(line.verifyLineIsActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[14/37]","verifying line state is same after paste");
		line.assertEquals(line.getXCoordinate(line.getLine(1, 1).get(0)), x, "checkpoint[15/37]", "verifying the line is pasted on same location");
		line.assertEquals(line.getYCoordinate(line.getLine(1, 1).get(0)), y, "checkpoint[16/37]", "verifying the line is pasted on same location");
		cs.assertEquals(cs.getAllResults().size(), results.size(), "Checkpoint[17/37]", "verifying there is no clone generated");

		line.scrollUpToSliceUsingKeyboard(4);
		line.assertTrue(line.getAllLines(1).isEmpty(), "Checkpoint[18/37]", "line is not present on previous slice");

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[19/37]","verifying the circle is pending focused");
		x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[20/37]","Verifying the pending circle opacity is changed after reload of viewer");

		circle.performCNTRLV();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[21/37]","verifying the state is not changed as copied on same slice");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[22/37]", "verifying the circle is copied on same location");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[23/37]", "verifying the circle is copied on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[24/37]", "verifying no clone is generated");


		polyline.selectPolyline(1, 1, 2);
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[26/37]","verifying the polyline is current rejected polyline");
		x = polyline.getXCoordinate(polyline.getPolyLineSvg(1,1));
		y = polyline.getYCoordinate(polyline.getPolyLineSvg(1,1));

		polyline.performCNTRLX();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[27/37]","Verifying the rejected polyline opacity is changed after reload of viewer");

		polyline.performCNTRLV();
		polyline.assertTrue(polyline.verifyPolylineIsActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[28/37]","verifying the state is not changed as copied on same slice");
		polyline.assertEquals(polyline.getXCoordinate(polyline.getPolyLineSvg(1,1)), x, "checkpoint[29/37]", "verifying the polyline is copied on same location");
		polyline.assertEquals(polyline.getYCoordinate(polyline.getPolyLineSvg(1,1)), y, "checkpoint[30/37]", "verifying the polyline is copied on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[31/37]", "verifying no clone is generated");

		textAnn.selectTextAnnotation(1, 1);
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[32/37]","verifying ellipse is current accepted and focused");
		x = textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1));
		y = textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1));

		textAnn.performCNTRLX();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[33/37]","Verifying the rejected ellipse opacity is changed after reload of viewer");

		textAnn.performCNTRLV();
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(1,1,true,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[34/37]","verifying the state is not changed as copied on same slice\"");
		textAnn.assertEquals(textAnn.getXCoordinate(textAnn.getTextAnnotation(1, 1)), x, "checkpoint[35/37]", "verifying the ellipse is copied on same location");
		textAnn.assertEquals(textAnn.getYCoordinate(textAnn.getTextAnnotation(1, 1)), y, "checkpoint[36/37]", "verifying the ellipse is copied on same location");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[37/37]", "verifying no clone is generated");

		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test04_US1613_TC8450_verifyCutPasteUserFindingAfterLayoutChange() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut annotation can be pasted in same viewbox after changing layout");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		ViewerLayout layout = new ViewerLayout(driver);
		
		point.doubleClickOnViewbox(1);
		cs = new ContentSelector(driver);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -300, -300);

		point.selectPoint(1, 1);
		int oneUpx = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int oneUpy = point.getYCoordinate(point.getPoint(1, 1).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[1/8]","Verifying the opacity after accepted finding being cut");
		List<String> results = cs.getAllResults();

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		int x = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int y = point.getYCoordinate(point.getPoint(1, 1).get(0));

		point.scrollDownToSliceUsingKeyboard(4);
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/8]","verifying the finding being paste after layout change");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), x, "checkpoint[3/8]", "verifying the finding location is same after paste on layout change");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), y, "checkpoint[4/8]", "verifying the finding location is same after paste on layout change");
		cs.assertEquals(cs.getAllResults().size(), results.size(), "Checkpoint[5/8]", "verifying the clone is not getting generated as cut and paste is happening in same session");

		point.doubleClickOnViewbox(1);
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), oneUpx, "checkpoint[6/8]", "verifying the finding location in oneup scenario");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), oneUpy, "checkpoint[7/8]", "verifying the finding location in oneup scenario");

		point.scrollUpToSliceUsingKeyboard(4);
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[8/8]", "verifying the finding is not present in previous slice");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Negative","E2E","F782"})
	public void test05_US1613_TC8451_verifyCutPasteFindingInDiffViewbox() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut annotation can NOT be pasted in different viewbox");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 300, 150,-80,-80);
		circle.selectAcceptfromGSPSRadialMenu();
		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/9]","verifying the finding is pending and focused");
		int x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/9]","verifying the state is not changed only opacity changed after cut of finding");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		circle.mouseHover(circle.getViewPort(2));
		circle.performCNTRLV();

		circle.assertTrue(circle.getAllCircles(2).isEmpty(), "Checkpoint[3/9]", "");
		circle.assertFalse(circle.verifyCircleIsCurrentActiveGSPS(2,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/9]","verifying the finding is not pasted on another viewbox");

		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[5/9]", "verifying the finding is present on first viewbox");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[6/9]","verifying that finding is still showing the opacity changed");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[7/9]", "verifying the location of finding is not changed");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[8/9]", "verifying the location of finding is not changed");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[9/9]", "verifying the clone is not created");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Negative","E2E","F782"})
	public void test06_US1613_TC8452_verifyCutPasteCancelOnEsc() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'cut' operation gets cancelled after pressing ESC key");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 300, 150,-80,-80);
		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/8]","verifying the finding is accpted and focused after selection");
		int x = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int y = circle.getYCoordinate(circle.getCircle(1, 1).get(0));

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/8]","verifying the opacity is changed post cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		circle.pressESCKey();		
		circle.assertFalse(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/8]","verifying that finding is focused again when ESC is pressed and shows the cut is cancelled");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[4/8]", "verifying the finding is present after cancel of cut operation");
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[5/8]", "Verifying the finding location is same");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[6/8]", "Verifying the finding location is same");

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[7/8]", "Verifying there is no clone created");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[8/8]", "verifying that finding is present on same slice");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Negative"})
	public void test07_01_US1613_TC8452_verifyCutPasteCancelOnBack() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'cut' operation gets cancelled after user logs out of the application OR performs viewer back operation");

		
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

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/10]","verifying the opacity is changed after cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/10]","Verifying that cut operationis cancelled after viewer reload and finding is active and focused");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[4/10]", "verifying finding is present");
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[5/10]", "verifying no clone is generated after reload");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[6/10]", "verifying the same slice is being loaded");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();
		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[7/10]", "Verifying the paste after reload is doing nothing no finding is pasted");
		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8/10]", "Verifying on going back to previous slice the finding is still present");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[9/10]", "Verifying the location is not changed after layout change");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[10/10]", "Verifying the location is not changed after layout change");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Negative"})
	public void test07_02_US1613_TC8452_verifyCutPasteCancelOnLogout() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'cut' operation gets cancelled after user logs out of the application OR performs viewer back operation");

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

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/10]","verifying the opacity is changed after cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);
		
		header = new Header(driver); 
		header.logout();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/10]","Verifying that cut operationis cancelled after logout and viewer reload and finding is active and focused");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[4/10]", "verifying finding is present");
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[5/10]", "verifying no clone is generated after logout and reload");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[6/10]", "verifying the same slice is being loaded after logout and viewer reload");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();
		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[7/10]", "Verifying the paste after reload is doing nothing no finding is pasted");
		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8/10]", "Verifying on going back to previous slice the finding is still present");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), x, "checkpoint[9/10]", "Verifying the location is not changed after layout change");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), y, "checkpoint[10/10]", "Verifying the location is not changed after layout change");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Negative","E2E","F782"})
	public void test08_US1613_TC8454_verifyCutPasteUpdationToNewFindingIsCut() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify current cut operation gets updated with new annotation cut operation when another annotation is cut");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

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

		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/14]","Verifying the Circle opacity is changed as it is being cut");
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);

		ellipse.selectEllipse(1, 1);
		ellipse.performCNTRLX();
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/14]","Verifying ellipse is cut and opacity is changed");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/14]","Verifying that circle opacity is changed to normal as another finding is cut");

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();

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

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test09_US1613_TC8455_verifyNothingHappensWhenNoneSelected() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify nothing should happen when no annotation is selected and CTRL+X is performed");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 60, -100,-10);

		ellipse  = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -50, -50, -20, -30);

		circle.closingConflictMsg();
		circle.mouseHover(circle.getViewPort(2));
		circle.mouseHover(circle.getViewPort(1));

		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/14]","Verifying the finding 1 is not focused");
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/14]","Verifying the finding 2 is not focused");

		int circleX = circle.getXCoordinate(circle.getCircle(1, 1).get(0));
		int circleY = circle.getYCoordinate(circle.getCircle(1, 1).get(0));
		int ellipseX = ellipse.getXCoordinate(ellipse.getAllEllipses(1).get(0));
		int ellipseY = ellipse.getYCoordinate(ellipse.getAllEllipses(1).get(0));

		circle.performCNTRLX();
		circle.assertFalse(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/14]","Verifying the finding 1 opacity is not changed after cut - as none was focused");
		ellipse.assertFalse(ellipse.verifyEllipseIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[4/14]","Verifying the finding 2 opacity is not changed after cut - as none was focused");

		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		int slicePos = circle.getCurrentScrollPositionOfViewbox(1);		

		cs.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();

		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[5/14]", "Verifying the paste operation is doing nothing as none of finding was selected during cut - finding 1 is absent");
		ellipse.assertTrue(ellipse.getAllEllipses(1).isEmpty(), "Checkpoint[6/14]", "Verifying the paste operation is doing nothing as none of finding was selected during cut - finding 2 is absent");
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[7/14]", "Verifying no clone is created");

		cs.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8/14]", "Verifying finding 1 is present on previous slice");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1, "Checkpoint[9/14]", "Verifying finding 2 is present on previous slice");

		circle.assertEquals(circle.getXCoordinate(circle.getCircle(1, 1).get(0)), circleX, "checkpoint[10/14]", "verifying the finding 1 is present on same location");
		circle.assertEquals(circle.getYCoordinate(circle.getCircle(1, 1).get(0)), circleY, "checkpoint[11/14]", "verifying the finding 1 is present on same location");
		ellipse.assertEquals(ellipse.getXCoordinate(ellipse.getAllEllipses(1).get(0)), ellipseX, "checkpoint[12/14]", "verifying the finding 2 is present on same location");
		ellipse.assertEquals(ellipse.getYCoordinate(ellipse.getAllEllipses(1).get(0)), ellipseY, "checkpoint[13/14]", "verifying the finding 2 is present on same location");
		cs.assertEquals(circle.getCurrentScrollPositionOfViewbox(1), slicePos, "Checkpoint[14/14]", "Verifying the slice is same");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test10_US1613_TC8457_TC8460_TC8461_TC8506_verifyJumpFindingAndSliderAfterCutPaste() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'jumpTo' functionality jumps to updated GSPS location from output panel / finding menu / slider bar"
				+ "<br> Verify clone should not get generated after only 'cut' operation"
				+ "<br> Verify clone should get generated after cut + paste operation"
				+ "<br> Verify that other viewbox with same series and result is updated(slider bar, finding menu) after paste");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		panel = new OutputPanel(driver);
		cs = new ContentSelector(driver);
		
		int currentSlice = point.getCurrentScrollPositionOfViewbox(1);

		List<String> clones = cs.getAllResults();
		List<String> findingsName = panel.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR);
		String selectedFindingName = findingsName.get(findingsName.size()-2);

		point.selectPoint(1, 1);

		int pointNo= 1;
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/37]","Verifying that point is pending and focuse");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/37]","Verifying opacity is changed after cut operation");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[3/37]", "verifying location is not changed after cut");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[4/37]", "verifying location is not changed after cut");
		point.scrollDownToSliceUsingKeyboard(1);
		point.assertEquals(clones,cs.getAllResults(),"Checkpoint[5/37]","Verifying clone is not created after cut");
		

		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/37]","verifying state is changed after paste on another slice");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[7/37]", "Verifying finding is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[8/37]", "Verifying finding is pasted on same location");
		point.assertEquals(clones.size()+2,cs.getAllResults().size(),"Checkpoint[9/37]","Verifying the clone is created");
		
		int slice = point.getCurrentScrollPositionOfViewbox(1);

		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[10/37]", "Verifying the previous slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[11/37]", "verifying finding is absent on previous slice");

		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[12.1/37]", "Verifying the jump to icon is navigating to new slice");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[12.2/37]", "verifying the finding is accepted and focused");
		
		panel.scrollUpToSliceUsingKeyboard(1, 1);
		WebElement marker = panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[13/37]", "verifying the finding is accepted and focused after selected from slider");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[14/37]", "verifying on selection it is navigating to new slice");

		panel.scrollUpToSliceUsingKeyboard(1, 1);
		panel.selectFindingFromTable(1, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[15/37]", "Verifying the finding is focused upon selected from finding menu");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[16/37]", "verifying on selection it is going to new slice");

		
		int badgeCount = point.getBadgeCountFromToolbar(1);
		for(int i=1;i<=2;i++) {
			cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", i);
			if(point.getBadgeCountFromToolbar(2)==badgeCount)
				break;
		}

		findingsName = panel.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		selectedFindingName = findingsName.get(0);
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		panel.selectFindingFromTable(2, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[17/37]", "verifying finding is focused in another viewbox where same clone is loaded");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[18/37]", "verifying finding is focused in another viewbox");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[19/37]", "Verifying the viewbox 1 is on same slice where finding is pasted");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(2), slice, "Checkpoint[20/37]", "Verifying the viewbox 1 and viewbox 2 both are in sync as same clone is loaded in both viewboxes");
		
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[21/37]", "verifying the finding in viewbox 2 is focused after selected from slider");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[22/37]", "verifying finding is focused in another viewbox as both viewbox in sync");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[23/37]", "verifying the new slice is loaded as selected from slider in viewbox 1");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(2), slice, "Checkpoint[24/37]", "verifying the same slice is loaded in viewbox 2");
		
		point.selectPoint(2, 1);
		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[25/37]","verifying the opacity is changed on cut in viewbox -1 as clone is loaded");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(2,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[26/37]","verifying the opacity is changed on cut in viewbox -2");
		
		point.scrollUpToSliceUsingKeyboard(2, 1);
		point.performCNTRLV();
		slice = point.getCurrentScrollPositionOfViewbox(2);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[27/37]","finding state is accepted and focused in viewbox 1");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(2,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[28/37]","finding state is accepted and focused in viewbox 2");
		point.assertEquals(point.getCurrentScrollPosition(1), point.getCurrentScrollPosition(2), "Checkpoint[29/37]", "Both the viewboxes are on same slice after paste as same clone is loaded in both viewboxes");
		
		point.scrollDownToSliceUsingKeyboard(2, 1);
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[30/37]", "verifying the finding is absent on previous slice in viewbox 1");
		point.assertTrue(point.getAllPoints(2).isEmpty(), "Checkpoint[31/37]", "verifying the finding is absent on previous slice in viewbox 2");
		
		helper.browserBackAndReloadViewer(multiPointPatientName,  1, 1);	
		
		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.getText(panel.findingsNameList.get(0)),ViewerPageConstants.FINDING_NAME+": "+selectedFindingName, "Checkpoint[32/37]", "verifying the finding is displayed under accepted tab on reload of viewer");
		panel.clickOnJumpIcon(1);
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[33.1/37]", "Verifying the updated slice is loaded on jump to icon click");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[33.2/37]", "verifying the finding is focused");
		
		panel.scrollUpToSliceUsingKeyboard(1, 1);
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[34/37]", "verifying the finding is focused");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[35/37]", "Verifying the updated slice is loaded on selection from slider");

		panel.scrollUpToSliceUsingKeyboard(1, 1);
		panel.selectFindingFromTable(1, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[36/37]", "verifying the finding is focused");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[37/37]", "Verifying the updated slice is loaded on selection from finding menu");

	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test11_US1613_TC8462_verifyOptNotCancelledAfterViewerOpt() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'cut' operation should not get cancelled after performing any DICOM operation(PAN/ZOOM/WWWL)");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		
		int currentSlice = point.getCurrentScrollPositionOfViewbox(1);
		point.selectPoint(1, 1);

		int pointNo= 1;
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/7]","Verifying the finding is focused ");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/7]","verifying the finding is cut");
		point.scrollDownToSliceUsingKeyboard(1);
		
		point.selectWindowLevelFromQuickToolbar(2);
		point.dragAndReleaseOnViewer(point.getViewPort(1), 10, 10, 40,40);

		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/7]","verifying the cut is not cancelled after performing the DICOM operation");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4/7]", "verifying the finding is pasted on same location as on previous slice");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y, "checkpoint[5/7]", "verifying the finding is pasted on same location as on previous slice");
		
		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[6/7]", "verifying the previous slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[7/7]", "verifying the finding is not present after DICOM operations and cut");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Negative"})
	public void test12_US1613_TC8502_verifyOptCancelOnNewFindingCreation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'cut' operation should get cancelled after drawing new annotation");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 60, -100,-10);

		ellipse  = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
	
		circle.selectCircle(1, 1);
		circle.performCNTRLX();
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[1/9]","verifying the finding is cut");

		ellipse.drawEllipse(1, -50, -50, -20, -30);
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/9]","verifying new finding is focused upon creation");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/9]","verifying the opacity is changed to normal");

		circle.scrollUpToSliceUsingKeyboard(1);
		circle.performCNTRLV();

		circle.assertTrue(circle.getAllCircles(1).isEmpty(), "Checkpoint[4/9]", "verifying the paste operation on another slice - finding 1 is absent");
		ellipse.assertTrue(ellipse.getAllEllipses(1).isEmpty(), "Checkpoint[5/9]", "verifying the paste operation on another slice - finding 2 is absent");
	
		circle.scrollDownToSliceUsingKeyboard(1);
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[6/9]", "Verifying the finding 1 is present in previous slice");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1, "Checkpoint[7/9]", "Verifying the finding 2 is present in previous slice");
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[8/9]","verifying finding 1 is not focused");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[9/9]","verifying finding 2 is not focused");


	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test13_US1613_TC8503_verifyCutPasteForAddedComment() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotation along with its comment should get cut and pasted");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatientName, 1);

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		
		int currentSlice = point.getCurrentScrollPositionOfViewbox(1);
		String comment = "CROSSPOINT_COMMENT";
		point.addResultComment(point.getHandlesOfPoint(1, 1).get(0),comment);
		point.selectAcceptfromGSPSRadialMenu();
		point.selectPreviousfromGSPSRadialMenu();
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/16]","verifying the finding is focused and selected");
		int x = point.getXCoordinate(point.getPoint(1, 1).get(0));
		int y = point.getYCoordinate(point.getPoint(1, 1).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/16]","Verifying the opacity is changed on cut");
		point.scrollDownToSliceUsingKeyboard(1);
		
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/16]","Verifying the fidning state is changed on paste");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), x, "checkpoint[4/16]", "Verifying the finding is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), y, "checkpoint[5/16]", "Verifying the finding is pasted on same location");
		point.assertEquals(point.getTextCommentForPoint(1, 1), comment, "Checkpoint[6/16]", "Verifying the added comment is also pasted on another slice");
		
		point.scrollUpToSliceUsingKeyboard(1);		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[7/16]", "Verifying the previous slice");
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[8/16]", "verifying the finding is absent on previous slice");
		
		point.scrollDownToSliceUsingKeyboard(1);
		point.selectPoint(1, 1);
		point.selectRejectfromGSPSRadialMenu();
		point.selectPreviousfromGSPSRadialMenu();
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"Checkpoint[9/16]","Point is rejected and focused");
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/16]","Verifying the finding opacity is normal");
		x = point.getXCoordinate(point.getPoint(1, 1).get(0));
		y = point.getYCoordinate(point.getPoint(1, 1).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[11/16]","Verifying the opacity is changed after cut");
		point.scrollUpToSliceUsingKeyboard(1);
		
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[12/16]","Verifying state is changed from rejected to accepted after paste");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, 1).get(0)), x, "checkpoint[13/16]", "verifying the finding is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(1, 1).get(0)), y, "checkpoint[14/16]", "verifying the finding is pasted on same location");
		point.assertEquals(point.getTextCommentForPoint(1, 1), comment, "Checkpoint[15/16]", "Verifying the comment is also pasted along with finding");
		
		point.scrollDownToSliceUsingKeyboard(1);		
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[16/16]", "Verifying the previous slice finding is absent");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1613","Positive","E2E","F782"})
	public void test14_US1613_TC8504_verifyCutPasteOnGroupData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste functionality on group  data having all findings on same slice");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName, 1);
		
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		textAnn = new TextAnnotation(driver);
		panel = new OutputPanel(driver);
		point.waitForViewerpageToLoad();
		cs.closeNotification();
		int viewbox = point.getNumberOfCanvasForLayout();
		point.doubleClickOnViewbox(viewbox);		
		
		int currentSlice = point.getCurrentScrollPositionOfViewbox(viewbox);
		int currentPhase = point.getValueOfCurrentPhase(viewbox);
		List<String> clones = cs.getAllResults();
		
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedFinding = panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingFinding = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		point.selectPoint(9, 1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/31]","Verifying the Point + text finding under group is focused and pending");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/31]","Verifying the Point + text finding under group is focused and pending");
		
		int pointX = point.getXCoordinate(point.getPoint(viewbox, 1).get(0));
		int pointY = point.getYCoordinate(point.getPoint(viewbox, 1).get(0));
		int textAnnX = textAnn.getXCoordinate(textAnn.getLineOfTextAnnotations(viewbox, 1));
		int textAnnY = textAnn.getYCoordinate(textAnn.getLineOfTextAnnotations(viewbox, 1));


		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[3/31]","Verifying the opacity is changed for Point + text finding under group ");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[4/31]","Verifying the opacity is changed for Point + text finding under group ");
	
		point.pressKey(NSGenericConstants.DOT_KEY);
		point.scrollDownToSliceUsingKeyboard(1);
		point.assertEquals(clones,cs.getAllResults(),"Checkpoint[5/31]","Verifying the clone is not generated upon cut of group");
	
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/31]","Verifying state of group finding is changed upon paste");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/31]","Verifying state of group finding is changed upon paste");
		
		int updatedSlice = point.getCurrentScrollPositionOfViewbox(viewbox);
		int updatedPhase = point.getValueOfCurrentPhase(viewbox);
		
		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, 1).get(0)), pointX, "checkpoint[8/31]", "Verifying it is pasted on same location as of previous slice of another phase");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, 1).get(0)), pointY, "checkpoint[9/31]", "Verifying it is pasted on same location as of previous slice of another phase");
		
		point.assertEquals(textAnn.getXCoordinate(textAnn.getLineOfTextAnnotations(viewbox, 1)), textAnnX, "checkpoint[10/31]", "Verifying it is pasted on same location as of previous slice of another phase");
		point.assertEquals(textAnn.getYCoordinate(textAnn.getLineOfTextAnnotations(viewbox, 1)), textAnnY, "checkpoint[11/31]", "Verifying it is pasted on same location as of previous slice of another phase");
		
		point.assertEquals(clones.size()+1,cs.getAllResults().size(),"Checkpoint[12/31]","Verifying the clone is generated");
		
		point.scrollUpToSliceUsingKeyboard(1);
		point.pressKey(NSGenericConstants.COMMA_KEY);
		
		point.assertTrue(point.getAllPoints(viewbox).isEmpty(), "Checkpoint[13/31]", "Verifying the group is absent on previous slice of phase");
		textAnn.assertTrue(textAnn.getTextAnnotations(viewbox).isEmpty(), "Checkpoint[14/31]", "Verifying the group is absent on previous slice of phase");
		point.assertEquals(currentSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[15/31]","verifying the previous slice");
		point.assertEquals(currentPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[16/31]","verifying the previous phase");
		
		List<String> groups = point.getListOfGroupsInFindingMenu(viewbox);
		
		panel.enableFiltersInOutputPanel(false, false, true);
		point.assertEquals(pendingFinding - 1, panel.thumbnailList.size(),"Checkpoint[17/31]","verifying the pending finding in OP");
		panel.enableFiltersInOutputPanel(true, false, false);		
		point.assertEquals(acceptedFinding+1, panel.thumbnailList.size(),"Checkpoint[18/31]","Verifying the group state is changed to accepted in OP");
//		point.assertEquals(panel.getText(panel.findingsNameList.get(0)), ViewerPageConstants.FINDING_NAME+": "+groups.get(0),"Checkpoint[19/31]","Verifying the group name in OP");
		panel.clickOnJumpIcon(1);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[20/31]","Verifying the group is accepted and focused on click of jump to Icon");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[21/31]","Verifying the group is accepted and focused on click of jump to Icon");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[22/31]","Verifying the updated slice is rendered");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[23/31]","verifying the updated phase is displayed");
	

		panel.scrollUpToSliceUsingKeyboard(viewbox, 1);
		WebElement marker = panel.getStateSpecificMarkerOnSlider(viewbox, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectGroupFromSlider(marker, groups.get(0));
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[24/31]","Verifying the group is accepted and focused on selection of slider");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[25/31]","Verifying the group is accepted and focused on selection of slider");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[26/31]","Verifying the updated slice is rendered");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[27/31]","verifying the updated phase is displayed");
		
		panel.scrollUpToSliceUsingKeyboard(viewbox, 1);
		panel.selectGroupFromTable(viewbox, groups.get(0));
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[28/31]","Verifying the group is accepted and focused on selection from finding menu");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveGSPS(viewbox, 1, true, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[29/31]","Verifying the group is accepted and focused on selection from finding menu");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[30/31]","Verifying the updated slice is rendered");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[31/31]","verifying the updated phase is displayed");
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Positive"})
	public void test15_US1613_TC8507_verifyCutPasteOnGSPSCopy() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cut-pasted measurement is saved in editable GSPS copy.");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);
		
	
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		
		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.movePoint(1, pointNo, 50, 60);
		point.selectRejectfromGSPSRadialMenu();
		point.selectPreviousfromGSPSRadialMenu();
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/11]","Verifying the finding is rejected and focused after multiple changes (move + state change)");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/11]","Verifying the opacity is changed but state is same for finding");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);
		
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/11]","Verifying the finding state is changed on paste of another slice");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4/11]", "Verifying the location is same for finding");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[5/11]", "Verifying the location is same for finding");
		int slice = point.getCurrentScrollPositionOfViewbox(1);

		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[6/11]", "Verifying no clone is generated");
		point.scrollUpToSliceUsingKeyboard(2);
		point.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint[7/11]","verifying no finding is present on prrevious slice");
	
		helper.browserBackAndReloadViewer(multiPointPatientName,  1, 1);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[8/11]","Verifying the finding state on viewer reload");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[9/11]", "verifying the location is same on reload");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[10/11]", "verifying the location is same on reload");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1),slice, "checkpoint[11/11]", "Verifying the same slice is displayed");

		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1613","Negative","E2E","F782"})
	public void test16_US1613_TC8514_verifyCutUsingContextMenuAndPasteUsingCntrl() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotation can be cut using context menu - Cut option and pasted using Ctrl+V");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);
	
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		
		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/11]","Verifying the finding is focused");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.selectCutUsingContextMenu(point.getHandlesOfPoint(1, pointNo).get(0));
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/11]","verifying the opacity is changed when cut is done using context menu");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);
		
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/11]","verifying the state is changed when paste is done using cntrl+v");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4/11]", "verifying the finding location is same");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[5/11]", "verifying the finding location is same");
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
	
	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Negative"})
	public void test17_US1613_TC8515_verifyCutUsingCntrlAndPasteUsingContextMenu() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotation can be cut using Ctrl + x and pasted using context menu - paste");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);
	
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		
		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/11]","Verifying the finding is focused");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/11]","verifying the opacity is changed when cut is done using contrl + x");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);
		
		point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.PASTE);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/11]","verifying the state is changed when paste is done using context menu");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[4/11]", "verifying the finding location is same");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[5/11]", "verifying the finding location is same");
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
		
	@Test(groups ={"Chrome","Edge","IE11","US1613","F782","Negative"})
	public void test18_US1613_TC8516_verifyCutCancelUsingContextMenu() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify annotation can be cut using Ctrl + X and cancel using context menu - cancel");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(multiPointPatientName,  1, 1);
	
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		
		int slice = point.getCurrentScrollPositionOfViewbox(1);
		int pointNo= point.getPointWhichIsPendingAndActive(1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/12]","verrifying finding is focused and selected");
		int x = point.getXCoordinate(point.getPoint(1, pointNo).get(0));
		int y = point.getYCoordinate(point.getPoint(1, pointNo).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/12]","verifying the opacity changed on cut using ctrl+x");
		List<String> results = cs.getAllResults();
		point.scrollDownToSliceUsingKeyboard(2);
				
		point.selectPasteOrCancelUsingContextMenu(1,ViewerPageConstants.CANCEL);
		point.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint[3/12]","Verifying on cancel the finding is absent ");				
		cs.assertEquals(cs.getAllResults(), results, "Checkpoint[4/12]", "Verifying no clone is generated");
		
		point.scrollUpToSliceUsingKeyboard(2);
		point.assertTrue(point.verifyPointAnnotationIsCurrentInActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/12]","verifying the finding is present on previous slice");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[6/12]", "verifying the finding is present on same location");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[7/12]", "verifying the finding is present on same location");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1),slice, "checkpoint[8/12]", "verifying the previous slice");
		
		helper.browserBackAndReloadViewer(multiPointPatientName,  1, 1);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(1,pointNo,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[9/12]","verifying the finding is present on previous slice");
		point.assertEquals(point.getXCoordinate(point.getPoint(1, pointNo).get(0)), x, "checkpoint[10/12]", "verifying the finding is present on same location");
		point.assertTrue(point.verifyAccuracyOfValues(point.getYCoordinate(point.getPoint(1, pointNo).get(0)), y,1), "checkpoint[11/12]", "verifying the finding is present on same location");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1),slice, "checkpoint[12/12]", "verifying the previous slice on reload");

		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1613","Positive","BVT","E2E","F782"})
	public void test19_US1613_TC8527_verifyCutPasteOnMultiPhase() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cut + paste functionality on multi-phase data");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(adcPilipsPatientName, 1);

		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		point.waitForViewerpageToLoad();
		int viewbox = 3 ;
		
		point.selectPointFromQuickToolbar(viewbox);
		point.drawPointAnnotationMarkerOnViewbox(viewbox, 50, 50);
		
		
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedFinding = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		point.selectPoint(viewbox, 1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/18]","verifying finding is focused");
		
		int pointX = point.getXCoordinate(point.getPoint(viewbox, 1).get(0));
		int pointY = point.getYCoordinate(point.getPoint(viewbox, 1).get(0));

		point.performCNTRLX();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_THUMBNAIL),"Checkpoint[2/18]","verifying the opacity is changed on cut");
		List<String> clones = cs.getAllResults();
		
		point.pressKey(NSGenericConstants.DOT_KEY);
		point.scrollDownToSliceUsingKeyboard(1);
	
		point.performCNTRLV();
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/18]","verifying state is changed for finding after paste");
		
		int updatedSlice = point.getCurrentScrollPositionOfViewbox(viewbox);
		int updatedPhase = point.getValueOfCurrentPhase(viewbox);
		
		point.assertEquals(point.getXCoordinate(point.getPoint(viewbox, 1).get(0)), pointX, "checkpoint[4/18]", "verifying finding is pasted on same location");
		point.assertEquals(point.getYCoordinate(point.getPoint(viewbox, 1).get(0)), pointY, "checkpoint[5/18]", "verifying finding is pasted on same location");
		
		point.assertEquals(clones.size(),cs.getAllResults().size(),"Checkpoint[6/18]","verifying the clones");
		
		point.scrollUpToSliceUsingKeyboard(1);
		point.pressKey(NSGenericConstants.COMMA_KEY);
		point.assertTrue(point.getAllPoints(viewbox).isEmpty(), "Checkpoint[7/18]", "verifying previous phase and checking the finding is not present");
		
		 List<String> findingsName = point.getFindingsName(viewbox, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.enableFiltersInOutputPanel(true, false, false);		
		point.assertEquals(acceptedFinding , panel.thumbnailList.size(),"Checkpoint[8/18]","verifying the OP for accepted finding");
//		point.assertEquals(panel.getText(panel.findingsNameList.get(0)), ViewerPageConstants.FINDING_NAME+": "+findingsName.get(0),"Checkpoint[9/18]","verifying the finding name in OP");
		panel.clickOnJumpIcon(1);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentActiveGSPS(viewbox,1,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/18]","verifying the finding is selected on jump to icon click");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[11/18]","verifying the updated slice");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[12/18]","verifying the updated phase");
	

		panel.scrollUpToSliceUsingKeyboard(1);
		WebElement marker = panel.getStateSpecificMarkerOnSlider(viewbox, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, findingsName.get(0));
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewbox, 1), "Checkpoint[13/18]", "verifying the finding is selected when selected from slider");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[14/18]","verifying the updated slice");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[15/18]","verifying the updated phase");
		
		panel.scrollUpToSliceUsingKeyboard(1);
		panel.selectFindingFromTable(viewbox, findingsName.get(0));
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewbox, 1), "Checkpoint[16/18]", "verifying the finding is selected when selected from finding menu");
		point.assertEquals(updatedSlice, point.getCurrentScrollPositionOfViewbox(viewbox),"Checkpoint[17/18]","verifying the updated slice");
		point.assertEquals(updatedPhase, point.getValueOfCurrentPhase(viewbox), "Checkpoint[18/18]","verifying the updated phase");
	
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1613","Negative","E2E","F782"})
	public void test20_US1613_TC8528_verifyCutPasteOnGroupPresentAcrossSlices() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify nothing happens on performing cut + paste on group data  spread across multiple slices");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(lidcPatientID, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.waitForViewerpageToLoad();
		
		ellipse.selectGroupFromTable(1, 1);
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[1/3]", "verifying the group is selected from finding menu");

		ellipse.performCNTRLX();
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/3]", "verifying the opacity is not changed on cut as fingings are present across slices");
	
		ellipse.performCNTRLV();
		ellipse.assertTrue(ellipse.verifyEllipseIsCurrentActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[3/3]", "verifying on paste nothing happens");

		
	}
	
	
}


