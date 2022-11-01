package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.Interpolation;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class InterpolationTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private DICOMRT drt;
	private OutputPanel panel;


	String filePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String tcgaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath1=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);
	
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	String patientID ="";
	private HelperClass helper;
	



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username,password);
	}

	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","BVT","E2E","F482"})
	public void test01_01_US1427_TC7907_verifyInterpolationForClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to edit the Polyline by enabling the polyline control point interpolation");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
	
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawPolyLineExitUsingDoubleClickKey(1, polylineCoordinates);
	
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		int[] coordinates = new int[]{47,-33,47,-19,-17,-44,40,-35,53,-37};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 5, coordinates),"Checkpoint[1/5]","Verifying the interpolation is enabled and blue dots are displayed during interpolation");
		
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/5]", "Verifying that polyline is turned accepted after interpolating");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/5]", "Verifying the number of lines are changed after interpolation");
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[4/5]","Lines should be same after editing of polyline after interpolating");
		panel.openAndCloseOutputPanel(false);		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[5/5]", "verfying the changes are persisting after reload");

		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","BVT","F482"})
	public void test01_02_US1427_TC7907_verifyInterpolationForFreeHandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to edit the Polyline by enabling the polyline control point interpolation");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);	
		
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		int[] coordinates = new int[]{80, -80 , 100, 10, 100, 20, 110, 30, -10, 40};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 5, coordinates),"Checkpoint[1/5]","Verifying the interpolation is enabled and blue dots are displayed during interpolation");
		
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/5]", "Verifying that polyline is turned accepted after interpolating");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/5]", "Verifying the number of lines are changed after interpolation");
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[4/5]","Lines should be same after editing of polyline after interpolating");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[5/5]", "verfying the changes are persisting after reload");

		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Negative","F482"})
	public void test02_01_US1427_TC8090_verifyInterpolationAtStartAndEndPointFreehandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline  is not closed when the  point interpolation mode is enabled verify user is not able to add points beyond the starting and ending point.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
	
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {-69,-67,47,-23,44,-22,46,21,47,-26,47,-40,25,-45,-25,-54,-48,-33,-54,-22,-41,29,-41,40,-33,39};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		
		polylineCoordinates = new int[] {-90,-70, -90,40,-90,30 ,-90,20,-90,10};
		inter.assertFalse(inter.performInterPolationOnViewbox(1, 1, 1, polylineCoordinates),"Checkpoint[1/6]","Verifying the interpolation is enabled and blue dots are displayed during interpolation");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/6]", "verifying the polyline state is accepted and focused");
		
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[3/6]", "Verifying the lines under polyline after interpolation are not same");
	
		polylineCoordinates = new int[] {-70,-150, -50,-160,-50,-170 ,-50,-180};			
		inter.assertFalse(inter.performInterPolationOnViewbox(1, 1, afterLine, polylineCoordinates),"Checkpoint[4/6]","Verifying the interpolation is not happening when clicked beyond the starting and ending point");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/6]", "verifying polyline state is same no change and focused");
		
		afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[6/6]", "verifying no editing in polyline so lines are same before and after interpolation");
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Negative","F482"})
	public void test02_02_US1427_TC8090_verifyInterpolationAtStartAndEndPointClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline  is not closed when the  point interpolation mode is enabled verify user is not able to add points beyond the starting and ending point.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawPolyLineExitUsingDoubleClickKey(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();		
		polylineCoordinates = new int[] {-10,25,-21,20,-40,15,-13,19,-30,30, 20,-32};				
		inter.assertFalse(inter.performInterPolationOffsetWay(1, 1, beforeLine, polylineCoordinates),"Checkpoint[1/6]","Verifying no interpolation when clicked beyond last point");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/6]", "verifying the state is same after interpolation");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[3/6]", "verifying no editing done");
				
		polylineCoordinates = new int[] {20,44};
		inter.assertFalse(inter.performInterPolationOnViewbox(1, 1, 1, polylineCoordinates),"Checkpoint[4/6]","Verifying no interpolation when clicked beyond last point");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/6]", "verifying the state is same after interpolation");
		afterLine = inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[6/6]", "verifying no editing done");
		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Negative","E2E","F482"})
	public void test03_01_US1427_TC7908_verifyInterDisabledFreehandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user clicks outside the viewbox the edit is cancelled.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
	
		Interpolation inter = new Interpolation(driver);
					
		int[] polylineCoordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, polylineCoordinates);
				
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		
		polylineCoordinates = new int[] {25,60,30, 10, 550,0};
		inter.assertFalse(inter.performInterPolationOffsetWay(1, 1, 1, polylineCoordinates),"Checkpoint[1/3]","Verifying the interpolation is not done when focus is out of viewbox");
		inter.click(inter.getViewPort(1));
		inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/3]", "verifying polyline is accepted and focused");
		
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[3/3]", "Verifying the no editing is done in polyline");
	
		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Negative","F482"})
	public void test03_02_US1427_TC7908_verifyInterDisabledClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user clicks outside the viewbox the edit is cancelled.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
	
		Interpolation inter = new Interpolation(driver);
		
		
		int[] polylineCoordinates = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawPolyLineExitUsingDoubleClickKey(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		int[] coordinates = new int[]{80, -80 , 100, 10, 350,350};
		
		
		inter.assertFalse(inter.performInterPolationOffsetWay(1, 1, 5, coordinates),"Checkpoint[1/3]","Verifying the interpolation is not done when focus is out of viewbox");
		inter.click(inter.getViewPort(1));
		inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/3]", "verifying polyline is accepted and focused");
		
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertEquals(afterLine, beforeLine, "Checkpoint[3/3]", "Verifying the no editing is done in polyline");
	
		
		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1427","Negative","F482"})
	public void test04_01_US1427_TC7909_verifyEditStopsForFreeHandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user lifts the interpolation keys edits are saved.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
		int[] coordinates = new int[]{80, -80 , 100, 10, 100, 20, 110, 30, -10, 40};
		int afterLine=0;
		inter.enableInterpolation();
		inter.click(inter.getLinesOfPolyLine(1, 1).get(3));
		
		for(int i =0;i<coordinates.length-1; i=i+2) {
			
			inter.click(coordinates[i], coordinates[i+1]);
			if(i==4) {
				inter.disableInterpolation();
				inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[1/7]", "verifying polyline is accepted and focused when interpolation is disabled in between");
				afterLine = inter.getLinesOfPolyLine(1, 1).size();
				inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[2/7]", "verifying polyline is changed");
				
			}
			if(i>4) {
				inter.assertEquals(afterLine, inter.getLinesOfPolyLine(1, 1).size(), "Checkpoint[3/7]", "verifying the polyline is not interpolating as interpolation is disabled in between");
				inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[4/7]", "verifying polyline state is same");
			}
			
		}
		
		inter.assertEquals(afterLine, inter.getLinesOfPolyLine(1, 1).size(), "Checkpoint[5/7]", "verifying polyline is same - no editing");
			
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[6/7]","Lines should be same after editing of polyline in thumbnail");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[7/7]", "verifying changes are persisted after reload of viewer");

		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","F482"})
	public void test04_02_US1427_TC7909_verifyEditStopsForClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user lifts the interpolation keys edits are saved.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawPolyLineExitUsingDoubleClickKey(1, polylineCoordinates);
	
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();
//		int[] coordinates = new int[]{80, -80 , 80,-100, 80,-120};
		int[] coordinates = new int[]{47,-33,47,-19,-17,-44,40,-35,53,-37};
		int afterLine=0;
		inter.enableInterpolation();
		inter.click(inter.getLinesOfPolyLine(1, 1).get(3));
		
		for(int i =0;i<coordinates.length-1; i=i+2) {
			
			inter.click(coordinates[i], coordinates[i+1]);
			if(i==4) {
				inter.disableInterpolation();
				inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[1/7]", "verifying polyline is accepted and focused when interpolation is disabled in between");
				afterLine = inter.getLinesOfPolyLine(1, 1).size();
				inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[2/7]", "verifying polyline is changed");
				
			}
			if(i>4) {
				inter.assertEquals(afterLine, inter.getLinesOfPolyLine(1, 1).size(), "Checkpoint[3/7]", "verifying the polyline is not interpolating as interpolation is disabled in between");
				inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[4/7]", "verifying polyline state is same");
			}
			
		}
		
		inter.assertEquals(afterLine, inter.getLinesOfPolyLine(1, 1).size(), "Checkpoint[5/7]", "verifying polyline is same - no editing");
			
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[6/7]","Lines should be same after editing of polyline in thumbnail");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[7/7]", "verifying changes are persisted after reload of viewer");

	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","BVT","E2E","F482"})
	public void test05_US1427_TC8089_verifyInterpolationForRTStruct() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to edit the  RT Struct Polyline by enabling the polyline control point interpolation");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(tcgaPatientName,  1, 1);
	
		drt = new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		Interpolation inter = new Interpolation(driver);
		ContentSelector cs = new ContentSelector(driver);
		
		List<String> results = cs.getAllResults();
		cs.mouseHover(cs.getViewPort(1));
		
		int beforeLine1=inter.getLinesOfPolyLine(1, 1).size();
		int beforeLine2=inter.getLinesOfPolyLine(1, 2).size();
		
		int[] coordinates = new int[]{80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 30, coordinates),"Checkpoint[1/12]","Verifying the interpolation on rt contours");
				
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, drt.getColorOfSegment(1).replace(", ", ","), ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/12]", "verifying 1 contour is focused and state is changed to accepted");
		inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 2, drt.getColorOfSegment(2).replace(", ", ","), ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[3/12]", "verifying 2 contour is inactive and state is same");
			
		int afterLine1=inter.getLinesOfPolyLine(1, 1).size();
		int afterLine2=inter.getLinesOfPolyLine(1, 2).size();
		
		
		inter.assertNotEquals(afterLine1, beforeLine1, "Checkpoint[4/12]", "verifying 1 Contour is edited");
		inter.assertEquals(afterLine2, beforeLine2, "Checkpoint[5/12]", "verifying 2 contour is not edited");
		
		
		String findingName = drt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).get(0);		
		drt.assertEquals(drt.getFindingsName(1, ViewerPageConstants.ACCEPTED_COLOR).get(0),findingName,"Checkpoint[6/12]","Verifying the state in finding menu is changed to accepted");
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
//		panel.assertEquals(panel.getText(panel.findingsNameList.get(0)), ViewerPageConstants.FINDING_NAME+": "+findingName, "Checkpoint[7/12]", "Verifying the same is reflected in Output panel");
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[7/12]", "Verifying the same is reflected in Output panel");
		panel.openAndCloseOutputPanel(false);
		
		inter.assertEquals(cs.getAllResults().size(), results.size()+1, "Checkpoint[8/12]", "verifying the clone is created after interpolation");
		
		
		helper.browserBackAndReloadViewer(tcgaPatientName,  1, 1);
		drt.waitForDICOMRTToLoad();
		
		inter.assertEquals(afterLine1, inter.getLinesOfPolyLine(1, 1).size(), "Checkpoint[9/12]", "verifying contour 1 is persisting the changes");
		inter.assertEquals(afterLine2, inter.getLinesOfPolyLine(1, 2).size(), "Checkpoint[10/12]", "verifying contour 2 is same as before");
	
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 2, drt.getColorOfSegment(2).replace(", ", ","), ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[11/12]", "verifying the contour 2 is focused");
		inter.assertTrue(inter.verifyPolylineIsInActiveGSPS(1, 1, drt.getColorOfSegment(1).replace(", ", ","), ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[12/12]", "verifying the contour 1 is not focused");

		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","F737","F482"})
	public void test06_01_US1427_TC8094_verifyInterpolationDirectionsForOpenFreeHandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user able to edit the polyline in clockwise and anticlockwise direction (Open Polyline)");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();

		// clockwise interpolation
		int[] coordinates = new int[]{-59,-57,37,-13,34,-12,36,10,37,-16,37,-30,15,35,38,23,44,12,31,19,31,30,23,29};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 1, coordinates),"Checkpoint[1/8]","Verifying the interpolation is done in clock wise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/8]", "verifying polyline is focused");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/8]", "verifying polyline is edited");

		// anticlock wise interpolation
		coordinates = new int[]{59, 57, 70, 57, 90, 60, 110, 65, 120, 70, 140, 70, 160, 65, 170, 70, 190, 57, 210, 60, 230, 60, 250, -50, 250, -80};		
		inter.assertTrue(inter.performInterPolationOnViewbox(1, 1, 1, coordinates),"Checkpoint[4/8]","verifying the interpolation is done anticlockwise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/8]", "verifying polyline is focused");
		afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[6/8]", "verifying polyline is edited");

				
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[7/8]","verifying polyline is edited in thumbnail too");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[8/8]", "verifying edited polyline is persisted after reload");

		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","F482"})
	public void test06_02_US1427_TC8094_verifyInterpolationDirectionsForOpenClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user able to edit the polyline in clockwise and anticlockwise direction (Open Polyline)");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] polylineCoordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawPolyLineExitUsingDoubleClickKey(1, polylineCoordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();

		// clockwise interpolation
		int[] coordinates = new int[]{-40,-57,37,-13,34,-12,36,10,37,-16,37,-30,15,35,38,23,44,12,31,19,31,30,23,29};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 2, coordinates),"Checkpoint[1/8]","Verifying the interpolation is done in clock wise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/8]", "verifying polyline is focused");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/8]", "verifying polyline is edited");

		// anticlock wise interpolation
		coordinates = new int[]{59, 57, 70, 57, 90, 60, 110, 65, 120, 70, 140, 70, 160, 65, 170, 70, 190, 57, 210, 60, 230, 60, 250, -50, 250, -80};		
		inter.assertTrue(inter.performInterPolationOnViewbox(1, 1, 2, coordinates),"Checkpoint[4/8]","verifying the interpolation is done anticlockwise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/8]", "verifying polyline is focused");
		afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[6/8]", "verifying polyline is edited");

				
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[7/8]","verifying polyline is edited in thumbnail too");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[8/8]", "verifying edited polyline is persisted after reload");

		
		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","F482"})
	public void test07_01_US1427_TC8096_verifyInterpolationDirectionsForClosedFreeHandPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user able to edit the polyline in clockwise and anticlockwise direction (closed Polyline)");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] coordinates = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25,-4, 27};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawFreehandPolyLineExitUsingESC(1, coordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();

		// clockwise interpolation
		coordinates = new int[]{-59,-57,37,-13,34,-12,36,10,37,-16,37,-30,15,35,38,23,44,12,31,19,31,30,23,29};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 1, coordinates),"Checkpoint[1/8]","Verifying the interpolation is done in clock wise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/8]", "verifying polyline is focused");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/8]", "verifying polyline is edited");

		// anticlock wise interpolation
		coordinates = new int[]{59, 57, 70, 57, 90, 60, 110, 65, 120, 70, 140, 70, 160, 65, 170, 70, 190, 57, 210, 60, 230, 60, 250, -50, 250, -80};		
		inter.assertTrue(inter.performInterPolationOnViewbox(1, 1, 1, coordinates),"Checkpoint[4/8]","verifying the interpolation is done anticlockwise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/8]", "verifying polyline is focused");
		afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[6/8]", "verifying polyline is edited");

				
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[7/8]","verifying polyline is edited in thumbnail too");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[8/8]", "verifying edited polyline is persisted after reload");

		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1427","Positive","F482","E2E"})
	public void test07_02_US1427_TC8096_verifyInterpolationDirectionsForClosedClassicPolyline() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when polyline control point interpolation mode is enabled and user able to edit the polyline in clockwise and anticlockwise direction (closed Polyline)");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9PatientName);
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
	
		Interpolation inter = new Interpolation(driver);
		inter.doubleClickOnViewbox(1);
		
		int[] coordinates = new int[] {-90,-90,90,-90,90,90,-90,90,-90,-90};
		
		inter.selectPolylineFromQuickToolbar(1);
		inter.drawClosedPolyLine(1, coordinates);
		
		int beforeLine=inter.getLinesOfPolyLine(1, 1).size();

		// clockwise interpolation
		coordinates = new int[]{-59,-57,37,-13,34,-12,36,10,37,-16,37,-30,15,35,38,23,44,12,31,19,31,30,23,29};
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, coordinates),"Checkpoint[1/8]","Verifying the interpolation is done in clock wise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[2/8]", "verifying polyline is focused");
		int afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[3/8]", "verifying polyline is edited");

		// anticlock wise interpolation
		coordinates = new int[]{59, 57, 70, 57, 90, 60, 110, 65, 120, 70, 140, 70, 160, 65, 170, 70, 190, 57, 210, 60, 230, 60, 250, -50, 250, -80};		
		inter.assertTrue(inter.performInterPolationOnViewbox(1, 1, 2, coordinates),"Checkpoint[4/8]","verifying the interpolation is done anticlockwise");
		inter.assertTrue(inter.verifyPolylineIsActiveGSPS(1, 1, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON), "Checkpoint[5/8]", "verifying polyline is focused");
		afterLine=inter.getLinesOfPolyLine(1, 1).size();
		inter.assertNotEquals(afterLine, beforeLine, "Checkpoint[6/8]", "verifying polyline is edited");

				
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),afterLine,"Checkpoint[7/8]","verifying polyline is edited in thumbnail too");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);	
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), afterLine, "Checkpoint[8/8]", "verifying edited polyline is persisted after reload");

		
		
	}

	//US1422: Basic interpolation of RT contours between 2 slices
	@Test(groups = {"Chrome","IE11","Edge","US1422","Positive","F737","E2E"})
	public void test08_US1422_TC8372_TC8373_TC8410_verifyInterpolationBtwTwoSlices() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation is applied successfully between 2 slices. <br>"+
		"Verify that only edited area is considered for interpolation. <br>"+
		"Verify that the user is able to apply interpolation on same slices again.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		Interpolation inter=new Interpolation(driver);
		drt.waitForDICOMRTToLoad();
		
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();

        //store value of lines of polylines before interpolation
        String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		
		int startSlice=inter.getCurrentScrollPositionOfViewbox(1);
		for(int i=1;i<=4;i++){
          hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
          inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png");
          inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
		int endSlice=inter.getCurrentScrollPositionOfViewbox(1)-1;
		
		//navigate to first contour of External and perform Interpolation
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation between two slices.");
		
		drt.navigateToFirstContourOfSegmentation(1);
		int[] coordinates = new int[] {80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		inter.performInterpolationBetweenTwoSlices(1, startSlice, 1, coordinates, 50, 75, endSlice, 1, coordinates, 50, 75);
		
		//navigate first contour and verify interpolation 
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation between two slices using Lines of Polyline.");
		drt.navigateToFirstContourOfSegmentation(1);
		
		for(int i=startSlice;i<=endSlice;i++){
			inter.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(i), "Checkpoint[1/4]", "Verified Lines of polyline are not same on slice "+i);
		
			inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png");
		
		String expectedImagePath = newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/test08_diff_Interpolation_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[2/4]", "Interpolation Perform succcesfully On Slice"+i);
	    hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
	    inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
        //TC8410
		//perform Interpolation on same slice but at different location
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation between two slices again but at different position.");
		
		drt.navigateToFirstContourOfSegmentation(1);
	    coordinates = new int[] {-80, 80 , -100, 0, -100, 0, -150, 0, -150, 0};
	    inter.performInterpolationBetweenTwoSlices(1, startSlice, 1, coordinates, 175, 200, endSlice, 1, coordinates, 175, 200);
		
		//navigate first contour and verify interpolation on same slice but at different position
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation between two slices again using Lines of Polyline.");
		drt.navigateToFirstContourOfSegmentation(1);
		for(int i=startSlice;i<=endSlice;i++){ 
		inter.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(i), "Checkpoint[3/4]", "Verified Lines of polyline are not same on slice "+i);
		
		inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_On_Same_Slice_"+i+".png");
		String expectedImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_On_Same_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/diff_Interpolation_On_Same_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[4/4]", "Interpolation Perform succcesfully On same Slice"+i);
		inter.scrollDownToSliceUsingKeyboard(1,1);
	    
		}
		
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1422","Negative","F737","E2E"})
	public void test09_US1422_TC8412_verifyInterpolationWhenStartAndEndSliceAreFromDifferentContours() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation doesn't happen when the start and end contours are different.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		//store value of polyline handle for 2 different segment before performing edit
		drt.scrollToImage(1, 51);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		Interpolation inter=new Interpolation(driver);
		
		for(int i=1;i<=4;i++){
          hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
          hm.put(i+4, inter.getLinesOfPolyLine(1, 2).size());
          inter.scrollDownToSliceUsingKeyboard(1);
		}
	
		//perform Interpolation between two different segment contours
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation between two different contours of segment.");
		int[] coordinates = new int[] {50,-30,70,-50,90,-70};
		int[] coordinates1 = new int[] {10,-50,50,0};
		
		drt.scrollToImage(1, 51);
		inter.performInterpolationBetweenTwoSlices(1, 1, 1, coordinates, 50, 75, 4, 2, coordinates1, 5, 15);
		
	   //verify interpolation when different start and end contours are edited from two different segement
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation when start and end contors are different");
		drt.scrollToImage(1, 51);
		drt.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(),hm.get(1), "Checkpoint[1/6]", "Verified that edit perform on First slice of External segment.");
		drt.compareElementImage(protocolName,inter.mainViewer, "Checkpoint[2/6]", "test09_02_Start_Contour_External");
		inter.scrollDownToSliceUsingKeyboard(1);
		 
		for(int i=2;i<=3;i++)
		{
		drt.assertEquals(inter.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[3/6]", "Verified that number of lines are same when no Interpolation perform for External segmenet on slice "+i);
		drt.assertEquals(inter.getLinesOfPolyLine(1, 2).size(),hm.get(i+4), "Checkpoint[4/6]", "Verified that number of lines are same no Interpolation perform for Aorta segmenet on slice "+i);
		inter.scrollDownToSliceUsingKeyboard(1);
		}
		
		drt.assertNotEquals(inter.getLinesOfPolyLine(1, 2).size(),hm.get(4), "Checkpoint[5/6]", "Verified that edit perform on First slice of Aorta segment.");
		drt.compareElementImage(protocolName,inter.mainViewer, "Checkpoint[6/6]", "test09_06_End_Contour_Aorta");
	
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1422","Negative","F737"})
	public void test10_US1422_TC8413_verifyInterpolationWhenUserDrawGSPSAnnotation() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation is not applied when user draws a GSPS annotation in between.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		//store value of polyline handle before performing any edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		int startSlice=poly.getCurrentScrollPositionOfViewbox(1);
		
		for(int i=startSlice;i<=8;i++){
          hm.put(i, poly.getLinesOfPolyLine(1, 1).size());
          poly.scrollDownToSliceUsingKeyboard(1);
		}
		
		int endSlice=poly.getCurrentScrollPositionOfViewbox(1)-1;
		
		//perform Interpolation between two slices by releasing shift key
		int[] coordinates = new int[] {50,-30,70,-50,90,-70};
	
		drt.navigateToFirstContourOfSegmentation(1);
		drt.holdShiftKeyPressed();
		List<WebElement>handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
		drt.releaseShiftKeyPressed();
		
		//user draw GSPS annotation
		poly.scrollToImage(1, 4);
	    MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
	    lineWithUnit.selectDistanceFromQuickToolbar(1);
	    lineWithUnit.drawLine(1,-50, -50, 150, 150);
	    
	    poly.scrollToImage(1, endSlice);
		handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
	
	   //verify interpolation when user draw GSPS  annotation in between
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation not happend when user draw GSPS annotation in between by releasing the shift key.");
		drt.navigateToFirstContourOfSegmentation(1);
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(1), "Checkpoint[1/5]", "Verified that edit perform on First slice of External segment.");
		drt.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[2/5]", "test10_02_Start_Contour_External");
		 poly.scrollDownToSliceUsingKeyboard(1,1);
		 
		for(int i=startSlice+1;i<=endSlice-1;i++)
		{
		drt.assertEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[3/5]", "Verified that number of lines are same when no interpolation perform for slice "+i );
		poly.scrollDownToSliceUsingKeyboard(1,1);
		}
		
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(endSlice), "Checkpoint[4/5]", "Verified that edit perform on last slice of External segment.");
		drt.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[5/5]", "test10_05_End_Contour_External");

	
		
		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1422","Positive","F737"})
	public void test11_US1422_TC8426_verifyInterpolationInBtwSlicesOfAlreadyInterpolated() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to interpolate the in between slices of already interpolated slices.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		drt.navigateToFirstContourOfSegmentation(1);
		
		//store value of lines of polyline before edit
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		Interpolation inter=new Interpolation(driver);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		int startSlice=inter.getCurrentScrollPositionOfViewbox(1);
		for(int i=startSlice;i<=8;i++){
          hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
          inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png");
          inter.scrollDownToSliceUsingKeyboard(1);
		}
		int endSlice=inter.getCurrentScrollPositionOfViewbox(1)-1;
		
		int[] coordinates = new int[] {80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		
		//perform Interpolation 
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation between slices.");
		drt.navigateToFirstContourOfSegmentation(1);
		
		inter.performInterpolationBetweenTwoSlices(1, startSlice, 1, coordinates, 50, 75, endSlice, 1, coordinates, 50, 75);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation between two slices.");
		drt.navigateToFirstContourOfSegmentation(1);
	
		for(int i=startSlice;i<=endSlice;i++){
		inter.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(i), "Checkpoint[1/6]", "Verified that number of lines are not same when interpolation perform for slice "+i);
		inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png");
		
		String expectedImagePath = newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/diff_Interpolation_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[2/6]", "Interpolation Perform succcesfully On Slice"+i);
		hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
		inter.scrollDownToSliceUsingKeyboard(1,1);
		}

		//perform Interpolation for in between slices of already Interpolated slices.
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation for in between slices of already Interpolated slices");
	    drt.scrollToImage(1, 2);
	    coordinates = new int[] {-80, 80 , -100, 0, -100, 0, -150, 0, -150, 0};
	    inter.performInterpolationBetweenTwoSlices(1, 2, 1, coordinates, 175, 200, endSlice-1, 1, coordinates, 175, 200);
		
		//verify both interpolation
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interpolation between two slices as well as in between slices");
		drt.navigateToFirstContourOfSegmentation(1);
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(startSlice), "Checkpoint[3/6]", "Verified that number of lines for the first slice are same after interolating in between slices.");
		inter.scrollDownToSliceUsingKeyboard(1,1);
		
		for(int i=startSlice+1;i<endSlice;i++){  
			inter.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(i), "Checkpoint[4/6]", "Verified that number of lines are not same after interolating in between slices.");
			inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_On_In_Between_Slice_"+i+".png");
		String expectedImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_On_In_Between_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/diff_Interpolation_On_In_Between_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[5/6]", "Interpolation Perform succcesfully On in between Slice"+i);
		inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
		inter.assertEquals(inter.getLinesOfPolyLine(1, 1).size(), hm.get(endSlice), "Checkpoint[6/6]", "Verified that number of lines for the last slice are same after interolating in between slices.");

		
	}
		
	@Test(groups = {"Chrome","IE11","Edge","US1422","Negative","F737"})
	public void test12_US1422_TC8427_verifyInterpolationWhenContourEditHappensAtDifferentPosition() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation are not accurate when the contour edit happens at different position");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		//store value of lines of polyline before edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();

		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		int startSlice=poly.getCurrentScrollPositionOfViewbox(1);
		for(int i=1;i<=5;i++){
          hm.put(i, poly.getLinesOfPolyLine(1, 1).size());
          poly.scrollDownToSliceUsingKeyboard(1);
		}
		
		int endSlice=poly.getCurrentScrollPositionOfViewbox(1)-1;
		int[] coordinates = new int[] {50,-30,70,-50,90,-70};
		
		//perform Interpolation at two different location
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Perform Interpolation on start and end slice at two different location.");
		drt.navigateToFirstContourOfSegmentation(1);
		drt.holdShiftKeyPressed();
		List<WebElement>handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
		
		for(int i=startSlice;i<endSlice;i++)
			poly.scrollDownToSliceUsingKeyboard(1);
		
		coordinates = new int[] {50,30,70,50,90,70};
		handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(175), coordinates, handles.get(200));
		drt.releaseShiftKeyPressed();
		
		//verify Interpolation
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Interplation when location is different on start and end slice.");
		drt.navigateToFirstContourOfSegmentation(1);
		for(int i=startSlice;i<=startSlice+2;i++){
		poly.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(), hm.get(i), "Checkpoint[1/6]", "Verified lines of polyline after edit on first slice.");
		poly.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[2/6]", "test12_02_Interpolation_On_Slice_"+i);
		poly.scrollDownToSliceUsingKeyboard(1); 
		}
		
		poly.assertEquals(poly.getLinesOfPolyLine(1, 1).size(), hm.get(4), "Checkpoint[3/6]", "Verified that interpolation is not perform accurately.");
		poly.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[4/6]", "test12_04_Interpolation_On_Slice_4");
		poly.scrollDownToSliceUsingKeyboard(1);

		poly.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(), hm.get(endSlice), "Checkpoint[5/6]", "Verified lines of polyline after edit on last slice.");
		poly.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[6/6]", "test12_06_Interpolation_On_Slice_"+endSlice);
		poly.scrollDownToSliceUsingKeyboard(1);
	
	
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1422","Negative","F737"})
	public void test13_US1422_TC8443_verifyInterpolationWhenShiftkeyUsed() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation doesn't happen when Shift key is not used.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		//store value of polyline handle before performing any edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		int startSlice=poly.getCurrentScrollPositionOfViewbox(1);
		
		for(int i=startSlice;i<=8;i++){
          hm.put(i, poly.getLinesOfPolyLine(1, 1).size());
          poly.scrollDownToSliceUsingKeyboard(1);
		}
		
		int endSlice=poly.getCurrentScrollPositionOfViewbox(1)-1;
		
		//perform Interpolation betwen two differen segment contours
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "","Perform Interpolation without using shift key");
		int[] coordinates = new int[] {50,-30,70,-50,90,-70};
		
		drt.navigateToFirstContourOfSegmentation(1);
		List<WebElement>handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
		
	    poly.scrollToImage(1, endSlice);
		handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
	
	    //verify interpolation shift key is not used
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "","Verify Interpolation when shift key is not used.");
		drt.navigateToFirstContourOfSegmentation(1);
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(1), "Checkpoint[1/5]", "Verified that lines of polyline are not same when edit perform on first slice.");
		drt.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[2/5]", "test13_02_Start_Contour_External");
		poly.scrollDownToSliceUsingKeyboard(1);
		 
		for(int i=startSlice+1;i<=endSlice-1;i++)
		{
		drt.assertEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[3/5]", "Verified that lines of polyline are same as interpolation not happened in between slices.");
		poly.scrollDownToSliceUsingKeyboard(1);
		}
		
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(endSlice), "Checkpoint[4/5]", "Verified that lines of polyline are not same when edit perform on last slice.");
		drt.compareElementImage(protocolName,poly.mainViewer, "Checkpoint[5/5]", "test13_05_End_Contour_External");
	
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1422","Positive","F737"})
	public void test14_US1422_TC8467_TC8486_verifyInterpolationAfterReloadAndCloneInCS() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation persists on a viewer reload. <br>"+
		"[Risk and Impact]: Verify the clone and contour state.");
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(tcgaPatientName, 1, 1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		ContentSelector cs=new ContentSelector(driver);
		int resultCount=cs.getAllResults().size();
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		
		//store value of lines of polyline before edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		
		Interpolation inter=new Interpolation(driver);
		int startSlice=inter.getCurrentScrollPositionOfViewbox(1);
		
		for(int i=startSlice;i<=5;i++){
          hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
          inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png");
          inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
		int endSlice=inter.getCurrentScrollPositionOfViewbox(1)-1;
		
		//perform Interpolation
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "","Perform Interpolation and verify count in CS");
		int[] coordinates = new int[] {80, -80 , 100, 0, 100, 0, 150, 0, 150, 0};
		drt.navigateToFirstContourOfSegmentation(1);
		
		inter.performInterpolationBetweenTwoSlices(1, startSlice, 1, coordinates, 50, 75, endSlice, 1, coordinates, 75, 100);
		
		//verify clone in CS after interpolation
		inter.assertEquals(cs.getAllResults().size(), resultCount+1, "Checkpoint[1/8]", "Verified that new clone is created after successful perform of Interpolation.");
		drt.navigateToFirstContourOfSegmentation(1);
		inter.assertTrue(inter.verifyAcceptGSPSRadialMenu(), "Checkpoint[2/8]", "Verified that Accepted GSPS radial menu.");
		drt.assertTrue(drt.verifyAcceptedRTSegment(1), "Checkpoint[3/8]","Verifying state icon is accepted");	
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "","Verify Interpolation.");
		for(int i=startSlice;i<=endSlice;i++)
		{	
		drt.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[4/8]", "Verified that lines of polylines are not same when interpolation perform.");
		inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png");
			
		String expectedImagePath = newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/test14_diff_Interpolation_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[5/6]", "Interpolation Perform succcesfully On Slice"+i);
		hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
		inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
		panel=new OutputPanel(driver);
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount=panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		drt.assertEquals(drt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),acceptedCount, "Checkpoint[6/8]", "Verified that state of segment is Accepted in Output panel as well as in finding menu.");
		
		//reload viewer and verify Interpolation
		helper.browserBackAndReloadViewer(tcgaPatientName,  1, 1);
		drt.waitForDICOMRTToLoad();
		
		//verify interpolation when user draw GSPS  annotation in between
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "","Verify Interpolation.");
		drt.navigateToFirstContourOfSegmentation(1);			 
		for(int i=startSlice;i<=endSlice;i++)
		{	
		drt.assertEquals(inter.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[7/8]", "Verified that lines of polylines are not same after reload when interpolation perform.");
		inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/test14_After_Reload _Interpolation_On_Slice_"+i+".png");
		
		String expectedImagePath = newImagePath+"/actualImages/After_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/test14_After_Reload _Interpolation_On_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/test14_diff_Interpolation_Slice_"+i+".png";

		inter.assertTrue(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[8/8]", "Verified interpolation after reload of viewer On Slice"+i);
		inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		
	}

	@Test(groups = {"Chrome","IE11","Edge","US1422","Positive","F737","E2E"})
	public void test15_US1422_TC8539_verifyInterpolationWhenEditPerformOnBothDirection() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that interpolation is working fine with clockwise and anticlockwise edits performed.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		//store value of lines of polyline before edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		Interpolation inter=new Interpolation(driver);
		int startSlice=inter.getCurrentScrollPositionOfViewbox(1);
		
		for(int i=startSlice;i<=4;i++){
          hm.put(i, inter.getLinesOfPolyLine(1, 1).size());
          inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png");
          inter.scrollDownToSliceUsingKeyboard(1,1);
		}
		int endSlice=inter.getCurrentScrollPositionOfViewbox(1)-1;
		int[] coordinates = new int[] {	60, -80 ,80, 0, 100, 0,120, 0,150, 0, 150, 0};
		int[] coordinates1 = new int[] {150, 0, 150, -25, 80, -25 ,80, 0,150, -150 };

		//perform edit in clockwise direction
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Edit polyline in clockwise direction on start slice and in anti-clockwise direction on end slice .");
		drt.navigateToFirstContourOfSegmentation(1);
		
		inter.performInterpolationBetweenTwoSlices(1, startSlice, 1, coordinates, 50, 80, endSlice, 1, coordinates1, 50, 80);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify interpolation when edit perform in clockwise and anti-clockwise direction.");
		drt.navigateToFirstContourOfSegmentation(1);
		for(int i=startSlice;i<=endSlice;i++)
		{	
		drt.assertNotEquals(inter.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[1/2]", "Verified that lines of polylines are not same when interpolation perform.");
		inter.takeElementScreenShot(inter.getViewPort(1), newImagePath+"/actualImages/After_Interpolation_On_Slice_"+i+".png");
		
		String expectedImagePath = newImagePath+"/goldImages/Before_Interpolation_Slice_"+i+".png";
		String actualImagePath = newImagePath+"/actualImages/After_Interpolation_On_Slice_"+i+".png";
		String diffImagePath = newImagePath+"/diffImages/test15_diff_Interpolation_Slice_"+i+".png";

		inter.assertFalse(inter.compareimages(expectedImagePath, actualImagePath, diffImagePath), "Checkpoint[2/2]", "Verified interpolation perform successfully On Slice"+i);
		inter.scrollDownToSliceUsingKeyboard(1,1);
	
		}
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge","US1422","Negative","F737"})
	public void test16_US1422_TC8485_verifyInterpolationAfterLayoutChange() throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that interpolation doesn't happen when user updates the layout in between.");
		
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(tcgaPatientName,1);
		ViewerLayout layout = new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		drt=new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		
		//store value of polyline handle before performing any edit
		drt.navigateToFirstContourOfSegmentation(1);
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		int startSlice=poly.getCurrentScrollPositionOfViewbox(1);
		
		poly.click(poly.getViewPort(1));
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		for(int i=startSlice;i<=8;i++){
          hm.put(i, poly.getLinesOfPolyLine(1, 1).size());
          poly.mouseHover(poly.getViewPort(1));
          poly.scrollDownToSliceUsingKeyboard(1);
		}
		
		int endSlice=poly.getCurrentScrollPositionOfViewbox(1)-1;
		poly.doubleClick(poly.getViewPort(1));
		poly.waitForViewerpageToLoad();
		
		//perform Interpolation
		int[] coordinates = new int[] {50,-30,70,-50,90,-70};
		
		drt.navigateToFirstContourOfSegmentation(1);
		drt.holdShiftKeyPressed();
		List<WebElement>handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
		
		//change layout and navigate to end slice then perform Interpolation
		poly.click(poly.getViewPort(1));
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		poly.mouseHover(poly.getViewPort(1));
		poly.scrollTheSlicesUsingSlider(1, 0, 0, 0, endSlice-1);

		handles=poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(50), coordinates, handles.get(75));
		drt.releaseShiftKeyPressed();
		
	   //verify interpolation perform or not after layout change
		drt.navigateToFirstContourOfSegmentation(1);
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(1), "Checkpoint[1/5]", "Verified that lines of polylines are not same when interpolation perform on start slice.");
		drt.compareElementImage(protocolName,poly.getViewPort(1), "Checkpoint[2/5]", "test16_02_Start_Contour_External");
		poly.scrollDownToSliceUsingKeyboard(1, 1);
		 
		for(int i=startSlice+1;i<=endSlice-1;i++)
		{
		drt.assertEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(i), "Checkpoint[3/5]", "Verified that lines of polylines are same because interpolation not happens.");
		poly.scrollDownToSliceUsingKeyboard(1, 1);
		}
		
		drt.assertNotEquals(poly.getLinesOfPolyLine(1, 1).size(),hm.get(endSlice), "Checkpoint[4/5]", "Verified that lines of polylines are not same when edit perform on end slice.");
		drt.compareElementImage(protocolName,poly.getViewPort(1), "Checkpoint[5/5]", "test16_05_End_Contour_External");
	
	}
	
		
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(username);
		
	}
	
		
}
