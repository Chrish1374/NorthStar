//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.collections.ListUtils;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.AISyncPreviewWindow;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DICOMRT;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PolyLineAnnotation;
//import com.trn.ns.page.factory.RegisterUserPage;
//import com.trn.ns.page.factory.ViewerLayout;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class OutputPanelForDICOMRTTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private ViewerPage viewerPage;
//	private ViewerSliderAndFindingMenu findingMenu;
//
//	String dicomRTPatientNameFilepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
//	String dicomRTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, dicomRTPatientNameFilepath);
//
//	String rendoEntPatientNameFilepath = Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
//	String rendoEntPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, rendoEntPatientNameFilepath);
//
//	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
//	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
//	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String newUser = "user";
//
//
//	private OutputPanel panel;
//	private CircleAnnotation circle;
//	private ContentSelector cs;
//	private DICOMRT rt;
//	private PolyLineAnnotation poly;
//	private RegisterUserPage register;
//	private HelperClass helper;
//	private ViewerLayout layout;
//
//
//
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException {
//
//
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username ,password);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053","DR2280","BVT"})
//	public void test01_US1053_TC5117_DR2280_TC9204_verifyDICOMRTInOutputPanel() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Output Panel displays the thumbnail for each Dicom-RT contour sequence. <br>"+
//		"[Risk and Impact] US1742- TC8747");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		cs = new ContentSelector(driver);
//
//		cs.openAndCloseSeriesTab(true);
//		DateFormat  originalFormat = new SimpleDateFormat(ViewerPageConstants.CONTENTSELECTOR_DATE_FORMAT);
//		Date date = originalFormat.parse(cs.getText(cs.allSeriesCreationDateFromSeriesTab.get(0)));
//
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		String formattedDate = targetFormat.format(date);
//
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
//		cs.openAndCloseSeriesTab(false);
//		cs.assertFalse(cs.isElementPresent(rt.getLossyOverlay(1)), "Checkpoint[1/2]", "Verified that Lossy is not present on viewer.");
//		verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[2/2]", formattedDate, true);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
//	public void test02_US1053_TC5140_verifyMiddleSlicePositionInOutputPanel() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify default slice position displayed in Output Panel is the middle slice from each of the  Dicom-RT contour sequence");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		panel=new OutputPanel(driver) ;
//		panel.enableFiltersInOutputPanel(false, false, true);	
//		panel.clickOnThumbnail(2);
//
//		rt.assertEquals(rt.getCurrentScrollPosition(1),"166","Checkpoint[1/1]","Verifying the middle slice of aorta is 166");
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
//	public void test03_US1053_TC5142_verifyOutputPanelForDICOMRTClone() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Output Panel displays the thumbnail from latest clone copy when user edit each Dicom-RT contour sequence and creates clone from one base series");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		rt = new DICOMRT(driver);
//		cs = new ContentSelector(driver);
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
//
//		viewerPage.inputImageNumber(166, 1);
//
//		List<WebElement> options = rt.getLegendOptionsList(1);
//		for(int i =0;i<options.size();i++)
//			rt.click(options.get(i));
//
//		rt.click(options.get(1));
//		rt.waitForTimePeriod(3000);
//
//		int []coordinates = new int[] {-40, 50};
//
//		poly = new PolyLineAnnotation(driver);
//		List<WebElement> handles = poly.getLinesOfPolyLine(1, 1);
//
//		poly.editPolyLine(handles.get(2), coordinates,handles.get(9));
//		poly.waitForTimePeriod(3000);
//		rt.assertTrue(rt.verifyAcceptedRTSegment(2),"Checkpoint[1/5]","Verifying the contour is turned in accepted");
//		rt.assertTrue(rt.verifyAcceptGSPSRadialMenu(), "Checkpoint[2/5]","Verifying the accept gsps radial menu");
//
//		List<String> clone = cs.getAllResults();
//		cs.assertEquals(clone.size(),2,"Checkpoint[3/5]","Verifying the clone is created");		
//		cs.selectResultFromSeriesTab(1, clone.get(1));
//
//		panel=new OutputPanel(driver) ;	
//		panel.enableFiltersInOutputPanel(true, false, false);	
////		List<String> findings = panel.getAllFindingsName();
////		panel.assertEquals(findings.get(0), legendNames.get(1), "Checkpoint[4/5]", "Verifying the contour name is same in output panel as well after accepting the segment");
//
//		viewerPage.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[5/5] verifying the thumbnail after editing the clone copy","test03_1");
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
//	public void test04_US1053_TC5196_TC5197_TC5233_TC5234_TC5267_verifyGSPSWithDICOMRTInOutputPanel() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Output Panel displays the GSPS and RTStruct thumbnails"
//				+ "<br> Verify Output Panel is updated when GSPS and RTStruct findings are accepted or rejected and study is reloaded"
//				+ "<br> Verify Output-Panel should show the contours from the loaded clone copy in the viewer"
//				+ "<br> Verify Output-Panel show the contours from the loaded clone copy in the viewer even if there are multiple clones."
//				+ "<br> Verify Output Panel is shows the latest version of GSPS and RTStruct findings when there are multiple clones and all are loaded in the viewer.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		rt = new DICOMRT(driver);
//		cs = new ContentSelector(driver);
//		circle = new CircleAnnotation(driver);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, -120, -120, -60,-60);	
//		findingMenu=new ViewerSliderAndFindingMenu(driver);
//
//
//		List<String> acceptedFinding = findingMenu.getFindingsName(1,ViewerPageConstants.ACCEPTED_COLOR);	
//		cs.assertEquals(cs.getText(cs.getAllResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT).get(0)),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[1/17]", "Verifying the clone generation in content selector");
//
//		DateFormat  originalFormat = new SimpleDateFormat(ViewerPageConstants.CONTENTSELECTOR_DATE_FORMAT);
//		cs.openAndCloseSeriesTab(true);
//		Date date = originalFormat.parse(cs.getText(cs.allSeriesCreationDateFromSeriesTab.get(0)));
//
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		String formattedDate = targetFormat.format(date);
//
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
//
//		verifyRTFindingsWithGSPSInOuputPanel(true, false, false, acceptedFinding, username,"Checkpoint[2/17]", true, "test04_1");
//		verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[3/17]", formattedDate, true);
//
//		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));		
//		cs.assertEquals(cs.getAllResults().size(),2, "Checkpoint[4/17]", "Verifying no new clone is generated");
//
//		rt.rejectSegment(3);
//
//		List<String> clones = cs.getAllResults();
//		cs.assertEquals(clones.size(),3, "Checkpoint[5/17]", "Verifying new clone is generated for rejecting the rt contour");
//		rt.assertTrue(rt.verifyRejectedRTSegment(3),"Checkpoint[6/17]","Verifying the contour is rejecting");
//
//		verifyRTFindingsWithGSPSInOuputPanel(false, true, false, ListUtils.union(acceptedFinding,rt.getSegmentsName(1, ViewerPageConstants.REJECTED_RESULT_COLOR)), username,"Checkpoint[7/17]", false, "");
//
//		helper=new HelperClass(driver);
//	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);
//
//	    cs.assertEquals(cs.getAllResults().size(),clones.size(), "Checkpoint[8/17]", "Verifying new clone is generated for rejecting the rt contour");
//	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clones.get(2)), "Checkpoint[9/17]", "Verifying The latest RTstruct clone copy should be loaded.");
//
//
//		verifyRTFindingsWithGSPSInOuputPanel(false, true, false, ListUtils.union(acceptedFinding,rt.getSegmentsName(1, ViewerPageConstants.REJECTED_RESULT_COLOR)),username, "Checkpoint[10/17]", false, "");
//
//
//		List<String> pendingSeg = rt.getSegmentsName(1, ViewerPageConstants.NO_RESULT_COLOR);
//		verifyFindingsInOuputPanel(false, false,true, pendingSeg, "Checkpoint[11/17]","",false);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 120, 120, 60, 60);
//
//		List<String> acceptedFindings = findingMenu.getFindingsName(1,ViewerPageConstants.ACCEPTED_COLOR);
//
//		cs.assertEquals(cs.getAllResults().size(),clones.size()+1, "Checkpoint[12/17]", "Verifying new clone is generated for rejecting the rt contour");
//		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2"), "Checkpoint[13/17]", "Verifying The latest RTstruct clone copy should be loaded.");
//		circle.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint[14/17]", "Verifying the Circles are present");
//
//		rt.rejectSegment(3);
//
//		viewerPage.click(viewerPage.getViewPort(1));
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.threeByTwoLayoutIcon);		
//		viewerPage.waitForTimePeriod(10000);
//
//		viewerPage.click(viewerPage.getViewPort(2));
//		verifyFindingsInOuputPanel(true, false, false, acceptedFindings, "Checkpoint[15/17]","",false);
//		//verifyFindingsInOuputPanel(false, true, false, rt.getSegmentsName(1, NSConstants.REJECTED_RESULT_COLOR), "Checkpoint[17/17]","",false);
//		//verifyFindingsInOuputPanel(false, false, true, legendNames, "Checkpoint[16/17]","",false);
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
//	public void test05_US1053_TC5235_verifyDICOMRTStateInOutputPanel() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Output-Panel show the contours from the latest edited clone copy when multiple clones from one base series is available");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		cs = new ContentSelector(driver);
//
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
//
//		rt.rejectSegment(3);
//		rt.waitForTimePeriod(10000);
//
//		List<String> clones = cs.getAllResults();
//		cs.assertEquals(clones.size(),2, "Checkpoint[1/10]", "Verifying new clone is generated for rejecting the rt contour");
//		rt.assertTrue(rt.verifyRejectedRTSegment(3),"Checkpoint[2/10]","Verifying the contour is rejecting");
//
//
//		verifyRTFindingsWithGSPSInOuputPanel(false, true, false, rt.getSegmentsName(1, ViewerPageConstants.REJECTED_RESULT_COLOR), username,"Checkpoint[3/10]", false, "");
//		verifyRTFindingsWithGSPSInOuputPanel(false, false, true, rt.getSegmentsName(1, ViewerPageConstants.NO_RESULT_COLOR), username,"Checkpoint[4/10]", false, "");
//
//		helper=new HelperClass(driver);
//	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);;
//
//	    cs.assertEquals(cs.getAllResults().size(),clones.size(), "Checkpoint[5/10]", "Verifying new clone is generated for rejecting the rt contour");
//	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clones.get(1)), "Checkpoint[6/10]", "Verifying The latest RTstruct clone copy should be loaded.");
//
//
//		for(int i=2;i<legendNames.size();i=i+2) {			
//			rt.acceptSegment(i);			
//		}
//
//		cs.assertEquals(cs.getAllResults().size(),clones.size()+1, "Checkpoint[7/10]", "Verifying new clone is generated for accepting the rt contour");
//
//		List<String> acceptedSeg = rt.getSegmentsName(1, ViewerPageConstants.ACCEPTED_RESULT_COLOR);
//		List<String> rejectedSeg = rt.getSegmentsName(1, ViewerPageConstants.REJECTED_RESULT_COLOR);
//		List<String> pendingSeg = rt.getSegmentsName(1, ViewerPageConstants.NO_RESULT_COLOR);
//
//		verifyFindingsInOuputPanel(true, false, false, acceptedSeg, "Checkpoint[8/10]","",false);
//		verifyFindingsInOuputPanel(false, true, false, rejectedSeg, "Checkpoint[9/10]","",false);
//		verifyFindingsInOuputPanel(false, false,true, pendingSeg, "Checkpoint[10/10]","",false);
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1054"})
//	public void test06_US1054_TC5536_verifyWarningMessageForRT() throws InterruptedException, ParseException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the error message displayed when the RT Struct series  selected from the thumbnail in the output panel is not loaded in the viewer.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(rendoEntPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForViewerpageToLoad();
//		cs = new ContentSelector(driver);
//		panel = new OutputPanel(driver);
//
//		String seriesDescription = rt.getSeriesDescriptionOverlayText(1);
//		cs.selectSeriesFromSeriesTab(1, seriesDescription);
//		cs.click(cs.getViewPort(1));
//
//		panel.enableFiltersInOutputPanel(false,false,true);		
//		panel.clickOnThumbnail(1);
//
//		// verify warning message for thumbnail
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
//		rt.assertEquals(rt.getText(panel.warningMessage).trim(),("\""+seriesDescription+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG).trim(), "Verify warning message if thumbnail corresponding slice is not open", "Verified");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1053"})
//	public void test07_US1054_TC5532_verifyJumpToForRT() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify selecting the RT Struct finding from the thumbnail present in the Output panel.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//		
//		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
//		patientListPage.waitForTimePeriod(1000);
//		String machineName=patientListPage.getText(patientListPage.machineNameOnEurekaAl);
//		
//		patientListPage.clickOntheFirstStudy();
//		
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		cs = new ContentSelector(driver);
//
//		verifyRTFindingsWithGSPSInOuputPanel(false, false, true, rt.getSegmentsName(1, ViewerPageConstants.NO_RESULT_COLOR),machineName,"Checkpoint[1/5]", false, "Verifying the Findings in  output panel");
//		cs.click(cs.getViewPort(1));
//
//		panel.enableFiltersInOutputPanel(false, false, true);
//
////		String findingName = panel.findingsNameTitleList.get(0).getText();
//
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/"+dicomRTPatientName+"_cineplayImage.png");
//
//		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);
//
//		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/"+dicomRTPatientName+"_cineplayImage.png");		
//		String expectedImagePath = newImagePath+"/goldImages/"+dicomRTPatientName+"_cineplayImage.png";
//		String actualImagePath = newImagePath+"/actualImages/"+dicomRTPatientName+"_cineplayImage.png";
//		String diffImagePath = newImagePath+"/actualImages/diffImage_"+dicomRTPatientName+"_cineplayImage.png";
//
//		boolean cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		rt.assertFalse(cpStatus, "Checkpoint[2/5] The actual and Expected image should not same","After cine images are changed");		
//		panel.assertFalse(jumpToIconPresence, "Checkpoint[3/5]", "verifying the jump to icon is not displayed when cine is working");
//
//		panel.clickOnThumbnail(1);
////		panel.assertEquals(rt.getSelectedCountorColor(),rt.getLegendOptionColor(findingName),"Checkpoint[4/5]","Verifying the selected countout and segment has some color");	
//		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[5/5]", "verifying that pending gsps toolbar is displayed");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050","BVT"})
//	public void test08_US1050_TC5499_verifyCineFunctionalityAndJumpToIconPresence() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify mouse actions on RT-Struct Contours in Output Panel");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		panel = new OutputPanel(driver);
//		panel.enableFiltersInOutputPanel(false, false, true);		
//
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test08_cineplayImage.png");
//
//		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);
//
//		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test08_cineplayImage.png");		
//		String expectedImagePath = newImagePath+"/goldImages/test08_cineplayImage.png";
//		String actualImagePath = newImagePath+"/actualImages/test08_cineplayImage.png";
//		String diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImage.png";
//
//		boolean cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		rt.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
//		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");
//
//		panel.mouseHover(panel.thumbnailList.get(0));		
//		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test08_cineplayImageStopped.png");
//
//		panel.waitForTimePeriod(2000);
//
//		rt.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test08_cineplayImageStopped.png");		
//		expectedImagePath = newImagePath+"/goldImages/test08_cineplayImageStopped.png";
//		actualImagePath = newImagePath+"/actualImages/test08_cineplayImageStopped.png";
//		diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImageStopped.png";
//
//		cpStatus =  rt.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		rt.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
//		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050"})
//	public void test09_US1050_TC5496_verifyFindingsArePresentInThumbnailOnPlayingCine() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify when cine works on GSPS findings that spans multiple slices");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//	
//		int findingsCount = rt.getFindingsCountFromFindingTable();
//
//		panel = new OutputPanel(driver);
//		panel.enableFiltersInOutputPanel(false, false, true);	
//
//		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/2]", "Verifying that findings menu has same findings in output panel");
//		panel.assertTrue(panel.verifyAnnotationsPresenceInThumbnail(4),"Checkpoint[2/2]","Verifying the annotations are displayed when cine is played on thumbnail");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1050"})
//	public void test10_US1050_TC5460_DE1855_TC7407_verifyingMultipleContoursDisplayedInThumbnail() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify when multiple contours are present in a single slice in a contour sequence output panel thumbnail displays all contours that are available in a slice."
//				+ "<br> Verify the contours are displayed with respect to slices during cine in output panel.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		int findingsCount = rt.getFindingsCountFromFindingTable();
//
//		panel = new OutputPanel(driver);
//		panel.enableFiltersInOutputPanel(false, false, true);	
//
//		panel.assertEquals(panel.thumbnailList.size(), findingsCount, "Checkpoint[1/2]", "Verifying that findings menu has same findings in output panel");
//
////		for(int i = 0 ;i<90;i++) {
////			List<WebElement> ann = panel.getAnnotationsFromThumnailCineSlow(2);
////			panel.assertTrue(ann.size()>=1&&ann.size()<=5,"checpoint[2/2]","Verifying that multiple annotations are also displayed "+ann.size());
////		}
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1369"})
//	public void test11_DE1369_TC5677_verifyCreatedDateInOutputPanel() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Series date should be displayed in output panel in Created On field");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		cs = new ContentSelector(driver);
//		panel=new OutputPanel(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		DateFormat  originalFormat = new SimpleDateFormat(ViewerPageConstants.CONTENTSELECTOR_DATE_FORMAT);
//		cs.openAndCloseSeriesTab(true);
//		String machineName=rt.getText(cs.allMachineName.get(0));
//		Date date = originalFormat.parse(cs.allSeriesCreationDateFromSeriesTab.get(0).getText());
//
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		String formattedDate = targetFormat.format(date);
//		Calendar cal = Calendar.getInstance();
//		String currentDate=targetFormat.format(cal.getTime());
//
//		//Bydefault machine name will be display as creator for RT data
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data before editing Contour");
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
//		rt.mouseHover(rt.getViewPort(1));
////		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, legendNames.size(), "Checkpoint[1/3]", formattedDate,machineName);
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour");
////		//navigation to first contour of segment (here, segment 2)
////		rt.navigateToFirstContourOfSegmentation(2);
////		rt.selectAcceptfromGSPSRadialMenu();
////		rt.mouseHover(rt.getViewPort(1));
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, true, legendNames.size(), "Checkpoint[2/3]", currentDate, username);
////
////		//creating new user and verifying it
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying the Creation date and Creator name for RT Struct data");
////		rt.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
////		register=new RegisterUserPage(driver);
////		register.createNewUser(newUser, "" ,newUser , LoginPageConstants.SUPPORT_EMAIL, newUser, newUser, newUser);
////		Header header = new Header(driver);
////		header.logout();
////		loginPage=new LoginPage(driver);
////		loginPage.login(newUser, newUser);
////		patientListPage.waitForPatientPageToLoad();
////		helper=new HelperClass(driver);
////	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);
////
////		
////		//navigation to first contour of segment (here, segment 2)
////		rt.navigateToFirstContourOfSegmentation(3);
////		rt.selectRejectfromGSPSRadialMenu();
////		rt.mouseHover(rt.getViewPort(1));
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, true, true, legendNames.size(), "Checkpoint[3/3]", currentDate, newUser);
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1350","DE1757"})
//	public void test12_DE1350_TC5661_DE1757_TC7160_verifyConsoleForFilterButtonInOPForRT() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify no console error received when pressing the filter button in output panel. <br>"+
//				"Verify that the console errors are not displayed when user clicks the thumbnail on output panel.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel=new OutputPanel(driver);
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		circle=new CircleAnnotation(driver);
//		
//		panel.enableFiltersInOutputPanel(false, true, false);
//		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[1/3]", "Verify No console error receive when click on output panel, filter button");
//		panel.openAndCloseOutputPanel(false);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 10, 10, 50, 50);
//
//		panel.enableFiltersInOutputPanel(true, false, true);
//		panel.clickOnThumbnail(1);
//		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[2/3]", "Verify No console error receive when click on thumbnail after drawing GSPS annotation");
//
//		helper=new HelperClass(driver);
//	    helper.browserBackAndReloadViewer(dicomRTPatientName,1,1);;
//
//		//panel.click(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, true);
//		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[3/3]", "Verify No console error receive when click on output panel, filter button after drawing GSPS annotation");
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1282", "Negative","BVT"})
//	public void test13_DE1772_TC7163_verifyNoCineForRTInPreviewPopup() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Copy button functionality in the Output panel."+"<br> Verify the Preview page UI.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " +dicomRTPatientName  + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		for(int i =1;i<=3;i++)
//			rt.acceptSegment(i);
//		for(int i =4;i<=6;i++)
//			rt.rejectSegment(i);
//
//		OutputPanel panel = new OutputPanel(driver);	
//		panel.enableFiltersInOutputPanel(true, true, true);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Opening the AI Sync Preview Popup");
//		int thumbnailCount=panel.findingsList.size();
//		AISyncPreviewWindow aiSync = new AISyncPreviewWindow(driver);
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//
//		panel.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),thumbnailCount,"Checkpoint[1/3]", "Verifying the findings count is same in O/P and on AI sync preview");
//
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//
//		for(int i =0;i<panel.thumbnailList.size();i++) {
//			panel.scrollIntoView(panel.thumbnailList.get(i));
//			panel.takeElementScreenShot(panel.thumbnailList.get(i), newImagePath+"/goldImages/test01_cineplayImage_expected"+i+".png");
//			panel.mouseHover(panel.thumbnailList.get(i));
//			panel.takeElementScreenShot(panel.thumbnailList.get(i), newImagePath+"/goldImages/test01_cineplayImage_actual"+i+".png");	
//
//
//			String expectedImagePath = newImagePath+"/goldImages/test01_cineplayImage_expected"+i+".png";
//			String actualImagePath = newImagePath+"/goldImages/test01_cineplayImage_actual"+i+".png";
//			String diffImagePath = newImagePath+"/goldImages/test01_cineplayImage_diff"+i+".png";
//
//			boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//			panel.assertTrue(cpStatus, "Checkpoint[2/3]","Verifying that no cine is played");
//
//			panel.assertTrue(panel.verifyPolyLineIsPresentInThumbnail(i+1),"Checkpoint[3/3]","Vreifying the annotation is present");
//
//		}
//
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1816", "Negative" })
//	public void test14_DE1816_TC7364_verifyContourAreNotBrokenAfterAcceptAll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that RT contours are not broken on sliding the scroll bar after accept all perform on machine/users button.[Happy Path]");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " +dicomRTPatientName  + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
//		patientListPage.waitForTimePeriod(1000);
//		String machineName=patientListPage.getText(patientListPage.machineNameOnEurekaAl);
//
//		patientListPage.clickOntheFirstStudy();
//	
//		patientListPage.clearConsoleLogs();
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 100, 100, 100, 100);
//
//		
//		OutputPanel panel = new OutputPanel(driver);			
//		panel.enableFiltersInOutputPanel(true, true, true);
////		panel.acceptAllMachineFilter(machineName);
////		panel.rejectAllMachineFilter(machineName);
////		panel.acceptAllMachineFilter(machineName);
////		panel.openAndCloseOutputPanel(false);
////		
//		
//		rt.assertEquals(rt.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_COLOR).size(),rt.getLegendOptionsList(1).size(),"Checkpoint[1/6]","Verifying all the findings are accepted");
//		
//		poly = new PolyLineAnnotation(driver);
//		
//		List<WebElement> polylines = poly.getAllPolylines(1);
//		int currentSliceNumber = rt.getCurrentScrollPositionOfViewbox(1);
//
//		rt.scrollTheSlicesUsingSlider(1, 0, -100, 0, -100);
//		rt.assertFalse(rt.isConsoleErrorPresent(),"Checkpoint[2/6]","Verifying there is no console error post slider scroll");
//		rt.assertNotEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[3/6]","verifying slice is getting changed post slider scroll");
//		rt.assertNotEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[4/6]","verifying contours are not same post scroll");
//		
//		rt.scrollTheSlicesUsingSlider(1, 0, 0, 0, 100);
//		rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[5/6]","verifying going back to its position which has same slice");
//		rt.assertEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[6/6]","After navigating back verifying same contours are displayed");
//		
//
//	}
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1816", "Negative" })
//	public void test15_DE1816_TC7371_verifyContourAreNotBrokenAfterRejectAll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that RT contours are not broken on sliding the scroll bar after reject all perform on machine/users button.[Happy Path]");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " +dicomRTPatientName  + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
//		patientListPage.waitForTimePeriod(1000);
//		String machineName=patientListPage.getText(patientListPage.machineNameOnEurekaAl);
//
//		patientListPage.clickOntheFirstStudy();
//
//		patientListPage.clearConsoleLogs();
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 100, 100, 100, 100);
//
//		
//		OutputPanel panel = new OutputPanel(driver);			
//		panel.enableFiltersInOutputPanel(true, true, true);
////		panel.rejectAllMachineFilter(machineName);
////		panel.acceptAllMachineFilter(machineName);
////		panel.rejectAllMachineFilter(machineName);
////		panel.openAndCloseOutputPanel(false);
//		
//		
//		rt.assertEquals(rt.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_COLOR).size(),rt.getLegendOptionsList(1).size(),"Checkpoint[1/6]","Verifying all the findings are rejected");
//		
//		poly = new PolyLineAnnotation(driver);
//		
//		List<WebElement> polylines = poly.getAllPolylines(1);
//		int currentSliceNumber = rt.getCurrentScrollPositionOfViewbox(1);
//
//		rt.scrollTheSlicesUsingSlider(1, 0, -100, 0, -100);
//		rt.assertFalse(rt.isConsoleErrorPresent(),"Checkpoint[2/6]","Verifying there is no console error post slider scroll");
//		rt.assertNotEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[3/6]","verifying slice is getting changed post slider scroll");
//		rt.assertNotEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[4/6]","verifying contours are not same post scroll");
//		
//		rt.scrollTheSlicesUsingSlider(1, 0, 0, 0, 100);
//		rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[5/6]","verifying going back to its position which has same slice");
//		rt.assertEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[6/6]","After navigating back verifying same contours are displayed");
//		
//
//	}
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1816", "Negative" })
//	public void test16_DE1816_TC7372_verifyContourAreNotBrokenAfterAcceptAllUserButton() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that RT contours are not broken on sliding the scroll bar after accept all perform on user button.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " +dicomRTPatientName  + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//		
//		patientListPage.clearConsoleLogs();
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 100, 100, 100, 100);
//
//		
//		OutputPanel panel = new OutputPanel(driver);			
//		
//		panel.enableFiltersInOutputPanel(true, true, true);
////		panel.acceptAllMachineFilter(username);
////		panel.rejectAllMachineFilter(username);
////		panel.acceptAllMachineFilter(username);
////		panel.openAndCloseOutputPanel(false);
//		
//		
//		rt.assertEquals(rt.getStateSpecificFindings(1, ViewerPageConstants.PENDING_COLOR).size(),rt.getLegendOptionsList(1).size(),"Checkpoint[1/8]","Verifying all the findings are rejected");
//		
//		poly = new PolyLineAnnotation(driver);
//		
//		List<WebElement> polylines = poly.getAllPolylines(1);
//		int currentSliceNumber = rt.getCurrentScrollPositionOfViewbox(1);
//
//		rt.scrollTheSlicesUsingSlider(1, 0, -100, 0, -100);
//		rt.assertFalse(rt.isConsoleErrorPresent(),"Checkpoint[2/8]","Verifying there is no console error post slider scroll");
//		rt.assertNotEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[3/8]","verifying slice is getting changed post slider scroll");
//		rt.assertNotEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[4/8]","verifying contours are not same post scroll");
//		
//		rt.scrollTheSlicesUsingSlider(1, 0, 0, 0, 100);
//		rt.assertEquals(rt.getCurrentScrollPositionOfViewbox(1),currentSliceNumber,"Checkpoint[5/8]","verifying going back to its position which has same slice");
//		rt.assertEquals(poly.getAllPolylines(1).size(),polylines.size(),"Checkpoint[6/8]","After navigating back verifying same contours are displayed");
//		rt.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint[7/8]","After navigating back verifying same contours are displayed");
//		rt.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[8/8]","After navigating back verifying same contours are displayed");
//
//	}
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"DE1932"})
//	public void test17_DE1932_TC7859_verifyConsoleErrorWhenUserNavigateToStudyPage() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that console error is not displayed when user navigates to the study page from output panel.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		panel=new OutputPanel(driver);
//	    rt.waitForDICOMRTToLoad();
//	
//		circle = new CircleAnnotation(driver);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, -120, -120, -60,-60);	
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.clickOnJumpIcon(1);
//		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[1/2]", "Verified that circle annotation is highlighted after click on jump icon.");
//		
//		//navigate back to study page
//		panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[2/2]", "Verified that no console error present after navigating back to study list page.");
//	
//	}
//	
//	//DR2188: [RT struct]Same findings from both user copy and machine copy are getting displayed in output panel when both copies are loaded into viewer
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2188"})
//	public void test18_DE2188_TC8703_verifyCreatedDateInOutputPanel() throws InterruptedException, ParseException, SQLException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Series date should be displayed in output panel in Created On field");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of Patiene "+dicomRTPatientName+" on viewer.");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		cs = new ContentSelector(driver);
//		panel=new OutputPanel(driver);
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		//edit contour
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Edit RT and verify clone in CS.");
//		poly = new PolyLineAnnotation(driver);
//		rt.navigateToFirstContourOfSegmentation(1);
//		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(3),poly.getLinesOfPolyLine(1, 1).get(25));
//		
//		panel.click(panel.getViewPort(1));
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//		
//		panel.click(panel.getViewPort(1));
//		List<String>results=cs.getAllResults();
//		
//		db=new DatabaseMethods(driver);
//		Date seriesDate=db.getSeriesOrResultDateFromSeriesLevel(results.get(0));
//		String seriesDateFormat=panel.convertDateFormat(seriesDate, ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		
//		panel.convertDateFormat(seriesDate, ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		
//		panel.assertTrue(cs.verifyPresenceOfEyeIcon(results.get(0)), "Checkpoint[1/19]", "Verified that first result is loaded on viewer.");
//		panel.assertTrue(cs.verifyPresenceOfEyeIcon(results.get(1)), "Checkpoint[2/19]", "Verified that new clone is loaded on viewer.");
//		
//		cs.openAndCloseSeriesTab(true);
//		String machineName=rt.getText(cs.allMachineName.get(0));
//		
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		Calendar cal = Calendar.getInstance();
//		String currentDate=targetFormat.format(cal.getTime());
//	
//		List<String> acceptedFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_COLOR));
//		List<String> pendingFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_COLOR));
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour for Accepted findings");
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false, acceptedFinding.size(), "Checkpoint[3/19]", currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[4/19]",currentDate,machineName);
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour for Pending findings");
////		panel.click(panel.getViewPort(1));
////	    panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true,pendingFinding.size(),"Checkpoint[5/19]" ,currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[6/19]",seriesDateFormat ,machineName);
//
//		//reload viewer page
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying new clone after editing Contour on viewer reload.");
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadRTData(dicomRTPatientName, 1, 1);
//	
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//		rt.navigateToFirstContourOfSegmentation(1);
//		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(25),poly.getLinesOfPolyLine(1, 1).get(30));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data before editing Contour");
//		acceptedFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_COLOR));
//		pendingFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_COLOR));
//	
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verified creation date,Creator,Edition date and Editor name in Output panel for accepted finding after re-load.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[7/19]", currentDate,username);
////		panel.click(panel.getViewPort(1));
////		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[8/19]",currentDate,machineName);
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the creation date,Creator,Edition date and Editor name in Output panel for pendings finding after re-load.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[9/19]", currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[10/19]",seriesDateFormat,machineName);
////		
////		panel.assertEquals(cs.getAllResults().size(), results.size()+1, "Checkpoint[11/19]", "Verified that new clone is created after editing the contour on viewer reload.");
////		results=cs.getAllResults();
////		
////		cs.selectResultFromSeriesTab(2, results.get(1));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify accepted findings in Output panel when second and first clone is loaded in respective viewbox.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[12/19]", currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[13/19]",currentDate,machineName);
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify pending findings in Output panel when second and first clone is loaded in respective viewbox.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[14/19]",currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[14/19]", seriesDateFormat,machineName);
//		
//
//		cs.selectResultFromSeriesTab(1, results.get(1));
//		cs.selectResultFromSeriesTab(2, results.get(0));
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify accepted findings in Output panel when first and default result is loaded in respective viewbox.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[16/19]", currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[17/19]", currentDate,machineName);
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify pending findings in Output panel when first and default result is loaded in respective viewbox.");
////		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[18/19]", currentDate,username);
////		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[19/19]",seriesDateFormat,machineName);
//		
//	}
//	
//	
//	@AfterMethod(alwaysRun=true)
//	public void afterTest() throws SQLException {
//		db = new DatabaseMethods(driver);
//		db.deleteRTafterPerformingAnyOperation(username);
//		db.deleteRTafterPerformingAnyOperation(newUser);
//		db.deleteUser(newUser);
//
//	}
//
//	private void verifyFindingsInOuputPanel(boolean accept, boolean reject, boolean pending , List<String> findings, String checkpoint,String createdOnDate, boolean imageComparison) throws InterruptedException {
//
//		panel=new OutputPanel(driver) ;	
//		panel.enableFiltersInOutputPanel(accept, reject, pending);
////		List<String> opfindings = panel.getAllFindingsName();		
////		panel.assertEquals(findings.size(),opfindings.size(), checkpoint+".a", "Verifying the legend and findings sizes are same");
////		panel.waitForTimePeriod(3000);
////		
////		for(int i =0;i<opfindings.size();i++) {
////			int thumbnailIndex=panel.getThumbnailForGivenResult(findings.get(i));
////			panel.mouseHover(panel.createdByUserList.get(thumbnailIndex));
////			if(imageComparison)
////				panel.compareElementImage(protocolName, panel.thumbnailList.get(thumbnailIndex), checkpoint+".b verifying the thumbnail images for all the legends","test01_"+i);
////			panel.assertTrue(panel.isElementPresent(panel.thumbnailList.get(thumbnailIndex)),checkpoint+".c","Verifying that DICOMRT thumbnail is displayed");	
////			panel.assertFalse(panel.getText(panel.createdOnDateList.get(thumbnailIndex)).isEmpty(),checkpoint+".d","Verifying the Creation Date field is not empty");
////			panel.assertTrue(findings.contains(opfindings.get(thumbnailIndex)), checkpoint+".e", "verifying finding name in output panel are same as segments in legend");
//			//if(!createdOnDate.isEmpty())
//			//	panel.assertTrue(panel.getText(panel.createdOnDateList.get(i)).contains(createdOnDate),checkpoint+".f","Verifying the Creation Date field in output panel same as content selector")
////		}
//		panel.openAndCloseOutputPanel(false);
//
//	}
//
//	private void verifyRTFindingsWithGSPSInOuputPanel(boolean accept, boolean reject, boolean pending , List<String> findings,String createdBy,String checkpoint,boolean imageComparison,String imageName) throws InterruptedException {
//
////		panel=new OutputPanel(driver) ;	
////		panel.enableFiltersInOutputPanel(accept, reject, pending);
////		List<String> opfindings = panel.getAllFindingsName();
////
////		panel.assertEquals(findings.size(),opfindings.size(), checkpoint+".a", "Verifying the GSPS findings and rt findings sizes are same in output panel");
////		panel.waitForTimePeriod(3000);
////
////		for(int i =0;i<panel.thumbnailList.size();i++) {
////
////			panel.scrollIntoView(panel.thumbnailList.get(i));
////			if(imageComparison)
////				panel.compareElementImage(protocolName, panel.thumbnailList.get(i), checkpoint+".b verifying the thumbnail images for all the legends",imageName);			
////			panel.assertTrue(panel.getText(panel.createdByUserList.get(i)).contains(createdBy),"Checkpoint[]","Verifying the created by field");
////			panel.assertTrue(panel.isElementPresent(panel.thumbnailList.get(i)),checkpoint+".c","Verifying that DICOMRT thumbnail is displayed");	
////			panel.assertFalse(panel.getText(panel.createdOnDateList.get(i)).isEmpty(),checkpoint+".d","Verifying the createdOn field is not empty");
////			panel.assertTrue(findings.contains(opfindings.get(i)), checkpoint+".e", "verifying finding name in output panel are same as segments in legend");
////
////		}
////		panel.openAndCloseOutputPanel(false);
//
//	}
//
//}
///	OBSOLETE
//	//DR2188: [RT struct]Same findings from both user copy and machine copy are getting displayed in output panel when both copies are loaded into viewer
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2188"})
//	public void test18_DE2188_TC8703_verifyCreatedDateInOutputPanel() throws InterruptedException, ParseException, SQLException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Series date should be displayed in output panel in Created On field");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of Patiene "+dicomRTPatientName+" on viewer.");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		cs = new ContentSelector(driver);
//		panel=new OutputPanel(driver);
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		//edit contour
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Edit RT and verify clone in CS.");
//		poly = new PolyLineAnnotation(driver);
//		rt.navigateToFirstContourOfSegmentation(1);
//		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(3),poly.getLinesOfPolyLine(1, 1).get(25));
//		
//		panel.click(panel.getViewPort(1));
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//		
//		panel.click(panel.getViewPort(1));
//		List<String>results=cs.getAllResults();
//		
//		db=new DatabaseMethods(driver);
//		Date seriesDate=db.getSeriesOrResultDateFromSeriesLevel(results.get(0));
//		String seriesDateFormat=panel.convertDateFormat(seriesDate, ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		
//		panel.convertDateFormat(seriesDate, ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		
//		panel.assertTrue(cs.verifyPresenceOfEyeIcon(results.get(0)), "Checkpoint[1/19]", "Verified that first result is loaded on viewer.");
//		panel.assertTrue(cs.verifyPresenceOfEyeIcon(results.get(1)), "Checkpoint[2/19]", "Verified that new clone is loaded on viewer.");
//		
//		cs.openAndCloseSeriesTab(true);
//		String machineName=rt.getText(cs.allMachineName.get(0));
//		
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		Calendar cal = Calendar.getInstance();
//		String currentDate=targetFormat.format(cal.getTime());
//	
//		List<String> acceptedFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_COLOR));
//		List<String> pendingFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_COLOR));
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour for Accepted findings");
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false, acceptedFinding.size(), "Checkpoint[3/19]", currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[4/19]",currentDate,machineName);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour for Pending findings");
//		panel.click(panel.getViewPort(1));
//	    panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true,pendingFinding.size(),"Checkpoint[5/19]" ,currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[6/19]",seriesDateFormat ,machineName);
//
//		//reload viewer page
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying new clone after editing Contour on viewer reload.");
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadRTData(dicomRTPatientName, 1, 1);
//	
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//		rt.navigateToFirstContourOfSegmentation(1);
//		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(25),poly.getLinesOfPolyLine(1, 1).get(30));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data before editing Contour");
//		acceptedFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_COLOR));
//		pendingFinding = rt.convertWebElementToStringList(rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_COLOR));
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verified creation date,Creator,Edition date and Editor name in Output panel for accepted finding after re-load.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[7/19]", currentDate,username);
//		panel.click(panel.getViewPort(1));
//		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[8/19]",currentDate,machineName);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the creation date,Creator,Edition date and Editor name in Output panel for pendings finding after re-load.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[9/19]", currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[10/19]",seriesDateFormat,machineName);
//		
//		panel.assertEquals(cs.getAllResults().size(), results.size()+1, "Checkpoint[11/19]", "Verified that new clone is created after editing the contour on viewer reload.");
//		results=cs.getAllResults();
//		
//		cs.selectResultFromSeriesTab(2, results.get(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify accepted findings in Output panel when second and first clone is loaded in respective viewbox.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[12/19]", currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[13/19]",currentDate,machineName);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify pending findings in Output panel when second and first clone is loaded in respective viewbox.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[14/19]",currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[14/19]", seriesDateFormat,machineName);
//		
//
//		cs.selectResultFromSeriesTab(1, results.get(1));
//		cs.selectResultFromSeriesTab(2, results.get(0));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify accepted findings in Output panel when first and default result is loaded in respective viewbox.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, false,acceptedFinding.size(),"Checkpoint[16/19]", currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(true,false, false,acceptedFinding.size(),"Checkpoint[17/19]", currentDate,machineName);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify pending findings in Output panel when first and default result is loaded in respective viewbox.");
//		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, pendingFinding.size(),"Checkpoint[18/19]", currentDate,username);
//		panel.verifyEditionDateAndEditorInOutputPanel(false,false, true,pendingFinding.size(),"Checkpoint[19/19]",seriesDateFormat,machineName);
//		
//	}// OBSOLETE
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1282", "Negative","BVT"})
//	public void test13_DE1772_TC7163_verifyNoCineForRTInPreviewPopup() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Copy button functionality in the Output panel."+"<br> Verify the Preview page UI.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " +dicomRTPatientName  + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		DICOMRT rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		for(int i =1;i<=3;i++)
//			rt.acceptSegment(i);
//		for(int i =4;i<=6;i++)
//			rt.rejectSegment(i);
//
//		OutputPanel panel = new OutputPanel(driver);	
//		panel.enableFiltersInOutputPanel(true, true, true);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Opening the AI Sync Preview Popup");
//		int thumbnailCount=panel.findingsList.size();
//		AISyncPreviewWindow aiSync = new AISyncPreviewWindow(driver);
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//
//		panel.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),thumbnailCount,"Checkpoint[1/3]", "Verifying the findings count is same in O/P and on AI sync preview");
//
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//
//		for(int i =0;i<panel.thumbnailList.size();i++) {
//			panel.scrollIntoView(panel.thumbnailList.get(i));
//			panel.takeElementScreenShot(panel.thumbnailList.get(i), newImagePath+"/goldImages/test01_cineplayImage_expected"+i+".png");
//			panel.mouseHover(panel.thumbnailList.get(i));
//			panel.takeElementScreenShot(panel.thumbnailList.get(i), newImagePath+"/goldImages/test01_cineplayImage_actual"+i+".png");	
//
//
//			String expectedImagePath = newImagePath+"/goldImages/test01_cineplayImage_expected"+i+".png";
//			String actualImagePath = newImagePath+"/goldImages/test01_cineplayImage_actual"+i+".png";
//			String diffImagePath = newImagePath+"/goldImages/test01_cineplayImage_diff"+i+".png";
//
//			boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//			panel.assertTrue(cpStatus, "Checkpoint[2/3]","Verifying that no cine is played");
//
//			panel.assertTrue(panel.verifyPolyLineIsPresentInThumbnail(i+1),"Checkpoint[3/3]","Vreifying the annotation is present");
//
//		}
//
//
//
//
//	}
// OBSOLETE
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1369"})
//	public void test11_DE1369_TC5677_verifyCreatedDateInOutputPanel() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Series date should be displayed in output panel in Created On field");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(dicomRTPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt = new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		cs = new ContentSelector(driver);
//		panel=new OutputPanel(driver);
//		rt.waitForDICOMRTToLoad();
//		
//		DateFormat  originalFormat = new SimpleDateFormat(ViewerPageConstants.CONTENTSELECTOR_DATE_FORMAT);
//		cs.openAndCloseSeriesTab(true);
//		String machineName=rt.getText(cs.allMachineName.get(0));
//		Date date = originalFormat.parse(cs.allSeriesCreationDateFromSeriesTab.get(0).getText());
//
//		DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		String formattedDate = targetFormat.format(date);
//		Calendar cal = Calendar.getInstance();
//		String currentDate=targetFormat.format(cal.getTime());
//
//		//Bydefault machine name will be display as creator for RT data
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data before editing Contour");
//		List<String> legendNames = rt.convertWebElementToStringList(rt.getLegendOptionsList(1));
////		rt.mouseHover(rt.getViewPort(1));
////		panel.verifyCreationDateAndCreatorInOutputPanel(false, false, true, legendNames.size(), "Checkpoint[1/3]", formattedDate,machineName);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the Creation date and Creator name for RT Struct data after editing Contour");
//		//navigation to first contour of segment (here, segment 2)
//		rt.navigateToFirstContourOfSegmentation(2);
//		rt.selectAcceptfromGSPSRadialMenu();
//		rt.mouseHover(rt.getViewPort(1));
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, false, true, legendNames.size(), "Checkpoint[2/3]", currentDate, username);
//
//		//creating new user and verifying it
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying the Creation date and Creator name for RT Struct data");
//		rt.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.createNewUser(newUser, "" ,newUser , LoginPageConstants.SUPPORT_EMAIL, newUser, newUser, newUser);
//		Header header = new Header(driver);
//		header.logout();
//		loginPage=new LoginPage(driver);
//		loginPage.login(newUser, newUser);
//		patientListPage.waitForPatientPageToLoad();
//		helper=new HelperClass(driver);
//	    helper.browserBackAndReloadRTData(dicomRTPatientName,1,1);
//
//		
//		//navigation to first contour of segment (here, segment 2)
//		rt.navigateToFirstContourOfSegmentation(3);
//		rt.selectRejectfromGSPSRadialMenu();
//		rt.mouseHover(rt.getViewPort(1));
//		panel.verifyCreationDateAndCreatorInOutputPanel(true, true, true, legendNames.size(), "Checkpoint[3/3]", currentDate, newUser);
//
//
//
//	}
//
//
