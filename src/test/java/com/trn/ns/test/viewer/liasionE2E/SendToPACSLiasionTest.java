package com.trn.ns.test.viewer.liasionE2E;

import java.sql.SQLException;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;



@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSLiasionTest extends TestBase{


	private ExtentTest extentTest;
	private ViewerSendToPACS sd;
	private HelperClass helper;

	String circle = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
	String circlePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, circle);


	String aidoc = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidoc);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String BADFORMATERROR = "SendUserFeedbackToLiaison: SendJsonFeedbackNotification failed with response code BadRequest.";
	String GUIDERROR = "SendDicomToLiaison: SendFeedbackNotification to Liaison failed with result code InternalServerError.";
	

	// This script is suppose to run with Liasion settings
	
	@Test(groups ={"Chrome","Edge","IE11","DR2801","Negative"})	
	public void test01_DR2801_TC10683_verifyErrorNotificationForBatchId() throws InterruptedException, SQLException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Error in Sending results to  Eureka AI' pop-up is not seen when findings are sent to PACS - with out the expected batch id format.");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(circlePatient, username, password, 1);
		
		sd = new ViewerSendToPACS(driver);
		sd.enableFiltersInOutputPanel(false, false, true);
		int count = sd.thumbnailList.size();
		sd.openSendToPACSMenu(true, true, true, true);
		
		db = new DatabaseMethods(driver);
		db.truncateLogTable();
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		
		String message=count+" findings are sent to PACS ( "+count+" as pending )";		
		sd.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message), "Checkpoint[1/3]","Verify send to pacs notification.");
		sd.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2/3]","All findings should be sent to PACS and error message 'Error in sending results to Eureka AI' will still be seen because of the incorrect batch id format in the json files.");
		
		sd.assertEquals(db.getLogsCount(BADFORMATERROR).size(),1,"Checkpoint[3/3]","Specified Error message should not be seen however a \"SendUserFeedbackToLiaison: SendJsonFeedbackNotification failed with response code BadRequest.\" error will be seen");
		

	}

	
	@Test(groups ={"Chrome","Edge","IE11","DR2801","Positive"})	
	public void test02_DR2801_TC10714_verifyErrorNotificationForGUID() throws InterruptedException, SQLException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Error in Sending results to  Eureka AI' pop-up is not seen when findings are sent to PACS - with the expected batch id format(GUID).");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, username, password, 1);
		
		sd = new ViewerSendToPACS(driver);
		
		sd.enableFiltersInOutputPanel(false, false, true);
		int count = sd.thumbnailList.size();
		sd.openSendToPACSMenu(true, true, true, true);
		db = new DatabaseMethods(driver);
		db.truncateLogTable();
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		
		String message=count+" findings are sent to PACS ( "+count+" as pending )";		
		sd.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message),"Checkpoint[1/3]", "Verify send to pacs notification.");
		
		//Note: Here, 500 Error is seen from Liason because it expects the GUID to be present in Liason and once this is fixed at Liason end we will not see the error message 'Error in sending results to Eureka AI' at Eureka's Client side. DR2811 from EnvoyAI Health.
		sd.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2/3]","All findings should be sent to PACS and error message 'Error in sending results to Eureka AI' will still be seen");
		sd.assertEquals(db.getLogsCount(GUIDERROR).size(),1,"Checkpoint[3/3]","Specified Error message should not be seen however a \"SendDicomToLiaison: SendFeedbackNotification to Liaison failed with result code InternalServerError.\" Error should be seen from Liason");
		
		

	}



} 
























