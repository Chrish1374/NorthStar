package com.trn.ns.test.viewer.outputPanel;

import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CopyFindingsInOutputPanelTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;


	// Get Patient Name
	String AH4_FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_FilePath);

	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	String chestCT1p25mm_filepath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, chestCT1p25mm_filepath);

	private OutputPanel panel;


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientListPage = new PatientListPage(driver);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1281", "Positive","BVT"})
	public void test01_US1281_TC6639_verifyCopyFindingsUsingWindowsShortcut() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the selection of the findings on the Output Panel using the windows shortcut.");

		// Loading the patient on viewer
		
		HelperClass helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameDICOMRT, 1);
		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the Select All icon is not selected and its title");
		verifyDeSelectedSelectAllIcon(1, 20);
		checkThumbnailWhenNotSelected(2, 20,0,panel.thumbnailList.size());
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Pressing control+A");
		panel.pressControlA();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the Select All icon is selected and its respective title is changed");
		verifySelectedSelectAllIcon(3, 20);
		checkThumbnailWhenSelected(4, 20,0, panel.thumbnailList.size());
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accept and rejecting the findings and verifying the selectAll feature");
		rt.acceptSegment(1);
		rt.rejectSegment(2);

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.checkSelectAllOption();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that all the findings all selected");
		verifySelectedSelectAllIcon(14, 20);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on accepted button");
		panel.enableFiltersInOutputPanel(true, false, true);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that select all icon is changed from selected to non selected");
		verifyDeSelectedSelectAllIcon(15, 20);
			
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that other findings are selected");
		checkThumbnailWhenSelected(17, 20,1, panel.thumbnailList.size());
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on rejected button");
		panel.enableFiltersInOutputPanel(true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that other findings are not selected");
		checkThumbnailWhenNotSelected(18, 20, 0, 1);
		checkThumbnailWhenNotSelected(19, 20, 1, 2);		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that other findings are selected");
		checkThumbnailWhenSelected(20, 20, 2, panel.thumbnailList.size());
}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1281", "Positive"})
	public void test02_US1281_TC6640_verifyCopyFindingsForSR() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'Select All' and 'Copy' icons and its functionality in the output panel.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(ChestCT1p25mm);

		patientListPage.clickOntheFirstStudy();
		
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(2);
		panel.closeWarningNotification();
		
		panel.enableFiltersInOutputPanel(false, false, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the Select All icon is not selected and its title");
		verifyDeSelectedSelectAllIcon(1, 15);
		checkThumbnailWhenNotSelected(2, 15,0,panel.thumbnailList.size());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting all the findings by cntrl +a and verifying the findings and icons - SR data");
		panel.pressControlA();		
		
		verifySelectedSelectAllIcon(3, 15);
		checkThumbnailWhenSelected(4, 15,0, panel.thumbnailList.size());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking outside of outpu panel and verifying the findings and icons - SR data");
		panel.click(panel.studySummary);

		verifySelectedSelectAllIcon(5, 15);
		checkThumbnailWhenSelected(6, 15,0, panel.thumbnailList.size());

		panel.uncheckSelectAllOption();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting all findings and clicking on expand and collapse icon - SR data");
		panel.pressControlA();	
		verifySelectedSelectAllIcon(7, 15);
		checkThumbnailWhenSelected(8, 15,0, panel.thumbnailList.size());
					
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on the thumbnail and verifying the selectAll icon - SR data");
		panel.clickOnThumbnailNotAtCenter(1);
		panel.mouseHover(panel.studySummary);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on the thumbnail deselects the finding");
		verifySelectedSelectAllIcon(10, 15);

		
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the select all settings are not saved when user open/close the output panel - SR data");
		panel.click(panel.getViewPort(2));
		panel.enableFiltersInOutputPanel(false, false, true);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting all the findings - SR data");
		panel.checkSelectAllOption();
		panel.openAndCloseOutputPanel(false);

		panel.click(panel.getViewPort(2));

		panel.enableFiltersInOutputPanel(false, false, true);	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the findings - SR data");
		verifyDeSelectedSelectAllIcon(14, 15);
		checkThumbnailWhenNotSelected(15, 15,0,panel.thumbnailList.size());
		panel.openAndCloseOutputPanel(false);

	}
	
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}

	private void verifyDeSelectedSelectAllIcon(int checkpoint, int finalCheckPoint) {
		
		panel.assertTrue(panel.isElementPresent(panel.deSelectedAllIcon),"Checkpoint["+checkpoint+".1/"+finalCheckPoint+"]","Verifying the select all icon is displayed");
		panel.assertTrue(panel.getAttributeValue(panel.deSelectedAllIcon,NSGenericConstants.CLASS_ATTR).contains(ViewerPageConstants.SELECT_ALL_ICON_DESELECTED), "Checkpoint["+checkpoint+".2/"+finalCheckPoint+"]", "Verifying select all icon is deselected");
		panel.assertEquals(panel.getCssValue(panel.deSelectedAllIcon,NSGenericConstants.STROKE),ThemeConstants.EUREKA_CHECKBOX_BORDER,"Checkpoint["+checkpoint+".3/"+finalCheckPoint+"]","verifying the select All is de selected by checking its background");
		panel.assertFalse(panel.isElementPresent(panel.selectAllIcon),"Checkpoint["+checkpoint+".4/"+finalCheckPoint+"]","Verifying the select all icon is not displayed");
		
		
	}
	
	private void checkThumbnailWhenNotSelected(int checkpoint, int finalCheckPoint, int startIndex, int endIndex) {
		
		for(int i=startIndex;i<endIndex;i++) {
			panel.assertEquals(panel.getAttributeValue(panel.getThumbnailCheckbox(i+1),NSGenericConstants.CLASS_ATTR),ViewerPageConstants.SELECT_ALL_ICON_DESELECTED,"Checkpoint["+checkpoint+"."+i+"/"+finalCheckPoint+"]","Verifying the findings background when not selected as black");
			panel.assertEquals(panel.getCssValue(panel.getThumbnailCheckbox(i+1),NSGenericConstants.STROKE),ThemeConstants.EUREKA_CHECKBOX_BORDER,"Checkpoint["+checkpoint+".3/"+finalCheckPoint+"]","verifying the select All is de selected by checking its background");
			
		}
	}

	private void verifySelectedSelectAllIcon(int checkpoint, int finalCheckPoint) {
		
		panel.assertTrue(panel.isElementPresent(panel.selectAllIcon),"Checkpoint["+checkpoint+".1/"+finalCheckPoint+"]","Verifying the select all icon is displayed");
		panel.assertTrue(panel.getAttributeValue(panel.selectAllIcon,NSGenericConstants.CLASS_ATTR).contains(ViewerPageConstants.SELECT_ALL_ICON_SELECTED), "Checkpoint["+checkpoint+".2/"+finalCheckPoint+"]", "Verifying select all icon is deselected");
		panel.assertEquals(panel.getCssValue(panel.selectAllIcon,NSGenericConstants.FILL),ThemeConstants.EUREKA_BUTTON_BORDER_COLOR,"Checkpoint["+checkpoint+".3/"+finalCheckPoint+"]","verifying the select All is de selected by checking its background");
		panel.assertFalse(panel.isElementPresent(panel.deSelectedAllIcon),"Checkpoint["+checkpoint+".4/"+finalCheckPoint+"]","Verifying the select all icon is not displayed");

		
	
	}
	
	private void checkThumbnailWhenSelected(int checkpoint, int finalCheckPoint, int startIndex, int endIndex) {
		
		for(int i=startIndex;i<endIndex;i++) {
			panel.assertEquals(panel.getAttributeValue(panel.getThumbnailCheckbox(i+1),NSGenericConstants.CLASS_ATTR),ViewerPageConstants.SELECT_ALL_ICON_SELECTED,"Checkpoint["+checkpoint+"."+i+"/"+finalCheckPoint+"]","Verifying the findings background when not selected as grey");
			panel.assertEquals(panel.getCssValue(panel.getThumbnailCheckbox(i+1),NSGenericConstants.FILL),ThemeConstants.EUREKA_BUTTON_BORDER_COLOR,"Checkpoint["+checkpoint+".3/"+finalCheckPoint+"]","verifying the select All is de selected by checking its background");
			
		}
	}

}




