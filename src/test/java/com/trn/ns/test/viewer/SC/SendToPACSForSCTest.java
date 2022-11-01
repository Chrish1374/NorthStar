package com.trn.ns.test.viewer.SC;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSForSCTest extends TestBase {

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ContentSelector cs;
	private ViewerSendToPACS sd;
	private HelperClass helper;

	String s="aidoc_CervicalSpine";
	String anonymize_Cervial_filepath =Configurations.TEST_PROPERTIES.get("Anonymize_Cervical_Spine_filepath");
	String cervical_Spine_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymize_Cervial_filepath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	private OutputPanel panel;
	static String patient_id="";
	private EllipseAnnotation ellipse;

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	//US1076: Send to PACS for supporting DICOM-SC results
	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test01_US1076_TC5355_verifySendToPACSForDefaultLoaded() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the SendToPacs for DICOM SC data when default loaded.");

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 3);
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(2);

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs pending SC data with option selected as 'Leave As Is'");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total pending findings from output panel after send to PACS");
		panel.waitForElementInVisibility(panel.notificationDiv);
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.waitForTimePeriod(30000);
		int pendingCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verified count of Pending findings from Notification UI");
		panel.assertEquals(message,(pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SC pending result");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username, "",firstSCResultName, 2,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive","BVT"})	
	public void test02_US1076_TC5356_verifySendToPACSForAcceptedState() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Accepted state.");

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 3);
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1);
		panel.acceptResult(1);
		sd=new ViewerSendToPACS(driver);

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu();
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs after changing the state of SC result as Accepted");
		String message=sd.getNotificationMessage(1);;
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted findings from output panel");
		panel.waitForElementInVisibility(panel.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,"One finding is sent to PACS ( "+acceptedCount+" as accepted )","Verify message post sending the findings to PACS","Verified");	
		
		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SS data when state is accepted");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username, "",firstSCResultName, 1,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test03_US1076_TC5357_verifySendToPACSForRejectedState() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Rejected state.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);

		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1);
		panel.rejectResult(1);
		sd=new ViewerSendToPACS(driver);

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(false, true, false);
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs after changing the state of SC result as rejected");
		String message=sd.getNotificationMessage(1);;
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total rejected findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of rejected findings from Notification UI");
		panel.assertEquals(message,"One finding is sent to PACS ( "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username, "",firstSCResultName, 1,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test04_US1076_TC5358_verifySendToPACSForAcceptedGSPSWithAcceptedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Accepted state+GSPS annotation is in Accepted State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+1;
		panel.acceptResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Draw annotation and keep the state of Annotation as Accepted");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu();
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs after changing the state of SC result as accepted along with Accepted GSPS.");
		String message=sd.getNotificationMessage(1);;
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted findings from output panel of both SC and GSPS");
		panel.waitForElementInVisibility(panel.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,(acceptedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SC and GSPS data with accepted state");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 2,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test05_US1076_TC5359_verifySendToPACSForPendingGSPSwithAcceptedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Accepted state+GSPS annotation is in Pending State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);

		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);
		sd=new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(2)+1;
		panel.acceptResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();
		//enable all finding option from send to PACS
		sd.openSendToPACSMenu(true,true, false, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs after changing the state of SC result as accepted along with Pending GSPS.");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		String message=sd.getNotificationMessage(1);;
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted and pending findings from output panel");
		panel.waitForElementInVisibility(panel.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted and pending findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of accepted SC and Pending GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 3,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test06_US1076_TC5360_verifySendToPACSForRejectedGSPSwithAcceptedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Accepted state+GSPS annotation is in Rejected State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		panel=new OutputPanel(driver);

		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+1;
		panel.acceptResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Rejected");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectRejectfromGSPSRadialMenu();
		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, false);
		panel.click(sd.sendToPacs);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs after changing the state of SC result as accepted along with Rejected GSPS.");
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");  
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted and rejected findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted and rejected findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of accepted SC and Pending GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 2,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test07_US1076_TC5361_verifySendToPACSForPendingGSPSwithPendingSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Pending state+GSPS annotation is in Pending State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(1)+1;

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();
		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,false, false, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for Pending SC along with Pending GSPS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");   
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total pending findings from output panel");
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of pending findings from Notification UI");
		panel.assertEquals(message,(pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of Pending SC and Pending GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 3,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test08_US1076_TC5362_verifySendToPACSForAcceptedGSPSwithPendingSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Pending state+GSPS annotation is in Accepted State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);
		sd=new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(1)+1;

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Accepted");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu(true,true, false, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for Pending SC along with Accepted GSPS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(true, false, true)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");    
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted and pending findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted and pending findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of Pending SC and Accepted GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 3,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test09_US1076_TC5363_verifySendToPACSForRejectedGSPSwithPendingSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Pending state+GSPS annotation is in Rejected State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);
	
		sd=new ViewerSendToPACS(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(1)+1;

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Rejected");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectRejectfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu(true,false, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for Pending SC along with Rejected GSPS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");    
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total pending and rejected findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of rejected and pending findings from Notification UI");
		panel.assertEquals(message,(rejectedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+rejectedCount+" as rejected, "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for Pending SC and Rejected GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 3,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test10_US1076_TC5365_verifySendToPACSForAcceptedGSPSwithRejectedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Rejected state+GSPS annotation is in Accepted State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+1;
		panel.rejectResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and keep the state of Annotation as Accepted");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, false);
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]",  "Send to pacs for Rejected SC along with Accepted GSPS");
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");   
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted and rejected findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted and rejected findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for Rejected SC and Accepted GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 2,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test11_US1076_TC5366_verifySendToPACSForPendingGSPSwithRejectedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Rejected state+GSPS annotation is in Pending State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		panel=new OutputPanel(driver);
	
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(1)+1;
		panel.rejectResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,false, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for Rejected SC along with Pending GSPS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");    
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total rejected and pending findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of total rejected and pending findings from Notification UI");
		panel.assertEquals(message,(rejectedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+rejectedCount+" as rejected, "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for Rejected SC and Pending GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 3,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test12_US1076_TC5367_verifySendToPACSForRejectedGSPSwithRejectedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send to PACS for supporting DICOM-SC results when DICOM SC is in Rejected state+GSPS annotation is in Rejected State.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+1;
		panel.rejectResult(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation and change the state of Annotation as rejected");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.selectRejectfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,false, true, false);
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for Rejected SC along with Rejected GSPS");
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");   
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total rejected findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of rejected findings from Notification UI");
		panel.assertEquals(message,(rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		String GSPS_ResultName=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for Rejected SC and Rejected GSPS");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,GSPS_ResultName ,firstSCResultName, 2,sliceNo);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test13_US1076_TC5461_verifySendToPACSWhenRejectAllSelectedForPendingSCResult() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify log for rejected result when user sent results to pacs selects Reject from "+"\'"+"There is one Pending finding.What You would like to do"+"\'"+" popup appeared.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Send to pacs and choose Reject All for Pending SC result");
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, true, false);
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");    
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Getting the total rejected findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verify count of rejected findings from Notification UI");
		panel.assertEquals(message,+(rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyRejectedIsChecked(1), "Checkpoint[4/5]", "Verified that first SC result state change to Rejected from Pending");
		panel.click(panel.getViewPort(2));	
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.assertTrue(panel.verifyRejectedIsChecked(2), "Checkpoint[5/5]", "Verified that second SC result state change to Rejected from Pending");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test14_US1076_TC5462_verifySendToPACSWhenAcceptAllSelectedForPendingSCResult() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify log for rejected result when user sent results to pacs selects Accept from "+"\'"+"There is one Pending finding.What You would like to do"+"\'"+" pop appeared.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Send to pacs and choose Accept All option for Pending SC result");
		sd.sendToPacsAndSelectOptionsFromPendingBox(true, false, false);
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");    
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Getting the total accepted findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,+(acceptedCount)+ " findings are sent to PACS ( "+acceptedCount+" as accepted )","Verify message post sending the findings to PACS","Verified");	

		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptedIsChecked(1), "Checkpoint[4/5]", "Verified that SC result state change to Accepted from Pending");
		panel.click(panel.getViewPort(2));
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.assertTrue(panel.verifyAcceptedIsChecked(2), "Checkpoint[5/5]", "Verified that SC result state change to Accepted from Pending");
	

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test15_US1076_TC5473_verifySendToPACSWhenRejectAllSelectedForPendingSCAndGSPSOnDifferentSeries() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of DICOM SC series on view box when DICOM SC is in Pending state and GSPS drawn on different series having pending annotation and selected Reject option on selection 'PopUp'");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		ellipse=new EllipseAnnotation(driver);
		sd=new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation on other than SC result and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/6]", "Send to pacs and choose the Reject ALL option from pending finding dialog");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, true, false)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");
		panel.click(panel.getViewPort(1));
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/6]", "Getting the total rejected findings from output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/6]", "Verify count of rejected findings from Notification UI");
		panel.assertEquals(message,(rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		panel.click(panel.getViewPort(4));
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(4, 1), "Checkpoint[4/6]", "Verified state of GSPS as Rejected after selecting Reject All option from Pending finding dialog");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyRejectGSPSRadialMenu(), "Checkpoint[5/6]", "Verified state of SC data as Rejected after selecting Reject All option from Pending finding dialog");
		panel.click(panel.getViewPort(2));
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.assertTrue(panel.verifyRejectGSPSRadialMenu(), "Checkpoint[6/6]", "Verified state of SC data as Rejected after selecting Reject All option from Pending finding dialog");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test16_US1076_TC5474_verifySendToPACSWhenAcceptAllSelectedForPendingSCAndGSPSOnDifferentSeries() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of DICOM SC series on view box when DICOM SC is in Pending state and GSPS drawn on different series having pending annotation and selected Accept option on selection 'PopUp'");
		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		panel.waitForViewerpageToLoad(2);
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation on other than SC result and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/6]", "Send to pacs and choose the Accept ALL option from pending finding dialog");
	    sd.sendToPacsAndSelectOptionsFromPendingBox(true, false, false)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");	    
		panel.click(panel.getViewPort(1));
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/6]", "Getting the total accepted findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/6]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,(acceptedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted )","Verify message post sending the findings to PACS","Verified");	

		panel.click(panel.getViewPort(4));
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(4, 1), "Checkpoint[4/6]", "Verified state of GSPS as Accepted after selecting Accept All option from Pending finding dialog");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[5/6]", "Verified state of SC data as Accepted after selecting Accept All option from Pending finding dialog");
		panel.click(panel.getViewPort(2));
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[6/6]", "Verified state of SC data as Accepted after selecting Accept All option from Pending finding dialog");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1076","positive"})	
	public void test17_US1076_TC5474_verifySendToPACSWhenLeaveAsIsSelectedForPendingSCAndGSPSOnDifferentSeries() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of DICOM SC series on view box when DICOM SC is in Pending state and GSPS drawn on different series having pending annotation and selected 'Leave as is' option on selection 'PopUp'");
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Draw annotation on other than SC result and change the state of Annotation as Pending");
		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 100, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/8]",  "Send to pacs and choose the Leave As Is option from pending finding dialog");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true)   ;
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");
		panel.click(panel.getViewPort(1));
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Getting the total pending findings from output panel");
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/8]", "Verify count of pending findings from Notification UI");
		panel.assertEquals(message,(pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		panel.click(panel.getViewPort(4));
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(4, 1), "Checkpoint[4/8]", "Verified state of GSPS as Pending after selecting Leave As Is option from Pending finding dialog");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[5/8]", "Verified state of SC data as Pending after selecting Leave As Is option from Pending finding dialog");
		panel.click(panel.getViewPort(2));
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[7/8]", "Verified state of SC data as Pending after selecting Leave As Is option from Pending finding dialog");
	
	}

	//DE1425: Entry for DICOM SC series are getting logged as 'Pending' in orthanc server for Accepted and Rejected state ,when DICOM SC sent to PACS again without making changes in state of DICOM SC series.
	@Test(groups ={"Chrome","Edge","IE11","DE1425","positive"})	
	public void test18_DE1425_TC5770_verifySendToPACSForAcceptedGSPSwithRejectedSC() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that state of DICOM SC series in Orthanc server when DICOM SC series sent to PACS again without changing its state.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");

		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Accept first SC result and Reject the second One");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1)+panel.getMaxNumberofScrollForViewbox(2);
		panel.acceptResult(1);
		panel.click(panel.getViewPort(2));
		panel.rejectResult(2);

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/7]", "Send to pacs for accepted and Rejected SC result ");
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");
		panel.waitForElementInVisibility(panel.notificationDiv);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/7]", "Getting the total accepted and rejected findings from output panel");
	
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.openAndCloseOutputPanel(false);

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/7]", "Verify count of accepted and rejected findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstSCResultName = cs.getSelectedResults().get(0);
		panel.click(panel.getViewPort(2));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/7]", "Verifying the orthanc for Accepted and Rejected SC data");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,"" ,firstSCResultName, 2,sliceNo);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Delete existing orthanc entry for Accepted and Rejected SC result");
		if(patient_id.isEmpty()) {
         RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();

		}

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		panel.click(sd.sendToPacs);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/7]", "Send to pacs for accepted and Rejected SC result again without making any change");
		panel.waitForElementVisibility(panel.notificationUI);
		String message1=sd.getNotificationMessage(1);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/7]", "Verify count for accepted and Rejected findings from Notification UI");
		panel.assertEquals(message1,(acceptedCount+rejectedCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )","Verify message post sending the findings to PACS","Verified");	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc entry for Accepted and Rejected SC result when user again send same result without making any change");
		verifyOrthancFindingsForSCAndPR(cervical_Spine_PatientName, username,"" ,firstSCResultName, 2,sliceNo+sliceNo);

	}

	//DE1889: [Automation] Findings are only sent when user clicks twice on send to pacs button
	@Test(groups ={"Chrome","Edge","IE11","DE1889","Positive"})	
	public void test19_DE1889_TC7531_verifySendToPACSForSCDataAtFirstClick() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send findings to PACS by clicking on send to PACS button at first click");

		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(cervical_Spine_PatientName, 2);
		
		panel=new OutputPanel(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, " ", "Loading patient "+cervical_Spine_PatientName+" on viewer");
		ellipse=new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,  " ", "Accept first SC result and Reject the second One");
		int sliceNo=panel.getMaxNumberofScrollForViewbox(1);
		panel.acceptResult(1);
		panel.click(panel.getViewPort(2));
		panel.rejectResult(2);

		ellipse.selectEllipseFromQuickToolbar(3);
		ellipse.drawEllipse(3, 100, 100, 50, 50);
		panel.selectAcceptfromGSPSRadialMenu();

		//enable all finding option from send to PACS
		sd=new ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, true, true);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs for accepted and Rejected SC result ");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		String message=sd.getNotificationMessage(1);
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verified error notification after send to pacs","Verified");   
		panel.waitForElementInVisibility(panel.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted, rejected and pending findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();	
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted, rejected and pending findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+rejectedCount+pendingCount)+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT+" ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )","Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String firstResultName = cs.getSelectedResults().get(2);
		String firstSCResultName = cs.getSelectedResults().get(1);
		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for Accepted and Rejected SC data");
		verifyOrthancFindingsForSCAndPR(imbio_PatientName, username,firstResultName,firstSCResultName, 2,sliceNo);

	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException, InterruptedException {
		//DB Default configuration
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
		db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
		db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);

		//Getting patient id
		if(!patient_id.isEmpty()) {

			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();

		}
		}
	public void verifyOrthancFindingsForSCAndPR(String patientName, String username, String resultName,String SCResultName,int numberOfEntries,int noOfSlices) {


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
				if (RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG).contains(OrthancAndAPIConstants.ORTHANC_OT_VALUE))
				{
					panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_OT_VALUE,"Checkpoint[o.3]","Verifying the PR entries name");
					assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(SCResultName+"_"+ViewerPageConstants.PENDING_TEXT),
							Matchers.is(SCResultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(SCResultName+"_"+ViewerPageConstants.REJECTED_TEXT)));

				}
				else
				{  panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_CT_VALUE,"Checkpoint[o.3]","Verifying the SR entries name");
				assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(SCResultName+"_"+ViewerPageConstants.PENDING_TEXT),
						Matchers.is(SCResultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(SCResultName+"_"+ViewerPageConstants.REJECTED_TEXT)));	
				}
		}

		List<String> instancesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL)).getList("");
		panel.assertEquals(instancesID.size(),noOfSlices,"Checkpoint[o.4]","Verifying the total instances");






	}


}
