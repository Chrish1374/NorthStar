//import java.awt.AWTException;
//import java.util.HashMap;
//import java.util.List;
//
//import org.testng.annotations.Test;
//
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.RegisterUserPage;
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerLayout;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;

//package com.trn.ns.test.obsolete;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.RegisterUserPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class ViewerOutputPanelTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private SinglePatientStudyPage spStudyListPage;
//	private ViewerPage viewerPage;
//	String flag = "false";
//	DatabaseMethods db= new DatabaseMethods(driver);
//	
//	// Get Patient Name
//	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName1 = DataReader.getPatientDetails(
//			PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, AH4_Filepath);
//	private CircleAnnotation circle;
//	private EllipseAnnotation ellipse;
//	private MeasurementWithUnit lineWithUnit;
//	private RegisterUserPage register;
//
//	String username_1 = "user_1";
//	String username_2 = "user_2";
//	private OutputPanel panel;
//	
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),
//				Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US827", "dbConfig", "Positive" })
//	public void test03_US827_TC3389_VerifyOutputPanelFunctionalityWhenDevFlagIsFalse()
//			throws InterruptedException, SQLException, IOException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the output panel functionality when dev flag is false");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,
//				"", "Loading the Patient " + patientName1 + "in viewer");
//		//Dev Flag is "false" in cofig table Data base
//		db.updateDevMode(flag);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3389_1][1/2]","Verifying the Dev Mode is false");
//		db.assertFalse(db.getDevMode(),"Verifying the Dev Mode is false","Verified");
//		db.resetIISPostDBChanges();
//		
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName1);
//
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		OutputPanel panel = new OutputPanel(driver);
//		
//		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon);
//		viewerPage.mouseHover(viewerPage.getViewPort(2));
//		viewerPage.waitForViewerpageToLoad();
//		//verify Output panel button should not appear on Mouse hover on viewer page when the dev flag is false in data base
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3389_1][2/2]","Verify  Output panel button should not appear on Mouse hover on viewer page when the dev flag is false in data base");
//		viewerPage.assertFalse(viewerPage.isElementPresent(panel.outputPanelDiv), "Verify  Output panel button should not appear on Mouse hover on viewer page when the dev flag is false in data base", "Verified");
//		}
//
//	
//@Test(groups = { "Chrome", "IE11", "Edge", "US827", "Positive" })
//	public void test01_US827_TC3351_TC3352_TC3353_TC3354_TC3355_TC3398_DE1111_TC4692_OutputPanelTest()
//			throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the display of output panel"+"<br> Verify the display of filter buttons"+"<br>Verify that the radial menu should not open on output panel"
//				+"<br>Verify Output panel should get close on mouse click outside the panel"
//				+"<br>Verify On mouse hover, button will display in the middle of left edge of output paneland on clicking that button, output panel gets close"
//				+ "<br> Output Panel tooltip is not capitalized- happy path");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName1);
//
//		patientListPage.clickOntheFirstStudy();
//
//		String expected = "viewbox", actual;
//
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByOneLayoutIcon);
//		panel.mouseHover(panel.getViewPort(2));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3351_2][1/8]","Verify Output panel should get open on mouse click outpanel button");
//		panel.assertTrue(panel.isElementPresent(panel.outputPanelSection),	"Verify Output panel should get open on mouse click outpanel button", "Verified");
//		panel.mouseHover(panel.getViewPort(1));
//		panel.waitForAllChangesToLoad();
//		actual = panel.getAttributeValue(panel.getViewPort(1),"class");
//		panel.waitForAllChangesToLoad();
//		panel.assertEquals(actual,	"viewbox active", "Verify viewer port 1 is now active",	"verified");
//		panel.mouseHover(panel.getViewPort(2));
//		actual = panel.getAttributeValue(panel.getViewPort(2),"class");
//		// Output panel should get open over the existing layout.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3352_1][2/8]","Verify Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer");
//		panel.assertEquals(actual,	expected, "Verify Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer",	"verified");
//
//		// Output panel should not cover header.
//		panel.click(panel.EurekaLogo);
//		panel.waitForAllChangesToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3352_2][3/8]",	"Verify Output panel should not cover header");
//		panel.assertFalse(panel.isElementPresent(panel.outputPanelSection), "Verify Output panel should not cover header", "Verified");
//
//		panel.openAndCloseOutputPanel(false);
//		panel.openAndCloseOutputPanel(true);
//		panel.waitForOutputPanelToLoad();
//
//
////		// On the top of output panel, filter buttons should display with below
////		// names Accepted / Rejected / Pending​
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3355_2][4/8]","Verify for presence of Accepted, Rejected and  Pending button");
////		panel.assertTrue(panel.checkForTextPresenceInElement(panel.acceptedButton, "Accepted"), "Verify for presence of Accepted button", "Verified");
////		panel.assertTrue(panel.checkForTextPresenceInElement(panel.rejectedButton, "Rejected"), "Verify for presence of Rejected button", "Verified");
////		panel.assertTrue(panel.checkForTextPresenceInElement(panel.pendingButton, "Pending"), "Verify for presence of Pending button", "Verified");
////
////		// Radial menu should not open on output panel
////		panel.performMouseRightClick(panel.outputPanelSection);
////		panel.waitForAllChangesToLoad();
//	//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3354_2][5/8]","Verify that the radial menu should not open on output panel");
//		//panel.assertFalse(panel.verifyPresenceOfContextMenu(2, panel.outputPanel,	panel.contextMenu),"Verify that the radial menu should not open on output panel", "Verified");
//		// Output panel should get close. Any mouse click outside the panel,
//		// will close the output panel
//		panel.click(panel.EurekaLogo);
//		panel.waitForAllChangesToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3353_2][6/8]","Verify Output panel should get close on mouse click outside the panel");
//		panel.assertFalse(panel.isElementPresent(panel.outputPanelSection), "Verify Output panel should get close on mouse click outside the panel",	"Verified");
//		panel.openAndCloseOutputPanel(false);
//		panel.openAndCloseOutputPanel(true);
//		panel.waitForOutputPanelToLoad();
//
//		panel.waitForAllChangesToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify Output panel should get open on mouse click outpanel button");
//		panel.assertTrue(panel.isElementPresent(panel.outputPanelSection),	"Verify Output panel should get open on mouse click outpanel button", "Verified");
//
//		// On mouse hover, button will display in the middle of left edge of
//		// output panel.On clicking that button, output panel gets close
//		panel.openAndCloseOutputPanel(false);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3353_3][8/8]","Verify On mouse hover, button will display in the middle of left edge of output paneland on clicking that button, output panel gets close");
//		panel.assertFalse(panel.isElementPresent(panel.outputPanelSection),	"Verify On mouse hover, button will display in the middle of left edge of output paneland on clicking that button, output panel gets close", "Verified");
//
//	}
//@Test(groups = { "Chrome", "IE11", "Edge", "US827", "Positive" })
//public void test02_US827_TC3380_VerifyNoChangeInOutputPanelAsViewerLayoutChanges()
//		throws InterruptedException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify the vierwer layout change but Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer");
//
//	// Loading the patient on viewer
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//	patientListPage = new PatientListPage(driver);
//	patientListPage.clickOnPatientRow(patientName1);
//
//	patientListPage.clickOntheFirstStudy();
//	
//	OutputPanel panel = new OutputPanel(driver);
//	panel.waitForViewerpageToLoad();
//	String expected = "viewbox active", actual;
//
//	layout=new ViewerLayout(driver);
//	layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
//	panel.mouseHover(panel.getViewPort(2));
//	panel.openAndCloseOutputPanel(true);
//	panel.waitForOutputPanelToLoad();
//	panel.assertTrue(panel.isElementPresent(panel.outputPanelSection), "Verify Output panel should get open", "Verified"); 
//	panel.mouseHover(panel.getViewPort(1));
//	panel.waitForAllChangesToLoad();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3380_3][1/2]","Verify viewer port 1 is active");
//	actual = panel.getAttributeValue(panel.getViewPort(1), PatientPageConstants.CLASS.replace(":", ""));
//	panel.assertEquals(actual,	expected, "Verify viewer port 1 is active",	"verified");
//	panel.mouseHover(panel.getViewPort(2));
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3380_3][1/2]","Verify Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer");
//	actual = panel.getAttributeValue(panel.getViewPort(2), PatientPageConstants.CLASS.replace(":", ""));
//	panel.assertNotEquals(actual,	expected, "Verify Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer",	"verified");
//	panel.mouseHover(panel.getViewPort(3));
//	actual = panel.getAttributeValue(panel.getViewPort(3), PatientPageConstants.CLASS.replace(":", ""));
//	panel.assertNotEquals(actual,	expected, "Verify Output panel should get open over the existing layout, With width equal to the half width of viewer and height should be same as viewer",	"verified");
//	panel.openAndCloseOutputPanel(false);
//
//}
//
//@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
//public void test13_US891_TC3761_verifyOPWhenUsersCopyIsLoadedAndLatestVersionIsPresent()  throws InterruptedException, AWTException {
//
//	extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("	Verify the list of findings to be displayed when one machine result user A copy is implicitly loaded and has latest version of copy in chain as well and another machine result and User C copy.");
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");
//
//	patientListPage = new PatientListPage(driver);
//	patientListPage.clickOnPatientRow(patientName3);
//
//	patientListPage.clickOntheFirstStudy();
//
//	viewerPage = new ViewerPage(driver);
//	viewerPage.waitForViewerpageToLoad();
//	contentSelector = new ContentSelector(driver);
//	ellipse = new EllipseAnnotation(driver);		
//	panel = new OutputPanel(driver);
//	TextAnnotation text = new TextAnnotation(driver);
//	List<String> results = contentSelector.getAllResults();
//	contentSelector.selectResultFromSeriesTab(1, results.get(0));
//	text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	
//
//	HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
//
//	List<String> source = contentSelector.getAllSeries();
//	results = contentSelector.getAllResults();
//
//	contentSelector.selectResultFromSeriesTab(1, results.get(1));
//	HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);
//
//	viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//	register = new RegisterUserPage(driver);		
//	register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
//	register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
//	register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);
//
//	Header header = new Header(driver);		
//	header.logout();
//	loginPage.login(username_1, username_1);
//
//	patientListPage.waitForPatientPageToLoad();		
//	patientListPage.clickOnPatientRow(patientName3);
//
//	patientListPage.clickOntheFirstStudy();
//	viewerPage.waitForViewerpageToLoad();
//
//	contentSelector.selectResultFromSeriesTab(1, results.get(1));
//
//	ellipse.selectEllipseFromQuickToolbar(1);
//	ellipse.drawEllipse(1, 150, -50, 40, -50);
//
//	contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
//
//	panel.click(panel.getViewPort(1));
//	panel.enableFiltersInOutputPanel(true, false, false);
//
//	//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
//	viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1]", "Verifying the total findings list");
//	for(int i =0;i<panel.thumbnailList.size();i++) {
//		viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_1, "Checkpoint[3]", "verifying the created by field");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameList.get(i)),ViewerPageConstants.FINDING_NAME+": "+ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[4]", "verifying the findings name");
//	}
//
//	panel.openAndCloseOutputPanel(false);
//	header.logout();
//	loginPage.login(username_2,username_2);		
//
//	patientListPage.waitForPatientPageToLoad();		
//	patientListPage.clickOnPatientRow(patientName3);
//
//	patientListPage.clickOntheFirstStudy();
//	viewerPage.waitForViewerpageToLoad();
//
//	contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
//
//	ellipse.selectEllipseFromQuickToolbar(1);
//	ellipse.drawEllipse(1, 200, -100, 40, -50);
//
//
//	panel.enableFiltersInOutputPanel(true, false, false);
//
//	//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
//	viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[5]", "Verifying the total findings list");
//	for(int i =0;i<panel.thumbnailList.size();i++) {
//		viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_2, "Checkpoint[7]", "verifying the created by field");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameList.get(i)),ViewerPageConstants.FINDING_NAME+": "+"Ellipse_"+(panel.thumbnailList.size()-i), "Checkpoint[8]", "verifying the findings name");
//	}
//
//	panel.enableFiltersInOutputPanel( false, false,true);
//
//	viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[9]", "Verifying the total findings list");
//	for(int i=0,j=0;i<panel.thumbnailList.size();i++) {
//
//		if(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j))) {
//			viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+findingsListVb1.get(findingsNameVb1.get(j)), "Checkpoint[7/20]", "machine result1 is same not changed");
//			viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j)), "Checkpoint[8/20]", "machine result 1 name is same");
//		}}
//
//	for(int i=0;i<findingsNameVb2.size();i++) {
//		for(int j=0 ; j<panel.thumbnailList.size();j++)	
//			if(panel.getText(panel.findingsNameList.get(j)).replaceAll(":","").trim().equals(findingsNameVb2.get(i))) {
//				viewerPage.assertEquals(panel.createdByUserList.get(j).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_2, "Checkpoint[9/20]", "verifying that machine result 2 is changed by userA");
//				viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(j)).contains(findingsNameVb2.get(i)), "Checkpoint[10/20]", "verifying the machine result 2 name");
//			}}		
//
//	panel.openAndCloseOutputPanel(false);
//	header.logout();
//	loginPage.login(username_3,username_3);		
//
//	patientListPage.waitForPatientPageToLoad();		
//	patientListPage.clickOnPatientRow(patientName3);
//
//	patientListPage.clickOntheFirstStudy();
//	viewerPage.waitForViewerpageToLoad();
//
//	contentSelector.selectSeriesFromSeriesTab(1, source.get(0));
//
//	ellipse.selectEllipseFromQuickToolbar(1);
//	ellipse.drawEllipse(1, 200, -100, 40, -50);
//
//
//	contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
//
//	panel.enableFiltersInOutputPanel(true, false, false);
//
//	//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
//	viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[15.a]", "Verifying the total findings list");
//	for(int i =0,j=1;i<panel.thumbnailList.size();i++) {
//
//		if(panel.createdByUserList.get(i).getText().equals(ViewerPageConstants.CREATED_BY_TEXT+" "+username_3)) {
//			viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_3, "Checkpoint[15]", "verifying the created by field");
//			viewerPage.assertEquals(panel.getText(panel.findingsNameList.get(i)),ViewerPageConstants.FINDING_NAME+": "+ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[16]", "verifying the finding name");
//		}else {
//
//			viewerPage.scrollIntoView(panel.findingsNameList.get(i));	
//			viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_1, "Checkpoint[17]", "verifying the created by field for findings created by "+username_2);
//			viewerPage.assertEquals(panel.getText(panel.findingsNameList.get(i)),ViewerPageConstants.FINDING_NAME+": "+"Ellipse_"+(panel.findingsNameList.size()-i), "Checkpoint[18]", "Verifying the finding name");
//			j++;
//
//		}}
//
//	panel.enableFiltersInOutputPanel( false, false,true);
//
//
//	viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[20]", "Verifying the total findings list");
//	for(int i=0,j=0;i<panel.thumbnailList.size();i++) {
//
//		if(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j))) {
//			viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+findingsListVb1.get(findingsNameVb1.get(j)), "Checkpoint[7/20]", "machine result1 is same not changed");
//			viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j)), "Checkpoint[8/20]", "machine result 1 name is same");
//		}}
//
//	for(int i=0;i<findingsNameVb2.size();i++) {
//		for(int j=0 ; j<panel.thumbnailList.size();j++)	
//			if(panel.getText(panel.findingsNameList.get(j)).replaceAll(":","").trim().equals(findingsNameVb2.get(i))) {
//				viewerPage.assertEquals(panel.createdByUserList.get(j).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+username_1, "Checkpoint[9/20]", "verifying that machine result 2 is changed by userA");
//				viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(j)).contains(findingsNameVb2.get(i)), "Checkpoint[10/20]", "verifying the machine result 2 name");
//			}}		
//
//	panel.openAndCloseOutputPanel(false);
//
//
//}

//	
//
//}