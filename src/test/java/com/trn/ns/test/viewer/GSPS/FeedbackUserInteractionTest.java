package com.trn.ns.test.viewer.GSPS;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F79_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  -revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class FeedbackUserInteractionTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	
	private ExtentTest extentTest;
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Dummy_16_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
	} 
	
	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test01_US328_TC1935_verifySendToPACSButtonOnDifferentPage() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Feedback of user result interaction to EnvoyAI Liaison - Send to PACS button");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPage(patientName, "", 1, 1);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		//verify Send to PACS is visible on viewer page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1] & Checkpoint[1/5]", "Verify that Send to PACS button is visible on viewer page");
		viewerPage.assertTrue(viewerPage.isElementPresent(sd.feedBackbutton),"Verify that Send to PACS is visible on viewer page","Send to PACS button is present on viewer page");
	
		//navigate back to study list page
		viewerPage.browserBackWebPage();
		
		//navigate back to single study page and verify Send to PACS button is not visible
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1] &  Checkpoint TC1[2] & Checkpoint[2/5]", "Verify that Send to PACS button is not visible on single study page");
		viewerPage.assertFalse(viewerPage.isElementPresent(sd.feedBackbutton),"Verify that Send to PACS is not visible on Single Study page","Send to PACS button is not present on single study page");
	
		//navigate to viewer page
		helper.loadViewerPage(patientName, "", 1, 1);
		
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		
		//verify Send to PACS button is disabled on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify that Send to PACS button is disabled on viewer page when there is no GSPS object");
		viewerPage.assertFalse(sd.feedBackbutton.isEnabled(),"Verify that Send to PACS is disabled","Send to PACS button is disabled");   
	
	   //Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
	   ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify that Linear Measurement is drawn on Viewbox1");
	   viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		//verify Send to PACS button is enabled on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify that Send to PACS button is enabled on viewer page when there is active GSPS object");
		viewerPage.assertTrue(sd.feedBackbutton.isEnabled(),"Checkpoint TC1[3]:Verify that Send to PACS is enabled","Send to PACS button is enabled");
	}
}
