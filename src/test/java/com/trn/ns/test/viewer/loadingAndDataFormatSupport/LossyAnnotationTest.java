package com.trn.ns.test.viewer.loadingAndDataFormatSupport;
import java.util.List;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LossyAnnotationTest extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ViewerTextOverlays textOverlay;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String deoLillyFilePath=Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String deoLillyPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, deoLillyFilePath);

	String johnDoeFilepath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnDoeFilepath);

	String runOffFilepath = Configurations.TEST_PROPERTIES.get("Runoff_filepath");
	String runOffPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, runOffFilepath);
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US577","US1742","E2E","F195"})
	public void test01_US577_TC1793_US1742_TC8748_verifyLossyOverlayZoom() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Lossy image overlay when image are not the RPD - Zoom.<br>"+
		"Risk and Impact - US577");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		patientPage.waitForElementInVisibility(patientPage.loadingIconInPatient);
		patientPage.waitForElementVisibility(patientPage.patientListTable);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Loading the Patient "+liver9PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);

		patientPage.clickOntheFirstStudy();
		textOverlay=new ViewerTextOverlays(driver);
		preset=new ViewBoxToolPanel(driver);
		layout=new ViewerLayout(driver);
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verifying the zoom % is below 100" );
		viewerPage.assertTrue(preset.getZoomValue(1)<100,"verifying the zoom is below 100","verified");		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at Minimum level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at full level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at full level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "Changing the annotation level to default and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at default level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at default level","verified");

		// changing the layout to 1x1 and verifying the annotation presence/absence
		layout.selectLayout(layout.oneByOneLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6]", "on layout change to 1x1, Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7]", "on layout change to 1x1, Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at full level","verified");			

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8]", "on layout change to 1x1, changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at default level","verified");

		// changing the layout back to 2x2 
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		// inputing the zoom >100 and verifying the annotation presence/absence
		preset = new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,110);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9]", "on input the zoom value, Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at Minimum level","verified");
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10]", "on input the zoom value, Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at full level","verified");			
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at full level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11]", "on input the zoom value, Changing the annotation level to default and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at default level","verified");
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at default level","verified");

		// changing the zoom level to zoom to fit and verifying the annotation presence/absence 
		preset.chooseZoomToFit(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "on selection of zoom to fit, Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at Minimum level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "on selection of zoom to fit, Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at full level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at full level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "on selection of zoom to fit, Changing the annotation level to default and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(1)),"verifying lossy annotation presence in viewbox1 at default level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at default level","verified");


		// changing the zoom level to zoom to 100 % and verifying the annotation presence/absence 
		preset.changeZoomNumber(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9]", "on selection of zoom to 100, Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at Minimum level","verified");
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10]", "on selection of zoom to 100, Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at full level","verified");			
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at full level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11]", "on selection of zoom to 100, Changing the annotation level to default and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at default level","verified");
		viewerPage.assertNull(viewerPage.getLossyOverlay(2),"verifying lossy annotation absence in viewbox2 at default level","verified");

		//changing the zoom <100
		preset.chooseZoomToFit(1);
		// performing the sync off on viewbox1
		viewerPage.performSyncONorOFF();

		// changing the zoom level to 100 % and verifying the annotation presence/absence
		preset.changeZoomNumber(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[18]", "on selection of zoom to 100 and sync off, Changing the annotation level to minimum and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at Minimum level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at Minimum level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[19]", "on selection of zoom to 100 and sync off, Changing the annotation level to full and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at full level","verified");			
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at full level","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[20]", "on selection of zoom to 100 and sync off, Changing the annotation level to default and verifying the lossy annotation" );
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.assertNull(viewerPage.getLossyOverlay(1),"verifying lossy annotation absence in viewbox1 at default level","verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyOverlay(2)),"verifying lossy annotation presence in viewbox2 at default level","verified");



	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US577","US1742","DR2358","E2E","F195"})
	public void test02_US577_TC1794_TC1873_US1742_TC8748_DR2358_TC9294_verifyLossyOverlayScroll() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Lossy image overlay when image are not the RPD - Zoom "
				+ "</br> Lossy image overlay when image are not the RPD - Cine. <br>"+
				"Risk and Impact - US577 .<br>"+
				"Verify lossy image is not rendered on loading viewer when rendering mode is auto");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		// verifying when images are cached and lossy annotation should not be displayed
		HelperClass helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName, 1,1);
		
		preset=new ViewBoxToolPanel(driver);
		textOverlay=new ViewerTextOverlays(driver);
		
		viewerPage = new ViewerPage(driver);
	
		//wait added to complete cache 
		viewerPage.waitForTimePeriod(10000);
		List<String> logs= viewerPage.getConsoleLogs();
	
		viewerPage.assertFalse(logs.toString().contains("getLossy"), "Checkpoint[1/6]", "Verified that Lossy image is not render when rendering mode is Lossy.");
		
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(1));
		viewerPage.waitForViewerpageToLoad(1);

		int total = viewerPage.convertIntoInt(viewerPage.getImageMaxScrollPositionOverlayText(1).replace("/", "").trim());

		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));

		for(int i =0 ; i<total;){
			viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getLossyAnnotation(1)), "Checkpoint[2."+i+"/6] verifying images are not cached"+i, "Images are not cached");
			i = i+1;
			break;		


		}
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));		
		viewerPage.assertFalse(viewerPage.isElementPresent(textOverlay.lossyAnnotationViewbox1),"Checkpoint[3/6] verifying the lossy annotation","verified");

		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+runOffPatientName+"in viewer" );
		
		patientPage.clickOnPatientRow(runOffPatientName);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		//		viewerPage.changeLayout();
		preset.changeZoomNumber(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Changing the annotation level to minimum and verifying the lossy annotation" );
		viewerPage.verifyTrue(viewerPage.verifyLossyAnnotationOnInputOfImageNum(1,0, -300,0,300),"","verified");
		viewerPage.verifyTrue(viewerPage.verifyLossyAnnotationOnInputOfImageNum(2,0, 300,0, -300),"","verified");
		total = viewerPage.getMaxNumberofScrollForViewbox(1);
		

		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		for(int i =0 ; i<total; i++){
			try{

				viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getLossyAnnotation(1)), "Checkpoint[5."+i+"/6] verifying images are not cached"+i, "Images are not cached");
				break;		

			}catch(AssertionError e){
				viewerPage.assertFalse(false, "verifying images are cached"+i, "Images are cached");
			}
		}
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.waitForTimePeriod(3000);
		viewerPage.assertFalse(viewerPage.isElementPresent(textOverlay.lossyAnnotationViewbox1),"Checkpoint[6/6] verifying the lossy annotation","verified");
		
		


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US577","US1742","E2E","F195"})
	public void test03_US577_TC1876_US1742_TC8748_verifyLossyOverlayOnPDFOrJPG() throws InterruptedException 

	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Lossy image overlay when image are not the RPD - PDF and JPG. <br>"+
		"Risk and Impact - US577");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Loading the Patient "+deoLillyPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(deoLillyPatientName);

		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForPdfToRenderInViewbox(3);

		viewerPage.compareElementImage(protocolName, viewerPage.getPDFViewbox(3), "Verify lossy annotation on PDF", "TC1876_checkpoint1");
		viewerPage.compareElementImage(protocolName, viewerPage.getPDFViewbox(4), "Verify lossy annotation on PDF","TC1876_checkpoint2");

		viewerPage.doubleClickOnViewport(viewerPage.getPDFViewPort(3));
		viewerPage.waitForPdfToRenderInViewbox(3);

		viewerPage.compareElementImage(protocolName, viewerPage.getPDFViewbox(3), "Verify lossy annotation on PDF", "TC1876_checkpoint3");
		viewerPage.doubleClickOnViewport(viewerPage.getPDFViewPort(3));
		viewerPage.waitForPdfToRenderInViewbox(3);

		viewerPage.assertNull(viewerPage.getLossyOverlay(3),"verifying lossy annotation absence in viewbox3","verified");

		viewerPage.browserBackWebPage();
		patientPage.clickOntheFirstStudy();
		patientPage.waitForPatientPageToLoad();

		patientPage.clickOnPatientRow(johnDoePatientName);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad(4);

		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify lossy annotation on PDF", "TC1876_checkpoint4");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Verify lossy annotation on PDF", "TC1876_checkpoint5");
		viewerPage.compareElementImage(protocolName, viewerPage.getPDFViewbox(3), "Verify lossy annotation on PDF","TC1876_checkpoint6");

		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify lossy annotation on JPG", "TC1876_checkpoint7");
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(1));

		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(2));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Verify lossy annotation on JPG", "TC1876_checkpoint8");
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(2));


		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(3));
		viewerPage.compareElementImage(protocolName, viewerPage.getPDFViewbox(3), "Verify lossy annotation on PDF","TC1876_checkpoint9");
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(3));

		viewerPage.assertNull(viewerPage.getLossyOverlay(3),"verifying lossy annotation absence in viewbox3","verified");


	}

	

}
