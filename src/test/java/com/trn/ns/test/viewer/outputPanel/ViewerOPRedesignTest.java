package com.trn.ns.test.viewer.outputPanel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;

import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerOPRedesignTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private RegisterUserPage register;

	private OutputPanel panel;
	private Header header;


	String flag = "false";
	DatabaseMethods db= new DatabaseMethods(driver);

	String username_1 = "user_1";
	String username_2 = "user_2";
	String username_3 = "user_3";

	// Get Patient Name

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String pointfilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINT_filepath");
	String GSPS_point = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, pointfilePath); 

	String liver9Filepath =Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9Filepath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	String TDAMaps = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String TDAMaps_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TDAMaps);

	String cpuTestFilePath = Configurations.TEST_PROPERTIES.get("cpu_test_Filepath");
	String cpuTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cpuTestFilePath);
	
	String vidaTestFilePath = Configurations.TEST_PROPERTIES.get("VIDA_LCS_COPD_Filepath");
	String vidaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, vidaTestFilePath);
	
	String iblFilePath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String iblPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iblFilePath);
	

	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_Filepath);


	String webGLConsoleError ="Failed to execute 'shaderSource' on 'WebGLRenderingContext': parameter 1 is not of type 'WebGLShader'";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerSendToPACS sd;

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);
		

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Negative" })
	public void test01_US1174_TC5755_verifyNoSeriesDescDisplayedWhenNoFindings()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Series Description is not displayed in output panel when there is no finding");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		panel.enableFiltersInOutputPanel(true, true, true);

		panel.assertFalse(panel.getText(panel.studySummary).isEmpty(), "Checkpoint[1/2]", "Verifying that there is no series description displayed when there is no finding");
		panel.assertFalse(panel.getText(panel.findingSummaryCount).isEmpty(), "Checkpoint[2/2]", "Verifying that there  is no findings count displayed when there is no finding");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1174", "Positive","BVT","DR2210" })
	public void test02_US1174_TC5755_TC5757_DR2210_TC8840_verifySeriesDescDisplayedWhenFindingsArePresentAndDataWOSeriesDesc()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Series Description summary with finding count in output panel"
				+ "<br> Verify Series number is displayed in output panel if Series Description is blank"
				+ "<br> Verify no webGL related console error is observed and thumbnail images are getting rendered properly");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeries + "in viewer");
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointMultiSeries, 1);

		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(1);
		panel.waitForViewerpageToLoad(2);


		int viewboxes = panel.getNumberOfCanvasForLayout();

		int totalFindings =0;
		for(int i =1 ; i<= viewboxes;i++)
			totalFindings = totalFindings + (panel.getFindingsCountFromFindingTable(i));

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", totalFindings),"Checkpoint[1/6]","Verifying the total findings are displayed");

		for(int i =1;i<= viewboxes;i++) {
			panel.selectFindingFromTable(1,i);
			panel.selectAcceptfromGSPSRadialMenu();
		}

		List<WebElement> acceptedFindings = panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("",acceptedFindings.size()),"Checkpoint[2/6]","Verifying only accepted findings");

		List<Integer> pendingFindings = new ArrayList<Integer>();
		for(int i =1 ; i<= viewboxes;i++)
			pendingFindings.add(panel.getStateSpecificFindings(i, ViewerPageConstants.PENDING_FINDING_COLOR).size());			

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", pendingFindings.size()),"Checkpoint[3/6]","Verifying only pending findings");


		for(int i =1 ; i<= viewboxes;i++) {
			panel.selectFindingFromTable(1,i);
			panel.selectRejectfromGSPSRadialMenu();
		}
		List<WebElement> rejectedFindings = panel.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR);
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", rejectedFindings.size()),"Checkpoint[4/6]","Verifying only rejected findings");		

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertTrue(panel.verifyStudyDescAndFindingCount("", totalFindings),"Checkpoint[5/6]","Verifying total findings displayed");

		panel.assertFalse(panel.isConsoleErrorPresent(webGLConsoleError),"Checkpoint[6/6]","Verify that Output panel should get opened and should not be any console error.");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1541", "Positive" })
	public void test07_DE1541_TC7358_verifySizeOfButtons() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("output panel");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + liver9PatientName + "in viewer");
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();

		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.WIDTH),ViewerPageConstants.OUTPUT_PANEL_OPEN_WIDTH,"Checkpoint[1/6]","Verifying the width of output panel open half circle button");
		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.HEIGHT),ViewerPageConstants.OUTPUT_PANEL_OPEN_HEIGHT,"Checkpoint[2/6]","Verifying the height of output panel open half circle button");
		panel.assertEquals(panel.getCssValue(panel.outputPanelTab, NSGenericConstants.CSS_BORDER_RADIUS),ViewerPageConstants.OUTPUT_PANEL_OPEN_BORDER_RADIUS,"Checkpoint[3/6]","Verifying the radius of output panel open half circle button");
		panel.enableFiltersInOutputPanel(true, false, false);

		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.WIDTH),ViewerPageConstants.OUTPUT_PANEL_CLOSE_WIDTH,"Checkpoint[4/6]","Verifying the width of output panel close half circle button");
		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.HEIGHT),ViewerPageConstants.OUTPUT_PANEL_CLOSE_HEIGHT,"Checkpoint[5/6]","Verifying the height of output panel close half circle button");
		panel.assertEquals(panel.getCssValue(panel.outputPanelMinimizeIcon, NSGenericConstants.CSS_BORDER_RADIUS),ViewerPageConstants.OUTPUT_PANEL_CLOSE_BORDER_RADIUS,"Checkpoint[6/6]","Verifying the radius of output panel close half circle button");

		panel.openAndCloseOutputPanel(false);



	}

	@Test(groups ={"Chrome","IE11","Edge","DR2371","Negative"})
	public void test08_DE2371_TC9360_VerifyMultipleEntriesForCPUTest()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that User created GSPS object on DICOM Stripe machine is not duplicated in output panel");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(cpuTestPatientName, 1);

		panel = new OutputPanel(driver);
		panel.pressKey(NSGenericConstants.DOT_KEY);

		lineWithUnit=new MeasurementWithUnit(driver);		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		panel.enableFiltersInOutputPanel(true, false,false);
		panel.waitForOutputPanelToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Veriying the count in output panel.");
		panel.assertEquals(panel.thumbnailList.size(),1, "Verifying the count in thumbnail should be 1 only. It should not be multiplied.", "Thumbnail count is 1 ");

		panel.enableFiltersInOutputPanel(false, true,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Verifying the count in thumbnail. It should not be multiplied.", "Thumbnail count is 0 under rejected tab");


		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Verifying the count in thumbnail. It should not be multiplied.", "Thumbnail count is 0 under pending tab");

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","E2E","F1082"})
	public void test09_US2165_TC10162_verifyOutputPanelTabPersistenceOnPageRefresh()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab state is persisted for user when page is refreshed.");

		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);

		panel=new OutputPanel(driver);
		helper.loadViewerDirectly(ah4PatientName, 1);

		cs.openAndCloseSeriesTab(true);
		panel.click(panel.opTabOpened);

		panel.refreshWebPage();
		loginPage.login(username, password);
		panel.waitForOutputPanelToLoad();
		panel.assertTrue(panel.isElementPresent(panel.opTabOpened), "Checkpoint[1/1]", "Verifying that output panel tab is highlighted after page refresh");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","DR2662","Positive","E2E","F1082"})
	public void test10_US2165_TC10163_DR2662_TC10407_verifyOutputPanelTabPersistenceOnReLogin()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab state is persisted for user after re login.<br>"+
		"Verify when you open and close the output panel selection state still retained when user logs off and log backs in");

		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		panel=new OutputPanel(driver);
		header=new Header(driver);
		helper.loadViewerDirectly(ah4PatientName, 1);
		cs.openAndCloseSeriesTab(true);

		panel.click(panel.opTabOpened);

		header.logout();

		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);
		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(ah4PatientName, 1);

		panel.assertTrue(panel.isElementPresent(panel.opTabOpened), "Checkpoint[1/1]", "Verifying that output panel tab is highlighted after re login");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","DR2662","Positive","E2E","F1082"})
	public void test11_US2165_TC10174_DR2662_TC10407_verifyMaximizedOutputPanelTabContentsOnReLogin()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the contents of persisted maximized tab is visible after the relogin. <br>"+
		"Verify when you open and close the output panel selection state still retained when user logs off and log backs in");

		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		helper.loadViewerDirectly(GSPS_point, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		cs.openAndCloseSeriesTab(true);

		panel.click(panel.opTabOpened);
		panel.enableFiltersInOutputPanel(false, false, true);

		int pendingFindingCount=panel.thumbnailList.size();

		header.logout();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);

		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GSPS_point, 1);
		panel.enableFiltersInOutputPanel(false, false, true);

		panel.assertEquals(panel.thumbnailList.size(),pendingFindingCount, "Checkpoint[1/1]", "Verifying the pending finding count in output panel, after the relogin");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","F1082"})
	public void test12_US2165_TC10164_verifyOutputPanelTabPersistenceOnTwoUsers()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab persistence of user A is not applicable to the user B.");

		cs=new ContentSelector(driver);
		header= new Header(driver);
		helper=new HelperClass(driver);
		panel= new OutputPanel(driver);

		panel.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

		header.logout();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GSPS_point, 1);
		panel.openAndCloseOutputPanel(true);
		panel.assertTrue(panel.isElementPresent(panel.opTabOpened), "Checkpoint[1/2]", "Verifying that Output panel tab is maximized for User A");

		panel.refreshWebPage();

		loginPage.login(username_2, username_2);
		helper.loadViewerDirectly(GSPS_point, 1);
		panel.assertFalse(panel.isElementPresent(panel.opTabOpened), "Checkpoint[2/2]", "Verifying that Output panel tab is minimized for User B");


	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","F1082"})
	public void test13_US2165_TC10175_verifyMaximizedOutputPanelTabContentsOnViewerRefresh()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and impact]: Verify that the maximized tab contents are displayed after the viewer load.");

		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		helper.loadViewerDirectly(GSPS_point, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		panel.openAndCloseOutputPanel(true);
		panel.assertTrue(panel.isElementPresent(panel.syncAllFindingsIcon), "Checkpoint[1/2]", "Verified that sync all finding is present inside the maximized output panel.");

		panel.assertTrue(panel.isElementPresent(panel.filterFindingsIcon), "Checkpoint[2/2]", "Verified that filter finding is present inside the maximized output panel.");


	}

	// US2284 - Tile based output panel view
	@Test(groups = { "Chrome", "IE11", "Edge", "US2284", "Positive" ,"F1125"})
	public void test01_US2284_TC9745_TC9752_TC9753_TC9785_verifyUIOP()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UI of the Output Panel."
				+ "<br> Verify the scroll bar on the Output Panel."
				+ "<br> Verify the options available on header like selecting all findings, filter options and copy to clipboard."
				+ "<br> Verify the new Output Panel in dark theme.");

		
		patientListPage.searchPatient(ChestCT1p25mm, "", "", "");
		String studyDesc = patientListPage.getStudyDescription(0);		
		
		patientListPage.clickOntheFirstStudy();
		
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(2);
		sd = new ViewerSendToPACS(driver);
		panel.closeNotification();
		
		panel.openAndCloseOutputPanel(true);	
		
		panel.assertTrue(panel.isElementPresent(panel.studySummary), "Checkpoint[1/31]", "Study information header at the top of the Output Panel");
		panel.assertTrue(panel.isElementPresent(panel.findingSummaryCount), "Checkpoint[2/31]", "Study- finding count information header at the top of the Output Panel");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Study information header should display the Study Description, no. of findings based on the findings available in the selected filter and 'Send to PACS' icon");
		panel.assertEquals(panel.getText(panel.studySummary),studyDesc, "Checkpoint[3/31]", "Study information header same as study table ");
		panel.assertEquals(panel.getText(panel.findingSummaryCount),panel.thumbnailList.size()+""+ViewerPageConstants.FINDING_MULTIPLE_HEADER, "Checkpoint[4/31]", "Finding count verification");
		panel.assertTrue(panel.isElementPresent(sd.sendToPacs), "Checkpoint[5/31]", "'Send to PACS' icon");
		
		panel.assertTrue(panel.isElementPresent(panel.deSelectedAllIcon), "Checkpoint[6/31]", "Checkbox to select Thumbnails");
		panel.assertEquals(panel.getText(panel.selectAllText),ViewerPageConstants.SELECT_ALL_TEXT, "Checkpoint[7/31]", "SelectAll Text verification");
		
		panel.assertTrue(panel.isElementPresent(panel.syncAllFindingsIcon), "Checkpoint[8/31]", "Filter icon presence");		
		panel.assertTrue(panel.isElementPresent(panel.filterFindingsIcon), "Checkpoint[9/31]", "Filter icon presence");
		
		panel.mouseHover(panel.studySummary);
		panel.assertFalse(panel.isElementPresent(panel.tooltip), "Checkpoint[10/31]", "No tooltip when study description is short");
					
		panel.mouseHover(sd.filterFindingsIcon);
		panel.assertTrue(panel.isElementPresent(panel.tooltip), "Checkpoint[11/31]", "Tooltip presence when hovered on filter icon");
		panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.FILTER_FINDINGS_TEXT, "Checkpoint[12/31]", "filter findings Text verification");
	
		panel.mouseHover(sd.sendToPacs);
		panel.assertTrue(panel.isElementPresent(panel.tooltip), "Checkpoint[13/31]", "Tooltip presence when hovered on send to pacs");
		panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.SEND_TO_PACS_TEXT, "Checkpoint[14/31]", "Send to pacs tooltip");
	
		
		panel.mouseHover(sd.syncAllFindingsIcon);
		panel.assertTrue(panel.isElementPresent(panel.tooltip), "Checkpoint[15/31]", "Tooltip presence when hovered on sync all findings icon");
		panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.SYNCALL_FINDINGS_TEXT, "Checkpoint[16/31]", "sync all findings Text verification");
			
		panel.openAndCloseOutputPanel(false);
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(vidaPatientName, 1);
		
		panel.openAndCloseOutputPanel(true);	
		
		panel.mouseHover(panel.studySummary);
		panel.assertTrue(panel.isElementPresent(panel.tooltip), "Checkpoint[17/31]", "tooltip when study description is long");
		panel.assertEquals(panel.getText(panel.tooltip),panel.getText(panel.studySummary), "Checkpoint[18/31]", "tooltip shows correct study");
		
		panel.openAndCloseOutputPanel(false);
	
		helper.loadViewerDirectly(pointMultiSeries, 1);
		
		panel.openAndCloseOutputPanel(true);	
		
		panel.mouseHover(panel.studySummary);
		panel.assertFalse(panel.isElementPresent(panel.tooltip), "Checkpoint[19/31]", "tooltip when study description is not present");
		panel.assertEquals(panel.getText(panel.studySummary),ViewerPageConstants.NO_STUDY_AVAILABLE_TEXT, "Checkpoint[20/31]", "verifying the text for study when no study present");
	
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getText(panel.findingSummaryCount),panel.thumbnailList.size()+ViewerPageConstants.FINDING_MULTIPLE_HEADER, "Checkpoint[21/31]", "Finding count verification");
	
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.getText(panel.findingSummaryCount),panel.thumbnailList.size()+ViewerPageConstants.FINDING_MULTIPLE_HEADER, "Checkpoint[22/31]", "Finding count verification");
	
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getText(panel.findingSummaryCount),panel.thumbnailList.size()+ViewerPageConstants.FINDING_MULTIPLE_HEADER, "Checkpoint[23/31]", "Finding count verification");
	
		for(int i=0;i<9;i=i+3) {
			
			int y = panel.getYCoordinate(panel.thumbnailList.get(i));
			for(int j=i+1;j<i+2;j++)
				panel.assertEquals(panel.getYCoordinate(panel.thumbnailList.get(i)), y, "Checkpoint[24/31]", "Verifying the three thumbnail in one row");
		}
		
		panel.assertTrue(panel.isElementPresent(panel.scrollbar), "Checkpoint[25/31]", "Verifying the scrollbar is displayed");
		panel.assertTrue(panel.isElementPresent(panel.scrollThumb), "Checkpoint[26/31]", "Verifying the scroll thumb is displayed");
		
		panel.resizeBrowserWindow(500, 500);
		panel.mouseHover(panel.studySummary);
		panel.assertTrue(panel.isElementPresent(panel.tooltip), "Checkpoint[27.1/31]", "tooltip when study description is not present");
		panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.NO_STUDY_AVAILABLE_TEXT, "Checkpoint[27.2/31]", "verifying the text for study when no study present");
		
		panel.mouseHover(panel.findingSummaryCount);
		panel.assertEquals(panel.getText(panel.findingSummaryCount),panel.thumbnailList.size()+ViewerPageConstants.FINDING_MULTIPLE_HEADER, "Checkpoint[28/31]", "Finding count verification");
				
		panel.assertTrue(!panel.thumbnailList.isEmpty(), "Checkpoint[29/31]", "Verifying on resize the OP is opened");
		
		panel.assertTrue(panel.isElementPresent(panel.scrollbar), "Checkpoint[30/31]", "Verifying the scrollbar is displayed");
		panel.assertTrue(panel.isElementPresent(panel.scrollThumb), "Checkpoint[31/31]", "Verifying the scroll thumb is displayed");

		panel.openAndCloseOutputPanel(false);
	
	}
		
	@Test(groups = { "Chrome", "IE11", "Edge", "US2284", "Positive","F1125" })
	public void test02_US2284_TC9754_TC9759_TC9785_verifyUIThumbnail()	throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify UI of the thumbnail in Output Panel."
				+ "<br> Verify the options available to filter the findings on Output Panel."
				+ "<br> Verify the new Output Panel in dark theme.");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointMultiSeries, 1);
		
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(2);
				
		panel.enableFiltersInOutputPanel(false, false, true);

		for(int i=0;i<5;i++) {
			
			int height = panel.convertIntoInt(panel.getAttributeValue(panel.getThumbnailImage(i+1),NSGenericConstants.HEIGHT));
			int width = panel.convertIntoInt(panel.getAttributeValue(panel.getThumbnailImage(i+1),NSGenericConstants.WIDTH));
			
			panel.assertTrue(height>1,"Checkpoint[1/22]","Thumbnail should display the finding image.");
			panel.assertTrue(width>1,"Checkpoint[2/22]","Thumbnail should display the finding image.");
			
			int x = panel.getXCoordinate(panel.thumbnailList.get(i));
			int y = panel.getYCoordinate(panel.thumbnailList.get(i));
			
			panel.assertEquals(panel.getXCoordinate(panel.getAnnotationFromThumbnail(i+1))-x, panel.getYCoordinate(panel.getAnnotationFromThumbnail(i+1))-y,"Checkpoint[3/22]","Verifying that annoation is at center");
			
			panel.assertEquals(panel.getHeightOfWebElement(panel.thumbnailList.get(i)),height,"Checkpoint[4/22]","Verifying thumbnail has same height");
			panel.assertEquals(panel.getWidthOfWebElement(panel.thumbnailList.get(i)),width,"Checkpoint[5/22]","Verifying thumbnail has same width");
			
			panel.assertTrue(panel.getXCoordinate(panel.getThumbnailCheckbox(i+1))<(x+width/2),"Checkpoint[6/22]","Verifying that checkbox is top left aligned");
			panel.assertTrue(panel.getYCoordinate(panel.getThumbnailCheckbox(i+1))<(y+height/2),"Checkpoint[7/22]","Verifying that checkbox is top left aligned");
			
			panel.assertEquals(panel.getXCoordinate(panel.getThumbnailCheckbox(i+1))-x, panel.getYCoordinate(panel.getThumbnailCheckbox(i+1))-y,"Checkpoint[8/22]","Verifying that checkbox is top left aligned");
			
			panel.assertFalse(panel.isElementPresent(panel.findingToolbar), "Checkpoint[9/22]", "Verifying the finding tile container is hidden by default");
			panel.mouseHover(panel.findingTileContainers.get(i));
			panel.assertTrue(panel.isElementPresent(panel.toolbar.get(i)), "Checkpoint[10/22]", "Verifying the finding tile container is displayed on mousehover");

			panel.assertTrue(panel.isElementPresent(panel.stateIndictorTiles.get(i)), "Checkpoint[11/22]", "Verifying the finding state icon is displayed on mousehover");
			panel.mouseHover(panel.stateIndictorTiles.get(i));
			panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.PENDING_TEXT,"Checkpoint[12/22]","Verifying the state on tooltip");
			
			panel.assertTrue(panel.isElementPresent(panel.commentIconOnTiles.get(i)), "Checkpoint[13/22]", "Verifying the comment icon is displayed on mousehover");
			panel.mouseHover(panel.commentIconOnTiles.get(i));
			panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.ADD_COMMENT_TEXT,"Checkpoint[14/22]","Verifying the add comment tooltip");
			
			panel.assertTrue(panel.isElementPresent(panel.sendToPacsIcons.get(i)), "Checkpoint[15/22]", "Verifying the send to pacs icon is displayed on mousehover");
			panel.mouseHover(panel.sendToPacsIcons.get(i));
			panel.assertEquals(panel.getText(panel.tooltip),ViewerPageConstants.NOT_SENT_TO_PACS_TEXT,"Checkpoint[16/22]","Verifying the sent to Pacs tooltip");

			panel.click(panel.studySummary);
			
		}
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the finding state icon");
		
		helper.loadViewerDirectly(liver9PatientName, 1);
		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		
		circle.drawCircle(1, -70, -70, -30, -30);
		circle.drawCircle(1, 70, -70, 30, 30);		
		circle.drawCircle(1, -70, 70, 30, 30);
		
		circle.selectAcceptfromGSPSRadialMenu();
		circle.selectRejectfromGSPSRadialMenu();
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[17/22]", "verifying the accepting icon color");
		panel.assertTrue(panel.isElementPresent(panel.toolbar.get(0)), "Checkpoint[18/22]", "Verifying the finding tile container is displayed on mousehover");
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[19/22]", "verifying the accepting icon color");
		panel.assertTrue(panel.isElementPresent(panel.toolbar.get(0)), "Checkpoint[20/22]", "Verifying the finding tile container is displayed on mousehover");
				
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.verifyPendingStateInThumbnail(1),"Checkpoint[21/22]", "verifying the accepting icon color");		
		panel.assertTrue(panel.isElementPresent(panel.toolbar.get(0)), "Checkpoint[22/22]", "Verifying the finding tile container is displayed on mousehover");
	
		panel.openAndCloseOutputPanel(false);
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US2284","F1125"})
	public void test03_US2284_TC9758_verifyNoCommentIconForNONDICOMAndSR() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the comment icon on Output Panel thumbnail tool bar for RT and non-dicom thumbnails.");

		// Loading the patient on viewer		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(iblPatientName, 1);
		
		panel=new OutputPanel(driver);
	    panel.enableFiltersInOutputPanel(false, false, true);
	    
	    for(int i =1;i<=panel.thumbnailList.size();i++) {
	    	panel.assertFalse(panel.verifyCommentIconPresence(i), "Checkpoint[1/2]","verify comment icon is absent- NON-");
	    	
	    }
	    panel.openAndCloseOutputPanel(false);
	    
	    helper.loadViewerDirectly(ChestCT1p25mm, 2);
	    
	    panel.click(panel.getViewPort(1));
	    panel.acceptResult(1);
	    panel.closeNotification();
	    
	    panel.enableFiltersInOutputPanel(true, false, false);
	    
	    for(int i =1;i<=panel.thumbnailList.size();i++) {
	    	panel.assertFalse(panel.verifyCommentIconPresence(i), "Checkpoint[2/2]","verify comment icon is absent");
	    	
	    }
	    panel.openAndCloseOutputPanel(false);
	    
	    
	}	

	//DR2522: Finding disappears when multiple filters are selected and unselected from output panel.
	@Test(groups = { "Chrome", "IE11", "Edge","Positive","DR2522","F262","E2E"})
	public void test04_DR2522_TC10561_verifyFindingsInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all findings are displayed when filters are selected and unselected multiple times in Output Panel.");

		// Loading the patient on viewer		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ah4PatientName, 1);
		
		CircleAnnotation circle=new CircleAnnotation(driver);
		PointAnnotation point=new PointAnnotation(driver);
		EllipseAnnotation ellipse=new EllipseAnnotation(driver);
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 30, 30, 30, 30);
		circle.selectAcceptfromGSPSRadialMenu();
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-100,-100);
		point.selectRejectfromGSPSRadialMenu();
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -30, -70, 40, 50);
		
		panel=new OutputPanel(driver);
	    panel.enableFiltersInOutputPanel(true, true, true);
	    
	    panel.assertEquals(panel.thumbnailList.size(),3,"Checkpoint[1/14]","Verify Accepted,rejected and Pending findings when all filters are selected.");
	    panel.assertTrue(panel.verifyEllipseIsPresentInThumbnail(1), "Checkpoint[2/14]", "Verified the ellipse annotation is accepted finding.");
	    panel.assertTrue(panel.verifyPointIsPresentInThumbnail(2), "Checkpoint[3/14]", "Verified that Point annotation is rejected finding.");
	    panel.assertTrue(panel.verifyCircleIsPresentInThumbnail(3), "Checkpoint[4/14]", "Verified that Circle annotation is pending finding");
	    	
	    panel.enableFiltersInOutputPanel(false, false, true);
	    panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[5/14]","Verify Pending findings when only Pending findings filter is selected.");
	    panel.assertTrue(panel.verifyCircleIsPresentInThumbnail(1), "Checkpoint[6/14]", "Verified that Circle annotation is visible under Pending filter.");
	
	    panel.enableFiltersInOutputPanel(true, false, true);
	    panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[7/14]","Verify Accepted,Pending findings when rejected filter is deselected.");
	    panel.assertTrue(panel.verifyEllipseIsPresentInThumbnail(1), "Checkpoint[8/14]", "Verified that Ellipse annotation is visible under Accepted filter.");
	    panel.assertTrue(panel.verifyCircleIsPresentInThumbnail(2), "Checkpoint[9/14]", "Verified that Circle annotation is visible under Pending filter.");
	    
	    panel.enableFiltersInOutputPanel(false, true, false);
	    panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[10/14]","Verify Rejected findings when only Rejected findings filter is selected.");
	    panel.assertTrue(panel.verifyPointIsPresentInThumbnail(1), "Checkpoint[11/14]", "Verified that Point annotation is visible under Rejected filter.");
	    
	    panel.enableFiltersInOutputPanel(false, true, true);
	    panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[12/14]","Verify Rejected,Pending findings when Accepted filter is deselected.");
	    panel.assertTrue(panel.verifyPointIsPresentInThumbnail(1), "Checkpoint[13/14]","Verified that Point annotation is visible under rejected filter.");
	    panel.assertTrue(panel.verifyCircleIsPresentInThumbnail(2), "Checkpoint[14/14]","Verified that Circle annotation is visible under Pending filter.");
	    
	}	

}