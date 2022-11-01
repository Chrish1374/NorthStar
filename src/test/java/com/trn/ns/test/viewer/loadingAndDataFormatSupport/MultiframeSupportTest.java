package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MultiframeSupportTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private CircleAnnotation circle;
	private OutputPanel panel;
	private EllipseAnnotation ellipse;
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;
	
	// Get Patient Name
	String filePath=Configurations.TEST_PROPERTIES.get("Brain_Perfusion_Filepath");
	String brainPerfusionPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	int sliceNumber[] = {3361,3041,2401,1121,1761,4641,4321,481,5281,161,5921,4961,5601,2081,1441,4001,2721,3681,801};
	
	int scrollSlice =160;
	private ContentSelector cs;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() 
	{

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName, username,password,1);
	}

	@Test(groups = {"Chrome", "IE11", "Edge","US1452" ,"Positive", "Sanity" ,"BVT"})
	public void test01_US1452_TC7771_verifyMultiframeData() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify display of CT Enhanced Storage (multiframe) data in viewer");
				
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		
		viewerPage.assertEquals(viewerPage.getValueOfMaxPhase(1), sliceNumber.length, "Checkpoint[1/5]", "Verifying the number of phases");
		
		for(int i=0;i<sliceNumber.length;i++) {
			
			viewerPage.assertEquals(viewerPage.getImageNumberLabelValue(1), sliceNumber[i], "Checkpoint[2/5]", "verifying the number of slice");
			viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1), (i+1), "Checkpoint[3/5]", "Verifying the phase");
			viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), scrollSlice, "Checkpoint[4/5]", "Verifying the scroll position");
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint[5/5] : verifying the image post phase scroll", "TC01_"+i);
			viewerPage.pressKey(NSGenericConstants.DOT_KEY);
		}
	}
		
	@Test(groups = {"Chrome", "IE11", "Edge","US1451","US1452","Positive","BVT"})
	public void test02_US1451_TC7639_TC7640_US1452_TC7772_verifyDICOMOperationOnMultiframeData() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify DICOM operations on CT Enhanced Storage (multiframe)"
				+ "<br> Verify data gets imported successfully after fresh install");
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		preset=new ViewBoxToolPanel(driver);
				
		String c = viewerPage.getWindowCenterValueOverlayText(1);
		String w = viewerPage.getWindowWidthValueOverlayText(1);
		
		
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Validate WW/WL functionality" );
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1),c, "verifying WC", "Verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1),w, "verifying WW", "Verified");

		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));
		int currentScrollPos = viewerPage.getCurrentScrollPositionOfViewbox(1);
		
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, 100);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Validate scroll functionality" );
		viewerPage.assertNotEquals(currentScrollPos,viewerPage.getCurrentScrollPositionOfViewbox(1), "Verifying the scroll functionality", "scroll is working fine");
		
		currentScrollPos = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int sliceNo = viewerPage.getImageNumberLabelValue(1);
		
		layout=new ViewerLayout(driver);
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		int beforeZoom = preset.getZoomValue(1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 0, -10);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Validate Zoom functionality" );
		viewerPage.assertTrue(beforeZoom < preset.getZoomValue(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+preset.getZoomLevelValue(1));

		layout=new ViewerLayout(driver);
		cs = new ContentSelector(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);		
		cs.selectSeriesFromSeriesTab(2, cs.getSeriesDescriptionOverlayText(1));		
		cs.assertTrue(Integer.parseInt(cs.getAttributeValue(cs.getViewerCanvas(2), NSGenericConstants.WIDTH))>0, "Checkpoint[4/7]", "Verifying that image is loaded");
		
		viewerPage.assertEquals(viewerPage.getImageNumberLabelValue(2), sliceNo, "Checkpoint[5/7]", "verifying the number of slice");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getImageCurrentPhasePosition(2)), "Checkpoint[6/7]", "Verifying the phase");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentScrollPos, "Checkpoint[7/7]", "Verifying the scroll position");
	
		
	}
	
	@Test(groups = {"Chrome", "IE11", "Edge","US1452" ,"Positive", "Sanity" ,"BVT"})
	public void test03_US1452_TC7776_verifyDICOMFunctForMultiframeData() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify basic DICOM functionality of CT Enhanced Storage (multiframe) data in viewer.");
		
		
		panel=new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		cs=new ContentSelector(driver);
		
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 30, 50, 60);
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[1/10]", "Console error not present after drawing circle annotation.");
		panel.assertEquals(cs.getAllResults().size(),1,"Checkpoint[2/10]", "Verified that new clone created in CS after drawing circle annotation.");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.getText(panel.thumbnailList.get(0)).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Checkpoint[3/10]", "Verified finding name in Output panel");
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[4/10]", "Console error not present after dopening output panel.");

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(brainPerfusionPatientName,1,1);
		
		patientPage.clickOntheFirstStudy();
		panel.waitForViewerpageToLoad();
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/10]", "Verified that clone is loaded on viewer.");
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[6/10]", "Verified that clone is heighlighted in content selector after reload of viewer page.");
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[7/10]", "Console error not present after reload of viewer page.");
	
	
		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1,0, 0, -100,-100);
		ellipse.selectRejectfromGSPSRadialMenu();
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[10/10]", "Console error not present after performing accept reject from AR toolbar for ellipse annotation.");
		panel.openAndCloseOutputPanel(false);
		
	}
	
}





