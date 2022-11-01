//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.util.Set;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class EllipseImageAnnotationTest extends TestBase{
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
//	String GSPS_ELLIPSE_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//	private String firstSeriesDescriptionGSPSEllipse = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath"));
//
//	String filePath1=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath1);
//
//	String filePath2=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath2);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	
//	//TC1716 : Image annotation: Ellipse- multi window
//	//Limitation : Should fail because of DE445 - Need to take base images after fixing the defect
//	//			 : Not work in firefox because of DE456
//	@Test(groups ={"firefox","Chrome","IE11","Edge","multimonitor","US524","Sanity"})
//	public void test03_US524_TC1716_verifyImageAnnotationEllipseOnMultiWindow() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "Image annotation: Ellipse- multi window");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_ELLIPSE_PatientName+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(GSPS_ELLIPSE_PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		// selecting 4 window
//		String parentWindow = viewerPage.getCurrentWindowID();
//		viewerPage.openOrCloseChildWindows(2);
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.maximizeWindow();
//		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//
//		// selecting check box apply to all monitor if not selected already
////		viewerPage.selectCheckBoxApplyToAllMonitor();
//		// changing layout to 3x3
//		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
//		int j=1;
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.maximizeWindow();
//				//Select same series in first viewbox of each monitor
////				viewerPage.selectSeriesFromContentSelector(1,firstSeriesDescriptionGSPSEllipse);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+j+"/3]", "Verify that the GSPS annotation Ellipses are located in the same region in first viewbox of Child window-"+viewerPage.getCurrentPageURL());
//				String childWinNum = viewerPage.getCurrentPageURL().substring(viewerPage.getCurrentPageURL().length()-1);
//				viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1, "Checkpoint "+j+": Verifying that GSPS annotation Ellipses are located in the same region in first viewbox of Child window-"+viewerPage.getCurrentPageURL(), "Ellipse_ChildWindow_"+childWinNum);
//
//				j++;
//			}
//		}
//
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.maximizeWindow();
//
//		//Resetting the child windows
//		viewerPage.openOrCloseChildWindows(1);
//	}
//
//	//TC1883:Image annotation - Ellipse/ellipse edition and manipulation
//		//Limitation : Not work in firefox because of DE456
//		@Test(groups ={"firefox","Chrome","IE11","Edge","US585","Sanity"})
//		public void test17_US585_TC1883_verifyUserCannotPerformDicomOperationInsideEllipseAnnotation() throws InterruptedException, AWTException{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify that on zoom the size of Ellipse annotation will be change proportionally with image");
//			//Loading the patient on viewer
//			patientPage = new PatientListPage(driver);
//			patientPage.clickOnPatientRow(ah4PatientName);
//
//			
//			studyPage.clickOntheFirstStudy();
//
//			viewerPage = new ViewerPage(driver);
//			viewerPage.waitForViewerpageToLoad();
////			ellipse = new EllipseAnnotation(driver);
////
////			//Draw a Ellipse on Viewer page.
////			ellipse.selectEllipseAnnotationFromContextMenu(1);
////			ellipse.drawEllipse(1, 50, 50, 130,150);	
////			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to draw a Ellipse annotation on DICOM");
////			viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");
////			viewerPage.assertEquals(ellipse.getEllipses(1).size(),1, "Verify a single Ellipse is drawn on Viewbox", "No of Ellipse on Viewbox1 :"+ellipse.getEllipses(1).size());
//
//			// get Zoom level for Canvas 0 before Zoom
//			int intialZoomLevel1 = viewerPage.getZoomLevel(1);
//
//			//Select a Zoom from radial bar
//			viewerPage.selectZoomFromQuickToolbar(1);
//
//			//Perform mouse drag inside circumference of a Ellipse
//			viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 80, 80, 110,110);
//
//			// get Zoom level for Canvas 0 after Zoom
//			int finalZoomLevel1 = viewerPage.getZoomLevel(1);
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verify user is not able to perform zoom inside a Ellipse annotation");
//			viewerPage.assertEquals(finalZoomLevel1,intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);
//
//		} 
//}
//
//
//
