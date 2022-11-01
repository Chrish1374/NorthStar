package com.trn.ns.test.viewer.envoyAI;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SentDICOMInfoTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;


	String filePath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerSendToPACS sd;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private CircleAnnotation circle;

	List<String> batchID = new ArrayList<String>();
	List<String> lastUpdate = new ArrayList<String>();

	@BeforeClass
	public void getMachineLastUpdatedDate() throws SQLException {

		db = new DatabaseMethods(driver);
		batchID = db.getBatchIDsFromBatchTable(aidocPatientName);		
		for(int i =0;i<batchID.size();i++) {
			lastUpdate.add(db.getLastUpdateFromBatchMachineTable(batchID.get(i)));

		}
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);



	}

	@Test(groups ={"Chrome","Edge","IE11","US2042","Positive","E2E","F1005"})	
	public void test01_US2042_TC9597_verifyMachineFindingInfoSent() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Liaison issue - Series UID is not created for DICOM conversion from GSPS to Secondary Capture with pushAfterValidate configuration");


		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocPatientName, 1);
		sd = new ViewerSendToPACS(driver);
		db = new DatabaseMethods(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send the accepted and pending results only");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		sd.assertTrue(!message.isEmpty(),"Checkpoint[1/4]","Verifying the message is displayed");		
		
		List<String> instances = sd.getAllInstances(aidocPatientName, "_"+ViewerPageConstants.PENDING_TEXT);
		HashMap<String, Object> information = sd.getAllInformationAboutInstance(instances.get(0));		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.DisplayedAreaSelectionSequence), "Checkpoint[2/4]", "Verifying the displayed area sequence is populated in response");		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.SoftcopyVOILUTSequence), "Checkpoint[3/4]", "verifying the SoftcopyVOILUTSequence is populated in respsonse");

		verifyOrthancForAidoc(instances.get(0), information,4,4);

	}

	@Test(groups ={"Chrome","Edge","IE11","US2042","Positive","E2E","F1005"})	
	public void test02_US2042_TC9610_verifyGSPSMachineInfoSent() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new Dicom Information is populated when sending the user created GSPS to PACS");


		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocPatientName, 1);
		sd = new ViewerSendToPACS(driver);
		db = new DatabaseMethods(driver);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(3, 10, 10, 50, 50);
		lineWithUnit.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send the accepted and pending results only");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		sd.assertTrue(!message.isEmpty(),"Checkpoint[1/7]","Verifying message is displayed");		
			
		List<String> instances = sd.getAllInstances(aidocPatientName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2_"+ViewerPageConstants.PENDING_TEXT);
		HashMap<String, Object> information = sd.getAllInformationAboutInstance(instances.get(0));		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.DisplayedAreaSelectionSequence), "Checkpoint[2/7]", "Verifying DisplayedAreaSelectionSequence is present in response");		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.SoftcopyVOILUTSequence), "Checkpoint[3/7]", "verifying the SoftcopyVOILUTSequence is present in response");

		verifyOrthancForAidoc(instances.get(0), information,4,7);

		instances = sd.getAllInstances(aidocPatientName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2_"+ViewerPageConstants.ACCEPTED_TEXT);
		information = sd.getAllInformationAboutInstance(instances.get(0));		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.DisplayedAreaSelectionSequence), "Checkpoint[5/7]", "Verifying DisplayedAreaSelectionSequence is present in response");		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.SoftcopyVOILUTSequence), "Checkpoint[6/7]", "verifying the SoftcopyVOILUTSequence is present in response");

		verifyOrthancForAidoc(instances.get(0), information,7,7);

	}

	@Test(groups ={"Chrome","Edge","IE11","US2042","Positive","F1005"})	
	public void test03_US2042_TC9629_verifyInfoForSpecificTag() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 0008,1115 (ReferencedSeriesSequence) contains  only information about the GSPS sent");


		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocPatientName, 1);
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		cs.selectResultFromSeriesTab(1, results.get(results.size()-1));

		sd = new ViewerSendToPACS(driver);
		db = new DatabaseMethods(driver);

		List <String> seriesId = new ArrayList<String>();
		seriesId.add(db.getSeriesInstanceUID(aidocPatientName, viewerPage.getSeriesDescriptionOverlayText(1)));
		seriesId.add(db.getSeriesInstanceUID(aidocPatientName, viewerPage.getSeriesDescriptionOverlayText(4)));

		List <String> instanceIdsForMachine = new ArrayList<String>();
		instanceIdsForMachine.add(db.getInstanceUID(aidocPatientName, viewerPage.getSeriesDescriptionOverlayText(1), NSDBDatabaseConstants.IMAGENUMBER, viewerPage.getCurrentScrollPosition(1)));
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectAcceptfromGSPSRadialMenu();
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, -50, -50);
		instanceIdsForMachine.add(db.getInstanceUID(aidocPatientName, viewerPage.getSeriesDescriptionOverlayText(1), NSDBDatabaseConstants.IMAGENUMBER, viewerPage.getCurrentScrollPosition(1)));
		lineWithUnit.closingConflictMsg();


		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(4, 30, 30, 50, 50);
		lineWithUnit.closingConflictMsg();
		String sopInstanceIDUserAccepted4 = db.getInstanceUID(aidocPatientName, viewerPage.getSeriesDescriptionOverlayText(4), NSDBDatabaseConstants.IMAGENUMBER, viewerPage.getCurrentScrollPosition(4));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send the accepted and pending results only");
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to pacs and choose the leave as is option");
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
	    String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		sd.assertTrue(!message.isEmpty(),"Checkpoint[1/19]","Verifying message is displayed");		
	
		List<String> instances = sd.getAllInstances(aidocPatientName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3_"+ViewerPageConstants.PENDING_TEXT);

		String items = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence);		
		List<String> totalItemsList = sd.convertTheResponseInList(items);
		sd.assertEquals(totalItemsList.size(), seriesId.size(), "Checkpoint[2/19]", "Verifying that total items in response present is equal to series count where annotations are present");

		String referencedSOPInstanceUID = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/0"+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedSOPInstanceUID);
		sd.assertEquals(sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/0"+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedSOPClassUID), db.getInstanceInfo(NSDBDatabaseConstants.SOP_CLASS_UID_COLUMN,referencedSOPInstanceUID), "Checkpoint[3/19]", "veifying the SOP instance ID");
		sd.assertEquals(referencedSOPInstanceUID,instanceIdsForMachine.get(1),"Checkpoint[4/19]","verifying the SOP instance ID is same as in DB");		

		for(int i=0;i<totalItemsList.size();i++)
			sd.assertEquals(sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/"+i+OrthancAndAPIConstants.SeriesInstanceUID),seriesId.get(i), "Checkpoint[5/19]", "verifying the series instance id is same as in DB");

		HashMap<String, Object> information = sd.getAllInformationAboutInstance(instances.get(0));		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.DisplayedAreaSelectionSequence), "Checkpoint[6/19]", "verifying DisplayedAreaSelectionSequence is present in response");		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.SoftcopyVOILUTSequence), "Checkpoint[7/19]", "verifying that SoftcopyVOILUTSequence is present in response");
		verifyOrthancForAidoc(instances.get(0), information,8,19);

		instances = sd.getAllInstances(aidocPatientName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3_"+ViewerPageConstants.ACCEPTED_TEXT);

		items = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence);		
		totalItemsList = sd.convertTheResponseInList(items);
		sd.assertEquals(totalItemsList.size(), seriesId.size(), "Checkpoint[9/19]", "verifying the total series present in reference series sequence present in DB");

		String images = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/0"+OrthancAndAPIConstants.ReferencedImageSequence);		
		List<String> totalImagesList = sd.convertTheResponseInList(images);
		sd.assertEquals(totalImagesList.size(), instanceIdsForMachine.size(), "Checkpoint[10/19]", "Verifying the instance ID same as in DB with response");

		for(int i=0;i<totalImagesList.size();i++) {
			referencedSOPInstanceUID = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/0"+OrthancAndAPIConstants.ReferencedImageSequence+"/"+i+OrthancAndAPIConstants.ReferencedSOPInstanceUID);
			sd.assertEquals(sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/0"+OrthancAndAPIConstants.ReferencedImageSequence+"/"+i+OrthancAndAPIConstants.ReferencedSOPClassUID), db.getInstanceInfo(NSDBDatabaseConstants.SOP_CLASS_UID_COLUMN,referencedSOPInstanceUID), "Checkpoint[11/19]", "verifying the SOP instance ID");
			sd.assertEquals(referencedSOPInstanceUID,instanceIdsForMachine.get(i),"Checkpoint[12/19]","verifying the instances ID are same in Db");
		}

		images = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/1"+OrthancAndAPIConstants.ReferencedImageSequence);		
		totalImagesList = sd.convertTheResponseInList(images);
		sd.assertEquals(totalImagesList.size(), 1, "Checkpoint[13/19]", "verifying the total images");

		for(int i=0;i<totalImagesList.size();i++) {
			referencedSOPInstanceUID = sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/1"+OrthancAndAPIConstants.ReferencedImageSequence+"/"+i+OrthancAndAPIConstants.ReferencedSOPInstanceUID);
			sd.assertEquals(sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/1"+OrthancAndAPIConstants.ReferencedImageSequence+"/"+i+OrthancAndAPIConstants.ReferencedSOPClassUID), db.getInstanceInfo(NSDBDatabaseConstants.SOP_CLASS_UID_COLUMN,referencedSOPInstanceUID), "Checkpoint[14/19]", "verifying the total images");
			sd.assertEquals(referencedSOPInstanceUID,sopInstanceIDUserAccepted4,"Checkpoint[15/19]","verifying the SOP instance ID for accepted clone");
		}

		for(int i=0;i<totalItemsList.size();i++)
			sd.assertEquals(sd.getTagInformation(instances.get(0),OrthancAndAPIConstants.ReferencedSeriesSequence+"/"+i+OrthancAndAPIConstants.SeriesInstanceUID),seriesId.get(i), "Checkpoint[16/19]", "verifying the series list is same in DB");


		information = sd.getAllInformationAboutInstance(instances.get(0));		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.DisplayedAreaSelectionSequence), "Checkpoint[17/19]", "verifying the DisplayedAreaSelectionSequence is present in response ");		
		sd.assertTrue(information.containsKey(OrthancAndAPIConstants.SoftcopyVOILUTSequence), "Checkpoint[18/19]", "verifying the SoftcopyVOILUTSequence is present in Response");
		verifyOrthancForAidoc(instances.get(0), information,19,19);

	}

	private void verifyOrthancForAidoc(String instanceID,HashMap<String, Object> information, int currentCheckpoint, int finalCheckpoint) throws SQLException {

		String items = sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag);		
		List<String> totalItemsList = sd.convertTheResponseInList(items);
		sd.assertEquals(sd.getOccurenceCount(information.get(OrthancAndAPIConstants.DisplayedAreaSelectionSequence).toString(), OrthancAndAPIConstants.DisplayedAreaTopLeftHandCorner_key+"=1"),totalItemsList.size(), "Checkpoint["+currentCheckpoint+".1/"+finalCheckpoint+"./11]","verifying that DisplayedAreaSelectionSequence is recorded in response");

		for(int i=0;i<totalItemsList.size();i++) {
			String referencedSOPInstanceUID = sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag+"/"+i+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedSOPInstanceUID);

			//SoftcopyVOILUTSequence
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag+"/"+i+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedSOPClassUID), db.getInstanceInfo(NSDBDatabaseConstants.SOP_CLASS_UID_COLUMN,referencedSOPInstanceUID), "Checkpoint["+currentCheckpoint+".2/"+finalCheckpoint+"./11]", "verifying refernce SOP insance id in SoftcopyVOILUTSequence tag");
			
			sd.assertEquals(sd.convertIntoInt(sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag+"/"+i+OrthancAndAPIConstants.WindowCenter)),(db.convertIntoFloat(db.getInstanceInfo(NSDBDatabaseConstants.WINDOWCENTER_COL,referencedSOPInstanceUID)).intValue()),"Checkpoint["+currentCheckpoint+".3/"+finalCheckpoint+"./11]","verifying the window center value is same as in DB");	
			
			sd.assertEquals(sd.convertIntoInt(sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag+"/"+i+OrthancAndAPIConstants.WindowWidth)),(db.convertIntoFloat(db.getInstanceInfo(NSDBDatabaseConstants.WINDOWWIDTH_COL,referencedSOPInstanceUID)).intValue()),"Checkpoint["+currentCheckpoint+".4/"+finalCheckpoint+"./11]","verifying the window width value is same as in DB");
			
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.SoftcopyVOILUTSequence_tag+"/"+i+OrthancAndAPIConstants.VOILUTFunction),OrthancAndAPIConstants.VOILUTFunction_val,"Checkpoint["+currentCheckpoint+".5/"+finalCheckpoint+"./11]","verifying that VOILUTFunction_val tag value is LINEAR");

			//		DisplayedAreaSelectionSequence
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.DisplayedAreaSelectionSequence_tag+"/"+i+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedSOPClassUID), db.getInstanceInfo(NSDBDatabaseConstants.SOP_CLASS_UID_COLUMN,referencedSOPInstanceUID), "Checkpoint["+currentCheckpoint+".6/"+finalCheckpoint+"./11]", "verifying the SOP instance id under DisplayedAreaSelectionSequence tag");
			
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.DisplayedAreaSelectionSequence_tag+"/"+i+OrthancAndAPIConstants.ReferencedImageSequence+"/0"+OrthancAndAPIConstants.ReferencedFrameNumber), db.getInstanceInfo(NSDBDatabaseConstants.FRAME_NUM_COLUMN,referencedSOPInstanceUID), "Checkpoint["+currentCheckpoint+".7/"+finalCheckpoint+"./11]", "verifying the frame number is same in db");
			
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.DisplayedAreaSelectionSequence_tag+"/"+i+OrthancAndAPIConstants.PixelOriginInterpretation),OrthancAndAPIConstants.PixelOriginInterpretation_val,"Checkpoint["+currentCheckpoint+".8/"+finalCheckpoint+"./11]","verifying the pixel interpretation is same as in db");		
			
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.DisplayedAreaSelectionSequence_tag+"/"+i+OrthancAndAPIConstants.PresentationSizeMode),OrthancAndAPIConstants.PresentationSizeMode_val,"Checkpoint["+currentCheckpoint+".9/"+finalCheckpoint+"./11]","verifying the size mode is 'SCALE to FIT'");
			
			sd.assertEquals(sd.getTagInformation(instanceID,OrthancAndAPIConstants.DisplayedAreaSelectionSequence_tag+"/"+i+OrthancAndAPIConstants.PresentationPixelSpacing),db.getInstanceInfo(NSDBDatabaseConstants.PIXEL_SPACING_ROW,referencedSOPInstanceUID)+"\\"+db.getInstanceInfo(NSDBDatabaseConstants.PIXEL_SPACING_COL,referencedSOPInstanceUID),"Checkpoint["+currentCheckpoint+".10/"+finalCheckpoint+"./11]","verifying the pixel spacing is same as in db");
			
			sd.assertEquals(sd.getOccurenceCount(information.get(OrthancAndAPIConstants.DisplayedAreaSelectionSequence).toString(), OrthancAndAPIConstants.DisplayedAreaBottomRightHandCorner_key+"="+db.getInstanceInfo(NSDBDatabaseConstants.ROWS,referencedSOPInstanceUID)),totalItemsList.size(),"Checkpoint["+currentCheckpoint+".11/"+finalCheckpoint+"./11]","verifying the column and rows are same as in db");

		}

	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod(){

		sd.deleteAllPatients();



	}

	@AfterMethod
	public void updatLastUpdate() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);

		for(int i =0;i<batchID.size();i++) {
			db.updateLastUpdateFromBatchMachineTable(batchID.get(i),lastUpdate.get(i));

		}
	}



} 
























