//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//
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
//public class E2E_TC1390_Workflow extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private SinglePatientStudyPage studyPage ;
//	private ViewerPage viewerPage;
//	private ExtentTest extentTest;
//
//	//Get Patient Name
//	String filePath = Configurations.TEST_PROPERTIES.get("Qure_ai_XRay_Patient1_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod () {
//
//		// Navigate to  Login Page, and log in.
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//
//	//Test script run successfully using Chrome and IE11 --Limitation -Do not support in Edge and Firefox
//	@Test(groups ={"Chrome","IE11"})
//	public void test01_E2E_TC1390_verifyContentSelector() throws InterruptedException, AWTException
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Content Selector (CS) Applies to DICOM, Non-DICOM Images and PDF Content");
//		patientPage = new PatientListPage(driver);
//
//
//		//Loading the patient Qure.ai_XRay_Patient1 on viewer
//		patientPage.clickOnPatientRow (patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		//viewerPage.waitForPdfToRenderInViewbox(2);
//		viewerPage.waitForAllImagesToLoad();
//
//		//Open Content Selector and verify check marks , Image Icon / PDF icon , Envoy AI icon .
//
////
////		viewerPage.openContentSelector(viewerPage.viewboxImage1, viewerPage.resultDescriptionFirstViewBox);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying Checkbox appears for visible series in CS " );
////		viewerPage.assertTrue(viewerPage.validateSeriesIsSelectedOnContentSelector(1, "CXR_Heatmap"), "Verifying Checkbox appears to result series 1", "Verified Checkbox appears to result series 1");
////		viewerPage.assertTrue(viewerPage.validateSeriesIsSelectedOnContentSelector(2, "CXR_Report"), "Verifying Checkbox appears to result series 2", "Verified Checkbox appears to result series 2");
////
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying PDF and Image icons appears for result series in CS" );
////		viewerPage.assertTrue(viewerPage.validateImageIconInSelectContainer(1, "CXR_Heatmap"), "Verifying Image Icon  appears to result series 1", "Verified Image Icon appears to result series 1");
////		viewerPage.assertTrue(viewerPage.validatePDFIconInSelectContainer(2, "CXR_Report"), "Verifying PDF icon appears to result series 2", "Verified PDF icon appears to result series 2");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying Envoy AI  icons shows for result series in CS " );
////		viewerPage.openContentSelector(viewerPage.viewboxImage2, viewerPage.resultDescriptionSecondViewBox);
////		viewerPage.assertTrue(viewerPage.validateEnvoyAIconInSelectContainer(1, "CXR_Heatmap"), "Verifying EnvoyAI icon appears to result series 1", "Verified EnvoyAI icon appears to result series 1");
////		viewerPage.assertTrue(viewerPage.validateEnvoyAIconInSelectContainer(2, "CXR_Report"), "Verifying EnvoyAI icon appears to result series 2", "Verified EnvoyAI icon appears to result series 2");
////
////		//Select the series which is not currently from content selector and verify the check mark appears for the newly selected series
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Open content selector select new sereis  and verify check mark is available for newly selected sereis from diffrent viewport" );	
////		viewerPage.openContentSelector(viewerPage.viewboxImage1, viewerPage.resultDescriptionFirstViewBox);
////		viewerPage.selectSeriesFromContentSelector(1, "1001");
////		viewerPage.openContentSelector(viewerPage.viewboxImage2, viewerPage.resultDescriptionSecondViewBox);
////		viewerPage.assertTrue(viewerPage.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(2, "1001"), "After selecting  a new sereis form CS verify Checkbox appears to dicom series ", "Verified Checkbox appears to new series ");
//
//	}	
//}
//
