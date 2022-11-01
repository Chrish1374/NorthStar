package com.trn.ns.test.viewer.PMAP;

import java.io.IOException;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.web.page.WebActions;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPSupportWithWebGLTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private HelperClass helper;
	private PMAP pmap;

	String filePath1 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBLJohnDoe_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 =Configurations.TEST_PROPERTIES.get("S2008-3CTP_Filepath");
	String s2008PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	
	String filepath4 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath4);

	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private WebDriver myDriver;

	@BeforeClass(alwaysRun=true)
	public void beforeClass() throws SQLException, IOException, InterruptedException{

		Thread.sleep(30000);
		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.MAXIMUM_CACHE_SIZE_LIMIT,NSDBDatabaseConstants.MAXIMUM_CACHE_SIZE_100);
		db.resetIISPostDBChanges();

	}

	@BeforeMethod
	public void beforeMethod() {
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();	
	}

	@Test(groups ={"Chrome","Edge","IE11","US1311","US1621","Positive","F631","F549","E2E"})
	public void test01_US1311_TC7630_TC7633_US1621_TC8646_verifyPMAPDataWhenWebGLIsEnabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP  is applied over lossy images when WebGI is enabled."
				+ "<br> Verify rendering of DICOM and Non-DICOM images when WebGL is enabled. <br>"+
				"Verify  that PMAP with original LUT is displayed on Viewer with or without WebGl.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
		
		viewerpage.playOrStopCineUsingKeyboardCKey();		
		pmap=new PMAP(driver);
		
		for(int i =0 ;i<4;i++) {
			viewerpage.mouseHover(viewerpage.getViewPort(1));
			viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getLossyOverlay(1)),"Checkpoint[1/6]","verifying lossy annotation presence in viewbox1 on pmap data");
			viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[2/6]", "Verifying the gradient bar is displayed");
		}
		viewerpage.playOrStopCineUsingKeyboardCKey();
		helper.browserBackAndReloadViewer(IBLJohnDoe_Patient, 1, 4);
		
		viewerpage.assertTrue(viewerpage.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[3/6]", "Verifying the jpeg is displayed in first viewbox");
		viewerpage.assertTrue(viewerpage.verifyNonDicomImageLoadedInViewer(2), "Checkpoint[4/6]", "Verifying the jpeg is displayed in second viewbox");
		viewerpage.assertTrue(viewerpage.verifyPDFLoadedInViewer(3), "Checkpoint[5/6]", "Verifying the pdf is displayed in third viewbox");
		viewerpage.assertTrue(viewerpage.verifyDicomImageLoadedInViewer(4), "Checkpoint[6/6]", "Verifying the dicom is displayed in fourth viewbox");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1311","US1621","Negative","F631"})
	public void test02_US1311_TC7631_TC7632_US1621_TC8646_verifyPMAPDataWhenWebGLIsDisabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP  is applied over lossy images when WebGI is disabled."
				+ "<br> Verify rendering of DICOM and Non-DICOM images when WebGL is disabled. <br>"+
				"Verify  that PMAP with original LUT is displayed on Viewer with or without WebGl.");
		
		myDriver = WebActions.openNewChromeInstanceWithDisabledWebGL();
			
		
		loginPage = new LoginPage(myDriver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		String parentwindow = loginPage.getCurrentWindowID();		
		
		patientPage = new PatientListPage(myDriver);
		patientPage.switchToWindow(parentwindow);

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
	
		
		pmap=new PMAP(myDriver);
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));	
		
		for(int i =0 ;i<4;i++) {
			viewerpage.mouseHover(viewerpage.getViewPort(1));
			viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getLossyOverlay(1)),"Checkpoint[1/6]","verifying lossy annotation presence in viewbox1 on pmap data");
			viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[2/6]", "Verifying the gradient bar is displayed");
		}
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));	
		
		viewerpage.playOrStopCineUsingKeyboardCKey();
		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(IBLJohnDoe_Patient, 1, 4);
		
		viewerpage.assertTrue(viewerpage.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[3/6]", "Verifying the jpeg is displayed in first viewbox");
		viewerpage.assertTrue(viewerpage.verifyNonDicomImageLoadedInViewer(2), "Checkpoint[4/6]", "Verifying the jpeg is displayed in second viewbox");
		viewerpage.assertTrue(viewerpage.verifyPDFLoadedInViewer(3), "Checkpoint[5/6]", "Verifying the pdf is displayed in third viewbox");
		viewerpage.assertTrue(viewerpage.verifyDicomImageLoadedInViewer(4), "Checkpoint[6/6]", "Verifying the dicom is displayed in fourth viewbox");

		
		
		WebActions.closeChromeBrowser(myDriver);
			
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1954","Positive","F141"})
	public void test03_DE1954_TC7997_verifyPmapDataOnChangeOfWWWLWebGLEnabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the imported PMAP data displays the PMAP overlay and the appropriate backgroud.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(s2008PatientName,1);
		viewerpage.closeNotification();
	
		pmap=new PMAP(myDriver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+s2008PatientName+" in viewer" );
		String originalmiddleValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1));
		String originalmaxValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0));
		String originalminValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2));

		int middleValue = viewerpage.convertIntoInt(originalmiddleValue);
		float maxValue = viewerpage.convertIntoFloat(originalmaxValue);
		float minValue = viewerpage.convertIntoFloat(originalminValue);

		viewerpage.assertEquals(middleValue, Math.round((maxValue+minValue)/2), "Checkpoint[3/16]", "Verifying that middle range is round of (min + max)/2");

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		int j=1;
		for(int i=0;i<30;i=i+5) {
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 30,0, -(5+i));			


			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))>maxValue, "Checkpoint[4/16]","Verifying the maximum value has changed on LUT bar");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))>middleValue, "Checkpoint[5/16]","Verifying the middle value has changed on LUT bar");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))>minValue, "Checkpoint[6/16]","Verifying the minimum value has changed on LUT bar");

			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[7.1/16]", "Verifying that window width is not changed after performing the WWWL on PMAP");
			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[7.2/16]", "Verifying that window center is not changed after performing the WWWL on PMAP");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[8.1/16]", "TC27_"+j);

			j++;
		}
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "Checkpoint[8.2/16]", "TC27_"+j);
		j++;

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		for(int i=5;i<30;i=i+10) {
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0,-40,0, i);

			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))< maxValue, "Checkpoint[9/16]","Verifying that maximum value on bar has reduced when drag perfromed from top to bottom");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))<middleValue, "Checkpoint[10/16]","Verifying that middle value on bar has reduced when drag perfromed from top to bottom");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))<minValue, "Checkpoint[11/16]","Verifying that minimum value on bar has reduced when drag perfromed from top to bottom");

			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[12.1/16]", "Verifying no change on WWWL parameters are not changed");
			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[12.2/16]", "Verifying no change on WWWL parameters are not changed");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[13/16]", "TC27_"+j);
			j++;

		}

		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "Checkpoint[13.2/16]", "TC27_"+j);
		j++;

		viewerpage.click(pmap.getGradientBar(1));
		viewerpage.click(pmap.resetButton);

		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)),originalmaxValue, "Checkpoint[14/16]","Verifying that maximum values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)),originalmiddleValue, "Checkpoint[15/16]","Verifying that middle values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)),originalminValue, "Checkpoint[16/16]","Verifying that minimum values has reset on click of reset on LUT bar");



	}

	@Test(groups ={"Chrome","Edge","IE11","DE1954","Negative","F141"})
	public void test04_DE1954_TC7997_verifyPmapDataOnChangeOfWWWLWebGLDisabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the imported PMAP data displays the PMAP overlay and the appropriate backgroud.");

		myDriver = WebActions.openNewChromeInstanceWithDisabledWebGL();

		loginPage = new LoginPage(myDriver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

//		String parentwindow = loginPage.getCurrentWindowID();	

		patientPage = new PatientListPage(myDriver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(s2008PatientName,1);
		viewerpage.closeNotification();
				
		viewerpage.waitForElementsVisibility(pmap.lutbar);
        pmap=new PMAP(myDriver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+s2008PatientName+" in viewer" );
		String originalmiddleValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1));
		String originalmaxValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0));
		String originalminValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2));

		int middleValue = viewerpage.convertIntoInt(originalmiddleValue);
		float maxValue = viewerpage.convertIntoFloat(originalmaxValue);
		float minValue = viewerpage.convertIntoFloat(originalminValue);

		viewerpage.assertEquals(middleValue, Math.round((maxValue+minValue)/2), "Checkpoint[3/16]", "Verifying that middle range is round of (min + max)/2");

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		int j=1;
		for(int i=0;i<30;i=i+5) {
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 30,0, -(5+i));			

			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))>maxValue, "Checkpoint[4/16]","Verifying the maximum value has changed on LUT bar");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))>middleValue, "Checkpoint[5/16]","Verifying the middle value has changed on LUT bar");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))>minValue, "Checkpoint[6/16]","Verifying the minimum value has changed on LUT bar");

			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[7.1/16]", "Verifying that window width is not changed after performing the WWWL on PMAP");
			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[7.2/16]", "Verifying that window center is not changed after performing the WWWL on PMAP");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[8.1/16]", "TC27_"+j);

			j++;
		}
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "Checkpoint[8.2/16]", "TC27_"+j);
		j++;

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		for(int i=5;i<30;i=i+10) {
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0,-40,0, i);

			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))< maxValue, "Checkpoint[9/16]","Verifying that maximum value on bar has reduced when drag perfromed from top to bottom");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))<middleValue, "Checkpoint[10/16]","Verifying that middle value on bar has reduced when drag perfromed from top to bottom");
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))<minValue, "Checkpoint[11/16]","Verifying that minimum value on bar has reduced when drag perfromed from top to bottom");

			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[12.1/16]", "Verifying no change on WWWL parameters are not changed");
			viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[12.2/16]", "Verifying no change on WWWL parameters are not changed");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[13.1/16]", "TC27_"+j);
			j++;

		}

		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "Checkpoint[13.2/16]", "TC27_"+j);
		j++;

		viewerpage.click(pmap.getGradientBar(1));
		viewerpage.click(pmap.resetButton);

		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)),originalmaxValue, "Checkpoint[14/16]","Verifying that maximum values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)),originalmiddleValue, "Checkpoint[15/16]","Verifying that middle values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)),originalminValue, "Checkpoint[16/16]","Verifying that minimum values has reset on click of reset on LUT bar");

		WebActions.closeChromeBrowser(myDriver);

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1620","Positive","F631","E2E"})
	public void test05_US1620_TC8390_US1621_TC8646_verifyPmapDataDisplayWhenWebGLEnabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PMAP color overlay is similar with WebGL and without WebGL. <br>"+
		"Verify  that PMAP with original LUT is displayed on Viewer with or without WebGl.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(covidPatientName,1);
		viewerpage.closeNotification();
	
		pmap=new PMAP(myDriver);
		viewerpage.waitForViewerpageToLoad();				
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verified display of PMAP data.", "TC31_01_WithWebGL_Covid003");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		helper.browserBackAndReloadViewer(pmapPatientID, 1, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verified display of PMAP data when WebGL is enabled for "+pmapPatientID );
		viewerpage.closeNotification();
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verified display of PMAP data.", "TC31_02_WithWebGL_PMAP");


	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1620","US1621","Negative","F631"})
	public void test06_US1620_TC8390_US1621_TC8646_verifyPmapDataDisplayWhenWebGLDisabled() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the imported PMAP data displays the PMAP overlay and the appropriate backgroud.<br>"+
		"Verify  that PMAP with original LUT is displayed on Viewer with or without WebGl.");

		myDriver = WebActions.openNewChromeInstanceWithDisabledWebGL();

		loginPage = new LoginPage(myDriver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		
		patientPage = new PatientListPage(myDriver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(covidPatientName,1);
		viewerpage.closeNotification();
	
		pmap=new PMAP(myDriver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verified display of PMAP data", "TC32_01_WithoutWebGL_Covid003");

	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		helper.browserBackAndReloadViewer(pmapPatientID, 1, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verified display of PMAP data when WebGL is disable for "+pmapPatientID );
		viewerpage.closeNotification();
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verified display of PMAP data.", "TC32_02_WithoutWebGL_PMAP");

		

	}


	@AfterClass(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.MAXIMUM_CACHE_SIZE_LIMIT,NSDBDatabaseConstants.MAXIMUM_CACHE_SIZE_1_GB);
		db.resetIISPostDBChanges();
		try {
			if(!myDriver.equals(null))
				myDriver.quit();
		}
		catch(NullPointerException e) {}
	}	

}
