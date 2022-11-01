//import org.testng.annotations.Test;
//
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.basic.StudyListPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.obsolete;
////
////import java.io.IOException;
////import java.util.List;
////
////import javax.xml.parsers.ParserConfigurationException;
////
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////import org.xml.sax.SAXException;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.trn.ns.page.factory.LoginPage;
////
////import com.trn.ns.page.factory.PatientListPage;
////
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.DataReader;
////import com.trn.ns.utilities.ExtentManager;
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class NavigationBetweenAppPages extends TestBase{
////
////	private LoginPage loginPage;
////	private PatientListPage patientPage;
////	private ViewerPage viewerpage;
////	private SinglePatientStudyPage patientStudyPage;
////
////	private ExtentTest extentTest;
////
////	private static String  viewerPageUrl="", viewportmonitorNumber1="";
////	
////	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
////	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
////
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod() throws IOException, InterruptedException, SAXException, ParserConfigurationException {
////
////		loginPage = new LoginPage(driver);
////		loginPage.navigateToBaseURL();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////	}
////	//TC544 - Verify that user is able to access the Northstar pages directly
////
////	@Test(groups ={"firefox","Chrome","Edge","multimonitor"})
////	public void test01_US133_TC544_part1_verifyNorthstarPagesAccess() throws InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to access the northstar pages directly - viewerpage");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Loading the Patient "+patientName+"in viewer" );
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(patientName);
////
////		patientStudyPage = new SinglePatientStudyPage(driver);
////		patientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		viewerPageUrl = viewerpage.getCurrentPageURL();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////		viewerpage.openOrCloseChildWindows(2);
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////
////		childWinHandles.remove(parentWindow);
////		viewerpage.switchToWindow(childWinHandles.get(0));
////		viewportmonitorNumber1 = viewerpage.getCurrentPageURL();
////
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.waitForViewerpageToLoad();
////		viewerpage.waitForViewerpageWithPDFToLoad();
////
////
////		//Step 2 - open new browser window and launch the URLs directly -
////		//verifying viewer in new tab directly 
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify the launching of Viewer page");
////		viewerpage.openNewWindow(viewerPageUrl);
////		List<String> childWindowIDs = viewerpage.getAllOpenedWindowsIDs();
////		childWindowIDs.remove(parentWindow);
////		childWindowIDs.remove(childWinHandles.get(0));
////		viewerpage.switchToWindow(childWindowIDs.get(0));
////
////		//Verifying viewer page url in new tab
////		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying the launching of Viewer page", "User is on "+ viewerpage.getCurrentPageURL()+" page");
////		viewerpage.openOrCloseChildWindows(1);	
////		viewerpage.closeWindow(childWindowIDs.get(0));
////		viewerpage.switchToWindow(parentWindow);
////
////
////
////
////	}
////
////	@Test(groups ={"firefox","Chrome","Edge","multimonitor"})
////	public void test01_part4_US133_TC544_part4verifyNorthstarPagesAccess() throws InterruptedException 
////	{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to access the northstar pages directly - child window");
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Loading the Patient "+patientName+"in viewer" );
////		patientPage = new PatientListPage(driver);
////		patientPage.clickOnPatientRow(patientName);
////
////		patientStudyPage = new SinglePatientStudyPage(driver);
////		patientStudyPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		viewerPageUrl = viewerpage.getCurrentPageURL();
////
////		String parentWindow = viewerpage.getCurrentWindowID();
////
////		//verifying viewer child window 1 page in new tab directly 
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify the launching of first child window");
////		patientStudyPage.openNewWindow(viewportmonitorNumber1);
////
////		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
////
////		childWinHandles.remove(parentWindow);
////		viewerpage.switchToWindow(childWinHandles.get(0));
////
////		//verifying patient list url in new tab
////		//Verifying viewer child window 1 page url in new tab
////		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains("viewport?monitorNumber=1"), "Verifying the launching of first child window", "User is on "+ viewerpage.getCurrentPageURL()+" page");
////		patientPage.closeWindow(childWinHandles.get(0));
////		patientPage.switchToWindow(parentWindow);
////
////
////	}
////}
//
//@Test(groups ={"firefox","Chrome","Edge"},enabled=false)
//	public void test01_part5_US133_TC544_part5_verifyNorthstarPagesAccess() throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to access the northstar pages directly - StudyList Page");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Loading the Patient "+patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		patientStudyPageUrl = patientPage.getCurrentPageURL();
//		patientPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerPageUrl = viewerpage.getCurrentPageURL();
//
//
//		//Step 3 -Launch the other pages -
//		//opening study list in new tab directly 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify the launching of Studylist page");
//		viewerpage.navigateToChangedURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);
//
//		//verifying patient list url in 
//		//verifying study list in 
//		studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying the launching of Studylist page", "User is on "+ studyPage.getCurrentPageURL()+" page");
//
//
//	}