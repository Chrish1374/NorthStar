package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PointAnnotationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector contentSelector;
	private PointAnnotation pointAnn ;

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String ah4FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ah4FilePath);
	String ah4pdfseries=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY,ah4FilePath);
	String ah4_4series=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY,ah4FilePath);

	String gspsFilePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String firstSeriesDescriptionMultiSeries ="";
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	@Test(groups ={"IE11","Chrome","US520","US2325","Positive","F1090","E2E"})
	public void test01_US520_TC1562_TC1563_US2325_TC9777_verifyPointAnnotationIsEnabledUsingContextMenuAndRadialMenuOuterArc() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the point annotation command functionality"
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		pointAnn = new PointAnnotation(driver);
		
		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.assertTrue(pointAnn.checkCurrentSelectedIcon(ViewerPageConstants.POINT),"Checkpoint[1/2]","Verifying Point icon is selected in quick toolbar");
		
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIcon(ViewerPageConstants.POINT),"Checkpoint[2/2]", "Verifying Point icon is selected in viewer bar");
	

	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test02_US520_TC1564_verifyUserPlacePointAnnotation() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the point annotation command functionality");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		pointAnn.selectPointFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that point annotation is selected and enabled");
		pointAnn.assertTrue(pointAnn.checkCurrentSelectedIcon(ViewerPageConstants.POINT),"Verifying Point icon is selected", "Point icon is selected");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");

		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");

		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,70);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,2),"verifying the point#2", "point is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,2),"verifying the point#2", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 2),"Verifying Point", "point is correctly drawn");

		pointAnn.assertEquals(pointAnn.getLinesOfPoint(1, 1).size(),ViewerPageConstants.POINT_LINES, "Verifying by default drawn point#1 is not selected", "verified");
		pointAnn.assertFalse(pointAnn.isPointSelected(1, 1), "Verifying by default drawn point#2 is not selected", "verified");


	}

	@Test(groups ={"IE11","Chrome","US520","BVT"})
	public void test03_US520_TC1565_verifyPointNotDrawnOnPDF() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is not able to draw point marker on PDF.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName,1);
		
		pointAnn = new PointAnnotation(driver);
		contentSelector = new ContentSelector(driver);
		pointAnn.selectPointFromQuickToolbar(1);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		pointAnn.takeElementScreenShot(pointAnn.getPDFViewbox(2), newImagePath+"/goldImages/"+imbio_PatientName+"_pointAnn.png");

		pointAnn.drawPointAnnotationMarkerOnViewbox(2,50,50);
		pointAnn.mouseHover(pointAnn.getViewPort(1));

		pointAnn.takeElementScreenShot(pointAnn.getPDFViewbox(2), newImagePath+"/actualImages/"+imbio_PatientName+"_pointAnn.png");


		String expectedImagePath = newImagePath+"/goldImages/"+imbio_PatientName+"_pointAnn.png";
		String actualImagePath = newImagePath+"/actualImages/"+imbio_PatientName+"_pointAnn.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_"+imbio_PatientName+"_pointAnn.png";

		boolean cpStatus =  pointAnn.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		pointAnn.assertTrue(cpStatus, "The actual and Expected image should be same","Images are same hence point is not drawn");
		//contentSelector.selectSeriesFromContentSelector(4, ah4_4series);
	}

	@Test(groups ={"IE11","Chrome","US520","Sanity","BVT"})
	public void test04_US520_TC1566_TC1679_verifyPointAnnotationDrawInAnyDirection() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can move point annotation in any direction \n Verify that point annotation consist of four lines");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		
		pointAnn = new PointAnnotation(driver);
		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,350,50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point#1 is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point#1", "point#1 is correctly drawn");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,350,-100);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,2),"verifying the point#2", "point#2 is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,2),"verifying the point#2", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 2),"Verifying Point#2", "point#2 is correctly drawn");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,-350,-100);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,3),"verifying the point#3", "point#3 is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,3),"verifying the point#3", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 3),"Verifying Point#3", "point#3 is correctly drawn");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,-370, 150);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,4),"verifying the point#4", "point#4 is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,4),"verifying the point#4", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 4),"Verifying Point#4", "point#4 is correctly drawn");
	}

	@Test(groups ={"IE11","Chrome","US520","US2329","Positive","F1090","E2E"})
	public void test05_US520_TC1567_TC1680_US2329_TC10165_verifyDeletionOfPoint() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user can delete point annotation \n verify that while selecting point annotation, dot/circle should be present between four lines. <br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		
		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		pointAnn.deletePoint(1, 1);		

		pointAnn.assertFalse(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is not present");



	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test06_US520_TC1568_verifyNoEditingAndNoColorChange() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify point annotation can't edited (No resize, No color change)");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);
		
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");		
		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		List<WebElement> pointDetails = pointAnn.getPoint(1,1);
		String xCoord =  pointDetails.get(pointDetails.size()-1).getAttribute(NSGenericConstants.CX);
		String yCoord =  pointDetails.get(pointDetails.size()-1).getAttribute(NSGenericConstants.CY);

		
		pointAnn.movePoint(1, 1, 270, 70);

		// verifying that color is green
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");		
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");
		// verifying that point is movable not editable
		pointAnn.assertFalse(pointAnn.isPointPresent(1,2),"verifying the point#2 - verification for another point creation", "point#2 is absent");

		pointDetails = pointAnn.getPoint(1,1);
		
		// verifying that coordinates has changed
		pointAnn.assertNotEquals(pointDetails.get(pointDetails.size()-1).getAttribute(NSGenericConstants.CX),xCoord,"Verifying Point is moved not edited", "point is moved hence x coordinates has changed");		
		pointAnn.assertNotEquals(pointDetails.get(pointDetails.size()-1).getAttribute(NSGenericConstants.CY),yCoord,"Verifying Point is moved not edited", "point is moved hence y coordinates has changed");		




	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test07_US520_TC1569_TC1674_verifyPointIconIsSelectedPostDrawn() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that point annotaion tool keep selected even after point annoation drawn in viewbox."
				+ "<br> Verify that point annotation tool should be present in context menu in Basic Tab in labelling section.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");

		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");

		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,-50,-50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,2),"verifying the point#2", "point is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,2),"verifying the point#2", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 2),"Verifying Point", "point is correctly drawn");

	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test08_US520_TC1675_verifyPointSizePostZoom() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of point will be same");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);
		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point#1", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		List<WebElement> point = pointAnn.getPoint(1, 1);

		double xLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1)));
		double xRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))); 

		double yLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1)));
		double yRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1)));

		double xTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1)));
		double xBottomtDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1)));		

		double yTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)));
		double yBottomDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));


		pointAnn.selectZoomFromQuickToolbar(1);
		pointAnn.dragAndReleaseOnViewer(pointAnn.getViewPort(1), 0, 0, 50, 50);

		point = pointAnn.getPoint(1, 1);
		
		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1))), xLeftDiff1, "Verifying the x for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))), xRightDiff2, "Verifying the x for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1))), yLeftDiff1, "Verifying the y for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1))), yRightDiff2, "Verifying the y for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1))), xTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1))), xBottomtDiff2, "Verifying the x for bottom line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1))), yTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1))), yBottomDiff2, "Verifying the x for bottom line", "verified");


		pointAnn.assertEquals(point.get(point.size()-1).getAttribute(NSGenericConstants.R), ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX, "Verifying the handler radius is same","It is 5Px");
	}

	@Test(groups ={"IE11","Chrome","US563"})
	public void test09_US520_TC1676_US563_TC1786_verifyDeletionOPointAndLine() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on pressing keys control + Del together all annotations (point + line) should be deleted");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		pointAnn = new PointAnnotation(driver);
		

		SimpleLine line = new SimpleLine(driver);
		
		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,100,100);

		line.selectLineFromQuickToolbar(pointAnn.getViewPort(1));

		pointAnn.dragAndReleaseOnViewer(pointAnn.getViewPort(1), 20, 20, 70, 80);

		pointAnn.click(pointAnn.getViewPort(1));
		pointAnn.assertTrue(line.getLine(1, 1).get(0).isDisplayed(),"Verifying the line is created","Line is created");

		pointAnn.deleteAllAnnotation(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that post CNTRL+ DEL all the annotation are deleted");
		pointAnn.assertFalse(pointAnn.isPointPresent(1, 1),"Verifying the Point#1 is deleted","Point#1 is deleted");
		pointAnn.assertFalse(pointAnn.isPointPresent(1, 2),"Verifying the Point#2 is deleted","Point#2 is deleted");
		pointAnn.assertEquals(line.getLine(1, 1).size(),0,"Verifying the Line is deleted","Line is deleted");

	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test10_US520_TC1677_verifyPointSizePostPAN() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user PAN the image size of point should not change and it should move with image");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);
		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point#1", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		List<WebElement> point = pointAnn.getPoint(1, 1);

		double xLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1)));
		double xRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))); 

		double yLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1)));
		double yRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1)));

		double xTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1)));
		double xBottomtDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1)));		

		double yTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)));
		double yBottomDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));


		pointAnn.selectPanFromQuickToolbar(pointAnn.getViewPort(1));

		pointAnn.dragAndReleaseOnViewer(pointAnn.getViewPort(1), 0, 0, 50, 50);

		point = pointAnn.getPoint(1, 1);
		
		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1))), xLeftDiff1, "Verifying the x for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))), xRightDiff2, "Verifying the x for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1))), yLeftDiff1, "Verifying the y for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1))), yRightDiff2, "Verifying the y for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1))), xTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1))), xBottomtDiff2, "Verifying the x for bottom line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1))), yTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1))), yBottomDiff2, "Verifying the x for bottom line", "verified");

		pointAnn.assertEquals(point.get(point.size()-1).getAttribute(NSGenericConstants.R), ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX, "Verifying the handler radius is same","It is 5Px");
	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test12_US520_TC1678_verifyPointNotMovabletoAnotherViewbox() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should not able to drag or move point annotation from one viewbox to other");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		String x1 = pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1);

		pointAnn.movePoint(1, 1, 450, 0);

		pointAnn.assertEquals(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1),x1,"Verifying Point annotation has not moved out of viewbox", "Verified");

		pointAnn.assertTrue(pointAnn.isViewboxActive(2), "verifying viewbox#2 is active now", "verified");




	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test13_US520_verifyPointAnnotationOnLayout() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the point annotation command functionality on Layout change");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);		

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		pointAnn.waitForViewerpageToLoad(1);

		pointAnn.assertEquals(pointAnn.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		pointAnn.assertTrue(pointAnn.isElementPresent(pointAnn.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		pointAnn.assertTrue(pointAnn.isElementPresent(pointAnn.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		pointAnn.assertTrue(pointAnn.isElementPresent(pointAnn.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	

		layout.selectLayout(layout.twoByOneLayoutIcon);
		

		pointAnn.assertEquals(pointAnn.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		pointAnn.assertFalse(pointAnn.isElementPresent(pointAnn.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		pointAnn.assertFalse(pointAnn.isElementPresent(pointAnn.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		pointAnn.assertFalse(pointAnn.isElementPresent(pointAnn.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	


	}

	@Test(groups ={"IE11","Chrome","US520","BVT"})
	public void test14_US520_verifyPointAnnotationOnOrientationChange() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the point annotation command functionality on flip");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);

		pointAnn = new PointAnnotation(driver);
		

		pointAnn.selectPointFromQuickToolbar(1);		

		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		pointAnn.assertTrue(pointAnn.isPointSelected(1,1),"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotation(1, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");
		ViewerOrientation orin = new ViewerOrientation(driver);
		orin.flipSeries(orin.getLeftOrientationMarker(1));
		pointAnn.compareElementImage(protocolName, pointAnn.getViewPort(1), "Top Orientation Marker: Image flipped horizontally.", "US520_checkpoint1");


	}

	@Test(groups ={"IE11","Chrome","US520"})
	public void test15_US520_verifyPointAnnotOnSeriesSelectedUsingCS() throws IOException, InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the point annotation when series is selected using content selector");

		//String series = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, gspsFilePath);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName,1);
		
		
		pointAnn = new PointAnnotation(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		contentSelector = new ContentSelector(driver);
		String series = pointAnn.getSeriesDescriptionOverlayText(1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		viewerpage.selectSeriesFromContentSelector(3, series);
		contentSelector.selectResultFromSeriesTab(3, "1", 2);
		pointAnn.assertEquals(pointAnn.getLinesOfPoint(3,1).size(),ViewerPageConstants.POINT_LINES,"verifying the point#1", "point is present");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotationIsPendingActiveGSPS(3, 1),"Verifying Point", "point is correctly drawn");
		pointAnn.assertTrue(pointAnn.isPointPresent(3,1),"verifying the point#1", "point is present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		pointAnn.assertTrue(pointAnn.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		pointAnn.assertTrue(pointAnn.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		pointAnn.assertTrue(pointAnn.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	


	}
	
	@Test(groups ={"IE11","Chrome","DE902","Positive","Sanity","BVT"})
	public void test16_DE902_TC3500_verifyStateIsSavedOnPointMovement() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that point position should get save after moving"
				+ "<br> Verify that point position should get save after moving");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientName,  1, 1);

		pointAnn = new PointAnnotation(driver);
		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);

		String x1 = pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1);

		pointAnn.movePoint(1, 1, 30, 0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verifying the point post movement" );
		
		pointAnn.assertNotEquals(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1),x1,"Verifying Point annotation has not moved out of viewbox", "Verified");

		helper.browserBackAndReloadViewer(patientName,  1, 1);
	
		x1 = pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Moving the point post reloading the viewer" );
		
		pointAnn.movePoint(1, 1, -10, -20);
		pointAnn.assertNotEquals(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1),x1,"Verifying Point annotation has not moved out of viewbox", "Verified");

		x1 = pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "Moving the point once again" );
		pointAnn.movePoint(1, 1, -10, -30);
		pointAnn.assertNotEquals(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1),x1,"Verifying Point annotation has not moved out of viewbox", "Verified");
		
		Float newX1 = Float.parseFloat(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1));
		
		Header header = new Header(driver);
		header.logout();
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
				
		helper.loadViewerPageUsingSearch(patientName,  1, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "Verifying the state post logut and on reload viewer" );
		pointAnn.assertEquals(((Float)Float.parseFloat(pointAnn.getPoint(1, 1).get(1).getAttribute(ViewerPageConstants.X1))).intValue(),newX1.intValue(),"Verifying Point annotation has not moved out of viewbox", "Verified");
		

	}

	@Test(groups ={"Chrome","IE11","US986","positive","US950"})
	public void test17_US986_TC4200_US950_TC4300_verifySelectionForPoint() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that point annotation should not get highlighted when user perform right click on distance annotation"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ah4PatientName,1);
	
		pointAnn =new PointAnnotation(driver);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify drawn annotaion is current active GSPS" );
		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.drawPointAnnotationMarkerOnViewbox(1,50,50);
		pointAnn.assertTrue(pointAnn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify point annotation is current active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		pointAnn.performMouseRightClick(pointAnn.getHandlesOfPoint(1,1).get(0));
		pointAnn.assertTrue(pointAnn.isElementPresent(pointAnn.cutOption), "Verifying the cut option is displayed", "verified");
		pointAnn.assertTrue(pointAnn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify point annotation is not active GSPS object", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify drawn annotaion is active GSPS after finishing editing of annotation");
		pointAnn.movePoint(1, 1, 10, 0);
		pointAnn.assertTrue(pointAnn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify point annotation is active GSPS object", "Verified");
	}
	
	@Test(groups ={"IE11","Chrome","US520","DE1329"})
	public void test18_US520_TC1697_DE1329_TC5646_verifyPointFunctionWithGSPSData() throws IOException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify point annotation behaviour (select, move, delete , resize) for GSPS image");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName,1);
	
		pointAnn = new PointAnnotation(driver);
		

		//		viewerpage.selectPointAnnotationFromContextMenu(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that point annotation is selected and enabled");

		pointAnn.assertTrue(pointAnn.verifyPendingPointAnnotation(1, 1),"Verifying Point", "gsps point is displayed");
		pointAnn.assertEquals(pointAnn.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"verifying the point#1 properties", "properties are verified");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

		List<WebElement> point = pointAnn.getPoint(1, 1);

		double xLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1)));
		double xRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))); 

		double yLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1)));
		double yRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1)));

		double xTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1)));
		double xBottomtDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1)));		

		double yTopDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)));
		double yBottomDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));

		pointAnn.movePoint(1, 1, 60, 0);
		
		point = pointAnn.getPointDetails(1, 1);

		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1))), xLeftDiff1, "Verifying the x for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1))), xRightDiff2, "Verifying the x for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1))), yLeftDiff1, "Verifying the y for left line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1))), yRightDiff2, "Verifying the y for right line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1))), xTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1))), xBottomtDiff2, "Verifying the x for bottom line", "verified");

		pointAnn.assertEquals((Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1))), yTopDiff1, "Verifying the x for top line", "verified");
		pointAnn.assertEquals((Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1))), yBottomDiff2, "Verifying the x for bottom line", "verified");


		pointAnn.assertEquals(point.get(point.size()-1).getAttribute(NSGenericConstants.R), ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX, "Verifying the handler radius is same","It is 5Px");

		// Can't delete already created user annotation
		pointAnn.deletePoint(1, 1);

		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1 - verification for another point creation", "point#1 is absent");

		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);

		pointAnn.assertTrue(pointAnn.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"Verifying Point", "gsps point is displayed");
		pointAnn.assertEquals(pointAnn.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"verifying the point#1 properties", "properties are verified");
		pointAnn.assertTrue(pointAnn.isPointPresent(1,1),"verifying the point#1", "point is present");

	}
		
	@Test(groups = {"Chrome", "IE11", "Edge", "DE989", "Positive"})
	public void test19_DE989_TC4063_verifyUpdatedPointAnnotationLocationIsRetainedAfterViewerReload() throws  InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify updated point annotation location is retained after viewer re-load");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		pointAnn = new PointAnnotation(driver);
		pointAnn.doubleClickOnViewbox(1);
		pointAnn.waitForViewerpageToLoad();
		//Selecting Point from radial menu
		pointAnn.selectPointFromQuickToolbar(1);
		
		pointAnn.drawPointAnnotationMarkerOnViewbox(1, 25, 15);
		pointAnn.dragAndReleaseOnViewerWithClick(1, 25, 15, 35, 30);
		String initX = pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CX);
		String initY = pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CY);
		
		helper.browserBackAndReloadViewer(patientName, 1, 1);
		pointAnn.assertTrue((pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CX) == initX && 
				pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CY) == initY), "Checkpoint[1/1]", "Verified updated point annotation location is retained after viewer re-load");
		pointAnn.deleteAllAnnotation(1);
		
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE989", "Positive"})
	public void test20_DE989_TC4062_verifyUpdatedPointAnnotationLocationRetainedAfterLoadingFromContentSelector() throws  InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify updated point annotation location is retained after selecting this series from content selector");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
	
		pointAnn = new PointAnnotation(driver);
		pointAnn.doubleClickOnViewbox(1);
		pointAnn.waitForViewerpageToLoad();
		
		ViewerLayout layout = new ViewerLayout(driver);
		//Selecting Point from radial menu
		pointAnn.selectPointFromQuickToolbar(1);
		
		firstSeriesDescriptionMultiSeries= pointAnn.getSeriesDescriptionOverlayText(1);
		
		pointAnn.drawPointAnnotationMarkerOnViewbox(1, 25, 15);
		pointAnn.dragAndReleaseOnViewerWithClick(1, 25, 15, 35, 30);
		String initX = pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CX);
		String initY = pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CY);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		pointAnn.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);
		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		pointAnn.doubleClickOnViewbox(1);
		pointAnn.waitForViewerpageToLoad();
		pointAnn.assertTrue((pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CX) == initX && 
				pointAnn.getAttributeValue(pointAnn.getPoint(1, 1).get(1),NSGenericConstants.CY) == initY), "Checkpoint[1/1]", "Verified updated point annotation location is retained after selecting this series from content selector");
		pointAnn.deleteAllAnnotation(1);
		
	}


}

