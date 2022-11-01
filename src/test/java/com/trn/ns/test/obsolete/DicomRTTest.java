
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.testng.annotations.Test;
//
//import com.trn.ns.page.factory.DICOMRT;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.PolyLineAnnotation;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.obsolete;
////
////
////import java.awt.AWTException;
////import org.openqa.selenium.TimeoutException;
////import org.openqa.selenium.WebElement;
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.trn.ns.page.factory.CircleAnnotation;
////import com.trn.ns.page.factory.ContentSelector;
////import com.trn.ns.page.factory.DICOMRT;
////import com.trn.ns.page.factory.LoginPage;
////import com.trn.ns.page.factory.MeasurementWithUnit;
////
////import com.trn.ns.page.factory.PatientListPage;
////
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.DataReader;
////import com.trn.ns.utilities.ExtentManager;
////
//////Safety.NS_F158_SupportForStorageAndDisplayOfDICOMRTSeries-CF0304ARevD - revision-0
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class DicomRTTest extends TestBase {
////
////	private ExtentTest extentTest;
////	private LoginPage loginPage;
////	private PatientListPage patientListPage;
////	private SinglePatientStudyPage spStudyListPage;
////	private ViewerPage viewerPage;
////	private ContentSelector contentSelector;
////    private CircleAnnotation circle;
////
////
////
////	// Get Patient Name
////	String RANDENT_filepath = Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
////	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, RANDENT_filepath);
////
////	String RANDOENT_with2RtStructSeries_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
////	String patientName2RTStructSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, RANDOENT_with2RtStructSeries_filepath);
////	String firstSeriesDescriptionrtStruct = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, RANDOENT_with2RtStructSeries_filepath);
////	
////	// Before method, handles the steps before loading the data for set up.
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod() {
////		// Begin on the Login Page, and log in.
////		loginPage = new LoginPage(driver);		
////		loginPage.navigateToBaseURL();		
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////
////	}
////	
////	@Test(groups = {"Chrome", "IE11", "Edge" , "US909","Sanity"})
//	public void test06_US909_TC3715_verifyNavigationWithGSPSandRT() throws  InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify RT struct navigation when any RT struct finding(same session) is selected from findings dropdown");
//
//		//Loading patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();	
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
//
//		PointAnnotation point = new PointAnnotation(driver);
//
//		point.selectPointAnnotationIconFromRadialMenu(1);
//
//		//		 Draw GSPS on any slice
//
//		rt.inputImageNumber(30,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
//
//		rt.inputImageNumber(35,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
//		point.drawPointAnnotationMarkerOnViewbox(1, -250, -250);
//
//		rt.inputImageNumber(1, 1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -270, -270);
//		point.drawPointAnnotationMarkerOnViewbox(1, -150, -200);
//
//		rt.inputImageNumber(80,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 150, 200);
//
//		int currentImage = 0;
//
//		//Open findings menu from A/R toolbar
//		rt.selectFindingFromTable(4);
//
//		int totalSegmentsSize= rt.segmentName.size();
//
//		for(int i=4;i<totalSegmentsSize+4;i++) {
//
//			if(i<totalSegmentsSize) {
//				currentImage = rt.getCurrentScrollPositionOfViewbox(1);				
//				rt.assertEquals(rt.getHexColorValue(rt.getSelectedCountorColor()),rt.getHexColorValue(rt.getColorOfSegment(i)), "Checkpoint[1/6]", "After selecting any finding from findings menu, the selected finding should get rendered into the viewer.");
//				rt.selectNextfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));		
//				rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[2/6]", "User should navigate through all RT struct findings first as RT struct is selected through findings menu and then user should navigate though all GSPS findings");
//
//			}
//			if(i>=totalSegmentsSize) {
//				rt.selectNextfromGSPSRadialMenu();
//				int pointsSize = point.getAllPoints(1).size();
//					
//				for(int k = pointsSize;k>0;k--) {
//					point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,k),"Checkpoint[3/6]","After clicking on 'Next'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered. ");
//					
//						if(k>1)
//						rt.selectNextfromGSPSRadialMenu();	
//				}}
//		}
//
//
//		for(int i=totalSegmentsSize+4;i>0;i--) {
//
//			if(i>totalSegmentsSize) {
//				rt.selectPreviousfromGSPSRadialMenu();
//				int pointsSize = point.getAllPoints(1).size();
//				for(int k = 0;k<pointsSize;k++) {
//					point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,(k+1)),"Checkpoint[4/6]","After clicking on 'Next'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered. ");
//					if(k<(pointsSize-1))
//						rt.selectPreviousfromGSPSRadialMenu();
//
//				}}
//
//			if(i<=totalSegmentsSize) {
//				currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
//
//				rt.assertEquals(rt.getHexColorValue(rt.getSelectedCountorColor()),rt.getHexColorValue(rt.getColorOfSegment(i)), "Checkpoint[5/6]", "After clicking on 'Next'/'Prev'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered.  ");
//
//				rt.selectPreviousfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));
//
//				viewerPage.assertNotEquals(currentImage, viewerPage.getCurrentScrollPositionOfViewbox(1), "Checkpoint[6/6]", "After clicking on 'Previous  icon, first contour of previous segment in case of RT struct finding  OR previous GSPS in case of GSPS finding, should be rendered.");
//
//			}
//
//
//		}
//
//	}
//	
//	@Test(groups = {"Chrome", "IE11", "Edge" , "US909"})
//	public void test07_US909_TC3716_verifyNavigationWithGSPS() throws  InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify RT struct navigation when GSPS finding(different session) is selected from findings dropdown");
//
//		//Loading patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();	
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//			
//		PointAnnotation point = new PointAnnotation(driver);
//
//		point.selectPointAnnotationIconFromRadialMenu(1);
//
//		//		 Draw GSPS on any slice
//
//		List<Integer> sliceNumber = new ArrayList<Integer>(Arrays.asList(30, 35, 80,1));
//
//
//		rt.inputImageNumber(sliceNumber.get(0),1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
//
//		rt.inputImageNumber(sliceNumber.get(1),1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
////		point.drawPointAnnotationMarkerOnViewbox(1, -250, -250);
//
//		rt.inputImageNumber(sliceNumber.get(3), 1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -270, -270);
////		point.drawPointAnnotationMarkerOnViewbox(1, -150, -200);
//
//		rt.inputImageNumber(sliceNumber.get(2),1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 150, 200);
//
//
//		//		Go back to patient list page and re-load viewer by selecting same patient as in step-1
//       helper=new HelperClass(driver);
//       helper.browserBackAndReloadRTData(patientNameDICOMRT,1,1);
//
//		//		Select any GSPS finding from findings menu from A/R bar.
//		rt.selectFindingFromTable(1);
//		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[1/6]","After clicking on 'Next'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered. ");
//
//		for(int i=0;i<4;i++) {						
//			int pointsSize = point.getAllPoints(1).size();		
//			point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), sliceNumber.get(i), "Checkpoint[2/6]", "Navigation should happen through GSPS only as GSPS finding is selected from finding menu. ");
//			for(int k = 0;k<pointsSize;k++) {		
//				point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,(k+1)),"Checkpoint[3/6]","Click on 'Accept' , ''Reject', 'Previous' , 'Next' icons and navigate through findings.");
//				rt.selectRejectfromGSPSRadialMenu();	
//			}
//		}
//
//		for(int i=0;i<4;i++) {
//
//			int pointsSize = point.getAllPoints(1).size();				
//			for(int k = 0;k<pointsSize;k++) {		
//				point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,(k+1)),"Checkpoint[4/6]","Click on 'Accept' , ''Reject', 'Previous' , 'Next' icons and navigate through findings.");
//				rt.selectNextfromGSPSRadialMenu();	
//			}}
//
//		for(int i=0;i<4;i++) {
//
//
//			int pointsSize = point.getAllPoints(1).size();				
//			for(int k = 0;k<pointsSize;k++) {		
//				point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,(k+1)),"Checkpoint[5/6]","Click on 'Accept' , ''Reject', 'Previous' , 'Next' icons and navigate through findings.");
//				rt.selectAcceptfromGSPSRadialMenu();	
//			}}
//
//		for(int i=0;i<4;i++) {
//
//			int pointsSize = point.getAllPoints(1).size();				
//			for(int k = pointsSize;k>0;k--) {		
//				point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,k),"Checkpoint[6/6]","Click on 'Accept' , ''Reject', 'Previous' , 'Next' icons and navigate through findings.");
//				rt.selectPreviousfromGSPSRadialMenu();	
//			}}
//
//
//
//	}
//	
//	@Test(groups = {"Chrome", "IE11", "Edge" , "US909","DE917"})
//	public void test08_US909_TC3767_DE917_TC3830_verifyNavigationWithGSPSandRT() throws  InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify RT struct navigation when any GSPS finding(same session) is selected from findings dropdown"
//				+ "<br> Verify that existing RT accept / reject , navigation from legend and finding drop down menu should not get broken");
//
//		//Loading patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();	
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
//
//		PointAnnotation point = new PointAnnotation(driver);
//
//		point.selectPointAnnotationIconFromRadialMenu(1);
//
//		//		 Draw GSPS on any slice
//
//		rt.inputImageNumber(30,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
//
//		rt.inputImageNumber(35,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 300, 300);
////		point.drawPointAnnotationMarkerOnViewbox(1, -250, -250);
//
//		rt.inputImageNumber(1, 1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -270, -270);
////		point.drawPointAnnotationMarkerOnViewbox(1, -150, -200);
//
//		rt.inputImageNumber(80,1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 150, 200);
//
//		int currentImage = 0;
//
//		int totalSegmentsSize= rt.segmentName.size();
//
//		//Open findings menu from A/R toolbar
////		rt.selectFindingFromTable(totalSegmentsSize);
//		rt.selectFindingFromTable(1);
//		int pointsSize=0;
//		int j=1;
//
//		for(int i=0;i<totalSegmentsSize+4;i++) {
//
//			if(i<4) {
//				pointsSize = point.getAllPoints(1).size();
//				for(int k = 0;k<pointsSize;k++) {		
//					point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,(k+1)),"Checkpoint[3/6]","After clicking on 'Next'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered. ");
//					rt.selectRejectfromGSPSRadialMenu();	
//				}
//			}else if(j<=totalSegmentsSize) {
//				currentImage = rt.getCurrentScrollPositionOfViewbox(1);				
//				rt.assertEquals(rt.getHexColorValue(rt.getSelectedCountorColor()),rt.getHexColorValue(rt.getColorOfSegment(j)), "Checkpoint[1/6]", "After selecting any finding from findings menu, the selected finding should get rendered into the viewer.");
//				rt.selectRejectfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));		
//				rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[2/6]", "User should navigate through all RT struct findings first as RT struct is selected through findings menu and then user should navigate though all GSPS findings");
//				j++;
//			}
//		}
//
//
//		for(int i=0;i<totalSegmentsSize+4;i++) {
//
//			if(i<4) {
//				pointsSize = point.getAllPoints(1).size();
//				for(int k = 0;k<pointsSize;k++) {
//					point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,(k+1)),"Checkpoint[4/6]","After clicking on 'Next'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered. ");
//					rt.selectAcceptfromGSPSRadialMenu();
//
//				}
//
//			}else if(j<=totalSegmentsSize) {
//
//				currentImage = rt.getCurrentScrollPositionOfViewbox(1);				
//				rt.assertEquals(rt.getHexColorValue(rt.getSelectedCountorColor()),rt.getHexColorValue(rt.getColorOfSegment(i)), "Checkpoint[5/6]", "After clicking on 'Next'/'Prev'  icon, first contour of next segment in case of RT struct finding  OR next GSPS in case of GSPS finding, should be rendered.  ");
//				rt.selectAcceptfromGSPSRadialMenu(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).get(4));
//				rt.assertNotEquals(currentImage, rt.getCurrentScrollPositionOfViewbox(1), "Checkpoint[6/6]", "After clicking on 'Previous  icon, first contour of previous segment in case of RT struct finding  OR previous GSPS in case of GSPS finding, should be rendered.");
//
//			}
//
//
//		}
//	}
////	
/*
@Test(groups = {"Chrome", "US762","DE1328"})
	public void test04_US762_TC2950_TC2973_TC2975_DE1328_TC5519_verifySelectionAndDeselectionOfItems() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user should able to select or deselect the items in the legend"
				+ "<br> Verify the multiple selection/deselection on the items of legend"
				+ "<br> Verify the select/deselect items for same collection of legends");

		//Loading Bone Age patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);

		//verifying DICOM RT color and slice position		
		int countourLength = DataReader.getNumberOfContours(TCGA_filepath);
		int j=1;

		drt.scrollToImage(1, 39);

		drt.assertTrue(drt.isElementPresent(drt.legendOptions),"Checkpoint[3]","Verifying the options are getting displayed");
		drt.assertEquals(drt.legendOptionsList.size(),countourLength,"Checkpoint[4]","verifying that options size is same");

		for(WebElement element : drt.legendOptionsList) {

			drt.click(element);
			drt.assertEquals(drt.getCssValue(element,NSGenericConstants.FILL),ViewerPageConstants.GREY_COLOR,"Checkpoint[5]","verifying the options colors when deselected");				
			drt.assertEquals(drt.getText(element),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+j,ViewerPageConstants.CONTOUR_DESCRIPTION, TCGA_filepath),"Checkpoint[6]","verifying the options are still displayed when deselected");
			drt.compareElementImage(protocolName, drt.mainViewer, "verifying the contour is not displayed for "+element.getText(), "TC04_checkpoint_a"+j);
			drt.click(element);
			drt.assertEquals(drt.getCssValue(element,NSGenericConstants.FILL),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+j,NSGenericConstants.CSS_PROP_COLOR, TCGA_filepath),"Checkpoint[7]","verifying the options colors when selected");
			drt.compareElementImage(protocolName, drt.mainViewer, "verifying the viewer for contour is displayed for "+element.getText(), "TC03_checkpoint_b"+j);
			j++;
		}

		drt.scrollToImage(1, 43);
		
		drt.waitForTimePeriod(2000);
		
		// deselecting all the options
		for(WebElement element : drt.legendOptionsList) 
			drt.click(element);

		drt.compareElementImage(protocolName, drt.mainViewer, "verifying the all the options are deselected", "TC04_checkpoint6");
		
		for(WebElement element : drt.legendOptionsList) {			
			drt.assertEquals(drt.getCssValue(element,NSGenericConstants.FILL),ViewerPageConstants.GREY_COLOR,"Checkpoint[8]","verifying the options colors");				
		}

		// selecting all the options again
		for(WebElement element : drt.legendOptionsList) 
			drt.click(element);
		
		for(WebElement element : drt.legendOptionsList) 
		drt.assertEquals(drt.getCssValue(element,NSGenericConstants.FILL),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+j,NSGenericConstants.CSS_PROP_COLOR, TCGA_filepath),"Checkpoint[5]","verifying the options colors after all options are enabled");
		drt.compareElementImage(protocolName, drt.mainViewer, "verifying the legend when all options are enabled", "TC03_checkpoint7");

	}
////}
*/