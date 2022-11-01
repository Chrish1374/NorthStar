package com.trn.ns.test.viewer.outputPanel;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.text.ParseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CineInOutputPanelTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private ViewerSliderAndFindingMenu findingMenu;
	
		
	// Get Patient Name
	
	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);
	
	private OutputPanel panel;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050","BVT","US2284","F1125","E2E"})
	public void test01_US1050_TC5498_US2284_TC9755_verifyCineFunctionalityAndJumpToIconPresence() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse actions on GSPS findings in Output Panel"
				+ "<br> Verify Cine on thumbnail displayed in Output Panel.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientUsingID(LIDCPatientID);
		
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		panel = new OutputPanel(driver);
		
		panel.enableFiltersInOutputPanel(false, false, true);		
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.scrollIntoView(panel.thumbnailList.get(1));
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(2), newImagePath+"/goldImages/test01_cineplayImage_expected.png");
		boolean jumpToIconPresence = panel.playCineOnThumbnail(3);
		
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(2), newImagePath+"/goldImages/test01_cineplayImage_actual.png");		
		String expectedImagePath = newImagePath+"/goldImages/test01_cineplayImage_expected.png";
		String actualImagePath = newImagePath+"/goldImages/test01_cineplayImage_actual.png";
		String diffImagePath = newImagePath+"/goldImages/test01_cineplayImage_diff.png";

		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");
		
		panel.stopCineOnThumbnail(3);		
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(2), newImagePath+"/goldImages/test01_cineplay_stopped_expected.png");
		
		panel.waitForTimePeriod(2000);
		
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(2), newImagePath+"/goldImages/test01_cineplay_stopped_actual.png");		
		expectedImagePath = newImagePath+"/goldImages/test01_cineplay_stopped_expected.png";
		actualImagePath = newImagePath+"/goldImages/test01_cineplay_stopped_actual.png";
		diffImagePath = newImagePath+"/goldImages/test01_cineplay_stopped_diff.png";

		cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(2)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");
		

		
	}
		
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050"})
	public void test02_US1050_TC5492_verifyFindingsArePresentInThumbnailOnPlayingCine() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when cine works on GSPS findings that spans multiple slices");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientUsingID(LIDCPatientID);

		patientListPage.clickOntheFirstStudy();

		findingMenu = new ViewerSliderAndFindingMenu(driver);
		findingMenu.waitForViewerpageToLoad(1);
		
		int findingsCount = findingMenu.getFindingsCountFromFindingTable();
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);	
		
		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/2]", "Output panel findings are equals to findings present in finding menu");
			
		panel.assertTrue(panel.verifyAnnotationsPresenceInThumbnail(3),"Checkpoint[2/2]","Verifying the annotations are displayed when cine is played on thumbnail");
		
		

		
	}
	
}