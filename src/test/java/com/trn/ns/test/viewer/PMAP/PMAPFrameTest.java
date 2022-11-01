package com.trn.ns.test.viewer.PMAP;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPFrameTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private OutputPanel panel;
	private ContentSelector cs;
	private PMAP pmap;
			
	String filepath4 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath4);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();	

	}

	@Test(groups ={"Chrome","Edge","IE11","US1997","Positive","E2E", "F195"})
	public void test01_US1997_TC8878_TC8880_verifyPMAPDataLoadingAndCS() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that gray scale image is not displayed on PMAP loading."
				+ "<br> Verify that grey scale image is not displayed after loading the series from content selector.");

	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, 1);
		
		cs= new ContentSelector(driver);
		cs.closeNotification();
		selectResultWhichHasLUT();
		pmap=new PMAP(driver);
		
		cs.assertTrue(cs.isElementPresent(pmap.lutbar), "Checkpoint[1/6]", "verify LUT bar presence");
		cs.assertTrue(cs.isElementPresent(cs.getResultAppliedElement(1)), "Checkpoint[2/6]", "verify result applied is displayed when pmap result is loaded");
				
		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
		cs.scrollDownToSliceUsingKeyboard(2);
		cs.assertFalse(cs.isElementPresent(pmap.lutbar), "Checkpoint[3/6]", "verify LUT bar is not displayed when source is loaded");
		cs.assertFalse(cs.isElementPresent(cs.getResultAppliedElement(1)), "Checkpoint[4/6]", "verify result applied is not displayed when source is displayed");
		
		selectResultWhichHasLUT();
		
		cs.assertTrue(cs.isElementPresent(pmap.lutbar), "Checkpoint[5/6]", "verify LUT bar presence upon selecting the result from CS");
		cs.assertTrue(cs.isElementPresent(cs.getResultAppliedElement(1)), "Checkpoint[6/6]", "verify result applied is displayed when pmap result is loaded from CS");

	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1997","Positive","E2E", "F195"})
	public void test02_US1997_TC8878_verifyCacheMessageOnScroll() throws SQLException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that caching message is displayed on frame navigations.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(covidPatientName, 1);

		viewerpage.closeNotification();
		
		viewerpage.clearConsoleLogs();
		int currentSlice = viewerpage.getCurrentScrollPositionOfViewbox(1);
		
		
		db = new DatabaseMethods(driver);
		List<Integer> instances = db.getInstanceLevelID(viewerpage.getAttributeValue(viewerpage.getSeriesDescriptionOverlay(1),NSGenericConstants.TEXT_CONTENT).trim());
		

		String validString = "RPD/lossy pixel data was found image = "+instances.get(currentSlice-1);
		
		viewerpage.scrollDownToSliceUsingKeyboard(4);
		viewerpage.waitForTimePeriod(4000);		
		
		List<String> logs = viewerpage.getConsoleLogs();
		boolean flag =logs.stream().anyMatch(s -> s.contains(validString));
		 viewerpage.assertTrue(flag,"Checkpoint[1/1]","verify the RPD caching is done");
		
	
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1997","Positive","F195"})
	public void test03_US1997_TC8878_verifyOutputPanelAndCine() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PMAP patient is displayed properly on viewer as well as in output panel");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, 1);
		viewerpage.closeNotification();
		
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		panel.closeNotification();
		
		String seriesInfo = panel.getSeriesDescriptionOverlayText(1);
		System.out.println(seriesInfo);
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		cs.openAndCloseSeriesTab(true);
		
		panel.enableFiltersInOutputPanel(false, false, true);		
		panel.assertTrue(panel.verifyStudyDescAndFindingCount(seriesInfo, results.size()), "Checkpoint[1/9]", "Verifying the series name with finding name");
		
//		panel.assertTrue(panel.getText(panel.resultName.get(0)).contains(results.get(0)), "Checkpoint[2/9]", "verifying the result description OP");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),results.get(0)+"."+ViewerPageConstants.DICOM_SC,"Checkpoint[3/9]","verifying the finding title in OP");
//		panel.assertTrue(panel.getText(panel.createdByUserList.get(0)).contains(machineName),"Checkpoint[4/9]","verifying the created by list");
//		panel.assertEquals(panel.getText(panel.findingsEditorName.get(0)),machineName,"Checkpoint[5/9]","verifying the editor name is machine name in OP");
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test03_cineplayImage.png");
		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test03_cineplayImage.png");	
		
		String expectedImagePath = newImagePath+"/goldImages/test03_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test03_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test03_cineplayImage.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[6/9] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[7/9]", "verifying the jump to icon is not displayed when cine is working");

		panel.mouseHover(panel.thumbnailList.get(0));		
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test03_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test03_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test03_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test03_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test03_cineplayImageStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[8/9] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[9/9]", "verifying the jump to icon is displayed when cine is stopped");

		
	}
		
	private void selectResultWhichHasLUT() throws InterruptedException, AWTException {
		
		cs= new ContentSelector(driver);
		pmap=new PMAP(driver);
		List<String> results = cs.getAllResults();
		
		for(int i =0;i<results.size();i++) {
			
			cs.selectResultFromSeriesTab(1, results.get(0),(i+1));
			if(cs.isElementPresent(pmap.lutbar))
				break;
			
		}
	}
	
}
