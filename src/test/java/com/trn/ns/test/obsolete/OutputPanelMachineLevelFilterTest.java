//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.commons.collections.ListUtils;
//import org.apache.commons.collections4.MultiValuedMap;
//import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSDBDatabaseConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DICOMRT;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.RegisterUserPage;
//import com.trn.ns.page.factory.ViewerLayout;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.page.factory.ViewerSendToPACS;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class OutputPanelMachineLevelFilterTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private ViewerPage viewerPage;
//	private ContentSelector cs;
//	private RegisterUserPage register;
//	private EllipseAnnotation ellipse;
//	private OutputPanel panel;
//	private CircleAnnotation circle;
//	private DatabaseMethods db;
//	private DICOMRT drt;
//	private HelperClass helper;
//	private ViewerLayout layout;
//	private ViewerSendToPACS sd;
//
//	private static int seriesLevelID;
//
//	// Get Patient Name
//	String liver9FilePath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liverPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);
//	
//	String filePath1 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1); 
//
//	String filePath2 =Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneage_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
//
//	String filePath3 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
//	String IHEMammoTest_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
//	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath3);
//
//	String filePath4 = Configurations.TEST_PROPERTIES.get("Head_CT_filepath");
//	String CTHeadPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
//
//	String filePath5 = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbioTexturePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
//
//	String filePath6 = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
//	String dxResultPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
//
//	String filePath7 = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
//	String johnDoePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);
//	String seriesName=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath7);
//
//
//	String filePath8 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
//	String chestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);
//
//	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
//	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
//
//	
//	String userName=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
//	String userA="AscanUser";
//	String userZ="ZscanUser";
//	String longUserName="UserCreatedResults";
//	private PointAnnotation point;
//	private ContentSelector contentSelector;
//	private MeasurementWithUnit lineWithUnit;
//	private DICOMRT rt;
//	public static final String PRESENCE = "presence";
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException, SQLException {
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(userName, password);
//		DatabaseMethods db = new DatabaseMethods(driver);
//		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);
//	
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1160","DE1653","BVT"})
//	public void test01_US1160_TC5810_TC5811_DE1653_TC6657_verifyFilterWhenOnlyOneMachineOrOnlyOneUser() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify no filter is displayed in output panel when there is only one machine generated findings  present. <br>"
//				+ "Verify no filter is displayed in output panel when there is only one user generated findings  present");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(GSPS_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+GSPS_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertFalse(panel.isElementPresent(panel.machineOrUserFilterName), "Checkpoint[1/2]", "Verified that no machine filter visible on OP when there is only one machine");
//
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(liverPatientName, 1, 1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page." );
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 10, 10, 50, 50);
//		circle.closingBannerAndWaterMark();
//
//		circle.selectCircleFromQuickToolbar(2);
//		circle.drawCircle(1, 10, 10, 50, 50);
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertFalse(panel.isElementPresent(panel.machineOrUserFilterName), "Checkpoint[2/2]", "Verified that no user filter visible on OP when there is only one user");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1160","DE1653","BVT"})
//	public void test02_US1160_TC5812_TC5814_DE1653_TC6656_verifyMachineAndUserFiltersWhenFindingsFromMultipleResultAndUser() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify machine filters are displayed in output panel when  there are findings from multiple results present.<br>"
//				+"Verify machine filters + user filters are displayed in output panel when  there are findings from multiple machines and users present.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		cs=new ContentSelector(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page with first user" );
//		String seriesname=viewerPage.getSeriesDescriptionOverlayText(4);
//		cs.openAndCloseSeriesTab(true);
//		String machineOne=cs.getText(cs.allMachineName.get(0));
//		String machineSecond=cs.getText(cs.allMachineName.get(1));
//		cs.openAndCloseSeriesTab(false);
//
//		circle.selectCircleFromQuickToolbar(4);
//		circle.drawCircle(4, 10, 10, 50, 50);
//
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertTrue(panel.isElementPresent(panel.allMachineFilterNameList.get(0)), "Checkpoint[1/]", "");
//
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(0)), machineSecond, "Checkpoint[1/6]", "Verified first machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(1)), machineOne, "Checkpoint[2/6]", "Verified second machine filter in Output panel");
//
//		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//	
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//		patientListPage.clickOntheFirstStudy();	
//		viewerPage.waitForViewerpageToLoad(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page with second user" );
//
//		cs.selectSeriesFromSeriesTab(1, seriesname);
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1, 0, 0, -100,-150);
//
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.assertEquals(panel.getText(panel.allMachineFilterNameList.get(0)), machineSecond, "Checkpoint[3/6]", "Verified first machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineFilterNameList.get(1)), machineOne, "Checkpoint[4/6]", "Verified second machine filter in Output panel");
//
//		panel.assertEquals(panel.getText(panel.allUserFilterNameList.get(0)), userName, "Checkpoint[5/6]", "Verified first user filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allUserFilterNameList.get(1)), userZ, "Checkpoint[6/6]", "Verified second user filter in Output panel");
//
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1160"})
//	public void test03_US1160_TC5813_verifyUserFiltersWhenFindingsFromMultipleUsersPresent() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify user filters are displayed in output panel when  there are findings from multiple users present.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(liverPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//		loginPage=new LoginPage(driver);
//
//		cs=new ContentSelector(driver);
//		ellipse=new EllipseAnnotation(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page." );
//		String seriesName=viewerPage.getSeriesDescriptionOverlayText(1);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 10, 10, 50, 50);
//
//		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();	
//		viewerPage.waitForViewerpageToLoad();
//		cs.selectSeriesFromSeriesTab(1,seriesName);
//
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1, 0, 0, -100,-150);
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		viewerPage.assertTrue(panel.isElementPresent(panel.machineOrUserFilterName), "Checkpoint[1/3]", "Verified display of user filter in Output panel.");
//
//		viewerPage.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(0)), userA, "Checkpoint[2/3]", "Verified name of first user filter in Output panel");
//		viewerPage.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(1)), userName, "Checkpoint[3/3]","Verified name of second user filter in Output panel");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1160"})
//	public void test04_US1160_TC5817_verifyEllipsesAndTooltipForUserAndMachineFilter() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify ellipse(...) and tooltip are displayed when machine/user name is too long to fit on button.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(IHEMammoTest_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//		loginPage=new LoginPage(driver);
//
//		cs=new ContentSelector(driver);
//		ellipse=new EllipseAnnotation(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+IHEMammoTest_PatientName+ " on viewer page." );
//
//		String seriesName=viewerPage.getSeriesDescriptionOverlayText(1);
//		cs.openAndCloseSeriesTab(true);
//		String machineName=viewerPage.getText(cs.allMachineName.get(0));
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 10, 10, 50, 50);
//
//		panel.enableFiltersInOutputPanel(true, true, true);
//		viewerPage.assertEquals(viewerPage.getAttributeValue(panel.allMachineFilterNameList.get(0), NSGenericConstants.TITLE), machineName, "Checkpoint[1/4]", "Verified tooltip for machine filter in output panel");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying tool tip with ellipse when machine text is too long");
//		viewerPage.compareElementImage(protocolName, panel.allMachineFilterNameList.get(0), "Verify ellipses when machine text is too long", "test04_machineName");
//
//		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(longUserName, longUserName, LoginPageConstants.SUPPORT_EMAIL, longUserName, longUserName, longUserName);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//
//		loginPage.login(longUserName,longUserName);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(IHEMammoTest_PatientName);
//
//		patientListPage.clickOntheFirstStudy();	
//		viewerPage.waitForViewerpageToLoad();
//		cs.selectSeriesFromSeriesTab(1,seriesName);
//
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1, 0, 0, -100,-150);
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		viewerPage.assertEquals(viewerPage.getAttributeValue(panel.allUserFilterNameList.get(1), NSGenericConstants.TITLE), longUserName, "Checkpoint[3/4]", "Verified tooltip for machine filter in output panel" );
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying tool tip with ellipse when user text is too long");
//		viewerPage.compareElementImage(protocolName, panel.allUserFilterNameList.get(1), "Verify ellipses when machine text is too long", "test04_userName");
//
//	}
//
//	// US1171 - Filter output panel findings based on machine filter buttons
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test05_US1171_TC5818_TC5825_VerifyMachineFilterSelection() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verfiy that Machine/Users filter are highlighted default for aceepted filter on opening the output panel"
//				+ "<br> Verify the selection and deselection of machine result button in Output panel.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+GSPS_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/8]", "Verified that no machine filter visible on OP when there is only one machine");
//
//
//		for(int i =0 ;i< panel.allMachineOrUserFilterNameList.size();i++)
//			verifyButtonsSelection(panel.machinesFilters.get(i),"Checkpoint[2/8]","Verifying all the buttons are selected");
//
//		int findings = panel.thumbnailList.size();
//		panel.unselectMachineButton(1);
//
//		verifyButtonsDeSelection(panel.machinesFilters.get(0),"Checkpoint[3/8]","Verifying the first button is unselected");
//		verifyButtonsSelection(panel.machinesFilters.get(1),"Checkpoint[4/8]","Verifying that second button us selected");
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[5/8]", "Verifying findings are changed");
//
//		findings = panel.thumbnailList.size();
//		panel.unselectMachineButton(2);
//
//		verifyButtonsDeSelection(panel.machinesFilters.get(0),"Checkpoint[6/8]","Verifying upon deselection of last button nothing changes hence button first is deselected");
//		verifyButtonsSelection(panel.machinesFilters.get(1),"Checkpoint[7/8]","verifying button two is not de selected");
//		panel.assertEquals(panel.thumbnailList.size(), findings, "Checkpoint[8/8]", "verifying the findings are same");
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})	
//	public void test06_US1171_VerifyFilteredFindingsWhenAllAreSelected() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the results are getting filtered on selection of machine/users button when user have all the output panel filters enabled (ARP)");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(4);
//		point.drawPointAnnotationMarkerOnViewbox(4, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(4, -50, -60);
//
//        layout=new ViewerLayout(driver);
//        layout.selectLayout(layout.threeByThreeLayoutIcon);
//		panel.waitForRespectedViewboxToLoad(6);
//
//		panel.click(panel.getViewPort(5));
//		panel.checkBoneage(5);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, true, true);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/16]", "Verifying filter buttons are available");
//
//		for(int i =0 ;i< panel.machineFiltersList.size();i++) 
//			verifyButtonsSelection(panel.machinesFilters.get(i), "Checkpoint[2."+i+"/16]", "Verifying all the machine buttons are enabled");
//
//		for(int i =0 ;i< panel.usersFiltersList.size();i++) 
//			verifyButtonsSelection(panel.usersFiltersList.get(i), "Checkpoint[3."+i+"/16]", "Verifying all the users buttons are enabled");
//
//
//		findings = panel.thumbnailList.size();
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[4/16]", "Verifying the findings are changed");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[5."+i+"/16]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[6."+i+"/16]","verifying that diabled machine findings are not present");
//
//		}
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, true, true);
//
//		findings = panel.thumbnailList.size();
//		disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//
//
//		panel.unselectMachineButton(1);
//		panel.unselectUsersButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[7/16]","");
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[8."+i+"/16]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[9."+i+"/16]","verifying that diabled machine findings are not present");
//			panel.assertFalse(panel.getText(panel.createdByUserList.get(i)).equals(disabledUsers),"Checkpoint[10."+i+"/16]","Verifying by created by field doesn't have disbaled user info");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledUsers),"Checkpoint[11."+i+"/16]","Verifying by machine name in header doesn't have disbaled user info");
//		}
//
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, true, true);
//
//		findings = panel.thumbnailList.size();
//		disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledMachine1 =panel.getText(panel.machineFiltersList.get(1));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.unselectMachineButton(2);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[12/16]", "Verifying the findings are changed");
//
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[13."+i+"/16]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[14."+i+"/16]","verifying that diabled machine findings are not present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine1),"Checkpoint[15."+i+"/16]","Verifying diabled machine fidnings are not present");
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[16."+i+"/16]","Verifying the created by field has enabled user button info");
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[16."+i+"/16]","Verifying the created by field has enabled user button info");
//
//		}
//
//		panel.openAndCloseOutputPanel(false);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test07_US1171_TC5862_TC5865_VerifyFindingsAreFilteredWhenPendingIsSelected() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Pending is selected and default all machine filter/User filters are selected."
//				+ "<br> Verfiy that Machine filter are highlighted for Pending filter and filtered findings are getting displayed.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/6]", "Verify machines filters are available");
//
//		for(int i =0 ;i< panel.machineFiltersList.size();i++) 
//			verifyButtonsSelection(panel.machinesFilters.get(i), "Checkpoint[2."+i+"/6]", "Verifying all the machine buttons are enabled");
//
//
//		for(int i =0 ;i< panel.usersFiltersList.size();i++) 
//			verifyButtonsSelection(panel.usersFiltersList.get(i), "Checkpoint[3."+i+"/6]", "Verifying all the users buttons are enabled");
//
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[4/6]", "Verifying the thumbnails are changed upon disabling of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[5."+i+"/6]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[6."+i+"/6]","verifying that diabled machine findings are not present");
//
//		}
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test08_US1171_TC5866_VerifyFilteredFindingWhenPendingIsSelectedwith1MachineAnd1Userfilter() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Pending is selected and only 1 machine filter+1 user filter is  selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(CTHeadPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(2);
//		point.drawPointAnnotationMarkerOnViewbox(2, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(2, -50, -60);		
//
//		point.selectAcceptfromGSPSRadialMenu(2);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(CTHeadPatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//		point.selectAcceptfromGSPSRadialMenu(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+CTHeadPatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/10]", "Verified machine buttons are present");
//
//		findings = panel.thumbnailList.size();
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		panel.unselectUsersButton(1);		
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[2/10]", "Verifying the findings are changed post deselection of first user button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[3."+i+"/10]","Verifying another machine findings are available");
//			panel.assertFalse(panel.getText(panel.createdByUserList.get(i)).equals(disabledUsers),"Checkpoint[4."+i+"/10]","Verifying that findings are not diabled users button");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledUsers),"Checkpoint[5."+i+"/10]","verifying that findings machine name is not same as disbaled user button ");
//		}
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[6/10]", "Verified machine buttons are present");
//
//		findings = panel.thumbnailList.size();
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[7/10]", "verifying the findings are changed after unselecting the machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[8."+i+"/10]","Verifying that this findings is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[9."+i+"/10]","Verifying the machine name in header doesn't contain disbaled machine");			
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers);
//		}
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test09_US1171_TC5868_VerifyFilteredFindingWhenPendingIsSelectedwithAllUsersfilterNoMachine() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Pending is selected and all users filters are selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(CTHeadPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(2);
//		point.drawPointAnnotationMarkerOnViewbox(2, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(2, -50, -60);
//		point.selectAcceptfromGSPSRadialMenu(2);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(CTHeadPatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//		point.selectAcceptfromGSPSRadialMenu(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+CTHeadPatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);
//
//		findings = panel.thumbnailList.size();
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[1/4]", "verifying the findings are changed upon deselection of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[2/4]","Verifying that this finding is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[3/4]","Verifying there is no findings present for disabled machine");
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers);
//
//		}
//
//		panel.openAndCloseOutputPanel(false);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test10_US1171_TC5869_TC5870_VerifyFindingsAreFilteredWhenRejectIsSelected() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Rejected is selected and default all machine filter/User filters are selected."
//				+ "<br> Verfiy that Machine filter are highlighted for Rejected filter and filtered findings are getting displayed.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true, false);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/6]", "Verify machines filters are available");
//
//		for(int i =0 ;i< panel.machineFiltersList.size();i++) 
//			verifyButtonsSelection(panel.machinesFilters.get(i), "Checkpoint[2."+i+"/6]", "Verifying all the machine buttons are enabled");
//
//
//		for(int i =0 ;i< panel.usersFiltersList.size();i++) 
//			verifyButtonsSelection(panel.usersFiltersList.get(i), "Checkpoint[3."+i+"/6]", "Verifying all the users buttons are enabled");
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[4/6]", "Verifying the thumbnails are changed upon disabling of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[5."+i+"/6]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[6."+i+"/6]","verifying that diabled machine findings are not present");
//
//		}
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test11_US1171_TC5871_VerifyFilteredFindingWhenRejectIsSelectedwith1MachineAnd1Userfilter() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Rejected is selected and only 1 machine filter+1 user filter is  selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		panel.openFindingTableOnBinarySelector(1);
//		panel.selectRejectfromGSPSRadialMenu(1);
//
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(3, -50, -60);		
//
//		point.selectRejectfromGSPSRadialMenu(3);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//		point.selectRejectfromGSPSRadialMenu(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+imbioTexturePatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true,false);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/10]", "Verified machine buttons are present");
//
//		findings = panel.thumbnailList.size();
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		panel.unselectUsersButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[2/10]", "Verifying the findings are changed post deselection of first user button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[3."+i+"/10]","Verifying another machine findings are available");
//			panel.assertFalse(panel.getText(panel.createdByUserList.get(i)).equals(disabledUsers),"Checkpoint[4."+i+"/10]","Verifying that findings are not diabled users button");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledUsers),"Checkpoint[5."+i+"/10]","verifying that findings machine name is not same as disbaled user button ");
//		}
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true, false);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[6/10]", "Verifying there is machine buttons are available");
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[7/10]", "verifying the findings are changed after unselecting the machine button");
//
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[8."+i+"/10]","Verifying that this findings is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[9."+i+"/10]","Verifying the machine name in header doesn't contain disbaled machine");			
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers);
//		}
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test12_US1171_TC5874_VerifyFilteredFindingWhenRejectIsSelectedwithAllUsersfilterNoMachine() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Rejected is selected and all users filters are selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		panel.openFindingTableOnBinarySelector(1);
//		panel.selectRejectfromGSPSRadialMenu(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(3, -50, -60);
//		point.selectRejectfromGSPSRadialMenu(3);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//		point.selectRejectfromGSPSRadialMenu(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+imbioTexturePatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, true, false);
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[1/4]", "verifying the findings are changed upon deselection of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[2/4]","Verifying that this finding is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[3/4]","Verifying there is no findings present for disabled machine");
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers);
//
//		}
//
//		panel.openAndCloseOutputPanel(false);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test13_US1171_TC5837_VerifyFindingsAreFilteredWhenAcceptIsSelected() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Accepted is selected and default all machine filter/User filters are selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/6]", "Verify machines filters are available");
//
//		for(int i =0 ;i< panel.machineFiltersList.size();i++) 
//			verifyButtonsSelection(panel.machinesFilters.get(i), "Checkpoint[2."+i+"/6]", "Verifying all the machine buttons are enabled");
//
//
//		for(int i =0 ;i< panel.usersFiltersList.size();i++) 
//			verifyButtonsSelection(panel.usersFiltersList.get(i), "Checkpoint[3."+i+"/6]", "Verifying all the users buttons are enabled");
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[4/6]", "Verifying the thumbnails are changed upon disabling of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[5."+i+"/6]","Verifying the findings from another machine is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[6."+i+"/6]","verifying that diabled machine findings are not present");
//
//		}
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171","DE1858"})
//	public void test14_US1171_TC5860_DE1858_TC7463_VerifyFilteredFindingWhenAcceptIsSelectedwith1MachineAnd1Userfilter() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Accepted is selected and only 1 machine filter+1 user filter is  selected. <br>"
//				+"Verify that Output panel findings are getting filtered on selection and deselection of multiple users button[Happy Path]");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		panel.openFindingTableOnBinarySelector(1);
//		panel.selectAcceptfromGSPSRadialMenu(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(3, -50, -60);		
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+imbioTexturePatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);	
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[1/10]", "Verified machine buttons are present");
//
//		findings = panel.thumbnailList.size();
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		panel.unselectUsersButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[2/10]", "Verifying the findings are changed post deselection of first user button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[3."+i+"/10]","Verifying another machine findings are available");
//			panel.assertFalse(panel.getText(panel.createdByUserList.get(i)).equals(disabledUsers),"Checkpoint[4."+i+"/10]","Verifying that findings are not diabled users button");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledUsers),"Checkpoint[5."+i+"/10]","verifying that findings machine name is not same as disbaled user button ");
//		}
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertFalse(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[6/10]", "Verifying there is machine buttons are available");
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[7/10]", "verifying the findings are changed after unselecting the machine button");
//
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[8."+i+"/10]","Verifying that this findings is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[9."+i+"/10]","Verifying the machine name in header doesn't contain disbaled machine");			
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[10."+i+"/10]","verifying the findings are from user "+disabledUsers);
//		}
//
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test15_US1171_TC5861_VerifyFilteredFindingWhenAcceptIsSelectedwithAllUsersfilterNoMachine() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that findings are getting filtered when Accepted is selected and all users filters are selected.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//		register.createNewUser(userZ, userZ, LoginPageConstants.SUPPORT_EMAIL, userZ, userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//
//		panel.openFindingTableOnBinarySelector(1);
//		panel.selectAcceptfromGSPSRadialMenu(1);
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 50, 60);
//		point.drawPointAnnotationMarkerOnViewbox(3, -50, -60);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForRespectedViewboxToLoad(1);
//		cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(1, cs.getSeriesDescriptionOverlayText(1));
//
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 60, 50);
//		point.drawPointAnnotationMarkerOnViewbox(1, -60, -50);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+imbioTexturePatient+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		int findings = panel.thumbnailList.size();
//
//		MultiValuedMap<String, String> map = getMapwithFindingsNameAndMachine();
//
//		panel.openAndCloseOutputPanel(false);
//		panel.selectScrollFromQuickToolbar(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);
//
//		String disabledMachine = panel.getText(panel.machineFiltersList.get(0));
//		String disabledUsers = panel.getText(panel.usersFiltersList.get(0));
//		String disabledUsers1 = panel.getText(panel.usersFiltersList.get(1));
//
//		panel.unselectMachineButton(1);
//		panel.assertNotEquals(panel.thumbnailList.size(), findings, "Checkpoint[1/4]", "verifying the findings are changed upon deselection of machine button");
//
//		for(int i =0 ;i< panel.thumbnailList.size();i++) {	
//
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//
//			panel.assertTrue(map.get(title).contains(machine),"Checkpoint[2/4]","Verifying that this finding is present");
//			panel.assertFalse(panel.getText(panel.machineNameInHeader.get(i)).equals(disabledMachine),"Checkpoint[3/4]","Verifying there is no findings present for disabled machine");
//			if(panel.getText(panel.createdByUserList.get(i)).contains(disabledUsers1))
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers1,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers1);
//			else
//				panel.assertEquals(panel.getText(panel.createdByUserList.get(i)).replace(ViewerPageConstants.CREATED_BY_TEXT, "").trim(),disabledUsers,"Checkpoint[4/4]","verifying the findings are from user "+disabledUsers);
//
//		}
//
//		panel.openAndCloseOutputPanel(false);
//
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1171"})
//	public void test16_US1171_TC5914_VerifyFiltersOnBrowserResize() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that machine/Users button and Machine:/Users: text are getting resized properly on resizing the browser.:");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//
//		for(char i='a';i<='j' ;i++)
//			register.createNewUser(userA+i, userA+i, LoginPageConstants.SUPPORT_EMAIL, userA+i, userA+i, userA+i);
//
//		for(char i='a';i<='j' ;i++) {
//
//			loginPage.logout();
//			loginPage.navigateToBaseURL();
//			loginPage.login(userA+i,userA+i);
//			patientListPage.waitForPatientPageToLoad();
//			patientListPage.clickOnPatientRow(boneage_PatientName);
//
//			patientListPage.clickOntheFirstStudy();
//
//			panel = new OutputPanel(driver);
//			panel.waitForRespectedViewboxToLoad(1);		
//			cs = new ContentSelector(driver);
//			cs.selectSeriesFromSeriesTab(1, cs.getAllSeriesFromSeriesTab().get(0));
//
//			point = new PointAnnotation(driver);
//			point.selectPointFromQuickToolbar(1);
//			point.drawPointAnnotationMarkerOnViewbox(1, 50, 60);
//			point.drawPointAnnotationMarkerOnViewbox(1, -50, -60);
//
//		}
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel.enableFiltersInOutputPanel(true, true, true);	
//		//		panel.compareElementImage(protocolName, panel.entireOutputPanel, "Checkpoint[1/2] : verifying the outputpanel when not resized","test16_1");
//		Dimension dimension = new Dimension(800, 600);
//		String parentWindow = panel.getCurrentWindowID();
//		panel.setWindowSize(parentWindow,dimension);
//		panel.compareElementImage(protocolName, panel.entireOutputPanel, "Checkpoint[2/2] : verifying the outputpanel when resized","test16_2");
//		panel.maximizeWindow();
//
//	}
//
//
//	@AfterMethod(alwaysRun=true)
//	public void afterMethod() throws SQLException {
//
//		db = new DatabaseMethods(driver);
//
//		if(db.checkUserPresence(userA))
//			db.deleteUser(userA);
//		if(db.checkUserPresence(userZ))
//			db.deleteUser(userZ);
//		if(db.checkUserPresence(longUserName))
//			db.deleteUser(longUserName);
//
//		for(char i='a';i<='j' ;i++) {
//
//			if(db.checkUserPresence(userA+i))
//				db.deleteUser(userA+i);
//		}
//
//		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
//		db.deleteRTafterPerformingAnyOperation(userName);
//		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);
//		 db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+userName );
//	}
//
//	private void verifyButtonsSelection(WebElement filter,String checkpoint,String comment) {
//
//		panel.assertTrue(panel.getCssValue(filter, NSGenericConstants.CSS_PRO_BACKGROUND).contains(ViewerPageConstants.MACHINE_FILTER_BACKGROUND_W),checkpoint,comment);
//		panel.assertTrue(panel.getCssValue(filter, NSGenericConstants.CSS_PROP_COLOR).contains(ViewerPageConstants.BLACK_COLOR),checkpoint,comment);
//
//
//
//	}
//
//	private void verifyButtonsDeSelection(WebElement filter, String checkpoint,String comment) {
//
//		panel.assertTrue(panel.getCssValue(filter, NSGenericConstants.CSS_PRO_BACKGROUND).contains(ViewerPageConstants.BLACK_COLOR_RGB),checkpoint,comment);
//		panel.assertTrue(panel.getCssValue(filter, NSGenericConstants.CSS_PROP_COLOR).contains(ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_FONT_COLOR),checkpoint,comment);
//
//
//	}
//
//	private MultiValuedMap<String, String> getMapwithFindingsNameAndMachine(){
//
//
//		MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
//		for(int i =0;i<panel.thumbnailList.size();i++)
//		{
//			HashMap<String, String> info = panel.getAllInformationOfThumbnail(i+1);
//			String title = info.get(ViewerPageConstants.FINDING_NAME_TITLE);
//			String machine = info.get(ViewerPageConstants.EDITED_BY);
//			map.put(title,machine);
//		}
//
//		return map;
//
//	}
//
//
//	//US1161 - Output panel - Implement machine level accept/reject
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161","BVT"})
//	public void test17_US1161_TC5961_TC5979_VerifyAcceptAllFunctionalityForPendingFindings() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Accept All machine button is working when findings are in pending state."
//				+ "<br> Verify that Reject All machine button is working when findings are in accepted state.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the rejected findings" );
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedfindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting the pending findings and validating it" );
//		panel.enableFiltersInOutputPanel(false, false, true);		
//		List<String> pendingFindings = panel.getAllFindingsName();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting all findings for machine "+ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.acceptAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameList.size(), "Checkpoiny[1/8]", "Validating that upon accepting all the findings , no more findings available under pending tab");		
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoiny[2/8]", "Validating that upon accepting all the findings , no more findings available under pending tab");		
//
//		panel.enableFiltersInOutputPanel(true, false, true);
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(acceptedFindings.get(i), pendingFindings.get(i), "Checkpoiny[3."+i+"/8]", "Verifying the finding name with the finding name before accepted");
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "rejecting the accepted findings and validating it" );
//		panel.enableFiltersInOutputPanel(true, false, false);
//		acceptedFindings = panel.getAllFindingsName();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "rejecting the accepted findings and validating it" );
//		panel.assertEquals(panel.getCssValue(panel.getAcceptAllFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[4/8]","Verifying that accept all filter is disabled");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting all findings for machine "+ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.rejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameList.size(), "Checkpoiny[5/8]", "Validating that upon rejected all the findings , no more findings available under accept tab");		
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoiny[6/8]", "Validating that upon rejected all the findings , no more findings available under accept tab");		
//
//		panel.enableFiltersInOutputPanel(false, true, false);		
//		List<String> rejectedFinalFindings = panel.getAllFindingsName();
//		panel.assertEquals(panel.getCssValue(panel.getRejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[7/8]","Verifying that accept all filter is disabled");
//		for (int i =0 ; i <rejectedFinalFindings.size();i++)
//			panel.assertEquals(rejectedFinalFindings.get(i), ListUtils.union(acceptedFindings, rejectedfindings).get(i).toString(), "Checkpoiny[8."+i+"/8]", "Verifying the finding name with the finding name before rejecting");
//		panel.openAndCloseOutputPanel(false);		
//
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161","BVT"})
//	public void test18_US1161_TC5962_TC5983_VerifyRejectAllFunctionalityForPendingFindings() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Reject All machine button is working when findings are in pending state."
//				+ "<br> Verify that Accept All machine button is working when findings are in rejected state.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the rejected findings" );
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedfindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting the pending findings and validating it" );
//		panel.enableFiltersInOutputPanel(false, false, true);		
//		List<String> pendingFindings = panel.getAllFindingsName();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting all for machine "+ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.rejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameTitleList.size(), "Checkpoiny[1/5]", "Validating that upon rejecting all the findings , no more findings available under pending tab");		
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoiny[2/5]", "Validating that upon rejecting all the findings , no more findings available under pending tab");		
//
//		panel.enableFiltersInOutputPanel(false, true, true);
//		List<String> allRejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <allRejectedFindings.size();i++) 			
//			panel.assertEquals(allRejectedFindings.get(i), ListUtils.union(pendingFindings, rejectedfindings).get(i).toString(), "Checkpoiny[3."+i+"/5]", "Verifying the finding name with the finding name before rejecting the findings");
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "accepting the rejected findings and validating it" );
//		panel.enableFiltersInOutputPanel(false, true, false);
//		allRejectedFindings = panel.getAllFindingsName();
//		panel.scrollIntoView(panel.acceptedButton);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting all for machine "+ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.acceptAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1);
//		panel.assertEquals(rejectedfindings.size(), panel.findingsNameTitleList.size(), "Checkpoiny[4/5]", "Validating that upon accepting the findings ,findings available under accept tab");		
//
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++)
//			panel.assertEquals(acceptedFindings.get(i), pendingFindings.get(i), "Checkpoiny[5."+i+"/5]", "Verifying the finding name with the finding name before accepting the findings");
//		panel.openAndCloseOutputPanel(false);		
//
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1161","BVT"})
//	public void test19_US1161_TC5986_TC6047_VerifyRejectedAlllDisabledForRejectedFindings() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Reject All machine/Users button disabled when all findings are already Rejected."
//				+ "<br> Verify that tooltip for button on hoveirng on Accept and Reject button.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(dxResultPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+dxResultPatient+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		panel.closingBannerAndWaterMark();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting all pending results and creating new point on source and rejecting it" );
//		panel.selectFindingFromTable(2,1);
//		panel.selectRejectfromGSPSRadialMenu();
//		panel.selectRejectfromGSPSRadialMenu();	
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting all pending results from viewbox 1" );
//		panel.click(panel.getViewPort(1));
//		panel.mouseHover(panel.getGSPSHoverContainer(1));
//		panel.selectFindingFromTable(1,1);
//		panel.selectRejectfromGSPSRadialMenu();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new point on source and rejecting it" );
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, 100);
//		panel.selectRejectfromGSPSRadialMenu();
//
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(3));
//		panel.enableFiltersInOutputPanel(true, true, true);		
//
//		panel.mouseHover(panel.rejectAllForMachine.get(0));
////		panel.assertEquals(panel.getText(panel.rejectAllMachineTitle.get(0)),NSConstants.REJECT_ALL_TOOLTIP,"","");
//		panel.assertEquals(panel.getCssValue(panel.rejectAllForMachine.get(0),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[1/2]","Verifying that when all the results are rejected, reject all button is disabled - machine");		
//
//		panel.mouseHover(panel.rejectAllForMachine.get(1));
////		panel.assertEquals(panel.getText(panel.rejectAllMachineTitle.get(1)),NSConstants.REJECT_ALL_TOOLTIP,"","");
//		panel.assertEquals(panel.getCssValue(panel.rejectAllForMachine.get(1),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[2/2]","Verifying that when all the results are rejected, reject all button is disabled - users");
//
//		panel.openAndCloseOutputPanel(false);
//
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1161"})
//	public void test20_US1161_TC5985_TC6047_VerifyAcceptAllDisbaledForAcceptedFindings() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Accept All machine/Users button disabled when all findings are already Accepted."
//				+ "<br> Verify that tooltip for button on hoveirng on Accept and Reject button.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(dxResultPatient);
//
//		patientListPage.clickOntheFirstStudy();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+dxResultPatient+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		panel.closingBannerAndWaterMark();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting all pending results and creating new point on source" );
//		panel.selectFindingFromTable(2,1);
//		panel.selectAcceptfromGSPSRadialMenu();
//		panel.selectAcceptfromGSPSRadialMenu();
//
//		panel.click(panel.getViewPort(1));
//		panel.mouseHover(panel.getGSPSHoverContainer(1));
//		panel.selectFindingFromTable(1,1);
//		panel.selectAcceptfromGSPSRadialMenu();
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, 100);
//
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.mouseHover(panel.acceptAllForMachine.get(0));
////		panel.assertEquals(panel.getText(panel.acceptAllMachineTitle.get(0)),NSConstants.ACCEPT_ALL_TOOLTIP,"","");
//		panel.assertEquals(panel.getCssValue(panel.acceptAllForMachine.get(0),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[1/2]","Verifying that when all the results are accepted, accept all button is disabled - machine");		
//		
//		panel.mouseHover(panel.acceptAllForMachine.get(1));
////		panel.assertEquals(panel.getText(panel.acceptAllMachineTitle.get(1)),NSConstants.ACCEPT_ALL_TOOLTIP,"","");
//		panel.assertEquals(panel.getCssValue(panel.acceptAllForMachine.get(1),NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[2/2]","Verifying that when all the results are accepted, accept all button is disabled - users");
//
//		panel.openAndCloseOutputPanel(false);
//
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test21_US1161_TC5987_TC5997_verifyCloningForNonDICOMAndGSPS() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the clone logic for NonDicom data(PNG/JPG/PDF) along with user findings."
//				+ "<br> Verify that Machine/user button should not get disabled or Enabled on clicking \"Accept All\" or Reject All button.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(johnDoePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+johnDoePatient+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		ContentSelector cs = new ContentSelector(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point on 4th viewbox" );
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(4);
//		point.drawPointAnnotationMarkerOnViewbox(4, 100, -100);	
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retrieving the results from content selector" );
//		panel.click(panel.bannerCloseIcon);
//		panel.click(panel.getViewPort(1));
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retrieving the accepted findings" );
//		panel.enableFiltersInOutputPanel(true, false, false);
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(4));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retrieving the pending findings" );
//		panel.enableFiltersInOutputPanel(false, false, true);
//		List<String> pendingFindings = panel.getAllFindingsName();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting the findings" );		
//		panel.click(panel.acceptAllForMachine.get(0));
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[1/11]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size()+acceptedFindings.size(), "Checkpoint[2/11]", "Verifying all the results under accepted tab");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[3/11]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Accepting All\".");
//
//		for (int i = 1; i <= 3; i++) {
//			panel.click(panel.getViewPort(i));
//			panel.assertTrue(panel.verifyResultsAreAccepted(i),"Checkpoint[4."+i+"/11]","Verifying all results are accepted under viewbox too");
//
//		}
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout with User Scan1 and Login with Scan2 and change the state of all the findings (Machine+User) from Accepted to Pending state.");
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(johnDoePatient);
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+johnDoePatient+ " on viewer page. for "+userZ);
//		panel.waitForRespectedViewboxToLoad(1);
//		panel.selectAcceptfromGSPSRadialMenu();
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.threeByThreeLayoutIcon);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing the findings state to pending");
//		for (int i = 2; i <= 4; i++) {
//			panel.click(panel.getViewPort(i));
//			panel.openGSPSRadialMenu(i);
//			panel.selectAcceptfromGSPSRadialMenu(i);
//
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting the results from content selector before rerjecting all the findings" );
//		resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the pending findings" );
//		panel.enableFiltersInOutputPanel(false, false, true);		
//		pendingFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Click on the \"Reject All\" button present on \"Machine+Scan2\" button." );
//		panel.mouseHover(panel.getViewPort(4));
//		panel.enableFiltersInOutputPanel(false, false, true);
//		
//		panel.clickRejectAllFilter(0);
//		panel.clickRejectAllFilter(1);
//		
//		verifyButtonsSelection(panel.machinesFilters.get(0),"Checkpoint[5/11]","Verifying that second button is selected");
//		verifyButtonsSelection(panel.machinesFilters.get(1),"Checkpoint[6/11]","Verifying that second button is selected");
//
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameList.size(), "Checkpoiny[7/11]", "Validating that upon rejecting all the findings , no more findings available under pending tab");		
//		panel.assertTrue(panel.findingsNameTitleList.isEmpty(), "Checkpoiny[8/11]", "Validating that upon reecting all the findings , no more findings available under pending tab");		
//
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), pendingFindings.get(i), "Checkpoiny[9."+i+"/11]", "Verifying the finding name with the finding name before rejected");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[8/9]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Rejecting All\".");
//
//		panel.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, 1), "Checkpoint[10.1/11]", "Verifying that point is rejected");
//
//		for (int i = 2; i <= 4; i++) {
//			panel.click(panel.getViewPort(i));
//			panel.assertTrue(panel.verifyResultsAreRejected(i),"Checkpoint[11."+i+"/11]","Verifying all results are rejected under viewbox too");
//
//		}
//
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test22_US1161_TC5993_verifyCloningForNonDICOMOnly() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("No cloning for NonDicom Data like JPG, PNG.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		ContentSelector cs = new ContentSelector(driver);		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retrieving the results from content selector" );
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//
//		panel.enableFiltersInOutputPanel(false, false,true);
//		List<String> pendingFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting all from both the machines" );
//		panel.mouseHover(panel.getViewPort(4));
//		panel.enableFiltersInOutputPanel(false, false, true);
//
//		panel.click(panel.acceptAllForMachine.get(0));
//		panel.click(panel.acceptAllForMachine.get(1));
//
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[1/9]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size(), "Checkpoint[2/9]", "Verifying all the results under accepted tab");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[3/9]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Accepting All\".");
//
//		for (int i = 1; i <= 3; i++) {
//			panel.assertTrue(panel.verifyResultsAreAccepted(i),"Checkpoint[4."+i+"/9]","Verifying all results are accepted under viewbox too");
//
//		}
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting all from both the machines" );
//		panel.click(panel.rejectAllForMachine.get(0));
//		panel.click(panel.rejectAllForMachine.get(1));
//
//		panel.assertNotEquals(acceptedFindings.size(), panel.findingsNameList.size(), "Checkpoiny[5/9]", "Validating that upon rejecting all the findings , no more findings available under pending tab");		
//		panel.assertTrue(panel.findingsNameTitleList.isEmpty(), "Checkpoiny[6/9]", "Validating that upon reecting all the findings , no more findings available under pending tab");		
//
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), acceptedFindings.get(i), "Checkpoiny[7."+i+"/9]", "Verifying the finding name with the finding name before rejected");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[8/9]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Rejecting All\".");
//
//		for (int i = 1; i <=3; i++) {
//			panel.assertTrue(panel.verifyResultsAreRejected(i),"Checkpoint[9."+i+"/9]","Verifying all results are rejected under viewbox too");
//
//		}
//
//
//	}	
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test23_US1161_TC5994_verifyCloningSR() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("No Cloning for SR data.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		patientListPage.clickOnPatientRow(chestCT1p25mm);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+chestCT1p25mm+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(2);
//		panel.click(panel.bannerCloseIcon);
//
//		ContentSelector cs = new ContentSelector(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting the source series" );
//		cs.selectSeriesFromSeriesTab(2, cs.getSeriesDescriptionOverlayText(2));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point on source and changing it's state to pending" );
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(2);
//		point.drawPointAnnotationMarkerOnViewbox(2, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the results from content selector" );
//		point.click(point.bannerCloseIcon);
//		point.click(point.getViewPort(1));
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//		System.out.println(resultsBeforeAccepting.size());
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the accepting findings" );
//		panel.enableFiltersInOutputPanel(true, false, false);
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(2));
//		panel.enableFiltersInOutputPanel(false, false, true);
//		List<String> pendingFindings = panel.getAllFindingsName();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting the pending findings both from machine and user filters" );
//		panel.clickAcceptAllFilter(0);
//		panel.clickAcceptAllFilter(1);		
//
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[1/9]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size()+acceptedFindings.size(), "Checkpoint[2/9]", "Verifying all the results under accepted tab");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size()+1, "Checkpoint[3/9]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Accepting All\".");
//
//		panel.assertTrue(panel.verifyResultsAreAccepted(1),"Checkpoint[4/9]","Verifying all results are accepted under viewbox too");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the accepted findings" );
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		acceptedFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Click on the \"Reject All\" button present on \"Machine+Scan2\" button." );
//		panel.mouseHover(panel.getViewPort(2));
//		panel.enableFiltersInOutputPanel(true, false, false);
//		
//		panel.clickRejectAllFilter(0);
//		panel.clickRejectAllFilter(1);
//
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameList.size(), "Checkpoiny[5/9]", "Validating that upon rejecting all the findings , no more findings available under accepted tab");		
//		panel.assertTrue(panel.findingsNameTitleList.isEmpty(), "Checkpoiny[6/9]", "Validating that upon reecting all the findings , no more findings available under accepted tab");		
//
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), pendingFindings.get(i), "Checkpoiny[7."+i+"/9]", "Verifying the finding name with the finding name before rejected");
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size()+1, "Checkpoint[8/9]", "Verifying No further clone copy for JPG/PNG/PDF should be created on performing \"Rejecting All\".");
//
//		panel.assertTrue(panel.verifyResultsAreRejected(1),"Checkpoint[9/9]","Verifying all results are rejected under viewbox too");
//
//
//
//
//	}	
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161","DE1746","DE1999"})
//	public void test24_US1161_TC5995_DE1746_TC7116_TC7117_DE1999_TC8234_TC8237_verifyCloningRTOnAcceptorRejectAll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cloning for RT data on Accept All or Reject All on machine button."
//				+ "<br> Verify if machine findings are showing under correct filter selection on performing 'Accept All/Reject All' from output panel"
//				+ "<br> Verify if Machine findings are showing correct state on viewer(Legends), Finding menu and finding marker after performing Accept All/Reject All' from output panel. <br>"+
//				"Verify that State of segments are getting changed when user accepts all or rejects all on machine button from Output panel. <br>"+
//				"Verify the state in Output panel, AR tool bar, vertical finding bar, finding menu after Accept all or Reject all from O/P panel.[Risk&Impact]");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+patientNameDICOMRT+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		
//		rt=new DICOMRT(driver);
//		rt.waitForDICOMRTToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating a point and rejecting it" );		
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the results from content selector" );
//		ContentSelector cs = new ContentSelector(driver);	
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the accepted findings" );
//		panel.enableFiltersInOutputPanel(true, false, false);
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the Pending findings" );
//		List<String> pendingFindings = panel.getAllFindingsName();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing accept all for machine and user filters" );
//		panel.clickAcceptAllFilter(0);
//		panel.clickAcceptAllFilter(1);		
//
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[1/15]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size()+acceptedFindings.size(), "Checkpoint[2/15]", "Verifying all the results under accepted tab");
//		panel.openAndCloseOutputPanel(false);
//
//		// waiting for clone to getting generated
//		panel.waitForTimePeriod(10000);
//
//		List<String> results = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(results.size(), resultsBeforeAccepting.size()+1, "Checkpoint[3/15]", "Verifying clone copy for RT should be created on performing Accepting All ");
//		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[4/15]", "Verifying that point state is accepted");
//
//		panel.assertTrue(panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size()>0, "Checkpoint[5/15]", "Verified state of finding on scroll slider.");
//		
//		DICOMRT drt = new DICOMRT(driver);
//		drt.mouseHover(drt.getGSPSHoverContainer(1));
//		for(int i =1;i<=drt.legendOptionsList.size();i++) {
//			drt.assertTrue(drt.verifyAcceptedRTSegment(i),"Checkpoint[6/15]", "Verifying state icon is accepted");	
//			drt.assertEquals(drt.getColorOfFindingFromTable(drt.getNameOfAcceptedRejectedSegment(i)),ViewerPageConstants.ACCEPTED_RT_RESULT_COLOR,"Checkpoint[7/15]","Verifying the state of segements from findings table");
//		}
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the accepted findings" );
//		panel.enableFiltersInOutputPanel(true, false, false);		
//		acceptedFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Click on the \"Reject All\" button present on \"Machine+Scan2\" button." );
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);
//
//		panel.clickRejectAllFilter(0);
//		panel.clickRejectAllFilter(1);	
//
//		panel.assertNotEquals(pendingFindings.size(), panel.findingsNameList.size(), "Checkpoiny[8/15]", "Validating that upon rejecting all the findings , no more findings available under accepted tab");		
//		panel.assertTrue(panel.findingsNameTitleList.isEmpty(), "Checkpoiny[9/15]", "Validating that upon reecting all the findings , no more findings available under accepted tab");		
//		
//		panel.waitForTimePeriod(10000);
//		
//		panel.enableFiltersInOutputPanel(false, true, false);
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), pendingFindings.get(i), "Checkpoiny[10."+i+"/15]", "Verifying the finding name with the finding name before rejected");
//		panel.openAndCloseOutputPanel(false);
//
//		// waiting for clone to getting generated
//		panel.waitForTimePeriod(10000);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), results.size(), "Checkpoint[11/15]", "Verifying clone copy for RT should be created on performing \"Rejecting All\".");
//		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, 1), "Checkpoint[12/15]", "Verifying point is rejected");
//
//		drt.mouseHover(drt.getGSPSHoverContainer(1));
//		for(int i =1;i<=drt.legendOptionsList.size();i++) {
//			drt.assertTrue(drt.verifyRejectedRTSegment(i),"Checkpoint[13/15]", "Verifying state icon is rejected");	
//			drt.assertEquals(drt.getColorOfFindingFromTable(drt.getNameOfAcceptedRejectedSegment(i)),ViewerPageConstants.REJECTED_RT_RESULT_COLOR,"Checkpoint[14/15]","Verifying the segments's state in findings menu");
//		}
//		panel.assertTrue(panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size()>0, "Checkpoint[15/15]", "Verified state of finding on scroll slider.");
//
//
//	}	
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test25_US1161_TC5996_TC5997_verifyCloningForUserCreatedFindings() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the clone for GSPS DICOM data on Accepting All or On Rejecting All when findings are in Pending state and user revisit the viewer page."
//				+ "<br> Verify that Machine/user button should not get disabled or Enabled on clicking \"Accept All\" or Reject All button.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page.for "+userA );
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//
//		ContentSelector cs = new ContentSelector(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point and chaging it's state to pending");
//		
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reloading the viewer");
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(liverPatientName, 1, 1);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page. for "+userZ);
//			loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout with User Scan1 and Login with Scan2 and change the state of all the findings (Machine+User) from Accepted to Pending state.");
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing the layout to 2x2");
//		panel.waitForViewerpageToLoad();
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Retriving the clones");
//		List<String> userAResult = cs.getAllResultsFromSeriesTab();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point on 3rd series and changing its state to pending");
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		
//		point.click(panel.getViewPort(1));
//		List<String> originalResult = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(originalResult.size(),userAResult.size()+1 , "Checkpoint[1/10]", "Verifying the clone is generated");
//		cs.assertEquals(originalResult.get(originalResult.size()-1),"GSPS_"+userZ+"_1" , "Checkpoint[2/10]", "Verifying the clone is created for user "+userZ);
//
//		helper.browserBackAndReloadViewer(liverPatientName, 1, 1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "ReLoad patient "+liverPatientName+ " on viewer page.");
//		panel.waitForViewerpageToLoad();
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//	
//		panel.mouseHover(panel.getViewPort(3));
//	
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertEquals(panel.usersFiltersList.size(), 2, "Checkpoint[3/10]", "Verifying there are 2 user filters button in output panel");		
//		List<String> pendingFindings = panel.getAllFindingsName();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting both the user's findings");
//		panel.acceptAllMachineFilter(userZ);
//		panel.acceptAllMachineFilter(userA);
//		
//		//verifyButtonsSelection(panel.usersFiltersList.get(0),"Checkpoint[4/12]","Verifying that first button is selected");
//	//	verifyButtonsSelection(panel.usersFiltersList.get(1),"Checkpoint[5/12]","Verifying that second button is selected");
//		
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[4/10]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size(), "Checkpoint[5/10]", "Verifying all the results under accepted tab");
//		
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) {			
//			panel.assertEquals(acceptedFindings.get(i), pendingFindings.get(i), "Checkpoiny[6."+i+"/10]", "Verifying the finding name with the finding name before accepted");
//			panel.assertEquals(panel.getText(panel.findingsEditorName.get(i)), userZ, "Chckpoint[7/10]", "Verifying the editor name is "+userZ+" for all the findings");
//		}
//	
//		panel.openAndCloseOutputPanel(false);
//
//		List<String> results = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(results.size(),originalResult.size()+2 , "Checkpoint[8/12]", "Verifying two clones are generated");
//		
//		cs.assertTrue(results.get(results.size()-1).contains("GSPS_"+userZ) , "Checkpoint[9/10]", "Verifying the last clone is from user "+userZ);
//		cs.assertTrue(results.get(results.size()-2).contains("GSPS_"+userZ) , "Checkpoint[10/10]", "Verifying the second last clone is from user "+userZ);
//
//		
//
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test26_US1161_TC6004_TC6017_verifyAcceptAllNotAllowedWhenResultsNotLoaded() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Machine button is not allowing to Accept All or Reject All if findings belonging to that Machine filters is not loaded."
//				+ "<br> Verify that \"Accept All\" and \"Reject All\" button should be disabled on that machine button which have the findings with Choose One selector.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.openAndCloseOutputPanel(false);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that accept and reject all buttons are disabled when results are not loaded");
//		panel.enableFiltersInOutputPanel(true, false, false);		
////		panel.click(panel.machineFilterBasedOnMachineName(ViewerPageConstants.BONEAGE_MACHINE_NAME1));		
//		panel.assertEquals(panel.getCssValue(panel.getAcceptAllFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[1/4]","verifying accept all is disabled");
//		panel.assertEquals(panel.getCssValue(panel.getRejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME1), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[2/4]","verifying reject all is disabled");
//		
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.mouseHover(panel.getAcceptAllFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME));
//		panel.assertEquals(panel.getCssValue(panel.getAcceptAllFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[3/4]","Verifying accept all is disabled for choose one type finding");
//		
//		//Verify that \"Accept All\" and \"Reject All\" button should be disabled on that machine button which have the findings with Choose One selector.
//		panel.mouseHover(panel.getRejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME));
//		panel.assertEquals(panel.getCssValue(panel.getRejectAllMachineFilter(ViewerPageConstants.BONEAGE_MACHINE_NAME), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[4/4]","Verifying reject all is disabled for choose one type finding");
//		
//		
//
//
//	}	
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test27_US1161_TC6005_TC6015_verifyAcceptOrRejectNotAllowedWhenMachinesResultsNotLoaded() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that User/machine button is not allowing to Accept All or Reject All if findings belonging to that user/machine filters is not loaded."
//				+ "<br> Verify the Accept All and Reject All button functionality on machine/user button which is disabled.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);		
//		patientListPage.clickOnPatientRow(johnDoePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+johnDoePatient+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(4);
//
//		ContentSelector cs = new ContentSelector(driver);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting source series and creating the point and changing it's state to pending" );
//		cs.selectSeriesFromSeriesTab(4, cs.getSeriesDescriptionOverlayText(4));
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(4);
//		point.drawPointAnnotationMarkerOnViewbox(4, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(4));
//		panel.enableFiltersInOutputPanel(true, false, false);
//		
//		panel.mouseHover(panel.acceptAllForMachine.get(0));
//		panel.assertEquals(panel.getCssValue(panel.acceptAllForMachine.get(0), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[1/7]","Verifying accept all is disabled for machine button");
//		
//		panel.mouseHover(panel.rejectAllForMachine.get(0));
//		panel.assertEquals(panel.getCssValue(panel.rejectAllForMachine.get(0), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[2/7]","Verifying reject all is disabled for machine button");
//		
//		panel.mouseHover(panel.acceptAllForMachine.get(1));
//		panel.assertEquals(panel.getCssValue(panel.acceptAllForMachine.get(1), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[3/7]","Verifying accept all is disabled for user button");
//		
//		panel.mouseHover(panel.rejectAllForMachine.get(1));
//		panel.assertEquals(panel.getCssValue(panel.rejectAllForMachine.get(1), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[4/7]","Verifying reject all is disabled for user button");
//		
//		panel.openAndCloseOutputPanel(false);
//		
//		panel.mouseHover(panel.getViewPort(4));
//		panel.enableFiltersInOutputPanel(true, false, true);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Disabling the machine filter button and validating that accept/reject all button are disabled" );
//		
//		panel.click(panel.machineFiltersList.get(0));
//		panel.mouseHover(panel.acceptAllForMachine.get(0));
//		panel.click(panel.acceptAllForMachine.get(0));
//		panel.assertEquals(panel.getCssValue(panel.acceptAllForMachine.get(0), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[5/7]","Verifying accept all is disabled though user tries to click");
//		
//		panel.mouseHover(panel.rejectAllForMachine.get(0));
//		panel.click(panel.rejectAllForMachine.get(0));
//		panel.assertEquals(panel.getCssValue(panel.rejectAllForMachine.get(0), NSGenericConstants.CSS_PROP_CURSOR),ViewerPageConstants.DISABLED_BUTTON_TEXT,"Checkpoint[6/7]","Verifying the reject all is disabled though user tries to click");
//	
//		
//		verifyButtonsDeSelection(panel.machineFiltersList.get(0), "Checkpoint[7/7]", "Verifying on click of accept/ reject all button machine filter should not get enabled");
//
//	}	
//		
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test28_US1161_TC6007_verifyCloningSC() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that No cloning for DICOM SC data.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		patientListPage.clickOnPatientRow(imbioTexturePatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+imbioTexturePatient+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//		circle=new CircleAnnotation(driver);
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		List<String> pendingFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point on source series and state changed to pending" );
//		ContentSelector cs = new ContentSelector(driver);
//		cs.selectSeriesFromSeriesTab(3, cs.getSeriesDescriptionOverlayText(3));
//
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		
//		circle.selectCircleFromQuickToolbar(3);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting result from content selector" );
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Accepting all the pending findings" );
//		panel.enableFiltersInOutputPanel(false, false, true);
//		List<String> allPendingFindings = panel.getCreatorList();
//		panel.clickAcceptAllFilter(0);		
//
//		panel.assertNotEquals(panel.findingsNameList.size(),allPendingFindings.size(), "Checkpoint[1/8]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.thumbnailList.size(),pendingFindings.size(), "Checkpoint[2/8]", "Verifying all the results under accepted tab");
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(acceptedFindings.get(i), pendingFindings.get(i), "Checkpoiny[3."+i+"/8]", "Verifying the finding name with the finding name before rejected");
//		
//		panel.openAndCloseOutputPanel(false);
//
//		cs.closeWaterMarkIcon(1);
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[4/8]", "No clones should be created");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.clickRejectAllFilter(0);		
//		
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[5/8]", "Verifying all the results are moved under rejected tab");
//		panel.enableFiltersInOutputPanel(false, true, false);
//		panel.assertEquals(panel.thumbnailList.size(),pendingFindings.size(), "Checkpoint[6/8]", "Verifying all the results under accepted tab");
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <rejectedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), pendingFindings.get(i), "Checkpoiny[7."+i+"/8]", "Verifying the finding name with the finding name before rejected");
//		
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size(), "Checkpoint[8/8]", "Verifying No further clone copy created");
//
//		
//	}	
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test29_US1161_TC6008_verifyCloningMammoCAD() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cloning for Mammo CAD data");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		patientListPage.clickOnPatientRow(IHEMammoTest_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+IHEMammoTest_PatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForRespectedViewboxToLoad(1);
//		
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.threeByThreeLayoutIcon);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting all the results and creating a point on source after performing sync off" );
//		ContentSelector cs = new ContentSelector(driver);
//		List<String> results = cs.getAllResultsFromSeriesTab();
//		for(int i =1;i<=results.size();i++) {
//			cs.selectResultFromSeriesTab(i, results.get(i-1),i);
//		}
//		
//		panel.performSyncONorOFF();
//		
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(5);
//		point.drawPointAnnotationMarkerOnViewbox(5, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		point.closingBannerAndWaterMark();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting all pending findngs from output panel" );
//		panel.enableFiltersInOutputPanel(false, false, true);
//		List<String> pendingFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting all the results from content selector" );
//		List<String> resultsBeforeAccepting = cs.getAllResultsFromSeriesTab();
//		panel.mouseHover(panel.getViewPort(1));
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting all the pending findings and clicking on accept all filter" );
//		List<String> allPendingFindings = panel.getAllFindingsName();
//		panel.clickAcceptAllFilter(0);
//		panel.clickAcceptAllFilter(1);
//
//		panel.assertNotEquals(panel.findingsNameList.size(),allPendingFindings.size(), "Checkpoint[1/10]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size(), "Checkpoint[2/10]", "Verifying all the results under accepted tab");
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) 			
//			panel.assertEquals(acceptedFindings.get(i), pendingFindings.get(i), "Checkpoiny[3."+i+"/10]", "Verifying the finding name with the finding name before accepted");
//		
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size()+results.size(), "Checkpoint[4/10]", "Verifying No further clone copy created on performing \"Accepting All\".");
//		
//		panel.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(5, 1), "Checkpoint[5/10]", "verifying point is accepted");
//		
//			
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.clickRejectAllFilter(0);	
//		panel.clickRejectAllFilter(1);
//
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[6/10]", "Verifying all the results are moved under rejected tab");
//		panel.enableFiltersInOutputPanel(false, true, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),pendingFindings.size(), "Checkpoint[7/10]", "Verifying all the results under rejected tab");
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <rejectedFindings.size();i++) 			
//			panel.assertEquals(rejectedFindings.get(i), pendingFindings.get(i), "Checkpoiny[8."+i+"/10]", "Verifying the finding name with the finding name before rejected");
//		
//		panel.openAndCloseOutputPanel(false);
//
//		cs.assertEquals(cs.getAllResultsFromSeriesTab().size(), resultsBeforeAccepting.size()+results.size(), "Checkpoint[9/10]", "Verifying No further clone copy created on performing \"Reject All\".");
//		panel.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(5, 1), "Checkpoint[10/10]", "verifying point is rejected");
//		
//	}	
//
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test30_US1161_TC6019_verifyAcceptRejectAllWhenFindingsAreinDiffState() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the clone for GSPS DICOM data on Accepting All or On Rejecting All when findigs is in Accepted and Rejected state.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//
//		ContentSelector cs = new ContentSelector(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating circle as user "+userA );
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 30, 50, 60, 70);	
//		
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout with User Scan1 and Login with Scan2 and change the state of all the findings (Machine+User) from Accepted to Pending state.");
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page. for "+userZ);
//		panel.waitForViewerpageToLoad();
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Getting the results from content selector" );
//		
//		List<String> userAResult = cs.getAllResultsFromSeriesTab();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating point and rejecting it" );
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, -100);		
//		point.selectRejectfromGSPSRadialMenu();
//		point.closingBannerAndWaterMark();
//		
//		List<String> originalResult = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(originalResult.size(),userAResult.size()+1 , "Checkpoint[1/11]", "Verifying clone is created ");
//		cs.assertEquals(originalResult.get(originalResult.size()-1),"GSPS_"+userZ+"_1" , "Checkpoint[2/11]", "Verifying clone is created for user "+userZ);
//	
//		panel.mouseHover(panel.getViewPort(3));
//		panel.enableFiltersInOutputPanel(true, true, false);		
//		List<String> allFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.usersFiltersList.size(), 2, "Checkpoint[3/11]", "Verifying there are 2 users buttons present");		
//				
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Rejecting the user "+userA+" findings" );
//		panel.rejectAllMachineFilter(userA);			
//		
//		panel.assertTrue(panel.usersFiltersList.isEmpty(), "Checkpoint[4/11]", "Verifying no filters button are present");
//		panel.assertTrue(panel.findingsNameList.isEmpty(), "Checkpoint[5/11]", "Verifying all the results are moved under accepted tab");
//
//		panel.enableFiltersInOutputPanel(false, true, false);
//		panel.assertEquals(panel.findingsNameTitleList.size(),allFindings.size(), "Checkpoint[6/11]", "Verifying all the results under rejected tab");
//		
//		List<String> rejectedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <rejectedFindings.size();i++) {			
//			panel.assertEquals(rejectedFindings.get(i), allFindings.get(i), "Checkpoiny[7."+i+"/11]", "Verifying the finding name with the finding name before rejected");
//			panel.assertEquals(panel.getText(panel.findingsEditorName.get(i)), userZ, "Checkpoint[8/11]", "Verifying the editor name");
//		}
//	
//		panel.openAndCloseOutputPanel(false);
//
//		List<String> results = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(results.size(),originalResult.size()+1 , "Checkpoint[9/11]", "Verifying the clone is created");
//		
//		cs.selectResultFromSeriesTab(2, results.get(results.size()-1));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(2, 1), "Checkpoint[10/11]", "Verifying the state upon selecting the last clone");
//		
//		cs.selectResultFromSeriesTab(4, results.get(0));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(4, 1), "Checkpoint[11/11]", "Verifying the state upon selecting the user "+userA+" clone");
//		
//
//	}	
//		
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1161"})
//	public void test31_US1161_TC6020_verifyCloneWhenFindingsAreinPendingState() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the clone for GSPS DICOM data on Accepting All or On Rejecting All when findings are in Pending state.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userA,userA);
//
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//
//		ContentSelector cs = new ContentSelector(driver);
//
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 30, 50, 60, 70);	
//		circle.selectAcceptfromGSPSRadialMenu();
//		
//		loginPage.logout();
//		loginPage.navigateToBaseURL();
//		loginPage.login(userZ,userZ);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout with User Scan1 and Login with Scan2 and change the state of all the findings (Machine+User) from Accepted to Pending state.");
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+liverPatientName+ " on viewer page. for "+userZ);
//		panel.waitForViewerpageToLoad();
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		List<String> userAResult = cs.getAllResultsFromSeriesTab();
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(3);
//		point.drawPointAnnotationMarkerOnViewbox(3, 100, -100);		
//		point.selectAcceptfromGSPSRadialMenu();
//		point.closingBannerAndWaterMark();
//		
//		List<String> originalResult = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(originalResult.size(),userAResult.size()+1 , "Checkpoint[1/8]", "Verifying the clone is created");
//		cs.assertEquals(originalResult.get(originalResult.size()-1),"GSPS_"+userZ+"_1" , "Checkpoint[2/8]", "Verifying the clone is created for user "+userZ);
//	
//		panel.mouseHover(panel.getViewPort(3));
//		panel.enableFiltersInOutputPanel(true, false, true);		
//		List<String> allFindings = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertEquals(panel.usersFiltersList.size(), 2, "Checkpoint[3/8]", "Verifying there are 2 buttons present");		
//			
//		panel.acceptAllMachineFilter(userA);
//	
//		panel.enableFiltersInOutputPanel(true,false, true);
//		panel.assertEquals(panel.findingsNameTitleList.size(),allFindings.size(), "Checkpoint[4/8]", "Verifying all the results under accepted tab");
//		
//		List<String> acceptedFindings = panel.getAllFindingsName();
//		for (int i =0 ; i <acceptedFindings.size();i++) {			
//			panel.assertEquals(panel.getText(panel.findingsEditorName.get(i)), userZ, "Checkpoint[5/8]", "Verifying the editor name");
//		}
//	
//		panel.openAndCloseOutputPanel(false);
//
//		List<String> results = cs.getAllResultsFromSeriesTab();
//		cs.assertEquals(results.size(),originalResult.size()+1 , "Checkpoint[6/8]", "Verifying clone is created");
//		
//		cs.selectResultFromSeriesTab(2, results.get(results.size()-1));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[7/8]", "Verifying the last clone");
//		
//		cs.selectResultFromSeriesTab(4, results.get(0));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(4, 1), "Checkpoint[8/8]", "verifying the clone from user "+userA);
//		
//
//	}	
//	
//	//DE1634 : [Hardening]-State of machine GSPS findings getting changed clicking on 'Accept All' or 'Reject All' on user button in output panel.
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1634"})
//	public void test32_DE1634_TC6696_verifyStateOfMachineFindingAfterClickOnUserButton() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the state of machine GSPS findings not getting changed clicking on 'Accept All' or 'Reject All' on user button in output panel.[Happy Path]");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
//		patientListPage.waitForElementVisibility(patientListPage.toolTip);
//		String machineName=patientListPage.getText(patientListPage.machineNameOnEurekaAl);
//	
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+patientNameDICOMRT+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//	    drt=new DICOMRT(driver);
//	    drt.waitForDICOMRTToLoad();
//	    
//		panel.openFindingTableOnBinarySelector(1);
//		int legendCount=panel.getNumberOfFindingsOnBadge();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw annotation and change state of annotation to pending" );
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 70, 70, 80, 80);
//		circle.selectAcceptfromGSPSRadialMenu();
//	
//        int totalCount=panel.getBadgeCountForBinarySelector(1);
//        List<String>legendName=drt.convertWebElementToStringList(drt.getLegendOptionsList(1));
//        
//    	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify machine and user filter list in Output panel" );
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertFalse(panel.machineFiltersList.isEmpty(), "Checkpoint[1/9]", "Verified that machine filter visible in Output Panel after drawing annotation");
//		panel.assertFalse(panel.usersFiltersList.isEmpty(), "Checkpoint[2/9]", "Verified that user filter visible in Output Panel after drawing annotation");
//		
//		panel.acceptAllMachineFilter(userName);
//        panel.assertTrue(panel.thumbnailList.size()< totalCount, "Checkpoint[3/9]", "Verified that all User's findings state get changed to Accepted state and  not be visible in Pending filter after click on \'Accept All \' from user filter button");
//		
//        panel.acceptAllMachineFilter(machineName);
//        panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[4/9]", "Verified that all Machine`s findings state get changed to Accepted state and  not be visible in Pending filter click on \'Accept All \' from machine filter button");
//        panel.openAndCloseOutputPanel(false);
//        
//        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify findings count under accepted tab and verify finding after click on Reject all from use filter." );
//        panel.click(panel.getViewPort(1));
//        panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.thumbnailList.size(), totalCount, "Checkpoint[5/9]", "Verified that all machine and user findings are visible under Accepted tab");
//		
//		panel.rejectAllMachineFilter(userName);
//		panel.assertTrue(panel.thumbnailList.size()< totalCount, "Checkpoint[6/9]", "Verified that all User's findings state get changed to Rejected state and  not be visible in Accepted filter.");
//	    panel.openAndCloseOutputPanel(false);
//	    
//	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify state of user finding in Output panel and Finding menu." );
//	    panel.openFindingTableOnBinarySelector(1);
//	    List<WebElement> rejectedFindings = panel.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_COLOR);
//	    panel.assertEquals(totalCount-legendCount,rejectedFindings.size() , "Checkpoint[7/9]", "Verified state of user finding in finding menu");
//	    
//		//verifying segment accepted
//	    for(int i=1;i<=legendCount;i++)
//		panel.assertTrue(drt.verifyAcceptedRTSegment(i), "Checkpoint[8/9]", "Verifying state icon is accepted for RT legened "+i);	
//	    
//	    for(int j=0;j<legendCount;j++)
//	    panel.assertEquals(drt.getColorOfFindingFromTable(legendName.get(j)),ViewerPageConstants.ACCEPTED_RT_RESULT_COLOR,"Checkpoint[9/9]","Verifying state icon as Accepted in finding menu for RT legend "+(j+1));
//	    
//	}
//	
//	//DE1653 : Machine filters are not getting displayed in output panel when there are findings from multiple results present.
//	
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1653"})
//	public void test33_DE1653_TC6655_VerifyMachineFilterSelectionWhenMultipleMachinePresent() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify machine filters in output panel when there are findings from multiple results present.");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(boneage_PatientName);
//
//		patientListPage.clickOntheFirstStudy();
//
//		panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad(4);
//		cs=new ContentSelector(driver);
//		circle=new CircleAnnotation(driver);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+boneage_PatientName+ " on viewer page with first user" );
//		cs.openAndCloseSeriesTab(true);
//		String machineOne=cs.getText(cs.allMachineName.get(0));
//		String machineSecond=cs.getText(cs.allMachineName.get(1));
//		cs.openAndCloseSeriesTab(false);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify machine filter in Output panel when multiple machine present." );
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.assertTrue(panel.isElementPresent(panel.allMachineFilterNameList.get(0)), "Checkpoint[1/13]", "Verified machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(0)), machineSecond, "Checkpoint[2/13]", "Verified first machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(1)), machineOne, "Checkpoint[3/13]", "Verified second machine filter in Output panel");
//		panel.openAndCloseOutputPanel(false);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify machine and user filter in Output panel after drawing annotation on viewer.");
//		panel.mouseHoverWithClick(PRESENCE, panel.getViewPort(4));
//		circle.selectCircleFromQuickToolbar(4);
//		circle.drawCircle(4, 10, 10, 50, 50);
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertTrue(panel.isElementPresent(panel.allMachineFilterNameList.get(0)), "Checkpoint[4/13]", "Verified machine filter in Output panel");
//		panel.assertTrue(panel.isElementPresent(panel.allUserFilterNameList.get(0)), "Checkpoint[5/13]","Verified user filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(0)), machineSecond, "Checkpoint[6/13]", "Verified first machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(1)), machineOne, "Checkpoint[7/13]", "Verified second machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allUserFilterNameList.get(0)), userName, "Checkpoint[8/13]", "Verified first user filter in Output panel");
//		panel.openAndCloseOutputPanel(false);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify machine and user filter in Output panel after changing the layout to 3*3");
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.threeByThreeLayoutIcon);
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		panel.assertTrue(panel.isElementPresent(panel.allMachineFilterNameList.get(0)), "Checkpoint[9/13]", "Verified machine filter in Output panel");
//		panel.assertTrue(panel.isElementPresent(panel.allUserFilterNameList.get(0)), "Checkpoint[10/13]", "Verified user filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(0)), machineSecond, "Checkpoint[11/13]", "Verified first machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allMachineOrUserFilterNameList.get(1)), machineOne, "Checkpoint[12/13]", "Verified second machine filter in Output panel");
//		panel.assertEquals(panel.getText(panel.allUserFilterNameList.get(0)), userName, "Checkpoint[13/13]", "Verified first user filter in Output panel");
//		panel.openAndCloseOutputPanel(false);
//		
//	
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","DE1661","Negative"})
//	public void test34_DE1661_TC7153_verifyUserCloneStateNotChangedAfterAcceptAllOP() throws InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify first user's copy/clone state should be retained after performing  accept all from output panel on it");
//
//		patientListPage = new PatientListPage(driver);
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//						
//		Header header = new Header(driver);		
//		header.logout();
//
//		loginPage.login(userA, userA);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		
//		patientListPage.clickOntheFirstStudy();
//
//		contentSelector=new ContentSelector(driver);
//		circle = new CircleAnnotation(driver);
//		circle.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Create a circle and change its state to pending" );		
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, -20, 30, 50, 50);
//		circle.selectAcceptfromGSPSRadialMenu();
//		
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[1/18]", "Verifying that circle is in pending state");
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab().size(),1, "Checkpoint[2/18]", "Verifying that there is one clone");
//		//contentSelector.assertTrue(contentSelector.getAllResultsFromSeriesTab().get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[3/18]","verifying there is one clone created by "+userA);
//
//		header = new Header(driver);		
//		header.logout();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout and login using user 2." );		
//		
//		loginPage.login(userZ, userZ);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the AH.4 data in viewer - currently viewer is loaded in 1x1");
//		circle.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change the layout to 2x2 and create the another annotation (line) on source series 3 which is present in 3rd viewbox " );
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		
//		lineWithUnit = new MeasurementWithUnit(driver);
//		lineWithUnit.selectDistanceFromQuickToolbar(3);
//		lineWithUnit.drawLine(3, -50, -50, 30, 60);
//		lineWithUnit.selectAcceptfromGSPSRadialMenu();
//				
//		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(3, 1), "Checkpoint[4/18]", "Verifying that line is in pending state");
//		lineWithUnit.closingBannerAndWaterMark();
//		circle.assertTrue(circle.verifyCircleAnnotationIsPendingGSPS(1, 1), "Checkpoint[5/18]", "Verifying that circle is in pending state");
//				
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab().size(),2, "Checkpoint[6/18]", "Verifying that there are two clones");
//		List<HashMap<String, String>> results = contentSelector.getAllResultsFromSeriesTab();
//		
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[7/18]","verifying there is first clone created by "+userA);
//		contentSelector.assertTrue(results.get(1).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint[8/18]","verifying there is second clone created by "+userA);
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open output panel and perform accept all from filter button of user 1.");
//		panel = new OutputPanel(driver);
//		panel.enableFiltersInOutputPanel(true, false, true);
//		panel.acceptAllMachineFilter(userA);
//		
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.findingsNameList.size(), 1, "Checkpoint[9/18]", "Verifying that finding is accepted");		
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)), ViewerPageConstants.CIRCLE_FINDING_NAME, "Checkpoint[10/18]", "Verifying that circle is accepted");
//		
//		panel.assertTrue(panel.allMachineOrUserFilterNameList.isEmpty(), "Checkpoint[11/18]", "No more user filter button available");
//		
//		panel.click(panel.pendingButton);
//		panel.assertEquals(panel.findingsNameList.size(), 2, "Checkpoint[12/18]", "Verifying that finding is accepted");		
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)), ViewerPageConstants.CIRCLE_FINDING_NAME, "Checkpoint[13/18]", "Verifying that circle is accepted");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)), ViewerPageConstants.LINEAR_FINDING_NAME, "Checkpoint[14/18]", "Verifying that circle is accepted");
//		
//		panel.openAndCloseOutputPanel(false);
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab(1).size(),3, "Checkpoint[15/18]", "Verifying that there are three clones");
//		results = contentSelector.getAllResultDetails();
//		
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[16/18]","verifying there is first clone created by "+userA);
//		contentSelector.assertTrue(results.get(1).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint[17/18]","verifying there is second clone created by "+userA);
//		contentSelector.assertTrue(results.get(2).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint[18/18]","verifying there is third clone created by "+userA);
//	
//	}
//	
//	@Test(groups ={"Chrome","IE11","Edge","DE1661","Negative"})
//	public void test35_DE1661_TC7154_verifyUserCloneStateNotChangedAfterAcceptGSPSMenu() throws InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify first user's copy/clone state should be retained after performing accept/reject from AR toolbar on it");
//
//		patientListPage = new PatientListPage(driver);
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//						
//		Header header = new Header(driver);		
//		header.logout();
//
//		loginPage.login(userA, userA);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		
//		patientListPage.clickOntheFirstStudy();
//
//		contentSelector=new ContentSelector(driver);
//		circle = new CircleAnnotation(driver);
//		circle.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Create a circle and change its state to pending" );		
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, -20, 30, 50, 50);
//		circle.selectAcceptfromGSPSRadialMenu();
//		
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[1/18]", "Verifying that circle is in pending state");
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab(1).size(),1, "Checkpoint[2/18]", "Verifying that there is one clone");
//		contentSelector.assertTrue(contentSelector.getAllResultDetails().get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[3/18]","verifying there is one clone created by "+userA);
//
//		header = new Header(driver);		
//		header.logout();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout and login using user 2." );		
//		
//		loginPage.login(userZ, userZ);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the AH.4 data in viewer - currently viewer is loaded in 1x1");
//		circle.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change the state of circle to rejected from AR toolbar. " );
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[1/9]", "Verifying that circle is in pending state");
//		List<HashMap<String, String>> results = contentSelector.getAllResultDetails();
//		contentSelector.assertEquals(results.size(),1, "Checkpoint[2/9]", "Verifying that there is one clone");
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[3/9]","verifying there is one clone created by "+userA);
//		circle.assertEquals(circle.getNumberOfCanvasForLayout(), 1, "Checkpoint[4/9]", "finding is loaded in 1x1 ");
//		
//		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
//	
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/9]", "Verifying that circle is in pending state");
//		results = contentSelector.getAllResultDetails();
//		contentSelector.assertEquals(results.size(),2, "Checkpoint[6/9]", "Verifying that there is one clone");
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[7/9]","verifying there is one clone created by "+userA);
//		contentSelector.assertTrue(results.get(1).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint[8/9]","verifying there is one clone created by "+userA);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the first clone created by user 1 from content selector ( which contains circle in pending state) " );
//		
//		contentSelector.selectResultFromSeriesTab(1, results.get(0).get(ViewerPageConstants.NAME));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[9/9]", "Verifying that circle is in pending state");
//		
//		}
//	
//	//@Test(groups ={"Chrome","IE11","Edge","DE1661","Negative"})
//	public void test36_DE1661_TC7155_verifyUserCloneStateNotChangedAfterAcceptAllSyncToPACs() throws InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify first user's copy/clone state should be retained after performing accept all/reject all on pending annotations through SendToPacs");
//
//		patientListPage = new PatientListPage(driver);
//		patientListPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		
//		createNewUser(userA, userA);
//		createNewUser(userZ, userZ);
//						
//		Header header = new Header(driver);		
//		header.logout();
//
//		loginPage.login(userA, userA);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		patientListPage.clickOntheFirstStudy();
//
//		contentSelector=new ContentSelector(driver);
//		circle = new CircleAnnotation(driver);
//		circle.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Create a circle and change its state to pending" );		
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, -20, 30, 50, 50);
//		circle.selectAcceptfromGSPSRadialMenu();
//		
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[1/15]", "Verifying that circle is in pending state");
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab(1).size(),1, "Checkpoint[2/15]", "Verifying that there is one clone");
//		contentSelector.assertTrue(contentSelector.getAllResultDetails().get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[3/15]","verifying there is one clone created by "+userA);
//
//		header = new Header(driver);		
//		header.logout();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Logout and login using user 2." );		
//		
//		loginPage.login(userZ, userZ);
//		patientListPage.waitForPatientPageToLoad();		
//		patientListPage.clickOnPatientRow(liverPatientName);
//		
//		patientListPage.clickOntheFirstStudy();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the AH.4 data in viewer - currently viewer is loaded in 1x1");
//		circle.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change the layout to 2x2 and create the another annotation (line) on source series 3 which is present in 3rd viewbox " );
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		
//		lineWithUnit = new MeasurementWithUnit(driver);
//		lineWithUnit.selectDistanceFromQuickToolbar(3);
//		lineWithUnit.drawLine(3, -50, -50, 30, 60);
//		lineWithUnit.selectAcceptfromGSPSRadialMenu();
//				
//		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(3, 1), "Checkpoint[4/15]", "Verifying that line is in pending state");
//		lineWithUnit.closingBannerAndWaterMark();
//		circle.assertTrue(circle.verifyCircleAnnotationIsPendingGSPS(1, 1), "Checkpoint[5/15]", "Verifying that circle is in pending state");
//				
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab(1).size(),2, "Checkpoint[6/15]", "Verifying that there are two clones");
//		List<HashMap<String, String>> results = contentSelector.getAllResultDetails();
//		
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[7/15]","verifying there is first clone created by "+userA);
//		contentSelector.assertTrue(results.get(1).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint[8/15]","verifying there is second clone created by "+userA);
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Enable all the filters from 'Sync to pacs' button");
//		sd=new ViewerSendToPACS(driver);
//		sd.openSendToPACSMenu();
//		sd.enableSendToPACSFindingOptions(true, true, true);
//		
//		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
//		circle.waitForTimePeriod(4000);
//
//		contentSelector.assertEquals(contentSelector.getAllResultsFromSeriesTab(1).size(),4, "Checkpoint[9/15]", "Verifying that there are four clones");
//		results = contentSelector.getAllResultDetails();
//		
//		contentSelector.assertTrue(results.get(0).get(ViewerPageConstants.OWNER).contains(userA),"Checkpoint[10/15]","verifying there is first clone created by "+userA);
//		
//		for (int i = 1; i < 4; i++) {
//			contentSelector.assertTrue(results.get(i).get(ViewerPageConstants.OWNER).contains(userZ),"Checkpoint["+(10+i)+"/15]","verifying there is second clone created by "+userA);
//			
//		}
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the first clone created by user 1 from content selector ( which contains circle in pending state) " );
//		contentSelector.selectResultFromSeriesTab(1, results.get(0).get(ViewerPageConstants.NAME));
//		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[15/15]", "Verifying that circle is in pending state");
//	
//	}
//	
//	//DE1640: [Hardening]: Machine/User filters are not showing correct result after 'Accept All/Reject All' action is performed on desktop
//	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1640"})
//	public void test37_DE1640_TC7115_verifyResultsWhenAcceptAllPerformOnFilters() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Machine/User filters are showing correct result after /'Accept All/' action been performed");
//
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);	
//		
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();
//		DICOMRT rt=new DICOMRT(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load patient "+patientNameDICOMRT+ " on viewer page." );
//		panel = new OutputPanel(driver);
//		rt.waitForDICOMRTToLoad();
//
//        int legendCount=rt.legendOptionsList.size();
//		circle = new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 30, 50, 60, 70);	
//
//		int findingCount=panel.getFindingsCountFromFindingTable();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify machine and user both filters are available.");
//	    panel.enableFiltersInOutputPanel(true, true, true);
//	    panel.assertFalse(panel.machineFiltersList.isEmpty(), "Checkpoint[1/11]", "Verified that machine filters visible in Output panel.");
//	    panel.assertFalse(panel.allUserFilterNameList.isEmpty(), "Checkpoint[2/11]", "Verified that user filters visible in Output panel.");
//	    panel.assertEquals(panel.thumbnailList.size(),findingCount, "Checkpoint[3/11]", "Verified count of thumbnail which contain user as well as machine finindgs in Output panel.");
//	    
//	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Disable user filter and verify selected and deselected property for both machine and user filter");
//	    panel.unselectUsersButton(1);
//	    verifyButtonsSelection(panel.machineFiltersList.get(0), "Checkpoint[4/11]", "Verified that machine button is selected.");
//	    verifyButtonsDeSelection(panel.usersFiltersList.get(0), "Checkpoint[5/11]", "Verified that user button is deslected");
//	    panel.assertEquals(panel.thumbnailList.size(),legendCount, "Checkpoint[6/11]", "Verified count of thumbnail after unselect of user filter button");
//	    
//	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Perform accept all for machine finding and verify finding state for machine finding in OP");
//	    panel.click(panel.acceptAllForMachine.get(0));
//	    //wait is added because after performing accept all ,OP took time to refresh the data.
//	    panel.waitForTimePeriod(5000);
//	    for(int i=0;i<panel.thumbnailList.size();i++)
//	    	panel.assertEquals(panel.getCssValue(panel.findingStateIndicator.get(i),NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Checkpoint[7."+(i+1)+"/11]", "Verifying that findings is turned into accepted after performin accept all for machine filter");	
//	    
//	   ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Enable user filter and disable machine filter then verify finding count in OP."); 
//	   panel.unselectUsersButton(1);
//	   panel.unselectMachineButton(1);
//	   panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[8/11]", "Verified that only user findings are visible in Output panel");
//	   
//	   ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "","Perform reject all on user finding and verify state of user finding along with deselection of machine filter button");
//	   panel.click(panel.getRejectAllMachineFilter(userName));
//	   panel.waitForTimePeriod(2000);
//	   panel.assertEquals(panel.getCssValue(panel.findingStateIndicator.get(0),NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.REJECTED_FINDING_COLOR, "Checkpoint[9/11]", "Verifying that findings is turned into rejected after performing reject  all for user filter");	
//	   panel.assertNotEquals(panel.thumbnailList.size(), legendCount, "Checkpoint[10/11]", "Verified that machine findings not visible in OP when machine filter is deselected");
//	   verifyButtonsDeSelection(panel.machineFiltersList.get(0), "Checkpoint[11/11]", "Verified that machine filter is deslected");
//	   
//		
//	}
//	
//	//DE1775: State of Accepted/Rejected/Pending filter and Machine/User button is not getting retained on Output panel refresh.
//    @Test(groups = { "Chrome", "IE11", "Edge", "DE1775", "Positive"})
//	public void test38_DE1775_TC7223_verifyStateOfMachiesUserFilterButton() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("State of Accepted/Rejected/Pending filter and Machine/User button is not getting retained on Output panel refresh.");
//		
//		// Loading the patient on viewer
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientNameDICOMRT);
//
//		patientListPage.clickOntheFirstStudy();
//
//		rt=new DICOMRT(driver);
//		panel = new OutputPanel(driver);
//		rt.waitForDICOMRTToLoad();
//	
//		int legendList=rt.legendOptionsList.size();
//		cs=new ContentSelector(driver);
//	
//		String machineName=cs.getMachineName(1).get(0);
//		
//		point=new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
//		
//		panel.enableFiltersInOutputPanel(true, true, true);	
//		panel.assertEquals(panel.thumbnailList.size(), legendList+1, "Checkpoint[1/7]", "Verified thumbnail count in Output panel .");
//		
//		panel.unselectUsersButton(1);
//		panel.assertEquals(panel.thumbnailList.size(), legendList, "Checkpoint[2/7]", "Verified that user created finding not visible after deselecting of user button.");
//		
//        panel.acceptAllMachineFilter(machineName);
//
//		panel.assertEquals(panel.thumbnailList.size(), legendList, "Checkpoint[3/7]", "Verified count of thumbnail after accept all perform for machine filter button.");
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.REJECTED_FINDING_COLOR, "Checkpoint[4/7]",  "Verified that state of rejected button");
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.PENDING_FINDING_COLOR, "Checkpoint[5/7]",  "Verified that state of pending button");
//
//		verifyButtonsSelection(panel.machineFiltersList.get(0), "Checkpoint[6/7]", "Verified that machine button is selected.");
//		verifyButtonsDeSelection(panel.usersFiltersList.get(0), "Checkpoint[7/7]", "Verified that user button is deslected");
//		}
//		
//    //DE1860: [Hardening]: User  is not able to deselect user button after deselecting machine filter button
//    @Test(groups = { "Chrome", "IE11", "Edge", "DE1860", "Positive"})
//   	public void test39_DE1860_TC7421_verifyDeselectionOfUserAndMachineFilter() throws InterruptedException{
//
//   		extentTest = ExtentManager.getTestInstance();
//   		extentTest.setDescription("Verify user is able to deselect user button after deselecting machine filter button");
//   		
//   		// Loading the patient on viewer
//   		patientListPage = new PatientListPage(driver);
//   		patientListPage.clickOnPatientRow(johnDoePatient);
//
//   		patientListPage.clickOntheFirstStudy();
//
//   		panel = new OutputPanel(driver);
//   		panel.waitForViewerpageToLoad(4);
//   	
//   		cs=new ContentSelector(driver);
//        List<String>resultName=cs.getAllResultsFromSeriesTab();
//   		
//   		//draw point annotation by scan user
//   		point=new PointAnnotation(driver);
//   		point.selectPointFromQuickToolbar(4);
//   		point.drawPointAnnotationMarkerOnViewbox(4, 50, 50);
//   		List<WebElement> findingName1=panel.getStateSpecificFindings(4, ViewerPageConstants.ACCEPTED_COLOR);
//		
//   		//create new user "UserA"
//   		panel.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//	    RegisterUserPage register = new RegisterUserPage(driver);
//	    register.waitForRegisterPageToLoad();
//	    register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
//	    
//	    loginPage = new LoginPage(driver);
//   		loginPage.logout();
//		loginPage.navigateToBaseURL();
//
//		//login with second user
//		loginPage.login(userA,userA);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(johnDoePatient);
//		patientListPage.clickOntheFirstStudy();	
//		panel.waitForViewerpageToLoad(1);
//		
//		//change layout
//		layout=new ViewerLayout(driver);
//		layout.selectLayout(layout.twoByTwoLayoutIcon);
//		cs.closingBannerAndWaterMark();
//		cs.selectSeriesFromSeriesTab(2, seriesName);
//		
//		//draw annotation on source series from second user 
//		circle=new CircleAnnotation(driver);
//		circle.selectCircleFromQuickToolbar(2);
//		circle.drawCircle(2, 70, 70, 80, 80);
//		
//		List<WebElement> findingName2=panel.getStateSpecificFindings(2, ViewerPageConstants.ACCEPTED_COLOR);
//		
//		//disable machine filter
//		panel.enableFiltersInOutputPanel(true, true, true);
//		int totalFinding=panel.thumbnailList.size();
//		panel.unselectMachineButton(1);
//		verifyButtonsDeSelection(panel.machineFiltersList.get(0), "Checkpoint[1/29]", "Verified that machine button is deselected.");
//		panel.assertEquals(panel.thumbnailList.size(), findingName1.size()+ findingName2.size(), "Checkpoint[2/29]", "Verified only user findings visible in Output panel after deselecting machine filter");
//		
//		//disable first user filter
//		panel.unselectUsersButton(1);
//		verifyButtonsDeSelection(panel.usersFiltersList.get(0), "Checkpoint[3/29]", "Verified that first user button is deslected");
//		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[4/29]", "Verified only second user findings visible in Output panel after deselecting machine and first user filter");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName2.size()),"Checkpoint[5/29]","Verified series name and finding count for the second user finding.");
//   		
//		//enable first user filter
//		panel.unselectUsersButton(1);
//	    verifyButtonsSelection(panel.usersFiltersList.get(0), "Checkpoint[6/7]", "Verified that first user button is selected");
//		panel.assertEquals(panel.thumbnailList.size(), findingName1.size()+findingName2.size(), "Checkpoint[7/29]", "Verified both user findings visible in Output panel after deselecting machine and selecting first user filter");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName1.size()+findingName2.size()),"Checkpoint[8/29]","Verified series name and finding count for the both users finding.");
//   		
//        //disable second user filter
//		panel.unselectUsersButton(2);
//		verifyButtonsDeSelection(panel.usersFiltersList.get(1), "Checkpoint[9/29]", "Verified that second user button is deslected");
//		panel.assertEquals(panel.thumbnailList.size(), findingName1.size(), "Checkpoint[10/29]", "Verified only first user findings visible in Output panel after deselecting machine and second user filter");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName1.size()),"Checkpoint[11/29]","Verified series name and finding count for the first user finding.");
//   		
//		//enable second user filter
//		panel.unselectUsersButton(2);
//		verifyButtonsSelection(panel.usersFiltersList.get(1), "Checkpoint[12/29]", "Verified that second user button is selected");
//	    panel.assertEquals(panel.thumbnailList.size(), findingName1.size()+1, "Checkpoint[13/29]", "Verified both user findings visible in Output panel after deselecting machine and selecting second user filter");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName1.size()+findingName2.size()),"Checkpoint[14/29]","Verified series name and finding count for both user findings.");
//   		
//	    //enable machine filter
//	    panel.unselectMachineButton(1);
//	    verifyButtonsSelection(panel.machineFiltersList.get(0), "Checkpoint[15/29]", "Verified that machine filter selected.");
//	    panel.assertEquals(panel.thumbnailList.size(), totalFinding, "Checkpoint[16/29]", "Verified machine and both user finding count in Output panel.");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName1.size()+findingName2.size()),"Checkpoint[17/29]","Verified series name and finding count for both user findings along with machine finding.");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(0), 1), "Checkpoint[18/29]", "Verified first result name and result count in Output panel");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(1), 1), "Checkpoint[19/29]", "Verified second result name and result count in Output panel");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(2), 1), "Checkpoint[20/29]", "Verified third result name and result count in Output panel");
//	    
//		//disable first user filter
//		panel.unselectUsersButton(1);
//		verifyButtonsDeSelection(panel.usersFiltersList.get(0), "Checkpoint[21/29]", "Verified that first user button is deslected");
//		panel.assertEquals(panel.thumbnailList.size(), resultName.size()+findingName2.size(), "Checkpoint[22/29]", "Verified  second user finding and machine finding visible in Output panel after deselecting first user filter");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName, findingName2.size()),"Checkpoint[23/29]","Verified series name and finding count for the second user finding.");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(0), findingName2.size()), "Checkpoint[24/29]", "Verified first result name and result count in Output panel");
//		panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(1), findingName2.size()), "Checkpoint[25/29]", "Verified second result name and result count in Output panel");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(resultName.get(2), findingName2.size()), "Checkpoint[26/29]", "Verified third result name and result count in Output panel");
//	    
//	    //disable machine filter
//	    panel.unselectMachineButton(1);
//	    verifyButtonsDeSelection(panel.machineFiltersList.get(0), "Checkpoint[27/29]", "Verified that machine button is deslected");
//	    panel.assertEquals(panel.thumbnailList.size(),findingName2.size(), "Checkpoint[28/29]", "Verified second user finding count in Output panel.");
//	    panel.assertTrue(panel.verifySeriesDescAndFindingCount(seriesName,findingName2.size()),"Checkpoint[29/29]","Verified series name and finding count for second user findings.");
//   		
//    
//    }
//    
//	private void createNewUser(String username,String pwd) {
//
//		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//
//		register=new RegisterUserPage(driver);
//		register.waitForRegisterPageToLoad();
//		register.createNewUser(username, username, LoginPageConstants.SUPPORT_EMAIL, username, pwd, pwd);
//
//
//	}
//	
//	//	@AfterMethod
//	//	public void updatDxTimeStamp() {
//	//
//	//		DatabaseMethods db = new DatabaseMethods(driver);
//	//
//	//		try {
//	//			List<String> machineUID = db.getMachineUIDFromMachinesTable(dxResultPatient);
//	//			String query1 ="select LastUpdate from batchMachines where batchID="+machineUID.get(0);
//	//			String query2 ="select LastUpdate from batchMachines where batchID="+machineUID.get(1);
//	//			String value1 = db.getValue(query1);
//	//			String value2 = db.getValue(query2);
//	//
//	//			query1 ="UPDATE [dbo].[BatchMachines] SET [LastUpdate] = '"+value2+"' where batchID="+machineUID.get(0);
//	//			query2 ="UPDATE [dbo].[BatchMachines] SET [LastUpdate] = '"+value1+"' where batchID="+machineUID.get(1);
//	//
//	//			db.updateValue(query1);
//	//			db.updateValue(query2);
//	//
//	//		} catch (SQLException e) {
//	//			// TODO Auto-generated catch block
//	//			e.printStackTrace();
//	//		}
//	//	}
//
//
//}