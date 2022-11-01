package com.trn.ns.test.viewer.dicomRT;

import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
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
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSForDICOMRTTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private HelperClass helper;

	//patient detail with multiple series data
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	
	String GSPS_filePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNameGSPS = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, GSPS_filePath);
	
	
	String RTStruct_With_2Machine_Filepath = Configurations.TEST_PROPERTIES.get("RTStruct_With_2Machine_Filepath");
	String patientNameRTWith2Machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, RTStruct_With_2Machine_Filepath);

	
	private CircleAnnotation circle;
	static String patient_id="";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	private ViewerSendToPACS sd;
	private OutputPanel panel;
	private DatabaseMethods db;
	private DICOMRT rt;
	private ViewerLayout layout;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test01_US1055_TC5595_verifySendToPACSForGSPSWithoutAnyChange() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS GSPS when only Machine result are sent without any modifications");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(patientNameGSPS, 1);
     
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		db=new DatabaseMethods(driver);
		cs=new ContentSelector(driver);

		int resultCountFromCS=cs.getAllResults().size();
		panel.click(panel.getViewPort(2));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameGSPS);
		//take a count of Findings from Output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		List<WebElement> pendingFindings = panel.thumbnailList;
		int pendingCount=pendingFindings.size();
		panel.openAndCloseOutputPanel(false);
		String expectedMessage =(pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+pendingCount+" as pending )";
		
		//take count of WiaResultElement Table from DB
		int ResultCountFromDB=db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		
		//9 findings are sent to PACS ( 9 as pending )

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data without any modification");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Checkpoint[1/5]","Verifying the finding count from message");	
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1.1/5]","Verifying the error message after send to pacs.");	
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verifying the orthanc for findings");
		sd.verifyOrthancFindings(patientNameGSPS, expectedMessage, "", 1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "");
		
		panel.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE), ResultCountFromDB, "Checkpoint[4/5]", "Verified count of WiaResultElement are same");
		
		cs.openAndCloseSeriesTab(true);
		panel.assertEquals(cs.getAllResults().size(), resultCountFromCS, "Checkpoint[5/5]", "Verified count of result are same after send to PACS for GSPS in content selector");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1055","positive","BVT"})	
	public void test02_US1055_TC5549_verifySendToPACSForRTWithoutAnyChange() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT struct  when findings are sent without any modifications (Single Machine)");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);
	   
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		db=new DatabaseMethods(driver);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		//take a count of Findings from Output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		List<WebElement> pendingFindings = panel.thumbnailList;
		int pendingCount=pendingFindings.size();
		panel.openAndCloseOutputPanel(false);
		String expectedMessage =(pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+pendingCount+" as pending )";
		
		//take count of WiaResultElement Table from DB
		int ResultCount=db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data without any modification");
		sd.openSendToPACSMenu(true,true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
	    panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Checkpoint[1.1/4]","Verifying the finding count from message");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1.2/4]","Verifying the error message after send to pacs.");	

		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		//9 findings are sent to PACS ( 9 as pending )	
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,"", currentSelectResult, 1);
		
		panel.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE), ResultCount, "Checkpoint[4/4]", "Verified count of WiaResultElement are same");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test03_US1055_TC5552_verifySendToPACSForUserGeneratedClone() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct  - When User generated Clone results are in the background (Cloning editable series that are in output Panel and have pending findings)");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);     
		panel=new OutputPanel(driver);
		rt = new DICOMRT(driver);
		sd=new ViewerSendToPACS(driver);
		
		cs=new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		String seriesName=panel.getSeriesDescriptionOverlayText(1);
		cs.openAndCloseSeriesTab(true);
		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +username+"_1";

		//reject contour and verify clone 
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone), "Checkpoint[1/7]", "Verified clone copy for machine after editing contour for RT data ");

		//take a count of Findings from Output panel after edit contour
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount=panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount=panel.thumbnailList.size();
		panel.assertEquals(rejectedCount, 1, "Checkpoint[2/7]", "Verified finding count in Rejected tab of Output panel");
		panel.openAndCloseOutputPanel(false);
		
		//reload viewer page and Load original Series for RT
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		cs.selectSeriesFromSeriesTab(1, seriesName);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[3/7]", "Verified that original source series is loaded in viewerpage");
		
		//take a count of Findings from Output panel after original source series loaded
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(rejectedCount, 1, "Checkpoint[4.1/7]", "Verified finding count in Rejected tab of Output panel");
		panel.openAndCloseOutputPanel(false);
		//send to PACS for RT data
		String expectedMessage =(pendingCount+rejectedCount)+" "+ ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+(pendingCount+rejectedCount)+" as rejected )";
		
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Checkpoint[4.2/7]","Verifying the finding count from message");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[4.3/7]","Verifying the error message after send to pacs.");	

		//take count from Output panel after send to PACS(Select Reject All option from Pending finding dialog)
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCountAfterSendToPACS=panel.thumbnailList.size();
	
		//9 findings are sent to PACS ( 9 as pending )
		panel.assertEquals((rejectedCountAfterSendToPACS),(pendingCount+rejectedCount),"Checkpoint[5/7]","Verifying the finding count from message when Reject all option is selected");
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		panel.waitForTimePeriod(3000);
		String currentSelectResult = cs.getAllResults().get(cs.getAllResults().size()-1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/7]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,resultName, currentSelectResult, 1);
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test04_US1055_TC5551_verifySendToPACSForMultipleCloneFromSingleBaseSeries() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct  - When User generated Clone results are in the background (Cloning non-editable series that are in output Panel and have pending findings)");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);     
		   
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		rt = new DICOMRT(driver);
		cs=new ContentSelector(driver);
		rt.waitForDICOMRTToLoad();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		String seriesName=panel.getSeriesDescriptionOverlayText(1);
		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +username;
		
		//edit contour and verify clone in CS
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[1/8]", "Verified clone copy for machine after editing contour for RT data ");

		//reload viewer and Load Clone copy
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
	    cs.selectResultFromSeriesTab(1,machineClone+"_1");
	    
	    //edit contour on previous clone copy
	    rt.navigateToFirstContourOfSegmentation(2);
		rt.selectAcceptfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_2"), "Checkpoint[2/8]", "Verified  new clone copy for machine after editing contour for RT data on existing clone");

		//take a count of finding from Finding menu
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> acceptedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		int pendingCount = pendingFindings.size();
		sd = new ViewerSendToPACS(driver);
		
		String expectedMessage =(pendingCount+rejectedCount+acceptedCount)+" "+ ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+(pendingCount+rejectedCount+acceptedCount)+" as accepted )";
		
		//take a count of Findings from Output panel after edit contour on Clone copy
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		verifyFindingsInOP(acceptedCount,rejectedCount,pendingCount,"Checkpoint[3/8]");
		
		//Load original series from Content selector
		//set of code need to remove once defect resolved
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		cs.selectSeriesFromSeriesTab(1, seriesName);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[4/8]", "Verified that original source series is loaded in viewerpage");
		
		//take a count of Findings from Output panel after original source series loaded
		verifyFindingsInOP(acceptedCount,rejectedCount,pendingCount,"Checkpoint[5.1/8]");
		
		//verify send to PACS
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Checkpoint[5.2/7]","Verifying the finding count from message");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[5.3/7]","Verifying the error message after send to pacs.");	

		//reload viewer page
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCountAfterSendToPACS=panel.thumbnailList.size();
		
		//9 findings are sent to PACS ( 9 as accepted )
		panel.assertEquals(acceptedCountAfterSendToPACS,(pendingCount+rejectedCount+acceptedCount),"Checkpoint[6/8]","Verifying the finding count from message when accept all option is selected");		
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/8]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/8]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,resultName, currentSelectResult, 1);
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test05_US1055_TC5589_verifySendToPACSWhenCloneIsNonEditableMode() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct  - When User generated Clone results are in the foreground and in non-editable mode");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);    
		panel=new OutputPanel(driver);
		rt = new DICOMRT(driver);
	
		sd=new ViewerSendToPACS(driver);
		cs=new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username;
		
		//edit contour and verify CS
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[1/7]", "Verified clone copy for machine after editing contour for RT data ");

		//after reload verify clone copy is Selected in CS
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[2/7]", "Verified clone copy for machine is selected after reload of Viewer page");
	    panel.enableFiltersInOutputPanel(false, true, false);
	    panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/7]", "Verified rejected contour in Output panel");
	    panel.openAndCloseOutputPanel(false);
	    
	    //send to PACS after reload
	    sd.openSendToPACSMenu(true,true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
	    String message=sd.getText(sd.notificationMessage.get(0));
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[4.1/7]","Verifying the error message after send to pacs.");	
		
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		//take a count from Finding menu after editing contour
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state
		List<WebElement> acceptedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		
		//9 findings are sent to PACS ( 8 as accepted and 1 rejected )
		panel.assertEquals(message,(acceptedCount+rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Checkpoint[4.2/7]","Verifying the finding count from message when accept all option is selected for Pending RT legend");		
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/7]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/7]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,resultName, currentSelectResult, 2);
		
		cs.selectResultFromSeriesTab(1, machineClone+"_2");
		panel.openFindingTableOnBinarySelector(1);
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),rejectedFindings.size(),"Checkpoint[7.1/7]","Data is loaded and displays the correct states of the findings.");
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),acceptedFindings.size(),"Checkpoint[7.2/7]","Data is loaded and displays the correct states of the findings.");
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),pendingFindings.size(),"Checkpoint[7.3/7]","Data is loaded and displays the correct states of the findings.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test06_US1055_TC5590_verifySendToPACSWhenCloneInEditableMode() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct  - When User generated Clone results are in the foreground and in editable mode");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);        
		panel=new OutputPanel(driver);
		rt = new DICOMRT(driver);
		sd=new ViewerSendToPACS(driver);
		
		cs=new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE +username;
		
		//reject contour and verify clone
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[1/9]", "Verified clone copy for machine after editing contour for RT data ");

		//verify in Output panel
	    panel.enableFiltersInOutputPanel(false, true, false);
	    panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[2/9]", "Verified rejected contour in Output panel");
	
	    //verify in Finding menu
	    panel.openAndCloseOutputPanel(false);
	    panel.openFindingTableOnBinarySelector(1);
	    panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Checkpoint[3.1/9]","Data is loaded and displays the correct states of the findings.");
	    panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),8,"Checkpoint[3.2/9]","Data is loaded and displays the correct states of the findings.");
	    
		//verify send to PACS
	    sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		String message=sd.getText(sd.notificationMessage.get(0));
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[5.1/9]","Verifying the error message after send to pacs.");
		
		//verify no new clone is created
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"),"Checkpoint[4/9]", "Verified no new clone is created after selecting Reject all from Pending finding dialog");
		
		//take a count from Finding menu after editing contour
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state
		List<WebElement> acceptedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int rejectedCount = rejectedFindings.size();
		
		//9 findings are sent to PACS ( 9 rejected )
		panel.assertEquals(message,(rejectedCount)+" findings are sent to PACS ( "+rejectedCount+" as rejected )","Checkpoint[5.2/9]","Verifying the finding count from message when Reject all option is selected");		
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/9]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/9]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,"", currentSelectResult, 1);
		
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),rejectedFindings.size(),"Checkpoint[8.1/9]","Data is loaded and displays the correct states of the findings.");
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),acceptedFindings.size(),"Checkpoint[8.2/9]","Data is loaded and displays the correct states of the findings.");
		panel.assertEquals(rt.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),pendingFindings.size(),"Checkpoint[8.3/9]","Data is loaded and displays the correct states of the findings.");
		
		for(int i=0;i<rt.legendOptionsList.size();i++)
		{
			panel.assertTrue(rt.verifyRejectedRTSegment(i+1),"Checkpoint[9/9]","Verifying state icon is accepted for RT legend");	
		}
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test07_US1055_TC5597_verifySendToPACSForRTAndGSPS() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct + GSPS findings");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);      
		panel=new OutputPanel(driver);
		rt = new DICOMRT(driver);
		sd=new ViewerSendToPACS(driver);
		
		cs=new ContentSelector(driver);
		circle=new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";
		String gspsClone=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";
		int legendOptions=rt.legendOptionsList.size();
		
		//draw GSPS and verify clone 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(gspsClone), "Checkpoint[1/5]", "Verified clone copy for GSPS after drawing circle annotation");

		//verify in Output panel
	    panel.enableFiltersInOutputPanel(true, false, true);
	    panel.assertEquals(panel.thumbnailList.size(), legendOptions+1, "Checkpoint[2.1/5]", "Verified drawn annotation as Accepted in Output panel");
	    panel.openAndCloseOutputPanel(false);
	    
	    //verify send to PACS for GSPS and RT 
	    sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		String message=sd.getText(sd.notificationMessage.get(0));
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2.2/9]","Verifying the error message after send to pacs.");
	
		//take a count from Finding menu after editing contour
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state
		List<WebElement> acceptedFindings = panel.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);

		//Getting all the findings counts based on state
		int rejectedCount = rejectedFindings.size();
		int acceptedCount=acceptedFindings.size();
		
		//9 findings are sent to PACS ( 9 rejected )
		panel.assertEquals(message,(rejectedCount+acceptedCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Checkpoint[3/5]","Verifying the finding count from message when Reject all option is selected for RT data");		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,gspsClone, machineClone, 2);
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test08_US1055_TC5592_verifySendToPACSForRTWithOneMachineIsInEditableMode() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct data - When we have two Machine Results, both are loaded in the viewer and only one result is in the editable mode.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameRTWith2Machine, 1, 1);     	     
		panel=new OutputPanel(driver);
		rt = new DICOMRT(driver);
		
		sd=new ViewerSendToPACS(driver);
		
		cs=new ContentSelector(driver);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameRTWith2Machine);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		rt.waitForDICOMRTToLoad(1);
		
		//take result name and legend option list for both the viewbox
		String resultName1=cs.getAllResults().get(1);

		String machineClone=resultName1+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username;
		
		int legendOptions=rt.legendOptionsList.size();
		
		//reject contour for first machine and verify clone
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		rt.closingConflictMsg();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[1/11]", "Verified new clone created  after editing contour for RT data for first machine result");
	
		//verify clone cpoy for both machine in content selector
		panel.assertEquals(cs.getAllResults().size(), 3, "Checkpoint[2/11]", "Verified that new clone copy created for edited machine result specified");
	    
		//verify changes in output panel
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Output panel after editing the contour for the first machine result");
		verifyFindingsInOP(0,1,17,"Checkpoint[3/11]");
		
		//take a count from Finding menu after selecting reject all option
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Finding dropdown menu for the first viewbox");
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state from first viewbox
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int rejectedCount =rejectedFindings.size();
		int pendingCount=pendingFindings.size();
		panel.assertEquals(rejectedCount, 1, "Checkpoint[4/11]", "Verified that rejected contour in Finding dropdown menu for the first viewbox");
		panel.assertEquals(pendingCount, 8, "Checkpoint[5/11]", "Verified that pending contour in Finding dropdown menu for the first viewbox");
		
		//take a count from Finding menu after selecting reject all option
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Finding dropdown menu for second viewbox");
		panel.click(panel.getViewPort(2));
		panel.openFindingTableOnBinarySelector(2);
		//Getting all the findings based on state from first viewbox
		List<String> pendingFindings1 = rt.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int pendingCount1=pendingFindings1.size();
		panel.assertEquals(pendingCount1, legendOptions/2, "Checkpoint[6.1/11]", "Verified that pending contour in Finding dropdown menu for the second viewbox");
		
	    //verify send to PACS for RT with 2 machine result and one is in editable mode
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		String message=sd.getText(sd.notificationMessage.get(0));
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[6.2/11]","Verifying the error message after send to pacs.");
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		
		//take a count from Finding menu after selecting accept all option
		panel.click(panel.getViewPort(2));
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state from first machine result
		List<String> acceptedFindings2 = rt.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
	    List<String> rejectedFindings2 = rt.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);

		//Getting all the findings counts based on state
	    int acceptedCount2 =acceptedFindings2.size();
	    int rejectedCount2 =rejectedFindings2.size();
	    
	    panel.click(panel.getViewPort(2));
	    panel.click(panel.getViewPort(1));
		panel.click(panel.getViewPort(2));
		panel.openFindingTableOnBinarySelector(2);
		panel.openFindingTableOnBinarySelector(2);
		//Getting all the findings based on state from first machine result
		List<String> acceptedFindings3 = rt.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		//Getting all the findings counts based on state
	    int acceptedCount3 =acceptedFindings3.size();
			    
		//9 findings are sent to PACS ( 9 rejected )
		panel.assertEquals(message,(legendOptions)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+(acceptedCount2+acceptedCount3+1)+" as accepted, "+rejectedCount2+" as rejected )","Checkpoint[7/11]","Verifying the finding count from message when Reject all option is selected for RT data");		

		//after send to PACS verify no new clone is created
		panel.assertEquals(cs.getAllResults().size(), 4, "Checkpoint[8/11]", "Verified that new clone copy created after send to PACS when one machine is in non-editable mode");
	    
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/11]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[10/11]", "Verifying the orthanc for findings for both the machine result");
		verifyOrthancFindingsForMultipleMachines(patientNameRTWith2Machine,username,machineClone+"_1",machineClone+"_2", 3);

		//Verify changes in Output panel after send to PACS with option as "Reject All"
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Output panel after send to PACS");
		verifyFindingsInOP(acceptedCount2+acceptedCount3+1,rejectedCount2,0,"Checkpoint[11/11]");

	
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test09_US1055_TC5591_verifySendToPACSForRTWithBothMachineIsInEditableMode() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct data when we have two Machine Results and both are in editable mode.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameRTWith2Machine, 1, 1);    
		rt = new DICOMRT(driver);
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		rt = new DICOMRT(driver);
		cs=new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameRTWith2Machine);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		rt.waitForDICOMRTToLoad();
		
		//take a result name of first machine  and Legend options for both the viewbox
		String resultName=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, RTStruct_With_2Machine_Filepath);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username;
		int legendOptions=rt.legendOptionsList.size();
		
		//reject contour for first machine and verify clone
		panel.click(panel.getViewPort(1));
		panel.waitForAllChangesToLoad();
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectRejectfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_1"), "Checkpoint[1/14]", "Verified clone copy for first machine after editing contour for RT data ");
	
		//accept contour for second machine and verify clone
		panel.doubleClick(panel.getViewPort(2));
		rt.navigateToFirstContourOfSegmentation(2);
		rt.selectAcceptfromGSPSRadialMenu();
		cs.waitForTimePeriod(15000);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(machineClone+"_2"), "Checkpoint[2/14]", "Verified clone copy for second machine after editing contour for RT data ");
				
		//verify clone cpoy for both machine in content selector
		panel.doubleClick(panel.getViewPort(2));
		panel.closingConflictMsg();
		panel.assertEquals(cs.getAllResults().size(), 4, "Checkpoint[3/14]", "Verified that new clone copy created for each of machine result specified");
	    
		//verify changes in output panel
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Output panel after editing 2 machine results for RT data");
		verifyFindingsInOP(1,1,16,"Checkpoint[4/14]");
		
		//take a count from Finding menu after selecting reject all option
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Finding dropdown menu of First viewbox");
		panel.click(panel.getViewPort(1));
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state from first viewbox
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int rejectedCount =rejectedFindings.size();
		int pendingCount=pendingFindings.size();
		panel.assertEquals(rejectedCount, 1, "Checkpoint[5/14]", "Verified that rejected contour in Finding dropdown menu in first viewbox");
		panel.assertEquals(pendingCount, 8, "Checkpoint[6/14]", "Verified that pending contour in Finding dropdown menu in first viewbox");
		
		//take a count from Finding menu after selecting reject all option
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Finding dropdown menu of second viewbox");
		panel.click(panel.getViewPort(1));
		panel.click(panel.getViewPort(2));
		panel.click(panel.getViewPort(2));
		panel.openFindingTableOnBinarySelector(2);
		
		//Getting all the findings based on state from first viewbox
		List<WebElement> acceptedFindings1 = rt.getStateSpecificFindings(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> pendingFindings1 = rt.getStateSpecificFindings(2,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int acceptedCount1 =acceptedFindings1.size();
		int pendingCount1=pendingFindings1.size();
		
		panel.assertEquals(acceptedCount1, 1, "Checkpoint[7/14]", "Verified that accepted contour in Finding dropdown menu in second viewbox");
		panel.assertEquals(pendingCount1, 8, "Checkpoint[8/14]", "Verified that pending contour in Finding dropdown menu in second viewbox");
		
	    //verify send to PACS for RT with 2 machine result with option as "Accept All"
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
	
		String message=sd.getText(sd.notificationMessage.get(0));
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[9.1/14]","Verifying the error message after send to pacs.");
		
		//take a count from Finding menu after selecting accept all option
		panel.click(panel.getViewPort(2));
		panel.openFindingTableOnBinarySelector(2);
		//Getting all the findings based on state from first viewbox
		List<WebElement> acceptedFindings2 = rt.getStateSpecificFindings(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		//Getting all the findings counts based on state
	    int acceptedCount2 =acceptedFindings2.size();
				
		//9 findings are sent to PACS ( 9 rejected )
		panel.assertEquals(message,(legendOptions)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+(acceptedCount2+pendingCount)+" as accepted, "+rejectedCount+" as rejected )","Checkpoint[9.2/14]","Verifying the finding count from message when Reject all option is selected for RT data");		

		//after send to PACS verify no new clone is created
		panel.assertEquals(cs.getAllResults().size(), 4, "Checkpoint[10/14]", "Verified that no new clone copy created after send to PACS option selected");
	    
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[11/14]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[12/14]", "Verifying the orthanc for findings with 2 machine results");
		verifyOrthancFindingsForMultipleMachines(patientNameRTWith2Machine,username,machineClone+"_1",machineClone+"_2", 3);
	
		//Verify changes in Output panel after send to PACS with option as "Reject All"
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify changes in Output panel after selecting Accept All option from Send to PACS menu");
		verifyFindingsInOP(acceptedCount2+pendingCount,rejectedCount,0,"Checkpoint[13/14]");
		panel.openAndCloseOutputPanel(false);
		panel.doubleClick(panel.getViewPort(2));
		for(int i=0;i<rt.legendOptionsList.size();i++)
		{
			panel.assertTrue(rt.verifyAcceptedRTSegment(i+1),"Checkpoint[14/14]","Verifying state icon is accepted for RT legend in second viewbox");	
		}
		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1055","positive"})	
	public void test10_US1055_TC5596_verifySendToPACSForRTBasedOnUserFeedbackPreference() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS RT Struct Data - Based on User A/R/P User preference setting.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1); 
		     
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		cs=new ContentSelector(driver);
		rt = new DICOMRT(driver);

		String resultName=cs.getAllResults().get(0);
		String machineClone=resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		 //edit contour on previous clone copy
	    rt.navigateToFirstContourOfSegmentation(1);
		rt.selectAcceptfromGSPSRadialMenu();
		rt.navigateToFirstContourOfSegmentation(7);
		rt.selectAcceptfromGSPSRadialMenu();
	    rt.navigateToFirstContourOfSegmentation(3);
		rt.selectRejectfromGSPSRadialMenu();
		rt.navigateToFirstContourOfSegmentation(5);
		rt.selectRejectfromGSPSRadialMenu();		
		cs.waitForTimePeriod(15000);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		//take a count of Findings from Finding Menu
		panel.openFindingTableOnBinarySelector(1);
		//Getting all the findings based on state
		List<WebElement> acceptedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<WebElement> rejectedFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<WebElement> pendingFindings = rt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int acceptedCount=acceptedFindings.size();
		int rejectedCount =rejectedFindings.size();
		int pendingCount=pendingFindings.size();
	
		//9 findings are sent to PACS ( 2 as accepted,2 as rejected and 5 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data with accepted,rejected and pending findings");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, (acceptedCount+rejectedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )"),"Checkpoint[1/3]","Verifying the finding count from message");		
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/3]", "Verify in Orthanc RTSTRUCT series are created for Patint");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/3]", "Verifying the orthanc for Accepted,Rejected and Pending findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA, username,"",machineClone, 3);
		
		
	}
	
	//DE1889: [Automation] Findings are only sent when user clicks twice on send to pacs button
	@Test(groups ={"Chrome","Edge","IE11","DE1889","DR2188","Positive"})	
	public void test11_DE1889_TC7531_DR2188_TC8707_verifySendToPACSForRT() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send findings to PACS by clicking on send to PACS button at first click. <br>"+
		"Verify send to PACS for RTStruct results.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);      
		panel=new OutputPanel(driver);
		DICOMRT rt=new DICOMRT(driver);
		cs=new ContentSelector(driver);
		
		sd=new ViewerSendToPACS(driver);

		rt.navigateToFirstContourOfSegmentation(1);
		for(int i=1;i<=3;i++)
		{
		rt.selectAcceptfromGSPSRadialMenu();
		rt.selectRejectfromGSPSRadialMenu();
		}
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		rt.navigateToFirstContourOfSegmentation(5);
		//take a count of Findings from Output panel
		List<WebElement> acceptedFindings =rt.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		int acceptedCount=acceptedFindings.size();
		rt.navigateToFirstContourOfSegmentation(5);
		List<WebElement> rejectedFindings =rt.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR);
		int rejectedCount=rejectedFindings.size();
		rt.navigateToFirstContourOfSegmentation(5);
		List<WebElement> pendingFindings =rt.getStateSpecificFindings(1, ViewerPageConstants.PENDING_FINDING_COLOR);
		int pendingCount=pendingFindings.size();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data for accepted,rejected and pending legend.");
	
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, (acceptedCount+rejectedCount+pendingCount)+" " +ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, " +pendingCount+" as pending )"),"Checkpoint[1.1/3]","Verifying the finding count from message");		
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1.2/3]","Verifying the error message after send to pacs.");
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		
		//9 findings are sent to PACS ( 9 as pending )
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/3]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/3]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,"", currentSelectResult, 3);
		
	
	}
	
	//DE1999:State of segments are not getting changed when user accepts all post send to pacs
	@Test(groups ={"Chrome","Edge","IE11","DE1999","DE2063","Positive","BVT"})	
	public void test12_DE1999_TC8233_TC8236_DE2063_TC8292_verifySendToPACSForRTAfterAcceptAll() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that State of segments are getting changed when user accepts all or rejects all post send to pacs. <br>"+
		"Verify the state in Output panel, AR tool bar, vertical finding bar, finding menu after SendToPacs.[Risk&Impact]. <br>"+
				"Verify that legend color is updated when user changes the state from A/R toolbar and performs send to pacs.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);        
		panel=new OutputPanel(driver);
		rt=new DICOMRT(driver);
		sd=new ViewerSendToPACS(driver);
		cs = new ContentSelector(driver);

		int legendCount=rt.getLegendOptions(1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data after selecting Accept All from Pending finding dialog.");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, (legendCount)+" " +ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+legendCount+" as accepted )"),"Checkpoint[1/14]","Verifying the accepted finding count from message");		
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","");

		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		//9 findings are sent to PACS ( 9 as accepted )
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/14]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/14]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,"", currentSelectResult, 1);
		
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(currentSelectResult), "Checkpoint[4/14]", "Verified new clone after performing Accept all from send to PACS.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), legendCount, "Checkpoint[5/14]", "Verified accepted finding count in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
		panel.assertEquals(rt.getStateSpecificFindings(1,  ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), legendCount, "Checkpoint[6/14]", "Verified accepted finding count in finding menu.");
		panel.assertTrue(panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size()>0, "Checkpoint[7/14]", "Verified state of finding on scroll slider.");
		panel.assertEquals(rt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).size(), legendCount, "Checkpoint[8/14]", "Verified state of segment color on viewer after Accept All.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify that legend color is updated when user changes the state from A/R toolbar and performs send to pacs.");
		rt.navigateToFirstContourOfSegmentation(1);
		rt.selectAcceptfromGSPSRadialMenu();
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		//9 findings are sent to PACS ( 9 as accepted )
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, (legendCount)+" " +ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+legendCount+" as accepted )"),"Checkpoint[9.1/14]","Verifying the accepted finding count from message");	
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[9.2/14]","Verifying the error message after send to pacs.");
	
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(currentSelectResult), "Checkpoint[10/14]", "Verified new clone after performing Accept all from send to PACS.");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), legendCount, "Checkpoint[11/14]", "Verified accepted finding count in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
		panel.assertEquals(rt.getStateSpecificFindings(1,  ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(), legendCount, "Checkpoint[12/14]", "Verified accepted finding count in finding menu.");
		panel.assertTrue(panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size()>0, "Checkpoint[13/14]", "Verified state of finding on scroll slider.");
		panel.assertEquals(rt.getStateSpecificSegmentNames(1, ThemeConstants.SUCCESS_ICON_COLOR).size(), legendCount, "Checkpoint[14/14]", "Verified state of segment color on viewer after Accept All.");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1999","Positive"})	
	public void test13_DE1999_TC8233_TC8236_verifySendToPACSForRTAfterRejectAll() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that State of segments are getting changed when user accepts all or rejects all post send to pacs. <br>"+
		"Verify the state in Output panel, AR tool bar, vertical finding bar, finding menu after SendToPacs.[Risk&Impact]");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameTCGA, 1, 1);    
		panel=new OutputPanel(driver);
		rt=new DICOMRT(driver);
		sd=new ViewerSendToPACS(driver);
		cs = new ContentSelector(driver);

		int legendCount=rt.getLegendOptions(1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Send to PACS for RT data after selecting Reject All from Pending finding dialog.");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);

		//9 findings are sent to PACS ( 9 as rejected )
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, (legendCount)+" " +ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+legendCount+" as rejected )"),"Checkpoint[1.1/8]","Verifying the rejected finding count from message");	
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1.2/8]","Verifying the error message after send to pacs.");
		panel.closeNotification();
		panel.openAndCloseOutputPanel(false);
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Verify in Orthanc RTSTRUCT series are created");
		sd.verifyNoOfPatientsInOrthanc(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/8]", "Verifying the orthanc for findings");
		verifyOrthancFindingsForPRAndRT(patientNameTCGA,username,"", currentSelectResult, 1);
		
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(currentSelectResult), "Checkpoint[4/8]", "Verified new clone after performing Reject all from send to PACS.");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), legendCount, "Checkpoint[5/8]", "Verified rejected finding count in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
		panel.assertEquals(rt.getStateSpecificFindings(1,  ViewerPageConstants.REJECTED_FINDING_COLOR).size(), legendCount, "Checkpoint[6/8]", "Verified rejected finding count in finding menu.");

		panel.assertTrue(panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size()>0, "Checkpoint[7/8]", "Verified state of finding on scroll slider.");
		
		panel.assertEquals(rt.getStateSpecificSegmentNames(1, ThemeConstants.ERROR_ICON_COLOR).size(), legendCount, "Checkpoint[8/8]", "Verified state of segment color on viewer after Reject All.");
		
	}
	
	@AfterMethod(alwaysRun=true)
    public void afterMethod() throws SQLException{
		DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(username);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
		//Getting patient id

		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

//		if(RESTUtil.getJsonPath(response).getList("").size()>0) {
		for(int i =0;i<RESTUtil.getJsonPath(response).getList("").size();i++) {
			patient_id = RESTUtil.getJsonPath(response).getList("").get(i).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}
	
		
	}
	
	
	public void verifyOrthancFindingsForPRAndRT(String patientName, String username, String resultName,String RTResultName,int numberOfEntries) {


		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there is only one patient
		panel.assertEquals(RESTUtil.getJsonPath(response).getList("").size(),1,"","");
		patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();

		//Getting patient details
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id);
		panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),patientName,"Checkpoint[o.1]","Verifying the patient name");
		
		//Getting PR/SR Entires
		List<String> seriesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL)).getList("");
		panel.assertEquals(seriesID.size(),numberOfEntries,"Checkpoint[o.2]","Verifying the total PR entries [Pending/accepted/rejected]");
		
		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));
        if (RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE))
        {
        	panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_PR_VALUE,"Checkpoint[o.3]","Verifying the PR entries name");
			assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(resultName+"_"+ViewerPageConstants.PENDING_TEXT),
					Matchers.is(resultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(resultName+"_"+ViewerPageConstants.REJECTED_TEXT)));
			
        }
        else
        	{panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_RT_VALUE,"Checkpoint[o.3]","Verifying the SR entries name");
		     assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(RTResultName+"_"+ViewerPageConstants.PENDING_TEXT),
        		Matchers.is(RTResultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(RTResultName+"_"+ViewerPageConstants.REJECTED_TEXT)));	
        	}
		}
		List<String> instancesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL)).getList("");
		panel.assertEquals(instancesID.size(),numberOfEntries,"Checkpoint[o.4]","Verifying the total instances");
		
	}

	public void verifyOrthancFindingsForMultipleMachines(String patientName, String username, String firstResultCopy,String secondResultCopy ,int numberOfEntries) 
	{

		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there is only one patient
		panel.assertEquals(RESTUtil.getJsonPath(response).getList("").size(),1,"","");
		patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();

		//Getting patient details
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id);
		panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),patientName,"Checkpoint[o.1]","Verifying the patient name");
		
		//Getting PR/SR Entires
		List<String> seriesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL)).getList("");
		panel.assertEquals(seriesID.size(),numberOfEntries,"Checkpoint[o.2]","Verifying the total PR entries [Pending/accepted/rejected]");
		
		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));
        if (RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).startsWith(firstResultCopy))
        {
        	panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_RT_VALUE,"Checkpoint[o.3]","Verifying the PR entries name");
			assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(firstResultCopy+"_"+ViewerPageConstants.PENDING_TEXT),
					Matchers.is(firstResultCopy+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(firstResultCopy+"_"+ViewerPageConstants.REJECTED_TEXT)));
			
        }
        else
        	{ if(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).startsWith(secondResultCopy))
        		panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_RT_VALUE,"Checkpoint[o.3]","Verifying the SR entries name");
		     assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(secondResultCopy+"_"+ViewerPageConstants.PENDING_TEXT),
        		Matchers.is(secondResultCopy+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(secondResultCopy+"_"+ViewerPageConstants.REJECTED_TEXT)));	
        	}
		}
		List<String> instancesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL)).getList("");
		panel.assertEquals(instancesID.size(),numberOfEntries,"Checkpoint[o.4]","Verifying the total instances");
		
	}

    public void verifyFindingsInOP(int acceptedCount,int rejectedCount,int pendingCount,String checkpoint  ) throws InterruptedException
    {
    	panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), acceptedCount, checkpoint+".a", "Verified accepted count in Output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), rejectedCount, checkpoint+".b", "Verified rejected count in Output panel");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), pendingCount, checkpoint+".c", "Verified pending count in Output panel");
		panel.openAndCloseOutputPanel(false);
    }


} 
























