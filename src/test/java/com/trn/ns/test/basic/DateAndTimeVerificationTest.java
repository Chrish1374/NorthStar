package com.trn.ns.test.basic;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.text.DateFormat;
import java.util.List;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DateAndTimeVerificationTest extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	
	private String MRfilePath = TEST_PROPERTIES.get("Breast_MR_2_filepath");
	private String CTfilePath = TEST_PROPERTIES.get("Liver_9_filepath");
	private String Subject60filePath = TEST_PROPERTIES.get("Subject_60_filepath");
	String MRPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, MRfilePath);
	String CTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, CTfilePath);
	String Subject60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Subject60filePath);

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

	}

	// TC893 : Verify patient DOB on patient page, single study list page and viewer page.
	@Test(groups ={"firefox", "Chrome", "IE11", "Edge"})
	public void test01_DE93_TC893_verifyDOBFormat() throws InterruptedException{

		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify patient DOB on patient page, single study list page and viewer page");
		DateFormat dateFormatDOB = DateFormat.getDateInstance(DateFormat.MEDIUM);

		patientPage = new PatientListPage(driver);
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(3),PatientPageConstants.CHECK);
		/*
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying DOB format on Patient list screen" );
		List<String> values = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(3));
		for(int i=0;i<values.size();i++)
			patientPage.assertTrue(patientPage.verifyDateFormat(dateFormatDOB,values.get(i)), "Verify DOB column data format on Patient list screen should be in 'Month Day, Year'", "DOB is in 'Month Day, Year' format");
*/
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+MRPatientName+"in viewer" );
		patientPage.clickOnPatientRow(MRPatientName);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying DOB format on Single Patient list screen" );
		
		patientPage.assertTrue(patientPage.verifyDateFormat(dateFormatDOB,patientPage.getText(patientPage.dateOfBirthHeaderInPatientInfo)),"Verify DOB column data format on Single patient list screen should be in 'Month Day, Year'","DOB is in 'Month Day, Year' format");
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		ViewerTextOverlays overlays = new  ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying DOB format on viewer screen" );
		viewerpage.assertTrue(viewerpage.verifyDateFormat(dateFormatDOB,viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim()),"Verify DOB column data format on viewer screen should be in 'Month Day, Year'","DOB is in 'Month Day, Year' format");


	}

	// TC894 : Verify patient Study date and time on Single study list page and Viewer page.
	//Not work in Chrome and FF because of DE299
	@Test(groups ={"Chrome", "IE11", "Edge"})
	public void test02_DE93_TC894_verifyStudyDateAndTimeFormat() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify patient Study date and time on Single study list page and Viewer page");
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+MRPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(MRPatientName);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying Study Date and Time format on Single patient list screen" );
		for(String value : patientPage.getColumnValue(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(5)))
			patientPage.assertTrue(patientPage.verifyDateFormat(formatter, value),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		ViewerTextOverlays overlays = new  ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying Study Date and Time format on viewer screen" );
		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");

		
	}


	// TC896 : Verify Acquisition date and time on viewer page.
	//Not work in Chrome and FF because of DE299
	@Test(groups ={"Chrome", "IE11", "Edge"})
	public void test04_DE93_TC896_verifyAcquisitionDateAndTimeFormat() throws InterruptedException{
		
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Acquisition date and time on viewer page");
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+CTPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(CTPatientName);

		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		
		ViewerTextOverlays overlays = new  ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		overlays.waitForFullOverlayDisplay(1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Aquisition Date and Time format on viewer screen" );
		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getImageAcquisitionDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");

		
	}

	

	
}
