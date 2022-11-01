package com.trn.ns.test.viewer.dicomRT;


import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
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
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DicomRTNavigationTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private CircleAnnotation circle;
	private ContentSelector cs;
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerSliderAndFindingMenu findingMenu;

	// Get Patient Name
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	String resultToSelect=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, TCGA_VP_A878_filepath);
			
	String CMR_ITA_BER_FOR_filepath = Configurations.TEST_PROPERTIES.get("CMR_ITA_BER_FOR_filepath");
	String cmrITAPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, CMR_ITA_BER_FOR_filepath);


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	@Test(groups = {"Chrome","IE11","Edge","US2511","US747","BVT","E2E","F1084","F1085"})
	public void test01_US747_TC3059_TC3067_US2511_TC10434_verifyNavigationOfSegmentation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify DICOM RT should be displayed on viewer page with correct contour color and slice position. <br>"+
		"Verify focus jumps to corresponding legend in the viewer when user clicks on the legend text");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify DICOM RT Display");

		int totalNumberOfSegmentsInLegend =DataReader.getNumberOfContours(TCGA_VP_A878_filepath);
		List<WebElement> findingName = drt.getLegendOptionsList(1);
		
		drt.assertEquals(totalNumberOfSegmentsInLegend,findingName.size(),"Checkpoint[1/2]","Verifying the selected countour and segment has some color");
		
		for(int i = 1; i <=totalNumberOfSegmentsInLegend; i++) {
			drt.navigateToFirstContourOfSegmentation(i);
			drt.assertEquals(drt.getHexColorValue(drt.getSelectedContourColor()),drt.getHexColorValue(drt.getLegendOptionColor(drt.getText(findingName.get(i-1)))),"Checkpoint[2."+i+"/2]","Verifying the selected contour and segment has some color");	
		}	
		
	}

	@Test(groups = { "Chrome", "IE11", "Edge" , "US747"})
	public void test02_US747_TC3060_TC3161_verifyNavigationAfterPerformingPanZoomSegmentation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify DICOM RT should be displayed on viewer page with correct contour color and slice position");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1/1]", "Verify DICOM RT Display");

		drt.compareElementImage(protocolName, drt.mainViewer, "verifying the viewer for patient "+patientNameDICOMRT, "test02_1");

		//Performing PAN
		drt.selectPanFromQuickToolbar(drt.getViewPort(1));

		drt.dragAndReleaseOnViewer(drt.getViewPort(1), 50, 50, 300, 50);

		//Performing Zoom
		drt.selectZoomFromQuickToolbar(drt.getViewPort(1));

		drt.dragAndReleaseOnViewer(drt.getViewPort(1), 0, 0, -100, -200);

		//Clicking on segment and navigating to first contour
		navigateToFirstContourOfSegment("test02_2_");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","US747","Sanity"})
	public void test03_US747_TC3066_verifySynchronizationWithNavigation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify DICOM RT should be displayed on viewer page with correct contour color and slice position");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1/1]", "Verify DICOM RT Display");
		drt.compareElementImage(protocolName, drt.mainViewer, "verifying the viewer for patient "+patientNameDICOMRT, "test03_1");

		//Changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);

		//Clicking on segment and navigating to first contour
		navigateToFirstContourOfSegment("test03_2_");

	}

	@Test(groups = {"Chrome", "IE11", "Edge" , "US909"})
	public void test04_US909_TC3711_TC3713_TC3714_verifyNavigationWhenContourIsAccepted() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify RT struct navigation when any contour is accepted through A/R bar"
				+ "<br> Verify RT struct navigation through previous arrow key of A/R bar"
				+ "<br> Verify RT struct navigation through NEXT arrow key of A/R bar");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
	
		int currentImage = 0;
		int badgeCount=0;
		rt.navigateToFirstContourOfSegmentation(1); 
		int firstLegend = rt.getCurrentScrollPositionOfViewbox(1);

		for(int i=0,j=0;i<rt.legendOptionsList.size();i++,j++) {

			currentImage = rt.getCurrentScrollPositionOfViewbox(1);
			badgeCount=rt.getBadgeCountFromToolbar(1);
			
			rt.waitForTimePeriod(2000);
			
			rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[1/9]", "Contour should have solid circle to highlight which contour is selected. ");

			rt.selectAcceptfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));			

			rt.waitForTimePeriod(2000);
			
			rt.assertEquals(rt.getBadgeCountFromToolbar(1), badgeCount-1, "Checkpoint[2/9]", "Verifying the badge count is reduced");


			rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[3/9]", "After clicking on 'Accept' icon, slice containing first contour of next segment should be rendered. ");

			// TC3713 
			rt.selectPreviousfromGSPSRadialMenu();

			rt.waitForTimePeriod(2000);
			
			rt.assertTrue(rt.verifyAcceptedIsChecked(1),"Checkpoint[4/9]", "verifying that accept icon is marked");

			rt.assertEquals(rt.getFindingState(rt.getText(rt.legendOptionsList.get(i))),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Checkpoint[5/9]", "Verifying that finding state is also changed in finding table");
			rt.closedFindingsMenu(1);
			rt.assertEquals(rt.getCssValue(rt.acceptedStateIcon.get(i),NSGenericConstants.FILL), ThemeConstants.SUCCESS_ICON_COLOR, "Checkpoint[6/9]", "After clicking on 'Accept' icon, all contours of that segment should be accepted.");

			if(i!=rt.legendOptionsList.size()-1)
			{
				rt.navigateToFirstContourOfSegmentation(i+1); 
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()), rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[7/9]", "Contour should have solid circle to highlight which contour is selected. ");
			
			
				
			}
			//TC3714
			rt.selectNextfromGSPSRadialMenu();
			rt.waitForTimePeriod(2000);
			
			//TC3713 and TC3714
			if(j==(rt.legendOptionsList.size()-1)) {
				rt.assertEquals(firstLegend, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[8/9]", "After clicking on 'Accept' icon, slice containing first contour of next segment should be rendered. ");
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(1)), "Checkpoint[9/9]", "Contour should have solid circle to highlight which contour is selected. ");

			}

		}


	}

	@Test(groups = {"Chrome", "IE11", "Edge" , "US909"})
	public void test05_US909_TC3712_TC3713_TC3714_verifyNavigationWhenContourIsRejected() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify RT struct navigation when any contour is rejected through A/R bar"
				+ "<br> Verify RT struct navigation through previous arrow key of A/R bar"
				+ "<br> Verify RT struct navigation through NEXT arrow key of A/R bar");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		int currentImage = 0;
		int badgeCount=0;
		int firstLegend = rt.getCurrentScrollPositionOfViewbox(1);

		
		for(int i=0,j=0;i<rt.legendOptionsList.size();i++,j++) {

			rt.navigateToFirstContourOfSegmentation(i+1);
			currentImage = rt.getCurrentScrollPositionOfViewbox(1);
			badgeCount=rt.getBadgeCountFromToolbar(1);

			rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[1/9]", "Contour should have solid circle to highlight which contour is selected. . ");

			rt.selectRejectfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));			

			rt.assertEquals(rt.getBadgeCountFromToolbar(1), badgeCount-1, "Checkpoint[2/9]", "Verifying the badge count is reduced");

			rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[3/9]", "After clicking on 'Reject' icon, first contour of next segment should be rendered");

			//			TC3713
			rt.selectPreviousfromGSPSRadialMenu();

			rt.assertTrue(rt.verifyRejectedIsChecked(1), "Checkpoint[4/9]", "On hover over Reject icon from A/R bar red color should be seen  for all rejected contours.");

			rt.assertEquals(rt.getFindingState(rt.getText(rt.legendOptionsList.get(i))),ViewerPageConstants.REJECTED_FINDING_COLOR, "Checkpoint[5/9]: Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");


			rt.assertEquals(rt.getCssValue(rt.rejectedStateIcon.get(i),NSGenericConstants.FILL), ThemeConstants.ERROR_ICON_COLOR, "Checkpoint[6/9]", "After clicking on 'Reject' icon, all contours of that segment should be rejected.");

			if(i!=rt.legendOptionsList.size()-1)
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()), rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[7/9]", "Contour should have solid circle to highlight which contour is selected. ");
			//			TC3714
			rt.selectNextfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));

			//			TC3713 and TC3714
			if(j==(rt.legendOptionsList.size())) {
				rt.assertEquals(firstLegend, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[8/9]", "After clicking on 'Reject' icon, slice containing first contour of next segment should be rendered. ");
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(1)), "Checkpoint[9/9]", "Contour should have solid circle to highlight which contour is selected. ");

			}

		}


	}

	@Test(groups = {"Chrome", "IE11", "Edge" , "US909"})
	public void test06_US909_TC3777_verifyNavigationWithRTPostLayoutChange() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify RT struct navigation after layout change");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		layout=new ViewerLayout(driver);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);		
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		poly.openGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(rt.getViewPort(1)).get(4));
		
		rt.selectFindingFromTable(1);
		
//		//	After selecting any finding from findings menu, the selected finding should get rendered into the viewer.
//		After clicking on 'Accept'  or 'Reject' icons, first contour of next segment (next segment of the currently rendered finding) should get highlighted.
//		(Segment order should be same as mentioned in the Legend)
		
		int currentImage = 0, badgeCount=0;
		int segmentSize = rt.legendOptionsList.size();
		int firstLegend = rt.getCurrentScrollPositionOfViewbox(1);

		
//		Navigation should happen through RT struct findings. 
		for(int i=0,j=0;i<segmentSize;i++,j++) {

			currentImage = rt.getCurrentScrollPositionOfViewbox(1);
			badgeCount=rt.getBadgeCountFromToolbar(1);

			rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[1/8]", "Contour should have solid circle to highlight which contour is selected. . ");

			rt.selectRejectfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(6));			

			rt.assertEquals(rt.getBadgeCountFromToolbar(1), badgeCount-1, "Checkpoint[2/8]", "Verifying the badge count is reduced");

			rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[3/8]", "After clicking on 'Reject' icon, first contour of next segment should be rendered");

	
			rt.selectPreviousfromGSPSRadialMenu();

			rt.assertTrue(rt.verifyRejectGSPSRadialMenu(), "Checkpoint[4/8]", "On hover over Reject icon from A/R bar red color should be seen  for all rejected contours.");

//			viewerPage.assertEquals(viewerPage.getFindingState(rt.getText(rt.segmentName.get(i))),ViewerPageConstants.REJECTED_RT_RESULT_COLOR, "Checkpoint[5/9]: Verify pointer color for pending finding on table", "The pointer color for pending finding is blue");

			rt.assertEquals(rt.getCssValue(rt.rejectedStateIcon.get(i),NSGenericConstants.FILL), ThemeConstants.ERROR_ICON_COLOR, "Checkpoint[5/8]", "After clicking on 'Reject' icon, all contours of that segment should be rejected.");

			if(i!=rt.legendOptionsList.size()-1)
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()), rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[6/8]", "Contour should have solid circle to highlight which contour is selected. ");
	
			rt.selectNextfromGSPSRadialMenu();


			if(j==(rt.legendOptionsList.size()-1)) {
				rt.assertEquals(firstLegend, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[7/8]", "After clicking on 'Reject' icon, slice containing first contour of next segment should be rendered. ");
				rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(1)), "Checkpoint[8/8]", "Contour should have solid circle to highlight which contour is selected. ");

			}

		}
	


	}
	
	@Test(groups = {"Chrome", "IE11", "Edge", "US909"})
	public void test10_US909_TC3834_TC3835_verifySyncOnNavigationWithRT() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify RT struct navigation on sync series"
				+ "<br> Verify A/R toolbar is getting displayed on RT struct by default on initial load.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		layout=new ViewerLayout(driver);
			
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
	
		//By default, A/R bar should be rendered for RT struct.
		
		rt.assertTrue(rt.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/5]", "By default, A/R bar should be rendered for RT struct.");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		poly.openGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(rt.getViewPort(1)).get(4));
		rt.selectFindingFromTable(1);
		int segmentSize = rt.legendOptionsList.size();
	
//		Navigation should happen through RT struct findings. 
		for(int i=0;i<segmentSize;i++) {
			
			rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),rt.getHexColorValue(rt.getColorOfSegment(i+1)), "Checkpoint[2/5]", "Contour should have solid circle to highlight which contour is selected. . ");

			rt.selectRejectfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(6));
//			rt.selectRejectfromGSPSRadialMenu();
			rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1), rt.getCurrentScrollPositionOfViewbox(2), "Checkpoint[3/5]", "After clicking on 'Reject' icon, While navigating from one segment to another , both series should be in sync. ");

			rt.selectPreviousfromGSPSRadialMenu();
			rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1), rt.getCurrentScrollPositionOfViewbox(2), "Checkpoint[4/5]", "After clicking on 'Previous' icon, While navigating from one segment to another , both series should be in sync. ");

			rt.selectNextfromGSPSRadialMenu();
			rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1), rt.getCurrentScrollPositionOfViewbox(2), "Checkpoint[5/5]", "After clicking on 'Next' icon, While navigating from one segment to another , both series should be in sync. ");

		}
	
	}
	
	/*
	 * US779 Test Cases
	 */
	
	@Test(groups ={"IE11","Chrome","Edge","US779","Positive"})
	public void test12_US779_TC3369_verifySegmentsInFindingTable() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findings dropdown will display the list of segmentation findings");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		List<WebElement> legends = drt.legendOptionsList;
		//verify name of each findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify name of each type of finding");

		for(int i =0;i<legends.size();i++) 
			drt.assertEquals(drt.getTextOfFindingFromTable(i+1),drt.getText(legends.get(i)), "Verify name of finding on table", "The name of finding is: "+drt.getTextOfFindingFromTable(i+1));
		
		
	}

	@Test(groups ={"IE11","Chrome","Edge","US779","Positive","DE1383"})
	public void test13_US779_TC3370_DE1383_TC5717_verifyFirstContourDisplayedOnFindingSelection() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when a segmentation from finding list is selected then the first contour of that segmentation should get displayed and highlighted"
				+ "<br> Verify there is no console error when any RT contour is selected from finding menu drop down");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT dr =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		int totalNumberOfSegmentsInLegend = dr.getCountOfAllNavigationArrows();
		List<Integer> firstSliceImageCount = new ArrayList<Integer>();

		for(int i = 1; i <= totalNumberOfSegmentsInLegend; i++) {
			dr.navigateToFirstContourOfSegmentation(i);
			firstSliceImageCount.add(dr.getCurrentScrollPositionOfViewbox(1));

		}

		List<WebElement> legends = dr.legendOptionsList;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", " First contour of selected segmentation should get highlighted in viewbox");

		for(int i =0;i<legends.size();i++) {

			//hover mouse over bottom on view box to display A/R radial bar.
			dr.selectFindingFromTable(i+1);
			//dr.assertFalse(dr.isConsoleErrorPresent(), "Checkpoint[2/3]", "Verifying there is no console error present");
			dr.assertEquals(dr.getCurrentScrollPositionOfViewbox(1),firstSliceImageCount.get(i),"Checkpoint[3/3]", " First contour of selected segmentation should get highlighted in viewbox");
		}

	}

	@Test(groups ={"IE11","Chrome","Edge","US779","Positive"})
	public void test14_US779_TC3371_verifyGSPSFindingsOnDicomRT() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when GSPS is also present in RtStruct data on viewer then GSPS finding should be display in Finding dropdown after all segment names");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT dcrt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		viewerPage=new ViewerPage(driver);
		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
				
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//navigate to next 2 slice and select Point annotation from radial menu and draw a point annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
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

		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));

		List<WebElement> legends = dcrt.legendOptionsList;
		
		viewerPage.click(viewerPage.getViewPort(1));
		findingMenu.openFindingTableOnBinarySelector(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "All the segments and annotations should be present in drop down finding list");
		viewerPage.assertTrue(findingMenu.verifyNumberOfFindingsOnHeaderOfFindingTable((legends.size()+ 4)) ,"Verify total finding count on header of finding table from binary selector", "The total finding count on header is "+(legends.size()+ 4));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that if GSPS loaded on viewer than only GSPS findings should get display in finding menu and if Rt result loaded into viewer than only Rt finding should get display into viewer");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify name of each type of finding");
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(legends.size()+1),ViewerPageConstants.LINEAR_FINDING_NAME, "Verify name of first GSPS finding on table", "The name of first finding is: "+findingMenu.getTextOfFindingFromTable(1));
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(legends.size()+2),ViewerPageConstants.POINT_FINDING_NAME, "Verify name of second GSPS finding on table", "The name of second finding is: "+findingMenu.getTextOfFindingFromTable(2));	
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(legends.size()+3),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Verify name of third GSPS finding on table", "The name of third finding is: "+findingMenu.getTextOfFindingFromTable(3));
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(legends.size()+4),ViewerPageConstants.CIRCLE_FINDING_NAME, "Verify name of fourth GSPS finding on table", "The name of fourth finding is: "+findingMenu.getTextOfFindingFromTable(4));



	}

	@Test(groups ={"IE11","Chrome","Edge","US779","Negative","BVT"})
	public void test15_US779_TC3387_verifyDICOMRTSelectedDisabledSegment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify behavior segmentation when it is disabled from Legend");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT dr =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		List<Integer> firstSliceImageCount = new ArrayList<Integer>();

		for(int i = 1; i <= dr.getCountOfAllNavigationArrows(); i++) {
			dr.navigateToFirstContourOfSegmentation(i);
			firstSliceImageCount.add(dr.getCurrentScrollPositionOfViewbox(1));

		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Click on any segmentation name in legend to disable it");
		List<WebElement> legends = dr.legendOptionsList;
		dr.click(legends.get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Open dropdown finding list from the A/R power bar");

		//hover mouse over bottom on view box to display A/R radial bar.
		dr.selectFindingFromTable(1);
		dr.assertEquals(dr.getCurrentScrollPositionOfViewbox(1),firstSliceImageCount.get(0), "Checkpoint[3/4]", "Verify that clicking on segmentation redirects to user on the slice which have first contour of that segment");

		dr.compareElementImage(protocolName, dr.getViewbox(1),"Checkpoint[4/4] : Contour not be visible as user disable the same segmentation (in step 3) from legend","test15_01");

	}

	@Test(groups ={"IE11","Chrome","Edge","US779","Positive"})
	public void test16_US779_TC3388_verifyBadgeCountOnFindingTableForDICOMRT() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that dropdown finding icon bubble display all the findings present in viewer");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		viewerPage=new ViewerPage(driver);
		
		//hover over binary selector and verify envoy AI finding icon is not visible
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify finding icon is  present on toolbar for DICOMRT");
		drt.openFindingTableOnBinarySelector(1);
		drt.assertTrue(drt.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is present on toolbar", "The finding icon is present on toolbar");

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -200, -150);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);
		
		drt.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));
		//hover over binary selector and verify envoy AI finding icon is visible after used draw annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify finding icon is present on binary selector,when GSPS object is present on UI");
		drt.click(drt.getViewPort(1));
		drt.assertTrue(drt.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is present on binary selector", "The finding icon is present on binary selector");
		drt.assertEquals(drt.getBadgeCountFromToolbar(1),drt.legendOptionsList.size(), "Verify number of pending finding on badge for binary selector", "The number of pending finding is "+drt.getBadgeCountFromToolbar(1));

		//change the state Point and ellipse annotation
		drt.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ellipse.selectAcceptfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));

		//hover mouse over bottom on view box to display binary selector and verify pending annotation count
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify badge count for pending finding on binary selector");
		drt.assertEquals(drt.getBadgeCountFromToolbar(1),(drt.legendOptionsList.size()+2), "Verify number of pending finding on badge for binary selector", "The number of pending finding is "+drt.getBadgeCountFromToolbar(1));

		//change the circle annotation and verify badge count increase
		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify badge count for pending finding on binary selector increase on user interaction");
		drt.assertEquals(drt.getBadgeCountFromToolbar(1),(drt.legendOptionsList.size()+3), "Verify number of pending finding on badge for binary increase on user interaction", "The number of pending finding is "+drt.getBadgeCountFromToolbar(1));

		
		//delete all the annotation and verify finding icon is not present on UI.
		drt.deleteAllAnnotation(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify finding icon is not present on binary selector,when GSPS object is present on UI");
		drt.navigateToFirstContourOfSegmentation(2);
		drt.assertTrue(drt.verifyFindingIconOnBinarySelector(1) ,"Verify finding icon is present on binary selector", "The finding icon is present on binary selector");
		drt.assertEquals(drt.getBadgeCountFromToolbar(1),drt.legendOptionsList.size(), "Verify number of pending finding on badge for binary selector", "The number of pending finding is "+drt.getBadgeCountFromToolbar(1));

	}

	@Test(groups ={"IE11","Chrome","Edge","US783"})
	public void test17_US779_TC3390_verifyNameOfFindingOnTable() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS finding menu functionality should be working as expected");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		viewerPage=new ViewerPage(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
				
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);

		List<WebElement> legends = drt.legendOptionsList;
		
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		drt.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));
		
		//hover over binary selector and click envoy AI finding
		drt.click(drt.getViewPort(1));
		drt.openFindingTableOnBinarySelector(1);

		//verify name of each findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify name of each type of finding");
		drt.assertEquals(findingMenu.getTextOfFindingFromTable(legends.size()+1),ViewerPageConstants.LINEAR_FINDING_NAME, "Verify name of first finding on table", "The name of first finding is: "+drt.getTextOfFindingFromTable(1));
	
		drt.selectFindingFromTable(ViewerPageConstants.LINEAR_FINDING_NAME);
		drt.openFindingTableOnBinarySelector(1);
		drt.assertTrue(drt.verifyFindingIsHighlighted(legends.size()+1),"Checkpoint[2/2]","Verified that finding is highlighted.");
		

	}
		
	@Test(groups = {"Chrome", "IE11", "Edge" , "DE1458","Positive"})
	public void test18_DE1458_TC6018_verifyNavigationOfSegments() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is redirected to first contour of legend when click on legend navigation icon from legend list (Specific to \"CMR_ITA_BER_FOR\" data)");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+cmrITAPatient+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(cmrITAPatient,1, 1);
		
		int[] phase = {17,18,7};
		int[] slices = {3,3,4};
		int[] imageNumber = {419,418,364};			
		
		//Clicking on segment and navigating to first contour
		int totalNumberOfSegmentsInLegend = drt.getCountOfAllNavigationArrows();
		List<WebElement> findingName = drt.getLegendOptionsList(1);
		
		drt.assertEquals(totalNumberOfSegmentsInLegend,3,"Checkpoint[1/7]","Verifying the selected countour and segment has some color");
		
		for(int i = 1; i <=totalNumberOfSegmentsInLegend; i++) {
			drt.navigateToFirstContourOfSegmentation(i);
			drt.assertEquals(drt.getHexColorValue(drt.getSelectedContourColor()),drt.getHexColorValue(drt.getLegendOptionColor(drt.getText(findingName.get(i-1)))),"Checkpoint[2/7]","Verifying the selected countout and segment has some color");	
			drt.assertEquals(drt.getValueOfCurrentPhase(1),phase[i-1],"Checkpoint[3/7]","Verifying the phase number");	
			drt.assertEquals(drt.getCurrentScrollPosition(1),slices[i-1]+"","Checkpoint[4/7]","Verifying the slice number");	
			drt.assertEquals(drt.getImageNumberLabelValue(1),imageNumber[i-1],"Checkpoint[5/7]","Verifying the image number");	
			
			
		}	
		
	}
	
	@Test(groups = {"Chrome", "IE11", "Edge" , "DE1405","Positive"})
	public void test19_DE1405_TC5762_verifySelectionOfContours() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the RT Struct contours are selected again after deselecting the contour - Happy Path.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
	
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		rt.waitForDICOMRTToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Un selecting the contour after clicking on anywhere on the viewer" );
		rt.click(-150,150);
				
		List<WebElement> polylines = poly.getAllPolylines(1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Trying selecting all the polylines present on current slice" );
		for(int i=0;i<polylines.size();i++) {
			
			poly.mouseHover(poly.getLinesOfPolyLine(1,i+1).get(10));
			poly.click(0,0);
			poly.assertTrue(poly.verifyClosedPolylineIsSelected(1, i+1), "Checkpoint[1/2]", "Verifying the contour is selected");	
		}
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changed the slice and verifying that on click of contours, it is getting selected" );
		rt.selectNextfromGSPSRadialMenu();		
		polylines = poly.getAllPolylines(1);
		for(int i=0;i<polylines.size();i++) {
			
			poly.mouseHover(poly.getLinesOfPolyLine(1,i+1).get(5));
			poly.click(0,0);
			poly.assertTrue(poly.verifyClosedPolylineIsSelected(1, i+1), "Checkpoint[2/2]", "Verifying the contour is getting selected after changing the slice");		
		}
	}
	
	@Test(groups = {"Chrome", "IE11", "Edge", "DE1754","Negative"})
	public void test20_DE1754_TC7302_verifyResultWhenAnnotationDrawnOnSourceSeriesForRT() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the added annotation on source series does not add the annotations on respective  RT struct result series.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		circle=new CircleAnnotation(driver);
		layout=new ViewerLayout(driver);
		
		//change layout and draw annotation
		layout.selectLayout(layout.twoByOneLayoutIcon);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 5, 5, -80, -80);
		rt.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[1/4]", "Verified that circle annotation is current active accepted GSPS.");
		rt.mouseHover(rt.getGSPSHoverContainer(2));
		rt.openFindingTableOnBinarySelector(2);
		rt.assertEquals(rt.acceptedFindings.size(), 1, "Checkpoint[2/4]", "Verified that circle annotation visible in finding table as well.");
		rt.closingConflictMsg();
		
		//mousehover on first viewbox i.e. result
		rt.mouseHover(rt.getViewPort(1));
		rt.assertFalse(circle.isCirclePresent(1), "Checkpoint[3/4]", "Verified that circle annotation is not present on result series.");
		rt.openFindingTableOnBinarySelector(1);
		rt.assertTrue(rt.acceptedFindings.isEmpty(), "Checkpoint[4/4]", "Verified that no accepted finding visible in finding menu dropdown.");
		
		
		
		}
	
	@Test(groups = {"Chrome","IE11","Edge","DE1799","Positive"})
	public void test21_DE1799_TC7304_verifyForwardBackwardNavigationUsingARToolbar() throws  InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify RT struct forward and backward navigation when any contour is accepted/rejected through A/R bar");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		cs=new ContentSelector(driver);
		
		rt.navigateToFirstContourOfSegmentation(1); 
		rt.selectAcceptfromGSPSRadialMenu();
		
		rt.navigateToFirstContourOfSegmentation(5);
		rt.selectRejectfromGSPSRadialMenu();
		
		//reload viewer page
	    helper=new HelperClass(driver);
	    helper.browserBackAndReloadRTData(patientNameDICOMRT,1,1);
	    
		rt.navigateToFirstContourOfSegmentation(1); 
		
		//Forward navigation using A/R toolbar
		verifyForwardNavigationUsingARToolbarOrKeyboard(rt.legendOptionsList.size(), 1, 4,true);
		
		//Previous navigation using A/R toolbar
		verifyBackwardNavigationUsingARToolbarorKeyboard(rt.legendOptionsList.size(), 2, 4,true);
		
		//select original result from Content selector
		cs.selectResultFromSeriesTab(1, resultToSelect);
		rt.waitForDICOMRTToLoad();
        rt.navigateToFirstContourOfSegmentation(1); 
		
		//Forward navigation using A/R toolbar
        verifyForwardNavigationUsingARToolbarOrKeyboard(rt.legendOptionsList.size(), 3, 4,false);
		
		//Previous navigation using A/R toolbar
        verifyBackwardNavigationUsingARToolbarorKeyboard(rt.legendOptionsList.size(), 4, 4,false);
		
	}
	
	@Test(groups = {"Chrome","IE11","Edge" ,"DE1799","Positive"})
	public void test22_DE1799_TC7304_verifyForwardBackwardNavigationUsingKeyboard() throws  InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify RT struct forward and backward navigation when any contour is accepted/rejected using keyboard.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		cs=new ContentSelector(driver);
		
		rt.navigateToFirstContourOfSegmentation(1); 
		rt.selectAcceptfromGSPSRadialMenu();
		
		rt.navigateToFirstContourOfSegmentation(5);
		rt.selectRejectfromGSPSRadialMenu();
		
		//reload viewer page
	    helper=new HelperClass(driver);
	    helper.browserBackAndReloadRTData(patientNameDICOMRT,1,1);
		rt.navigateToFirstContourOfSegmentation(1); 
		
		//Forward navigation using keyboard
		verifyForwardNavigationUsingARToolbarOrKeyboard(rt.legendOptionsList.size(), 1, 4,false);
		
		//Previous navigation using keyboard
		verifyBackwardNavigationUsingARToolbarorKeyboard(rt.legendOptionsList.size(), 2, 4,false);
		
		//select original result from Content selector
		cs.selectResultFromSeriesTab(1, resultToSelect);
		rt.waitForDICOMRTToLoad();
        rt.navigateToFirstContourOfSegmentation(1); 
       
        //Forward navigation using keyboard
        verifyForwardNavigationUsingARToolbarOrKeyboard(rt.legendOptionsList.size(), 3, 4,false);
		
    	//Previous navigation using keyboard
        verifyBackwardNavigationUsingARToolbarorKeyboard(rt.legendOptionsList.size(), 4, 4,false);
		
		
		
	}
	
	
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}

	public void navigateToFirstContourOfSegment(String testcase) throws InterruptedException {
		DICOMRT drt = new DICOMRT(driver);
		int totalNumberOfSegmentsInLegend = drt.getCountOfAllNavigationArrows();
		for(int i = 1; i <= totalNumberOfSegmentsInLegend; i++) {
			drt.navigateToFirstContourOfSegmentation(i);
        drt.compareElementImage(protocolName, drt.mainViewer, "verifying the viewer for patient "+patientNameDICOMRT, testcase +i);
		}		
	}
	
	public void verifyForwardNavigationUsingARToolbarOrKeyboard(int segment,int checkpoint,int lastCheckpoint,boolean ARToolOrKeyboard) throws InterruptedException
	{
	  for(int i=0;i<segment;i++) 
		  
	    if(ARToolOrKeyboard)
	    {
		DICOMRT rt = new DICOMRT(driver);
		int currentImg = rt.getCurrentScrollPositionOfViewbox(1);
		rt.selectNextfromGSPSRadialMenu();
		rt.assertNotEquals(currentImg, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint["+checkpoint+"."+(i+1)+"/"+lastCheckpoint+"]", "After clicking on Next from acceptReject toolbar, first contour of next segment rendered.");
        }
	    else
	    {
			DICOMRT rt = new DICOMRT(driver);
			int currentImg = rt.getCurrentScrollPositionOfViewbox(1);
			rt.navigateGSPSForwardUsingKeyboard();
			rt.assertNotEquals(currentImg, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint["+checkpoint+"."+(i+1)+"/"+lastCheckpoint+"]", "After clicking on forward arrow from keyboard, first contour of next segment rendered.");
	     }
	    	
	
		
	}

	public void verifyBackwardNavigationUsingARToolbarorKeyboard(int segment,int checkpoint,int lastCheckpoint,boolean ARToolOrKeyboard) throws InterruptedException
	{
	for(int i=0;i<segment;i++) 
		if (ARToolOrKeyboard)
	    {
		   DICOMRT rt = new DICOMRT(driver);
		   int currentImg = rt.getCurrentScrollPositionOfViewbox(1);
		   rt.selectPreviousfromGSPSRadialMenu();
		   rt.assertNotEquals(currentImg, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint["+checkpoint+"."+(i+1)+"/"+lastCheckpoint+"]", "After clicking on Previous from acceptReject toolbar, first contour of previous segment rendered.");
        }
		else	
		{
			DICOMRT rt = new DICOMRT(driver);
			int currentImg = rt.getCurrentScrollPositionOfViewbox(1);
			rt.navigateGSPSBackUsingKeyboard();
			rt.assertNotEquals(currentImg, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint["+checkpoint+"."+(i+1)+"/"+lastCheckpoint+"]", "After clicking on back arrow from keyboard, first contour of previous segment rendered");
	     }
		
	}
	
	
}
