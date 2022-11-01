//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.util.Set;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
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
//public class PdfDisplayTest extends TestBase{
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	//Loading the patient on viewer
//	String filePath = TEST_PROPERTIES.get("AH4_pdf_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//	}
//
//	//TC996 - Verify that the selected viewport should be highlighted when pdf is loaded in viewer in case of multimonitor set up
//	@Test(groups ={"Chrome", "Edge","firefox","IE11","multimonitor"})
//	public void test07_DE248_TC996_verifySelectedViewboxIsHighlightedFromMultimonitor() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that the selected viewport should be highlighted when pdf is loaded in viewer in case of multimonitor set up");
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer");
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForPdfToRenderInViewbox(3);
//
//		//Open child window
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Opening 3 child windows");
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(4);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForAllImagesToLoad();
//
//		//Verify active overlay in each child window
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				for(int i = 1 ; i<=2 ;i++){
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+"/2]", "Verifying selected viewport"+i+" is highlighted when pdf is loaded in viewer in case of multimonitor set up");
//					viewerpage.assertTrue(viewerpage.verifyActiveViewbox(i), "Verify that the selected viewport"+i+" is highlighted on child window-"+viewerpage.getCurrentPageURL()+"", "viewport"+i+" is highlighted");
//				}
//			}
//		//Closing all open child windows
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//}
