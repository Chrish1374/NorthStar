package com.trn.ns.test.viewer.GSPS;


import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ReRunIconTest extends TestBase {

	private ExtentTest extentTest;
	private DatabaseMethodsADB db;
	private DICOMRT drt;
	String flag = "false";

	// Get Patient Name

	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
	String machineName = DataReader.getPatientDetails(NSDBDatabaseConstants.MACHINE_NAME, TCGA_filepath);

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	
	@Test(groups = { "Chrome", "IE11", "Edge", "US1362", "Positive" })
	public void test05_US1362_TC7071_VerifyReRunIcon()
			throws Throwable {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that ReRun icon is changed as per the new Icon.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientNameTCGA + "in viewer");
		db = new DatabaseMethodsADB(driver);
		db.SetOrRemoveType2InMachineTable(true, machineName);
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameTCGA,username, password,1);
	
		drt= new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		db= new DatabaseMethodsADB(driver);

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(2);

		//clicking on accept button from A/R bar
		drt.selectAcceptfromGSPSRadialMenu();
		drt.mouseHover(drt.getAcceptRejectToolBar(1));
		drt.waitForElementVisibility(drt.reRunIconContainer);

		//Hovering over Rerun Icon Container and Verifying the Sync Icon is present.

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verifying that new Sync icon is present in place of rerun icon.");
		drt.assertTrue(drt.isElementPresent(drt.reRunIconContainer), "Verifying that rerun icon container is present or not", " Rerun icon container is present");
		drt.mouseHover(drt.reRunIcon);
		drt.assertEquals(drt.getAttributeValue(drt.reRunIcon, NSGenericConstants.TITLE), "AI sync", "Verifying that rerun icon is present as new Sync icon", "Icon is present.");


	}
	
	
	
	@AfterMethod(alwaysRun=true)
	public void deleteDBValue() throws SQLException  {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.SetOrRemoveType2InMachineTable(false, machineName);
		db.deleteCopyFilterPreference();

		

	}
}
