package com.trn.ns.test.viewer.GSPS;
import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MirrorROISupportTest extends TestBase {


	private ExtentTest extentTest;
	private SimpleLine mirror;
	private ContentSelector cs;

	private HelperClass helper; 
	
	String ctpFilepath=Configurations.TEST_PROPERTIES.get("S2008-3CTP_Filepath");
	String ctpPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,ctpFilepath);
	
	String liverFilepath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liverPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,liverFilepath);

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private OutputPanel panel;
	private PointAnnotation point;
	private CircleAnnotation circle;

	int totalMirrorLines = 2;

	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test01_US2824_TC10965_verifyMirrorLinePresence() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that GSPS drawn Mirror line is present on view box.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName,username, password, 1);
		
		mirror = new SimpleLine(driver);
		
		mirror.assertEquals(mirror.getAllLines(1).size(),totalMirrorLines,"Checkpoint[1/5]","verifying the mirror lines are present");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/5]","verifying the mirror lines (1) are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[3/5]","verifying the mirror lines (2)are in pending state");
		
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[4/5]", "verifyng that there is no finding");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[5/5]","verifying there is no marks present on slider");

		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test02_01_US2824_TC10965_verifyMirrorLineAccepted() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit the mirror line by moving, by accepting, or by rejecting.");

		
		helper = new HelperClass(driver);
		helper.loadPatientsPageDirectly(username, password);
		
		helper.loadViewerPageUsingSearch(ctpPatientName, 1, 1);
		
		
		mirror = new SimpleLine(driver);
		cs = new ContentSelector(driver);
				
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/15]","verifying the mirror lines are in pending non focused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/15]","verifying the mirror lines are in pending focused state");

		List<String> results = cs.getAllResults();
		mirror.assertEquals(mirror.getAllLines(1).size(),totalMirrorLines,"Checkpoint[3/15]","verifying the total mirror lines are present");
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the mirror line and verifying the mirror lines");
		mirror.selectLine(1, 2);
		mirror.selectAcceptfromGSPSRadialMenu(1);
		
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/15]","verifying the mirror lines are in pending focused state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/15]","verifying the mirror lines are in accepted non-focused state");
	
		
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[6/15]","Verifying that Mirror lines are not displayed in finding menu");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[7/15]", "verifyng that there is no pending finding");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[8/15]", "verifying that on accepting the mirror line clone is created");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[9/15]","verifying there is no marks present on slider");
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "reloading the viewer and verifying the persistence");
		helper.browserBackAndReloadViewer(ctpPatientName, 1, 1);
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/15]","verifying the mirror lines are in pending non focused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/15]","verifying the mirror lines are in pending focused state");
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[12/15]","Verifying that mirror line is not present under finding menu");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[13/15]","verifying there is no marks present on slider");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[14/15]", "verifyng that there is no finding");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[15/15]", "verifying that clones are still persisted");

		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test02_02_US2824_TC10965_verifyMirrorLineRejected() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit the mirror line by moving, by accepting, or by rejecting.");

		
		helper = new HelperClass(driver);
		helper.loadPatientsPageDirectly(username, password);
		
		helper.loadViewerPageUsingSearch(ctpPatientName, 1, 1);
		
		
		mirror = new SimpleLine(driver);
		cs = new ContentSelector(driver);
				
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/15]","verifying the mirror lines are in pending unfocused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/15]","verifying the mirror lines are in pending focused state");

		List<String> results = cs.getAllResults();
		mirror.assertEquals(mirror.getAllLines(1).size(),totalMirrorLines,"Checkpoint[3/15]","verifying the mirror lines are present");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "rejecting the mirror line");
		mirror.selectLine(1, 2);
		mirror.selectRejectfromGSPSRadialMenu(1);
		
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/15]","verifying the mirror lines are in pending focused state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/15]","verifying the mirror lines are in rejected unfocused state");
	
		
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[6/15]","Verifying that finding menu is not opened as mirror line should not be displayed in finding menu");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[7/15]", "verifyng mirror line should not be considered as finding hence badge count is displayed as 0");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[8/15]", "verifying that on accepting the mirror line clone is created");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[9/15]","verifying there is no marks present on slider");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reloading the viewer and verifying the persistence");
		helper.browserBackAndReloadViewer(ctpPatientName, 1, 1);
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/15]","verifying the mirror lines are in pending unfocused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/15]","verifying the mirror lines are in rejected focused state");
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[12/15]","Verifying that finding menu is not opened");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[13/15]","verifying there is no marks present on slider");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[14/15]", "verifyng that there is no finding");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[15/15]", "verifying that clone is persisted");

		
	}
		
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test02_03_US2824_TC10965_verifyMirrorLineMove() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit the mirror line by moving, by accepting, or by rejecting.");

		
		helper = new HelperClass(driver);
		helper.loadPatientsPageDirectly(username, password);
		
		helper.loadViewerPageUsingSearch(ctpPatientName, 1, 1);
		
		mirror = new SimpleLine(driver);
		cs = new ContentSelector(driver);
						
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/15]","verifying the mirror lines are in pending unfocused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/15]","verifying the mirror lines are in pending focused state");

		List<String> results = cs.getAllResults();
		mirror.assertEquals(mirror.getAllLines(1).size(),totalMirrorLines,"Checkpoint[3/15]","verifying the mirror lines are present");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the state after editing/ moving the mirror line");
		mirror.moveLine(1, 2, 20, 20);
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[4/15]","verifying the mirror lines are in pending unfocused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/15]","verifying the mirror lines are in pending focused state");
	
		
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[6/15]","Verifying mirror lines are not displayed in finding menu");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[7/15]", "verifyng that there is no finding");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[8/15]", "verifying that clone is created after editing mirror line");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[9/15]","verifying there is no marks present on slider");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reload the viewer and verifying the persistence");
		helper.browserBackAndReloadViewer(ctpPatientName, 1, 1);
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/15]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/15]","verifying the mirror lines are in accepted focused state");
		mirror.assertFalse(mirror.verifyFindingMenuIsDisplyed(1),"Checkpoint[12/15]","Verifying mirror line is not displayed in menu");
		mirror.assertTrue(mirror.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[13/15]","verifying there is no marks present on slider");
		mirror.assertEquals(mirror.getBadgeCountFromToolbar(1), 0, "Checkpoint[14/15]", "verifyng that badge count for pending/ accepted menu");
		cs.assertEquals(cs.getAllResults().size(), results.size()+2, "Checkpoint[15/15]", "verifying that clones are persisted");

		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286","US2825"})
	public void test03_01_US2824_TC10967_TC11044_TC11046_US2825_TC11171_TC11178_TC11181_TC11182_verifyMirrorAnnotationCreationPoint() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user draws an annotation on either one of the side of mirror line, mirror annotation gets created."
				+ "<br> Verify the name of mirrored annotation and its order in finding menu."
				+ "<br> Verify that state of mirrored object is updated in thumbnail in output panel synchronously on changing dimension of user drawn annotation or vice a versa."
				+ "<br> Verify that labels are assigned to annotations when user draws annotation on along the mirror line."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in finding menu."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in vertical finding scroll bar."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in thumbnail image.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/12]","verifying the mirror lines are in pending non focused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/12]","verifying the mirror lines are in pending focused state");
	
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
		int count = panel.thumbnailList.size();
		
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating point and verifying its state");
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 10);
				
		int points = point.getAllPoints(1).size();
		point.assertEquals(points, totalMirrorLines, "Checkpoint[3.1/12]", "Upon creation of point two points are created");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[3.2/12]", "First Point is focused");
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[4/12]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/12]","verifying the mirror lines are in pending state - state is not changed if we create annotation");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/12]","verifying the mirror lines are in pending state - state is not changed if we create annotation");

		List<String> tags = new ArrayList<String>();
		for (int i = 1; i <= points; i++) {
			tags.add(point.getLabel(1, i));
		}
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), totalMirrorLines, "Checkpoint[7/12]", "Verifying that these two points are also present in Output panel");
		
		for (int i = 1, j=panel.thumbnailList.size()-1; i <= panel.thumbnailList.size(); i++,j--) {
			
			panel.assertTrue(panel.verifyPointIsPresentInThumbnail(i),"Checkpoint[8/12]","verifying that point is present in thumbnail");
			panel.assertEquals(panel.getROILabelInThumbnail(i),tags.get(j),"Checkpoint[9/12]","Verifying that tag displayed on viewer is also displayed in thumbnail");
		}
				
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), count, "Checkpoint[10/12]", "Verifying that these two points are also present in Output panel");
		panel.openAndCloseOutputPanel(false);
		
		List<String> findingNames = panel.getFindingNames(1);
		List<String> findingNamesSliderMenu = panel.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.assertTrue(findingNames.size()==totalMirrorLines, "Checkpoint[11.1/12]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		panel.assertTrue(findingNames.size()==findingNamesSliderMenu.size(), "Checkpoint[11.2/12]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		
		for (int i = 0, j=points-1; i <points; i++,j--) {
			
		panel.assertTrue(findingNames.get(i).contains(tags.get(j)),"Checkpoint[12.1/12]","Verifying that tag is also displayed in finding menu");
		panel.assertTrue(findingNamesSliderMenu.get(i).contains(tags.get(j)),"Checkpoint[12.2/12]","Verifying that tag is also displayed in finding menu");
		
	}
		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286","US2825"})
	public void test03_02_US2824_TC10967_TC11044_TC11046_US2825_TC11171_TC11178_TC11181_TC11182_verifyMirrorAnnotationCreationCircle() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user draws an annotation on either one of the side of mirror line, mirror annotation gets created."
				+ "<br> Verify the name of mirrored annotation and its order in finding menu."
				+ "<br> Verify that state of mirrored object is updated in thumbnail in output panel synchronously on changing dimension of user drawn annotation or vice a versa."
				+ "<br> Verify that labels are assigned to annotations when user draws annotation on along the mirror line."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in finding menu."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in vertical finding scroll bar."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in thumbnail image.");


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/15]","verifying the mirror lines are in pending unfocused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/15]","verifying the mirror lines are in pending focused state");
	
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
		int count = panel.thumbnailList.size();
		
		
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
	
		circle.drawCircle(1, 50, 10,50,50);
				
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[3/15]", "Upon creation of circle two circle are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[4/15]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[5/15]", "Mirrored circle is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/15]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/15]","verifying the mirror lines are in pending state");

		List<String> tags = new ArrayList<String>();
		for (int i = 1; i <= circles; i++) {
			tags.add(circle.getLabel(1, i));
		}
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), totalMirrorLines, "Checkpoint[8/15]", "Verifying that these two circles are also present in Output panel");
		
		for (int i = 1, j=panel.thumbnailList.size()-1; i <= panel.thumbnailList.size(); i++,j--) {
			
			panel.assertTrue(panel.verifyCircleIsPresentInThumbnail(i),"Checkpoint[9/15]","verifying that circle is present in thumbnail");
			panel.assertEquals(panel.getROILabelInThumbnail(i),tags.get(j),"Checkpoint[10/15]","Verifying that tag displayed on viewer is also displayed in thumbnail");
		}
				
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), count, "Checkpoint[11/15]", "Verifying that existings findings are also present in Output panel");
		panel.openAndCloseOutputPanel(false);
		
		List<String> findingNames = panel.getFindingNames(1);
		List<String> findingNamesSliderMenu = panel.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.assertTrue(findingNames.size()==totalMirrorLines, "Checkpoint[12/15]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		panel.assertTrue(findingNames.size()==findingNamesSliderMenu.size(), "Checkpoint[13/15]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		for (int i = 0, j=circles-1; i <circles; i++,j--) {
			
		panel.assertTrue(findingNames.get(i).contains(tags.get(j)),"Checkpoint[14/15]","Verifying that tag is also displayed in finding menu");
		panel.assertTrue(findingNamesSliderMenu.get(i).contains(tags.get(j)),"Checkpoint[15/15]","Verifying that tag is also displayed in finding menu");
		
		}
		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286","US2825"})
	public void test03_03_US2824_TC10967_TC11044_TC11046_US2825_TC11171_TC11178_TC11181_TC11182_verifyMirrorAnnotationCreationPolyline() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user draws an annotation on either one of the side of mirror line, mirror annotation gets created."
				+ "<br> Verify the name of mirrored annotation and its order in finding menu."
				+ "<br> Verify that state of mirrored object is updated in thumbnail in output panel synchronously on changing dimension of user drawn annotation or vice a versa."
				+ "<br> Verify that labels are assigned to annotations when user draws annotation on along the mirror line."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in finding menu."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in vertical finding scroll bar."
				+ "<br> Verify that labels assigned to  user drawn annotation and mirrored annotations are visible in thumbnail image.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/15]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/15]","verifying the mirror lines are in pending state");
	
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
		int count = panel.thumbnailList.size();
		
		PolyLineAnnotation polyline = new PolyLineAnnotation(driver);
		polyline.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		polyline.drawFreehandPolyLine(1, abc);
		
				
		int polylines = polyline.getAllPolylines(1).size();
		polyline.assertEquals(polylines, 2, "Checkpoint[3/15]", "Upon creation of point two points are created");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[4/15]", "First polyline is focused");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[5/15]", "Mirrored polyline is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/15]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/15]","verifying the mirror lines are in pending state");

		List<String> tags = new ArrayList<String>();
		for (int i = 1; i <= polylines; i++) {
			tags.add(polyline.getLabel(1, i));
		}
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), totalMirrorLines, "Checkpoint[8/15]", "Verifying that these polylines are also present in Output panel");
		
		for (int i = 1, j=panel.thumbnailList.size()-1; i <= panel.thumbnailList.size(); i++,j--) {
			
			panel.assertTrue(panel.verifyPolyLineIsPresentInThumbnail(i),"Checkpoint[9/15]","verifying that polyline is present in thumbnail");
			panel.assertEquals(panel.getROILabelInThumbnail(i),tags.get(j),"Checkpoint[10/15]","Verifying that tag displayed on viewer is also displayed in thumbnail");
		}
				
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), count, "Checkpoint[11/15]", "Verifying that existing findings are also present in Output panel");
		panel.openAndCloseOutputPanel(false);
		
		List<String> findingNames = panel.getFindingNames(1);
		List<String> findingNamesSliderMenu = panel.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.assertTrue(findingNames.size()==totalMirrorLines, "Checkpoint[12/15]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		panel.assertTrue(findingNames.size()==findingNamesSliderMenu.size(), "Checkpoint[13/15]", "verifying that there are two findings displayed 1 user drawn and 1 mirrored one");
		
		for (int i = 0, j=polylines-1; i <polylines; i++,j--) {
			
		panel.assertTrue(findingNames.get(i).contains(tags.get(j)),"Checkpoint[14/15]","Verifying that tag is also displayed in finding menu");
		panel.assertTrue(findingNamesSliderMenu.get(i).contains(tags.get(j)),"Checkpoint[15/15]","Verifying that tag is also displayed in finding menu");
		
		}
		
		
	}
		
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test04_US2824_TC10968_TC10973_TC10972_TC11046_verifyMirrorAnnotationUpdationPoint() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the behavior on Accept / Reject / Delete of any mirror annotation."
				+ "<br> Verify that user is able to add comment on annotation and its behavior."
				+ "<br> Verify that on reload, the mirroring is preserved"
				+ "<br> Verify that state of mirrored object is updated in thumbnail in output panel synchronously on changing dimension of user drawn annotation or vice a versa.");

		
		helper = new HelperClass(driver);
		helper.loadPatientsPageDirectly(username, password);
		helper.loadViewerPageUsingSearch(ctpPatientName, 1, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/31]","verifying the mirror lines are present and in pending non focused state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/31]","verifying the mirror lines are in pending focused state");
	
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
		int count = panel.thumbnailList.size();
		
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 10);
				
		int points = point.getAllPoints(1).size();
		point.assertEquals(points, totalMirrorLines, "Checkpoint[3/31]", "Upon creation of point two points are created");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[4/31]", "First Point is focused");
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[5/31]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/31]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/31]","verifying the mirror lines are in pending state");

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting the point and verifying the state");
		point.selectPoint(1, 2);
		point.selectRejectfromGSPSRadialMenu();
				
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 2), "Checkpoint[8/31]", "First Point is focused and rejected");
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, 1), "Checkpoint[9/31]", "Mirrored Point is not focused and gets rejected");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/31]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/31]","verifying the mirror lines are in pending state");

		
		List<String> tags = new ArrayList<String>();
		for (int i = 1; i <= points; i++) {
			tags.add(point.getLabel(1, i));
		}
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), points, "Checkpoint[12/31]", "Verifying that these two points are also present in Output panel under rejected filter");
		
		for (int i = 1; i <= panel.thumbnailList.size(); i++) {
			
			panel.assertTrue(panel.verifyPointIsPresentInThumbnail(i),"Checkpoint[13/31]","verifying that point is present in thumbnail");
			panel.assertEquals(panel.getROILabelInThumbnail(i),tags.get(i-1),"Checkpoint[14/31]","Verifying that tag displayed on viewer is also displayed in thumbnail");
		}
				
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), count, "Checkpoint[15/31]", "Verifying that there is no change in existing findings in OP");
		panel.openAndCloseOutputPanel(false);
		
		List<String> findingNames = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> findingNamesSliderMenu = panel.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.REJECTED_FINDING_COLOR);
		for (int i = 0; i <points; i++) {
			
		panel.assertTrue(findingNames.get(i).contains(tags.get(i)),"Checkpoint[16/31]","Verifying that tag is also displayed in finding menu");
		panel.assertTrue(findingNamesSliderMenu.get(i).contains(tags.get(i)),"Checkpoint[17/31]","Verifying that tag is also displayed in finding menu");
		
	}
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "reloading the viewer and verifying the persistence");
		helper.browserBackAndReloadViewer(ctpPatientName, 1, 1);
		
		for (int i = 1; i <=points; i++) 
			point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, i), "Checkpoint[18/31]", "First Point is focused and rejected");

		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[19/31]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[20/31]","verifying the mirror lines are in pending state");

		String comment = "comment";
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Adding comment to finding");
		point.selectPoint(1, 1);
		point.addResultComment(1, comment);		
		point.assertEquals(point.getTextCommentForPoint(1, 2),comment,"Checkpoint[21/31]","Verifying that comment is added");
		point.assertEquals(point.resultComment.size(),1, "Checkpoint[21/31]", "Verified comment is not added to  mirrored annotation");
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[22/31]", "First Point is focused and accepted");
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, 1), "Checkpoint[23/31]", "Mirrored Point is not focused and in rejected state");
		point.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, comment),"Checkpoint[24/31]","verifying that comment is also displayed in finding menu");
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		point.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingNames.get(0), comment),"Checkpoint[25/31]","verifying that comment is also displayed in slider menu");
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), 1,"Checkpoint[26/31]", "Verifying that upon addition of comment the point state is changed");
		
		panel.enableFiltersInOutputPanel(true,false, false);
		panel.assertEquals(panel.thumbnailList.size(),points-1, "Checkpoint[27/31]", "Verifying that upon addition of comment the state is changed to accepted");
		panel.assertTrue(panel.getCommentText(1).contains(comment),"Checkpoint[28/31]", "Verifying that comment is also displayed in OP");
		
		point.deletePoint(1, 1);
		
		panel.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[29/31]", "verifying that no point (user drawn and mirrored) persist upon deletion of point");
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[30/31]", "Verifying point is deleted from OP");
		
		panel.enableFiltersInOutputPanel(true,false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[31/31]", "Verifying mirror point is deleted from OP");

		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test05_US2824_TC10969_verifyMirrorLineBehavior() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the behavior on deleting mirror line.");
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/24]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/24]","verifying the mirror lines are in pending state");
	
		cs = new ContentSelector(driver);
		int results = cs.getAllResults().size();
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 10);
		
		int points = point.getAllPoints(1).size();
		point.assertEquals(points, totalMirrorLines, "Checkpoint[3/24]", "Upon creation of point two points are created");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[4/24]", "First Point is focused");
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[5/24]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/24]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/24]","verifying the mirror lines are in pending state");

		cs.openAndCloseSeriesTab(true);
		cs.assertEquals(cs.getAllResults().size(),(results),"Checkpoint[8/24]","verifying that clones are created");
	
		
		mirror.selectLine(1, 1,3,4);
		mirror.pressKeys(Keys.DELETE);
		mirror.waitForTimePeriod(2000);
		
		for (int i = 1; i <= points; i++)			
			point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, i), "Checkpoint[9/24]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[10/24]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/24]","verifying the mirror lines are in pending state");

		cs.assertEquals(cs.getAllResults().size(),(results),"Checkpoint[12/24]","verifying that clones are created");
		
		
		mirror.selectLine(1, 1,3,4);
		mirror.selectAcceptfromGSPSRadialMenu();
		
		for (int i = 1; i <= points; i++)			
			point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, i), "Checkpoint[13/24]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[14/24]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[15/24]","verifying the mirror lines are in pending state");

		cs.assertEquals(cs.getAllResults().size(),(results),"Checkpoint[16/24]","verifying that clones are created");
		
		mirror.deleteAllAnnotation(1);
		point.assertTrue(point.getAllPoints(1).isEmpty(), "Checkpoint[17/24]", "Mirrored Point is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[18/24]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[19/24]","verifying the mirror lines are in pending state");

		cs.assertEquals(cs.getAllResults().size(),(results+2),"Checkpoint[20/24]","verifying that clones are created");
		
		
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
	
		circle.drawCircle(1, 50, 10,50,50);
		
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[21/24]", "Upon creation of circle two circle are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[22/24]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 2), "Checkpoint[23/24]", "Mirrored circle is not focused");
	
		for (int i = 1; i <= points; i++)			
			mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[24/24]","verifying the mirror lines are in pending state");
		
			
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test06_US2824_TC10970_verifyEditingOfUserDrawnAnn() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that editing or moving User drawn measurement will also edit/move the mirrored one and vice a versa.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/17]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/17]","verifying the mirror lines are in pending state");
	

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
	
		circle.drawCircle(1, 50, 10,50,50);
				
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[3/17]", "Upon creation of circle two circle are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[4/17]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[5/17]", "Mirrored circle is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/17]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/17]","verifying the mirror lines are in pending state");

		int x1 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y1 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x2 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y2 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		
		circle.moveElement(circle.getAllCircles(1).get(1), 10, 10);
		
		int x11 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y11 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x12 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y12 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		circle.assertNotEquals(x11, x1, "Checkpoint[8/17]", "Verifying that circle is moved");
		circle.assertNotEquals(y11, y1, "Checkpoint[9/17]", "Verifying that circle is moved");
	
		circle.assertNotEquals(x12, x2, "Checkpoint[10/17]", "Verifying that circle is moved");
		circle.assertNotEquals(y12, y2, "Checkpoint[11/17]", "Verifying that circle is moved");
		
		circle.assertEquals(circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R),circle.getCircle(1, 2).get(0).getAttribute(NSGenericConstants.R),"Checkpoint[12/17]","both circle are still of same size");
		
		x1 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		y1 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		x2 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		y2 =circle.getYCoordinate(circle.getAllCircles(1).get(1));

		circle.mouseHover(circle.EurekaLogo);
		circle.resizeCircle(30, 30);
		
		 x11 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		 y11 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		 x12 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		 y12 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		circle.assertNotEquals(x11, x1, "Checkpoint[13/17]", "Verifying that circle is moved");
		circle.assertNotEquals(y11, y1, "Checkpoint[14/17]", "Verifying that circle is moved");
	
		circle.assertNotEquals(x12, x2, "Checkpoint[15/17]", "Verifying that circle is moved");
		circle.assertNotEquals(y12, y2, "Checkpoint[16/17]", "Verifying that circle is moved");
	
		circle.assertEquals(circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R),circle.getCircle(1, 2).get(0).getAttribute(NSGenericConstants.R),"Checkpoint[17/17]","both circle are still of same size");
		

		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test07_US2824_TC10971_verifyMovementOfMirrorLine() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that moving mirror line will update only mirrored measurements as well.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/17]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/17]","verifying the mirror lines are in pending state");
	

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
	
		circle.drawCircle(1, 50, 10,50,50);
				
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[3.1/17]", "Upon creation of circle two circle are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[3.2/17]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[4/17]", "Mirrored circle is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[5/17]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/17]","verifying the mirror lines are in pending state");

		int x1 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y1 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x2 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y2 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		int line1x = mirror.getXCoordinate(mirror.getLine(1, 1).get(0));
		int line1y = mirror.getYCoordinate(mirror.getLine(1, 1).get(0));
		
		int line2x = mirror.getXCoordinate(mirror.getLine(1, 2).get(0));
		int line2y = mirror.getYCoordinate(mirror.getLine(1, 2).get(0));
		
		mirror.moveLine(1, 1, 30, 30);
		mirror.moveLine(1, 1, 80, -50);
		
		int x11 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y11 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x12 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y12 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		circle.assertNotEquals(x11, x1, "Checkpoint[7/17]", "Verifying that circle is moved upon moving the mirror line");
		circle.assertNotEquals(y11, y1, "Checkpoint[8/17]", "Verifying that circle is moved upon moving the mirror line");
	
		circle.assertEquals(x12, x2, "Checkpoint[9/17]", "Verifying that user drwan circle is not moved");
		circle.assertEquals(y12, y2, "Checkpoint[10/17]", "Verifying that user drwan circle is not moved");
		
		int line1x1 = mirror.getXCoordinate(mirror.getLine(1, 1).get(0));
		int line1y1 = mirror.getYCoordinate(mirror.getLine(1, 1).get(0));
		
		int line2x1 = mirror.getXCoordinate(mirror.getLine(1, 2).get(0));
		int line2y1 = mirror.getYCoordinate(mirror.getLine(1, 2).get(0));

		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[11/17]","verifying the mirror lines are in accepted state after movement");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[12/17]","verifying the mirror lines are in accepted stateafter movement");

		circle.assertNotEquals(line1x, line1x1, "Checkpoint[13/17]", "Verifying that circle is moved");
		circle.assertNotEquals(line1y, line1y1, "Checkpoint[14/17]", "Verifying that circle is moved");
		
		circle.assertNotEquals(line2x, line2x1, "Checkpoint[15/17]", "Verifying that circle is moved");
		circle.assertNotEquals(line2y, line2y1, "Checkpoint[16/17]", "Verifying that circle is moved");

		
		for (int i = 1; i <=circle.getAllCircles(1).size(); i++) 
			circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, i), "Checkpoint[17/17]", "Verifying the circle state is not changed");
		
		
	}

	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286","US2825"})
	public void test08_US2824_TC11045_TC11049_US2825_TC11183_verifyNavigationOrderAndOneup() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the navigation order when user drawn annotation + Mirrored annotation +Mirror line are present on viewer."
				+ "<br> Verify that count in finding menu is getting incremented only on drawing annotation not only just by click or by one up and one down."
				+ "<br> Verify that mirrored line (major and minor axis), which comes from the GSPS, will NOT show in the finding list or output panel.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/37]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/37]","verifying the mirror lines are in pending state");
	
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);		
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 10);
		point.assertEquals(point.getAllPoints(1).size(), totalMirrorLines, "Checkpoint[3/37]", "Upon creation of point two points are created");
	
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);	
		circle.drawCircle(1, 80, 40,50,50);
		circle.assertEquals(circle.getAllCircles(1).size(), totalMirrorLines, "Checkpoint[4/37]", "Upon creation of circle two circle are created");
		
		PolyLineAnnotation polyline = new PolyLineAnnotation(driver);
		polyline.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		polyline.drawFreehandPolyLine(1, abc);
		polyline.assertEquals(polyline.getAllPolylines(1).size(), totalMirrorLines, "Checkpoint[5/37]", "Upon creation of polyline two polylines are created");
		
		List<String> findings = polyline.getFindingNames(1);
		
		polyline.assertEquals(findings.size(), 3*totalMirrorLines, "Checkpoint[6.1/37]", "Verifying the total findings in menu");
		polyline.doubleClickOnViewbox(1);
		polyline.assertEquals(polyline.getFindingNames(1), findings, "Checkpoint[6.2/37]", "Verifying that findings are not added in menu in oneup");
		
		polyline.doubleClickOnViewbox(1);
		polyline.assertEquals(polyline.getFindingNames(1), findings, "Checkpoint[7/37]", "Verifying that findings are not added in menu in restoring oneup");
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, true, true);
		for (int i = 1; i <= panel.thumbnailList.size(); i++)
			panel.assertFalse(panel.verifyLineIsPresentInThumbnail(i), "Checkpoint[8/37]", "Verifying that mirrorline is not present in OP ");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		for (int i = 1, j=3; i <= 3; i++,j--)
			panel.assertEquals(panel.getROILabelInThumbnail(i),j+ViewerPageConstants.LEFT_OR_OVERLAY,"Checkpoint[9/37]","Verifying that tag displayed on viewer is also displayed in thumbnail");
		
		for (int i = 4; i <= panel.thumbnailList.size(); i++)
			panel.assertTrue(panel.getROILabelInThumbnail(i).contains(ViewerPageConstants.RIGHT_OR_OVERLAY),"Checkpoint[10/37]","Verifying that tag displayed on viewer is also displayed in thumbnail");
	
		
		panel.openAndCloseOutputPanel(false);
		
		List<String> findingNames = panel.getFindingNames(1);
		List<String> findingNamesSliderMenu = panel.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		for (int i = 0; i < findingNames.size(); i++) {			
			
			if(findingNames.get(i).contains(ViewerPageConstants.LINE))
			{
				panel.assertTrue(findingNames.get(i).contains(ViewerPageConstants.POLYLINE),"Checkpoint[11/37]","Verifying that tag is also displayed in finding menu");
				panel.assertTrue(findingNamesSliderMenu.get(i).contains(ViewerPageConstants.POLYLINE),"Checkpoint[12/37]","Verifying that tag is also displayed in finding menu");

			}else {
				panel.assertFalse(findingNames.get(i).contains(ViewerPageConstants.LINE),"Checkpoint[13/37]","Verifying that tag is also displayed in finding menu");
				panel.assertFalse(findingNamesSliderMenu.get(i).contains(ViewerPageConstants.LINE),"Checkpoint[14/37]","Verifying that tag is also displayed in finding menu");
			}

		}
		int i =1;
		point.selectPoint(1, 1);
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[15/37]", "Verifying that user drawn annotation is selected first");
		point.assertEquals(point.getLabel(1, 1),i+ViewerPageConstants.LEFT_OR_OVERLAY,"Checkpoint[16/37]","Verifying that tag on first Point");
		point.assertTrue(point.getSelectedFindingName().contains(point.getLabel(1, 1)),"Checkpoint[17/37]","Verifying the same finding is selected in finding menu");
		
		point.selectNextfromGSPSRadialMenu();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[18/37]", "Verifying that user drawn annotation is selected first");
		point.assertEquals(point.getLabel(1, 2),i+ViewerPageConstants.RIGHT_OR_OVERLAY,"Checkpoint[19/37]","Verifying that tag on second Point");
		point.assertTrue(point.getSelectedFindingName().contains(point.getLabel(1, 2)),"Checkpoint[20/37]","Verifying the same finding is selected in finding menu");
	
		i++;
		point.selectNextfromGSPSRadialMenu();
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[21/37]", "Verifying that user drawn annotation is selected first");
		circle.assertEquals(circle.getLabel(1, 1),i+ViewerPageConstants.LEFT_OR_OVERLAY,"Checkpoint[22/37]","Verifying that tag on first Circle");
		circle.assertTrue(circle.getSelectedFindingName().contains(circle.getLabel(1, 1)),"Checkpoint[23/37]","Verifying the same finding is selected in finding menu");
	
		
		point.selectNextfromGSPSRadialMenu();
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[24/37]", "Verifying that user drawn annotation is selected first");

		circle.assertEquals(circle.getLabel(1, 2),i+ViewerPageConstants.RIGHT_OR_OVERLAY,"Checkpoint[25/37]","Verifying that tag on second Circle");
		circle.assertTrue(circle.getSelectedFindingName().contains(circle.getLabel(1,2)),"Checkpoint[26/37]","Verifying the same finding is selected in finding menu");
	
		i++;
		point.selectNextfromGSPSRadialMenu();
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[27/37]", "Verifying that user drawn annotation is selected first");
		polyline.assertEquals(polyline.getLabel(1, 1),i+ViewerPageConstants.LEFT_OR_OVERLAY,"Checkpoint[28/37]","Verifying that tag on first polyline");
		polyline.assertTrue(polyline.getSelectedFindingName().contains(polyline.getLabel(1, 1)),"Checkpoint[29/37]","Verifying the same finding is selected in finding menu");
	
		point.selectNextfromGSPSRadialMenu();
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[30/37]", "Verifying that user drawn annotation is selected first");
		polyline.assertEquals(polyline.getLabel(1, 2),i+ViewerPageConstants.RIGHT_OR_OVERLAY,"Checkpoint[31/37]","Verifying that tag on second polyline");
		polyline.assertTrue(polyline.getSelectedFindingName().contains(polyline.getLabel(1, 2)),"Checkpoint[32/37]","Verifying the same finding is selected in finding menu");
	
		int slice = point.getCurrentScrollPositionOfViewbox(1);
		int phase = point.getValueOfCurrentPhase(1);
		point.selectNextfromGSPSRadialMenu();		
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[33/37]", "verifying that slice is moved to another");
		point.assertNotEquals(point.getValueOfCurrentPhase(1), phase, "Checkpoint[34/37]", "verifying that slice is moved to another");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[35/37]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[36/37]","verifying the mirror lines are in pending state");
		mirror.assertEquals(mirror.getAllGSPSObjects(1).size(),totalMirrorLines, "Checkpoint[37/37]", "Verifying that another slice has no objects created by user");
		
	}
		
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test09_US2824_TC11047_verifyCutCopyPaste() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that mirroring remains even when mirrored annotations are cut and pasted to different slice.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/20]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/20]","verifying the mirror lines are in pending state");
	

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false,true);
		int pendingList = panel.thumbnailList.size();
		
		panel.enableFiltersInOutputPanel(true, false, false);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);	
		circle.drawCircle(1, 80, 40,50,50);
		circle.assertEquals(circle.getAllCircles(1).size(), totalMirrorLines, "Checkpoint[3/20]", "Upon creation of circle two circle are created");
		panel.waitForThumbnailToGetDisplayed();
		
		circle.selectAcceptfromGSPSRadialMenu();
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[4/20]", "Verifying that OP is changed");
		
		circle.selectCutUsingContextMenu(1, 1);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_THUMBNAIL), "Checkpoint[5/20]", "verify the circle is cut");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[6/20]", "verify the circle is not cut");
		
		circle.scrollDownToSliceUsingKeyboard(1);
		
		circle.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);		
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[7/20]", "verify the circle is pasted onto other slice");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[8/20]", "verify mirror circle is not pasted");
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[9/20]", "Verifying that OP is changed with correct state");
		
		circle.scrollUpToSliceUsingKeyboard(1);
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[10/20]", "verify the circle state");
		circle.assertEquals(circle.getAllCircles(1).size(),1, "Checkpoint[11/20]", "verify mirror circle is present");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(),pendingList+1, "Checkpoint[12/20]", "Verifying that OP for pending finding");
		
		circle.scrollDownToSliceUsingKeyboard(1);
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[13/20]", "Verifying that OP has no finding for rejected filter");
		
		circle.selectCircleWithClick(1, 1);
		circle.selectRejectfromGSPSRadialMenu();
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[14/20]", "verify the circle is rejected");
		panel.assertEquals(panel.thumbnailList.size(),totalMirrorLines, "Checkpoint[15/20]", "Verifying that OP is changed upon change in state of user drawn finding");

		circle.scrollUpToSliceUsingKeyboard(1);
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1, 1, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[16/20]", "verify the mirrored circle state");
		
		
		circle.scrollDownToSliceUsingKeyboard(1);
		panel.enableFiltersInOutputPanel(true, false, false);
		
		circle.resizeCircle(-40, -40);		
		String val = circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R);
		circle.assertTrue(circle.verifyCircleIsCurrentActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[17/20]", "verify upon resizing the state is changed to accepted");
		panel.assertEquals(panel.thumbnailList.size(),totalMirrorLines, "Checkpoint[18/20]", "Verifying that OP is changed");

		circle.scrollUpToSliceUsingKeyboard(1);
		circle.assertEquals(val,circle.getCircle(1, 1).get(0).getAttribute(NSGenericConstants.R),"Checkpoint[19/20]","both circle are still of same size");
		circle.assertTrue(circle.verifyCircleIsCurrentInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[20/20]", "verify the circle state - accepted");
		
		
	}
		
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2824","F1286"})
	public void test10_US2824_TC11048_verifyRotationOfMirrorLine() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on rotating mirror line by selecting one end of line, mirrored annotations also rotates.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ctpPatientName, username, password, 1);
		
		mirror = new SimpleLine(driver);
	
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[1/14]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[2/14]","verifying the mirror lines are in pending state");
	

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
	
		circle.drawCircle(1, 50, 10,50,50);
				
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[3/14]", "Upon creation of circle two circle are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[4/14]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[5/14]", "Mirrored circle is not focused");
		
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 1, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[6/14]","verifying the mirror lines are in pending state");
		mirror.assertTrue(mirror.verifyLineIsInActiveGSPS(1, 2, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON),"Checkpoint[7/14]","verifying the mirror lines are in pending state");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the circle's state to pending");
		circle.selectAcceptfromGSPSRadialMenu();
				
		String beforeX1=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);
		
		int x1 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y1 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x2 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y2 =circle.getYCoordinate(circle.getAllCircles(1).get(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rotating the mirrored line");
		mirror.resizeSelectedLinearMeasurement(1, 2, 60, 20);
		
		String afterX1=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=mirror.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		int x11 =circle.getXCoordinate(circle.getAllCircles(1).get(0));
		int y11 =circle.getYCoordinate(circle.getAllCircles(1).get(0));
		
		int x12 =circle.getXCoordinate(circle.getAllCircles(1).get(1));
		int y12 =circle.getYCoordinate(circle.getAllCircles(1).get(1));
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/14]", "Verify user is able to move line inside a Viewbox");
		mirror.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		mirror.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		mirror.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		mirror.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		mirror.assertTrue(mirror.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[9/14]", "Verifying that after rotation line state is changed");
		for (int i = 1; i <=circle.getAllCircles(1).size(); i++) 
			circle.assertTrue(circle.verifyCircleAnnotationIsPendingGSPS(1, i), "Checkpoint[10/14]", "Circle state is not changed upon rotating the line");
	
			
		circle.assertEquals(x11, x1, "Checkpoint[11/14]", "Verifying that circle is moved");
		circle.assertEquals(y11, y1, "Checkpoint[12/14]", "Verifying that circle is moved");
	
		circle.assertNotEquals(x12, x2, "Checkpoint[13/14]", "Verifying that circle is moved");
		circle.assertNotEquals(y12, y2, "Checkpoint[14/14]", "Verifying that circle is moved");
		
	}
		
	@Test(groups ={"Chrome","IE11","Edge","Positive","US2825","F1286"})
	public void test11_US2825_TC11048_verifyLabellingOnNonMirrorObject() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on rotating mirror line by selecting one end of line, mirrored annotations also rotates.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liverPatientName, username, password, 1);
		
		circle = new CircleAnnotation(driver);
		circle.doubleClickOnViewbox(1);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 10,50,50);
		circle.drawCircle(1, -50, -10,-50,-50);
		
		List<String> tags = new ArrayList<String>();
		
		int circles = circle.getAllCircles(1).size();
		circle.assertEquals(circles, totalMirrorLines, "Checkpoint[1/24]", "Upon creation of circle no two objects are created");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[2/24]", "First circle is focused");
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[3/24]", "Another circle is not focused");
		
		int i =1;
		circle.assertEquals(circle.getLabel(1, 1),i+ViewerPageConstants.CIRCLE_LABEL,"Checkpoint[4/24]","Verifying that tag on first Circle");
		tags.add(i+ViewerPageConstants.CIRCLE_LABEL);
		i++;
		circle.assertEquals(circle.getLabel(1, 2),i+ViewerPageConstants.CIRCLE_LABEL,"Checkpoint[5/24]","Verifying that tag on second Circle");
		tags.add(i+ViewerPageConstants.CIRCLE_LABEL);
		
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);		
		point.drawPointAnnotationMarkerOnViewbox(1, 150, 110);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, -110);
		point.assertEquals(point.getAllPoints(1).size(), circles, "Checkpoint[6/24]", "Upon creation of point two points are not created");
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[7/24]", "First point is focused");
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[8/24]", "another point is not focused");
		i=1;
		point.assertEquals(point.getLabel(1, 1),i+ViewerPageConstants.POINT_LABEL,"Checkpoint[9/24]","Verifying that tag on first point");
		tags.add(i+ViewerPageConstants.POINT_LABEL);
		i++;
		point.assertEquals(point.getLabel(1, 2),i+ViewerPageConstants.POINT_LABEL,"Checkpoint[10/24]","Verifying that tag on second point");
		tags.add(i+ViewerPageConstants.POINT_LABEL);
			
		PolyLineAnnotation polyline = new PolyLineAnnotation(driver);
		polyline.selectPolylineFromQuickToolbar(1);
		int[] abc1 = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		int[] abc2 = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};
		polyline.drawFreehandPolyLine(1, abc1);
		polyline.drawFreehandPolyLine(1, abc2);
		polyline.assertEquals(polyline.getAllPolylines(1).size(), circles, "Checkpoint[11/24]", "Upon creation of polyline two polyline are not created");
		
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[12/24]", "Verifying the polyline is focused");
		polyline.assertTrue(polyline.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[13/24]", "Verifying the another polyline is not focused");
		
		i=1;
		polyline.assertEquals(polyline.getLabel(1, 1),i+ViewerPageConstants.POLYLINE_LABEL,"Checkpoint[14/24]","Verifying that tag on first polyline");
		tags.add(i+ViewerPageConstants.POLYLINE_LABEL);
		
		i++;
		polyline.assertEquals(polyline.getLabel(1, 2),i+ViewerPageConstants.POLYLINE_LABEL,"Checkpoint[15/24]","Verifying that tag on second polyline");
		tags.add(i+ViewerPageConstants.POLYLINE_LABEL);
		
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
 		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -150, -150, -60,-60);
		ellipse.drawEllipse(1, 130, 140, 100,10);

		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), circles, "Checkpoint[16/24]", "Upon creation of ellipse two ellipse are not created");
		
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[17/24]", "Verifying the ellipse is focused");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[18/24]", "Verifying the ellipse is not focused");
		
		i=1;
		ellipse.assertEquals(ellipse.getLabel(1, 1),i+ViewerPageConstants.ELLIPSE_LABEL,"Checkpoint[19/24]","Verifying that tag on first ellipse");
		tags.add(i+ViewerPageConstants.ELLIPSE_LABEL);
		
		i++;
		ellipse.assertEquals(ellipse.getLabel(1, 2),i+ViewerPageConstants.ELLIPSE_LABEL,"Checkpoint[20/24]","Verifying that tag on second ellipse");
		tags.add(i+ViewerPageConstants.ELLIPSE_LABEL);
	
		
		List<String> names = polyline.getFindingNames(1);		
		List<String> findingNamesSliderMenu = polyline.getFindingsNameFromSliderContainer(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		polyline.assertEquals(names.size(), tags.size(), "Checkpoint[21/24]", "Verifying the labels and findings are of equals size");
		for (int j = 0; j < names.size(); j++) {
			polyline.assertTrue(names.get(i).contains(tags.get(i)), "Checkpoint[22/24]", "Verifying the labels in finding menu");
			polyline.assertTrue(findingNamesSliderMenu.get(i).contains(tags.get(i)), "Checkpoint[23/24]", "Verifying the labels in slider menu");
			
		}
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);
		
		for (int thumbnailNumber = 1, tagsNumber=panel.thumbnailList.size()-1; thumbnailNumber < panel.thumbnailList.size(); thumbnailNumber++,tagsNumber--) {
			panel.assertEquals(panel.getROILabelInThumbnail(thumbnailNumber),tags.get(tagsNumber) ,"Checkpoint[24/24]", "Verifying the labels are part of thumbnail");
			
		}
		
	}
	
}










