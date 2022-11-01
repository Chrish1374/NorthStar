package com.trn.ns.test.viewer.envoyAI;


import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class UserActionTrackingTest extends TestBase{
	
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	
	private ViewerPage viewerPage;

	//patient detail with multiple series data
	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String gspsFilePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath);
	
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	private EllipseAnnotation ellipse;
	private OutputPanel panel;
	private ContentSelector cs;
	private PointAnnotation point;
	private PolyLineAnnotation poly;
	private RegisterUserPage register;
	static String patient_id="";
	String user=Configurations.TEST_PROPERTIES.get("nsUserName");
	String username = "sendtopacs";
	private HelperClass helper;
	private DICOMRT drt;
	private ViewerSendToPACS sd;
	private ViewerLayout layout;
	
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//US996: Do not save the changes made to the results of a study when track my changes setting is off
	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test01_US996_TC4672_verifyCloneWhenTrackChangesDisableForGSPS() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new result copy should not get created & save when Track my change is off from UI");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName, 1, 1);
		
		point=new PointAnnotation(driver);
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd=new ViewerSendToPACS(driver);
		
		int resultCount=cs.getAllResults().size();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);
		
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[1/4]", "Verify clone copy in content selector after editing annotation");
	
		helper.browserBackAndReloadViewer(gspsPatientName, "", 1, 1);
		
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[2/4]", "Verify previous entry not seen in Content selector after reloading viewer page when track changes off");
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[3/4]", "Verify clone copy in content selector after editing annotation on viewer reload");
		
		viewerPage.click(viewerPage.getViewPort(1));
		loginPage.logout();
		loginPage.login(user, Configurations.TEST_PROPERTIES.get("nsPassword")	);
		viewerPage = helper.loadViewerPage(gspsPatientName, "", 1, 1);

		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[4/4]", "Verify previous clone not visible when again login into application");
       
	
	}

	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test02_US996_TC4695_verifyCloneWhenTrackChangesDisableForRTAndGSPS() throws  InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that non of result copies should not get save when Track my change is off");

		helper = new HelperClass(driver);
		drt = helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT, 1, 1);
		
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		
		ellipse=new EllipseAnnotation(driver);
		loginPage=new LoginPage(driver);
		sd=new ViewerSendToPACS(driver);
	
		int resultCount=cs.getAllResults().size();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+" in viewer" );	
		sd.enableSendToPACSUserActionTracking(false);
		
		String resultName=cs.getAllResults().get(0);
		
		drt.navigateToFirstContourOfSegmentation(2);
		drt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		drt.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +user+"_1"), "Checkpoint[1/3]", "Verified clone copy for machine after editing contour for RT data ");
	
		drt.click(drt.getViewPort(1));
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		drt.waitForViewerpageToLoad();
		cs.selectResultFromSeriesTab(2, resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +user+"_1");
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		drt.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+user+"_1"), "Checkpoint[2/3]", "Verified clone copy for GSPS after drawing annotation on RT data");
		
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);

		drt.assertEquals(cs.getAllResults().size(),resultCount,"Checkpoint[3/3]", "Verify previous entry not seen in Content selector after reloading viewer page when track changes off");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test03_US996_TC4680_verifyCloneWhenTrackChangesOffFromUIForRT() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("RT data : Verify that new result copy should not get created & save when Track my change is off from UI");

		helper = new HelperClass(driver);
		drt = helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT, 1, 1);

		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		
		ellipse=new EllipseAnnotation(driver);
		poly=new PolyLineAnnotation(driver);
		loginPage=new LoginPage(driver);
	
		int resultCount=cs.getAllResults().size();
		String resultName=cs.getAllResults().get(0);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);
	
	    int []coordinates = new int[] {-40, 50};
		poly = new PolyLineAnnotation(driver);
		List<WebElement> handles = poly.getLinesOfPolyLine(1, 1);
		poly.editPolyLine(handles.get(2), coordinates,handles.get(9));
		cs.waitForTimePeriod(10000);
		drt.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +user+"_1"), "Checkpoint[1/3]", "Verified clone copy for machine after editing contour for RT data ");
		
		
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);
		
		drt.rejectSegment(3);
		drt.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +user+"_1"), "Checkpoint[2/3]", "Verify new entry in content selector after editing contour for machine after reload");
		
		loginPage.logout();
		loginPage.login(user, Configurations.TEST_PROPERTIES.get("nsPassword")	);
		patientPage=new PatientListPage(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT, 1, 1);
		drt.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[3/3]", "Verify previous entry not seen in Content selector after reloading viewer page when track changes off");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test04_US996_TC4679_verifyCloneWhenTrackChangesDisableForDifferentUser() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that other user should not be able  see the new result copy created by first user when Track my change is off from UI");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName, 1, 1);

		point=new PointAnnotation(driver);
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd=new ViewerSendToPACS(driver);
	
		int resultCount=cs.getAllResults().size();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);
		
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[1/2]", "Verify clone copy in content selector after rejecting the machine drawn annotation");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user");
 		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
 		register=new RegisterUserPage(driver);
 		register.createNewUser(username, "" ,username , LoginPageConstants.SUPPORT_EMAIL, username, username, username);
 		Header header = new Header(driver);
 		header.logout();
 		loginPage=new LoginPage(driver);
 		loginPage.login(username, username);
 		patientPage.waitForPatientPageToLoad();
 		patientPage.clickOnPatientRow(gspsPatientName);
 		patientPage.clickOntheFirstStudy();
 		viewerPage.waitForViewerpageToLoad();
 		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[2/2]", "Verify previous entry created by Scan user not visible to Second user");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test05_US996_TC4676_verifyCloneInOutputPanelWhenTrackChangesDisableForMachine() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can see new temporary result copy results in output panel even when Track my change is off from UI");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		
		point=new PointAnnotation(driver);
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd=new ViewerSendToPACS(driver);
	
		int resultCount=cs.getAllResults().size();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);
		
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[1/6]", "Verify clone copy in content selector after rejecting annotation");
		panel.enableFiltersInOutputPanel(false, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[2/6]", "Verified thumbnail count in rejected tab for Output panel ");
		
		helper.browserBackAndReloadViewer(gspsPatientName,  1, 1);
		
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[3/6]", "Verify previous entry not seen in Content selector after reloading viewer page");
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon( ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[4/6]", "Verify clone copy in content selector after accepting annotation ");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[5/5]", "Verified thumbnail count in accepted tab for Output panel ");
		
		loginPage.logout();
		loginPage.login(user, Configurations.TEST_PROPERTIES.get("nsPassword")	);
		
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[6/6]", "Verify previous entry not seen in Content selector after login into application");
       
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test06_US996_TC4674_verifyCloneWhenTrackChangesEnableFromUIForMachine() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new result copy should not get created & save when Track my change is off from UI");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		
		point=new PointAnnotation(driver);
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd=new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[1/5]", "Verify clone after rejecting annotation");
	
		helper.browserBackAndReloadViewer(gspsPatientName,  1, 1);
		
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_1"), "Checkpoint[2/5]", "Verify previous clone in content selector after reload of viewer page");
		
		viewerPage.click(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_2"), "Checkpoint[3/5]", "Verify clone after accepting annotation");
		
		loginPage.logout();
		loginPage.login(user, Configurations.TEST_PROPERTIES.get("nsPassword")	);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+user+"_2"), "Checkpoint[4/5]", "Verify new clone in content selector after editing annotation when relogin into application");
		viewerPage.assertEquals(cs.getAllResults().size(),6, "Checkpoint[5/5]", "Verify previous entry seen in Content selector when relogin into application");
       
	
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{
		//DB Default configuration
		 DatabaseMethods db = new DatabaseMethods(driver);
		 db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
	     db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
	     db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
	     db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
	     db.deleteUser(username);
	     db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}
	
}

	
	
	
	
	

	
	

	

	

	


	

	

