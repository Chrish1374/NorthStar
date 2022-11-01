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
//import com.trn.ns.page.factory.CircleAnnotation;
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
//public class CircleAnnotationTest extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//	private CircleAnnotation circle;
//	
//	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
//	String GSPS_CIRCLE_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//	private String firstSeriesDescriptionGSPSCircle = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath"));
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
//	}
//
//	//TC1709	Image annotation: circle- multi window
//	//Limitation : Should fail because of DE445 - Need to take base images after fixing the defect
//	//			 : Not work in firefox because of DE456
//	@Test(groups ={"firefox","Chrome","IE11","Edge","multimonitor" })
//	public void test03_US522_TC1709_verifyImageAnnotationCircleOnMultiWindow() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Image annotation: Circle- multi window");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_CIRCLE_patientName+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(GSPS_CIRCLE_patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		circle = new CircleAnnotation(driver);
//		// selecting 4 window
//		String parentWindow = viewerPage.getCurrentWindowID();
////		viewerPage.openOrCloseChildWindows(2);
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
////				viewerPage.selectSeriesFromContentSelector(1,firstSeriesDescriptionGSPSCircle);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+j+"/3]", "Verify that the GSPS annotation Circles are located in the same region in first viewbox of Child window-"+viewerPage.getCurrentPageURL());
//				String childWinNum = viewerPage.getCurrentPageURL().substring(viewerPage.getCurrentPageURL().length()-1);
//				viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1, "Checkpoint "+j+": Verifying that GSPS annotation Circles are located in the same region in first viewbox of Child window-"+viewerPage.getCurrentPageURL(), "Circle_ChildWindow_"+childWinNum);
//
//				j++;
//			}
//		}
//
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.maximizeWindow();
//
//		//Resetting the child windows
////		viewerPage.openOrCloseChildWindows(1);
//	}
//}
//
//
//
