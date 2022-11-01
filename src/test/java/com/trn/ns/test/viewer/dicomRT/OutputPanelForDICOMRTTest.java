package com.trn.ns.test.viewer.dicomRT;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class OutputPanelForDICOMRTTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private ViewerSliderAndFindingMenu findingMenu;

	String dicomRTPatientNameFilepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String dicomRTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, dicomRTPatientNameFilepath);

	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String newUser = "user";


	private OutputPanel panel;
	private CircleAnnotation circle;
	private ContentSelector cs;
	private DICOMRT rt;
	private PolyLineAnnotation poly;
	private HelperClass helper;
	private ViewerLayout layout;



	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username ,password);
		patientListPage = new PatientListPage(driver);
		

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053","DR2280","BVT"})
	public void test01_US1053_TC5117_DR2280_TC9204_verifyDICOMRTInOutputPanel() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel displays the thumbnail for each Dicom-RT contour sequence. <br>"+
		"[Risk and Impact] US1742- TC8747");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
		
		cs = new ContentSelector(driver);

		cs.openAndCloseSeriesTab(true);
		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
		cs.openAndCloseSeriesTab(false);
		cs.assertFalse(cs.isElementPresent(rt.getLossyOverlay(1)), "Checkpoint[1/2]", "Verified that Lossy is not present on viewer.");
		verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[2/2]");

	
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
	public void test02_US1053_TC5140_verifyMiddleSlicePositionInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify default slice position displayed in Output Panel is the middle slice from each of the  Dicom-RT contour sequence");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);

		panel=new OutputPanel(driver) ;
		panel.enableFiltersInOutputPanel(false, false, true);	
		panel.clickOnThumbnail(2);

		rt.assertEquals(rt.getCurrentScrollPosition(1),"166","Checkpoint[1/1]","Verifying the middle slice of aorta is 166");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
	public void test03_US1053_TC5142_verifyOutputPanelForDICOMRTClone() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel displays the thumbnail from latest clone copy when user edit each Dicom-RT contour sequence and creates clone from one base series");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
		cs = new ContentSelector(driver);
		viewerPage=new ViewerPage(driver);
		panel=new OutputPanel(driver) ;	

		List<WebElement> options = rt.getLegendOptionsList(1);
		for(int i =0;i<options.size();i++)
			rt.click(options.get(i));

		int []coordinates = new int[] {-40, 50};

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(5);
		poly = new PolyLineAnnotation(driver);
		int beforeEdit=poly.getLinesOfPolyLine(1, 4).size();
		List<WebElement> handles = poly.getLinesOfPolyLine(1, 4);

		poly.editPolyLine(handles.get(2), coordinates,handles.get(9));
		poly.waitForTimePeriod(3000);
		rt.assertTrue(rt.verifyAcceptedRTSegment(2),"Checkpoint[1/5]","Verifying the contour is turned in accepted");
		rt.assertTrue(rt.verifyAcceptGSPSRadialMenu(), "Checkpoint[2/5]","Verifying the accept gsps radial menu");

		List<String> clone = cs.getAllResults();
		cs.assertEquals(clone.size(),2,"Checkpoint[3/5]","Verifying the clone is created");		
		cs.selectResultFromSeriesTab(1, clone.get(1));

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).size(), panel.getCountOfLinesInPolylineInThumbnail(1), "Checkpoint[5/5]","verifying the thumbnail after editing the clone copy");
        panel.assertTrue(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).size()<beforeEdit, "Checkpoint[]", "");
        panel.assertTrue(panel.getCountOfLinesInPolylineInThumbnail(1)<beforeEdit, "Checkpoint[]", "");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
	public void test04_US1053_TC5196_TC5197_TC5233_TC5234_TC5267_verifyGSPSWithDICOMRTInOutputPanel() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel displays the GSPS and RTStruct thumbnails"
				+ "<br> Verify Output Panel is updated when GSPS and RTStruct findings are accepted or rejected and study is reloaded"
				+ "<br> Verify Output-Panel should show the contours from the loaded clone copy in the viewer"
				+ "<br> Verify Output-Panel show the contours from the loaded clone copy in the viewer even if there are multiple clones."
				+ "<br> Verify Output Panel is shows the latest version of GSPS and RTStruct findings when there are multiple clones and all are loaded in the viewer.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		viewerPage=new ViewerPage(driver);
		panel=new OutputPanel(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -120, -120, -60,-60);	
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		List<String> acceptedFinding = findingMenu.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);	
		cs.assertEquals(cs.getText(cs.getAllResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT).get(0)),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[1/17]", "Verifying the clone generation in content selector");
		cs.openAndCloseSeriesTab(true);

		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), acceptedFinding.size(), "Checkpoint[]", "");
		panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(0)),"Checkpoint[3/3]"," verifying annotation within thumbnail for machine finding  "+(1));
		verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[3/17]");

		circle.selectFindingFromTable(legendNames.size()+1);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));		
		cs.assertEquals(cs.getAllResults().size(),2, "Checkpoint[4/17]", "Verifying no new clone is generated");

		rt.rejectSegment(3);
        rt.waitForTimePeriod(5000);
		List<String> clones = cs.getAllResults();
		cs.assertEquals(clones.size(),3, "Checkpoint[5/17]", "Verifying new clone is generated for rejecting the rt contour");
		rt.assertTrue(rt.verifyRejectedRTSegment(3),"Checkpoint[6/17]","Verifying the contour is rejecting");

		panel.assertTrue(panel.verifyRTFindingsWithGSPSInOuputPanel(false, true, false, ListUtils.union(acceptedFinding,rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR))),"Checkpoint[7/17]", "Verified rejected findings in Output panel.");
	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);

	    cs.assertEquals(cs.getAllResults().size(),clones.size(), "Checkpoint[8/17]", "Verifying new clone is generated for rejecting the rt contour");
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clones.get(1)), "Checkpoint[9/17]", "Verifying The latest RTstruct clone copy should be loaded.");

	    panel.assertTrue(panel.verifyRTFindingsWithGSPSInOuputPanel(false, true, false, ListUtils.union(acceptedFinding,rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR))), "Checkpoint[10/17]", "Verified rejected findings in Output panel after reload.");

		List<String> pendingSeg = rt.getStateSpecificSegmentNames(1,  ViewerPageConstants.PENDING_RT_FINDING_COLOR);
		verifyFindingsInOuputPanel(false, false,true, pendingSeg, "Checkpoint[11/17]");

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 120, 120, 60, 60);

		List<String> acceptedFindings = findingMenu.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		cs.assertEquals(cs.getAllResults().size(),clones.size()+1, "Checkpoint[12/17]", "Verifying new clone is generated for rejecting the rt contour");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2"), "Checkpoint[13/17]", "Verifying The latest RTstruct clone copy should be loaded.");
		circle.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint[14/17]", "Verifying the Circles are present");

		rt.rejectSegment(3);

		viewerPage.click(viewerPage.getViewPort(1));
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);		
		viewerPage.waitForTimePeriod(10000);

		panel.enableFiltersInOutputPanel(true, false, false);
		verifyFindingsInOuputPanel(true, false, false, acceptedFindings, "Checkpoint[15/17]");
		verifyFindingsInOuputPanel(false, true, false, rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR), "Checkpoint[16/17]");
		verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[16/17]");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
	public void test05_US1053_TC5235_verifyDICOMRTStateInOutputPanel() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output-Panel show the contours from the latest edited clone copy when multiple clones from one base series is available");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
        panel=new OutputPanel(driver);
		cs = new ContentSelector(driver);

		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));

		rt.rejectSegment(3);
		rt.waitForTimePeriod(10000);

		List<String> clones = cs.getAllResults();
		cs.assertEquals(clones.size(),2, "Checkpoint[1/10]", "Verifying new clone is generated for rejecting the rt contour");
		rt.assertTrue(rt.verifyRejectedRTSegment(3),"Checkpoint[2/10]","Verifying the contour is rejecting");

		cs.assertTrue(panel.verifyRTFindingsWithGSPSInOuputPanel(false, true, false, rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR)),"Checkpoint[3/10]","Verified rejected findings in Output panel.");
		cs.assertTrue(panel.verifyRTFindingsWithGSPSInOuputPanel(false, false, true, rt.getStateSpecificSegmentNames(1,  ViewerPageConstants.PENDING_RT_FINDING_COLOR)),"Checkpoint[4/10]", "Verified pending findings in Output panel.");

		helper=new HelperClass(driver);
	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);;

	    cs.assertEquals(cs.getAllResults().size(),clones.size(), "Checkpoint[5/10]", "Verifying new clone is generated for rejecting the rt contour");
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clones.get(1)), "Checkpoint[6/10]", "Verifying The latest RTstruct clone copy should be loaded.");


		for(int i=2;i<legendNames.size();i=i+2) {			
			rt.acceptSegment(i);			
		}

		cs.assertEquals(cs.getAllResults().size(),clones.size()+1, "Checkpoint[7/10]", "Verifying new clone is generated for accepting the rt contour");

		List<String> acceptedSeg = rt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR);
		List<String> rejectedSeg = rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR);
		List<String> pendingSeg = rt.getStateSpecificSegmentNames(1, ViewerPageConstants.PENDING_RT_FINDING_COLOR);

		verifyFindingsInOuputPanel(true, false, false, acceptedSeg, "Checkpoint[8/10]");
		verifyFindingsInOuputPanel(false, true, false, rejectedSeg, "Checkpoint[9/10]");
		verifyFindingsInOuputPanel(false, false,true, pendingSeg, "Checkpoint[10/10]");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1054"})
	public void test06_US1054_TC5536_verifyWarningMessageForRT() throws InterruptedException, ParseException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the error message displayed when the RT Struct series  selected from the thumbnail in the output panel is not loaded in the viewer.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
		cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);

		String seriesDescription = rt.getSeriesDescriptionOverlayText(1);
		cs.selectSeriesFromSeriesTab(1, seriesDescription);
		cs.click(cs.getViewPort(1));

		panel.enableFiltersInOutputPanel(false,false,true);		
		panel.clickOnThumbnail(1);

		// verify warning message for thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		rt.assertTrue(rt.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+seriesDescription+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified");

		 
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053","US2284","F1125","E2E"})
	public void test07_US1054_TC5532_US2284_TC9756_verifyJumpToForRT() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify selecting the RT Struct finding from the thumbnail present in the Output panel."
				+ "<br> Verify Jump to finding from Output Panel thumbnail.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		rt=helper.loadViewerPageForRTUsingSearch(dicomRTPatientName, 1, 1);
		
		cs = new ContentSelector(driver);
        panel=new OutputPanel(driver);
		panel.assertTrue(panel.verifyRTFindingsWithGSPSInOuputPanel(false, false, true, rt.getStateSpecificSegmentNames(1,  ViewerPageConstants.PENDING_RT_FINDING_COLOR)),"Checkpoint[1/5]", "Verifying the pending Findings in  output panel");

		panel.enableFiltersInOutputPanel(false, false, true);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/"+dicomRTPatientName+"_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);

		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/"+dicomRTPatientName+"_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/"+dicomRTPatientName+"_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/"+dicomRTPatientName+"_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_"+dicomRTPatientName+"_cineplayImage.png";

		boolean cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		rt.assertFalse(cpStatus, "Checkpoint[2/5] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[3/5]", "verifying the jump to icon is not displayed when cine is working");

		panel.clickOnThumbnail(1);
		panel.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),panel.getHexColorValue(panel.getContourColorThumbnail(1)),"Checkpoint[4/5]","Verifying the selected contour and segment has some color");	

		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[5/5]", "verifying that pending gsps toolbar is displayed");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050","BVT"})
	public void test08_US1050_TC5499_verifyCineFunctionalityAndJumpToIconPresence() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse actions on RT-Struct Contours in Output Panel");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
		
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);		

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test08_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);

		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test08_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/test08_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test08_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImage.png";

		boolean cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		rt.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");

		panel.mouseHover(panel.thumbnailList.get(0));		
		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test08_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test08_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test08_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test08_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImageStopped.png";

		cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		rt.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");



	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050"})
	public void test09_US1050_TC5496_verifyFindingsArePresentInThumbnailOnPlayingCine() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when cine works on GSPS findings that spans multiple slices");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);
	
		int findingsCount = rt.getFindingsCountFromFindingTable();

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);	

		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/2]", "Verifying that findings menu has same findings in output panel");
		panel.assertTrue(panel.verifyAnnotationsPresenceInThumbnail(4),"Checkpoint[2/2]","Verifying the annotations are displayed when cine is played on thumbnail");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050"})
	public void test10_US1050_TC5460_DE1855_TC7407_verifyingMultipleContoursDisplayedInThumbnail() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when multiple contours are present in a single slice in a contour sequence output panel thumbnail displays all contours that are available in a slice."
				+ "<br> Verify the contours are displayed with respect to slices during cine in output panel.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(dicomRTPatientName,1, 1);

		int findingsCount = rt.getFindingsCountFromFindingTable();

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);	

		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/2]", "Verifying that findings menu has same findings in output panel");

		for(int i = 0 ;i<90;i++) {
			List<WebElement> ann = panel.getAnnotationsFromThumnailCineSlow(2);
			panel.assertTrue(ann.size()>=1&&ann.size()<=5,"checpoint[2/2]","Verifying that multiple annotations are also displayed "+ann.size());
		}
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1350","DE1757"})
	public void test11_DE1350_TC5661_DE1757_TC7160_verifyConsoleForFilterButtonInOPForRT() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no console error received when pressing the filter button in output panel. <br>"+
				"Verify that the console errors are not displayed when user clicks the thumbnail on output panel.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(dicomRTPatientName, 1);

		panel=new OutputPanel(driver);

		circle=new CircleAnnotation(driver);
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[1/3]", "Verify No console error receive when click on output panel, filter button");
		panel.openAndCloseOutputPanel(false);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 50, 50);

		panel.enableFiltersInOutputPanel(true, false, true);
		panel.clickOnThumbnail(1);
     	panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[2/3]", "Verify No console error receive when click on thumbnail after drawing GSPS annotation");

		helper=new HelperClass(driver);
	    helper.browserBackAndReloadViewer(dicomRTPatientName,1,1);;

		//panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[3/3]", "Verify No console error receive when click on output panel, filter button after drawing GSPS annotation");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"DE1932"})
	public void test12_DE1932_TC7859_verifyConsoleErrorWhenUserNavigateToStudyPage() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that console error is not displayed when user navigates to the study page from output panel.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(dicomRTPatientName, 1);

		rt = new DICOMRT(driver);
		panel=new OutputPanel(driver);
	
		circle = new CircleAnnotation(driver);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -120, -120, -60,-60);	

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[1/2]", "Verified that circle annotation is highlighted after click on jump icon.");
		
		
		//navigate back to study page
		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[2/2]", "Verified that no console error present after navigating back to study list page.");
	
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US2284","F1125","E2E"})
	public void test13_US2284_TC9758_verifyNoCommentIconForRT() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the comment icon on Output Panel thumbnail tool bar for RT and non-dicom thumbnails.");

		// Loading the patient on viewer
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(dicomRTPatientName, 1);
		
		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();
		
		panel=new OutputPanel(driver);
	    panel.enableFiltersInOutputPanel(false, false, true);
	    
	    for(int i =1;i<=panel.thumbnailList.size();i++) {
	    	
	    	panel.assertFalse(panel.verifyCommentIconPresence(i), "Checkpoint[1/1]","verify comment icon is absent");
	    }
		
	}
	
	
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(username);
		db.deleteRTafterPerformingAnyOperation(newUser);
		db.deleteUser(newUser);

	}

	private void verifyFindingsInOuputPanel(boolean accept, boolean reject, boolean pending , List<String> findings, String checkpoint) throws InterruptedException {

		panel=new OutputPanel(driver) ;	
		poly=new PolyLineAnnotation(driver);
		panel.enableFiltersInOutputPanel(accept, reject, pending);
		panel.assertEquals(findings.size(),panel.thumbnailList.size(), checkpoint+".a", "Verifying the legend and findings sizes are same");
		panel.waitForTimePeriod(3000);
		
		for(int i =1;i<=panel.thumbnailList.size();i++) {
			panel.clickOnJumpIcon(i);
			panel.assertEquals(poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).size(), panel.getCountOfLinesInPolylineInThumbnail(i), "Checkpoint[]","Verified thumbnail for RT segment");
			panel.assertTrue(panel.isElementPresent(panel.thumbnailList.get(i-1)),checkpoint+".c","Verifying that DICOMRT thumbnail is displayed");	

		}
		panel.openAndCloseOutputPanel(false);

	}

}



